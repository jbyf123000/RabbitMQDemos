package top.jbyf.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import top.jbyf.config.RabbitMqUtils;

import java.util.Scanner;

public class EmitLog {
    private static final String EXCHANGE_NAME = "fanout::exchange";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入需要发送的消息：");
        while (scanner.hasNext()){
            System.out.println("输入需要发送的消息：");
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME,"", MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes("UTF-8"));
            System.out.println("消息发送完成");
        }
    }
}