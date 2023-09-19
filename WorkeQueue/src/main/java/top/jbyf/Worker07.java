package top.jbyf;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import top.jbyf.config.RabbitMqUtils;
import top.jbyf.config.SleepUtils;

public class Worker07 {
    public static final String QUEUE_NAME = "task_prefetch";
    public static final boolean AUTOACK = false;
    public static void main(String[] args) {
        try {
            Channel channel = RabbitMqUtils.getChannel();
            // 设置不公平分发,预取值2
            channel.basicQos(2);
            DeliverCallback deliverCallback = (consumerTag,delivery) ->{
                String s = new String(delivery.getBody());
                SleepUtils.sleep(1);
                System.out.println(consumerTag + "消费者C7消费的回调");
                System.out.println("C7接收到消息:" + s);
                channel.basicAck(delivery.getEnvelope().getDeliveryTag() , false);
            };
            CancelCallback cancelCallback = (consumerTag)->{
                System.out.println(consumerTag + "消费者C7取消消费接口的回调");
            };
            System.out.println("C7消费者等待消费");
            channel.basicConsume(QUEUE_NAME,AUTOACK,deliverCallback,cancelCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}