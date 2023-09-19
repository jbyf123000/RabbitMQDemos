package top.jbyf.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import top.jbyf.config.RabbitMqUtils;

import static top.jbyf.direct.EmitLog.EXCHANGE_NAME;

public class ReceiveLogs1 {
    private static final String QUEUE_NAME = "console";
    private static final String ROUTING_KEY = "info";
    private static final String ROUTING_KEY2 = "warning";

    public static void main(String[] args) throws Exception {
        // 获取信道
        Channel channel = RabbitMqUtils.getChannel();
        // 创建交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // 等待接受消息，输出消息
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,ROUTING_KEY);
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,ROUTING_KEY2);
        // 接收消息的lambda
        System.out.println("等待接收消息并打印");
        // 取消消息的lambda
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogs01 控制台打印接收到的消息： " + new String(message.getBody(),"UTF-8"));
        };

        channel.basicConsume(QUEUE_NAME,true,deliverCallback,consumerTag -> {});
    }
}
