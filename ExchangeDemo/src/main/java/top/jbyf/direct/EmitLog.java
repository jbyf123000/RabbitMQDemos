package top.jbyf.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import top.jbyf.config.RabbitMqUtils;

import java.util.Scanner;

public class EmitLog {
    public static final String EXCHANGE_NAME = "direct_logs";
    private static final String ROUTING_KEY_1 = "info";
    private static final String ROUTING_KEY_2 = "warning";
    private static final String ROUTING_KEY_3 = "error";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        Scanner scanner = new Scanner(System.in);
        System.out.println("输入需要发送的消息：");
        while (scanner.hasNext()){
            System.out.println("输入需要发送的消息：");
            String message = scanner.next();
            if (message.hashCode() % 2 == 0){
                channel.basicPublish(EXCHANGE_NAME,ROUTING_KEY_1, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes("UTF-8"));
            }
            else if (message.hashCode() % 3 == 0){
                channel.basicPublish(EXCHANGE_NAME,ROUTING_KEY_2, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes("UTF-8"));
            }
            else {
                channel.basicPublish(EXCHANGE_NAME,ROUTING_KEY_3, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes("UTF-8"));
            }
            System.out.println("消息发送完成");
        }
    }
}