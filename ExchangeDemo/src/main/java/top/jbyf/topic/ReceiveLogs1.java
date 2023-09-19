package top.jbyf.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import top.jbyf.config.RabbitMqUtils;

import static top.jbyf.topic.EmitLog.*;

public class ReceiveLogs1 {

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,BuiltinExchangeType.TOPIC);
        channel.queueDeclare("Q1",false,false,false,null);
        channel.queueBind("Q1",EXCHANGE_NAME,"*.orange.*");
        System.out.println("r1 等待接受消息");

        DeliverCallback deliverCallback = ((consumerTag, message) -> {
            System.out.print("接受队列" + "Q1" + "  绑定键：" + message.getEnvelope().getRoutingKey()+ "  信息：");
            System.out.println(new String(message.getBody(),"UTF-8"));
        });

        channel.basicConsume("Q1",true,deliverCallback,consumerTag -> {});
    }
}
