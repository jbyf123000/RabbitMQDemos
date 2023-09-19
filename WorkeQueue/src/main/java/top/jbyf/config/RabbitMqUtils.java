package top.jbyf.config;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMqUtils {
    public static Channel getChannel() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.31.249");
        factory.setUsername("admin");
        factory.setPassword("123");


        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        return channel;
    }
}
