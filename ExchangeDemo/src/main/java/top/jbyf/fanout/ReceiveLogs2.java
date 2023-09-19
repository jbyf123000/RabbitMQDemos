package top.jbyf.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import top.jbyf.fanout.config.RabbitMqUtils;

public class ReceiveLogs2 {
    private static final String EXCHANGE_NAME = "fanout::exchange";

    public static void main(String[] args) throws Exception {
        // 获取信道
        Channel channel = RabbitMqUtils.getChannel();
        // 创建交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        // 声明一个临时队列,队列名称是随机的
        // 当消费者断开连接时，删除队列
        String queueName = channel.queueDeclare().getQueue();
        // 等待接受消息，输出消息
        channel.queueBind(queueName,EXCHANGE_NAME,"");
        // 接收消息的lambda
        System.out.println("等待接收消息并打印");
        // 取消消息的lambda
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogs02 控制台打印接收到的消息： " + new String(message.getBody(),"UTF-8"));
        };

        channel.basicConsume(queueName,true,deliverCallback,consumerTag -> {});
    }
}
