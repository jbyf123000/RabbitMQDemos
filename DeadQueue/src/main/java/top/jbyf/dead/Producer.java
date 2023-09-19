package top.jbyf.dead;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import top.jbyf.config.RabbitMqUtils;

public class Producer {
    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        //设置信息超时时间
        AMQP.BasicProperties properties = new AMQP.BasicProperties()
                .builder()
                .expiration("10000")
                .build();
        for (int i = 0; i < 10; i++) {
            String message = "info::" + i;
            channel.basicPublish(Consumer1.NORMAL_EXCHANGE,Consumer1.NORMAL_ROUTING_KEY,null,message.getBytes());
            System.out.println("已发送消息");
        }
    }
}
