package top.jbyf;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class Worker01 {
    public static final String QUEUE_NAME = "hello";
    public static void main(String[] args) {
        try {
            Channel channel = RabbitMqUtils.getChannel();
            DeliverCallback deliverCallback = (consumerTag,delivery) ->{
                String s = new String(delivery.getBody());
                System.out.println(consumerTag + "消费者C1消费的回调");
                System.out.println("C1接收到消息:" + s);
            };
            CancelCallback cancelCallback = (consumerTag)->{
                System.out.println(consumerTag + "消费者C1取消消费接口的回调");
            };
            System.out.println("C1消费者等待消费");
            channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}