package top.jbyf;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.sun.xml.internal.ws.api.message.Message;
import top.jbyf.config.RabbitMqUtils;

import java.util.Scanner;

public class Publisher01 {
//    public static final String QUEUE_NAME="task2";
//    public static final String QUEUE_NAME="task_unfair";
    public static final String QUEUE_NAME="task_prefetch";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入需要发送的消息：");
        while (scanner.hasNext()){
            System.out.println("输入需要发送的消息：");
            String message = scanner.next();
            if ("jbyf".equals(message)){
                for (int i = 1; i <= 7; i++) {
                    channel.basicPublish("",QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,(11*i + "").getBytes("UTF-8"));
                }
            }
            // 使用 MessageProperties.PERSISTENT_TEXT_PLAIN 设置消息持久化
            else {
                channel.basicPublish("",QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes("UTF-8"));
            }
            System.out.println("消息发送完成");
        }
    }
}
