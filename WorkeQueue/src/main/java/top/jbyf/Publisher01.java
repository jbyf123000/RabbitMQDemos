package top.jbyf;

import com.rabbitmq.client.Channel;
import top.jbyf.config.RabbitMqUtils;

import java.util.Scanner;

public class Publisher01 {
    public static final String QUEUE_NAME="task2";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入需要发送的消息：");
        while (scanner.hasNext()){
            System.out.println("输入需要发送的消息：");
            String message = scanner.next();
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes("UTF-8"));
            System.out.println("消息发送完成");
        }
    }
}
