package top.jbyf.dead;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import top.jbyf.config.RabbitMqUtils;

public class Consumer2 {
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("C2 wait for message.");

        DeliverCallback callback = (consumerTag,message)->{
            System.out.println("C2 consumed a message: " + new String(message.getBody(),"UTF-8"));
        };
        channel.basicConsume(Consumer1.DEAD_QUEUE,true,callback,(consumerTag -> {}));
    }
}