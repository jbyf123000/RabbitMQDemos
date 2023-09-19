package top.jbyf.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import top.jbyf.config.RabbitMqUtils;

import static top.jbyf.topic.EmitLog.*;

public class ReceiveLogs2 {

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,BuiltinExchangeType.TOPIC);
        channel.queueDeclare("Q2",false,false,false,null);
        channel.queueBind("Q2",EXCHANGE_NAME,"lazy.#");
        channel.queueBind("Q2",EXCHANGE_NAME,"*.*.rabbit");
        System.out.println("r2 等待接受消息");

        DeliverCallback dcall = ((consumerTag, message) -> {
            System.out.print("接受队列：Q2的信息，绑定键" + message.getEnvelope().getRoutingKey() + "  信息：");
            System.out.println(new String(message.getBody(),"UTF-8"));
        });
        channel.basicConsume("Q2",true,dcall, consumerTag -> {});
    }
}
