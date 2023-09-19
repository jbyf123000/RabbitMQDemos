package top.jbyf.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import top.jbyf.config.RabbitMqUtils;

import static top.jbyf.direct.EmitLog.EXCHANGE_NAME;

public class ReceiveLogs2 {
    public static final String QUEUE_NAME = "disk";
    private static final String ROUTING_KEY = "error";

    public static void main(String[] args) throws Exception {
        // 获取信道
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // 创建交换机
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        // 等待接受消息，输出消息
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,ROUTING_KEY);
        // 接收消息的lambda
        System.out.println("等待接收消息并打印");
        // 取消消息的lambda
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogs03 控制台打印接收到的消息： " + new String(message.getBody(),"UTF-8"));
        };

        channel.basicConsume(QUEUE_NAME,true,deliverCallback,consumerTag -> {});
    }
}
