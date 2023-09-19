package top.jbyf.confirm;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import top.jbyf.confirm.config.RabbitMqUtils;
import top.jbyf.confirm.config.SleepUtils;

public class Worker01 {
    public static final String QUEUE_NAME = "task_confirm";
    public static final boolean AUTOACK = false;
    public static void main(String[] args) {
        try {
            Channel channel = RabbitMqUtils.getChannel();
            // 设置不公平分发,预取值2
            channel.basicQos(2);
            DeliverCallback deliverCallback = (consumerTag,delivery) ->{
                String s = new String(delivery.getBody());
                SleepUtils.sleep(1);
                System.out.println(consumerTag + "消费者C1消费的回调");
                System.out.println("C1接收到消息:" + s);
                channel.basicAck(delivery.getEnvelope().getDeliveryTag() , false);
            };
            CancelCallback cancelCallback = (consumerTag)->{
                System.out.println(consumerTag + "消费者C1取消消费接口的回调");
            };
            System.out.println("C1消费者等待消费");
            channel.basicConsume(QUEUE_NAME,AUTOACK,deliverCallback,cancelCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}