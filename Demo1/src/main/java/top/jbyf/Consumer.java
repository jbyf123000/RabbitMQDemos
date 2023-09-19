package top.jbyf;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.31.249");
        factory.setUsername("admin");
        factory.setPassword("123");

        try(Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            //推送的消息如何进行消费的接口回调
            DeliverCallback callback = (consumerTag,delivery)->{
                String message = new String(delivery.getBody());
                System.out.println(message);
            };
            //取消消费的一个回调接口 如在消费的时候队列被删除掉了
            CancelCallback cancelCallback = (consumerTag) ->{
                System.out.println("broken message");
            };
            /**
             * 消费者消费消息
             * 1.消费哪个队列
             * 2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
             * 3.消费者未成功消费的回调
             */
            channel.basicConsume(QUEUE_NAME,true,callback,cancelCallback);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
