package top.jbyf;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import top.jbyf.config.RabbitMqUtils;
import top.jbyf.config.SleepUtils;

public class Worker04 {
    public static final String QUEUE_NAME = "task";
    public static final boolean AUTOACK = false;
    public static void main(String[] args) {
        try {
            Channel channel = RabbitMqUtils.getChannel();
            DeliverCallback deliverCallback = (consumerTag,delivery) ->{
                String s = new String(delivery.getBody());
                System.out.println(consumerTag + "消费者C4消费的回调");
                // 睡眠30秒，在此期间关闭work4线程后，消息会重入队列，被C3处理
                SleepUtils.sleep(30);
                System.out.println("C4接收到消息:" + s);
                channel.basicAck(delivery.getEnvelope().getDeliveryTag() , false);
            };
            CancelCallback cancelCallback = (consumerTag)->{
                System.out.println(consumerTag + "消费者C4取消消费接口的回调");
            };
            System.out.println("C4消费者等待消费");
            channel.basicConsume(QUEUE_NAME,AUTOACK,deliverCallback,cancelCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}