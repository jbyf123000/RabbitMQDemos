package top.jbyf.confirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;
import top.jbyf.confirm.config.RabbitMqUtils;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

public class Publisher {
    // 批量发消息的个数
    public static final int MESSAGE_COUNT = 1000;

    /*  开启发布确认
                    生产者将信道设置成 confirm 模式，一旦信道进入 confirm 模式，所有在该信道上面发布的
                消息都将会被指派一个唯一的 ID(从 1 开始)，一旦消息被投递到所有匹配的队列之后，
                broker 就会发送一个确认给生产者(包含消息的唯一 ID)，这就使得生产者知道消息已经正确到达目的队列了，
                如果消息和队列是可持久化的，那么确认消息会在将消息写入磁盘之后发出，
                broker 回传给生产者的确认消息中 delivery-tag 域包含了确认消息的序列号，
                此外 broker 也可以设置 basic.ack 的 multiple 域，表示到这个序列号之前的所有消息都已经得到了处理。
                confirm 模式最大的好处在于他是异步的，一旦发布一条消息，
                生产者应用程序就可以在等信道返回确认的同时继续发送下一条消息，
                当消息最终得到确认之后，生产者应用便可以通过回调 方法来处理该确认消息，
                如果 RabbitMQ 因为自身内部错误导致消息丢失，就会发送一条 nack 消息，
                生产者应用程序同样可以在回调方法中处理该 nack 消息。
             */
    public static void main(String[] args) throws Exception {
//        publishMessageIndividually();
        //单个确认发布 1000 次工作时间： 10163  ms
//        publishMessageBatch();
        // 批量确认发布 1000 次工作时间： 192  ms
        publishMessageAsync();
        // 异步确认发布 1000 次工作时间： 92  ms
    }

    /**
     * 单个确认模式
     *
     * @throws Exception
     */
    public static void publishMessageIndividually() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, false, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        long beginTime = System.currentTimeMillis();
        for (int i = 1; i <= MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            // 单个消息马上进行发布确认
            boolean flag = channel.waitForConfirms();
//            if (flag) {
//                System.out.println("消息发送成功");
//            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("单个确认发布 " + MESSAGE_COUNT + " 次工作时间： " + (endTime - beginTime) + "  ms");

    }

    public static void publishMessageBatch() throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, false, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        long beginTime = System.currentTimeMillis();

        // 批量确认消息的大小
        int batchSize = 100;
        for (int i = 1; i <= MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            if (i % batchSize == 0){
                channel.waitForConfirms();
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("批量确认发布 " + MESSAGE_COUNT + " 次工作时间： " + (endTime - beginTime) + "  ms");

    }


    /**
     * 异步发布确认
     * @throws Exception
     */
    public static void publishMessageAsync() throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, false, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        long beginTime = System.currentTimeMillis();
        ArrayList<String> nack = new ArrayList<>();

        // 消息发送成功
        ConfirmCallback ackCallback = (deliveryTag,multiple)->{
            System.out.println("确认的消息：" + deliveryTag);
        };
        // 消息发送失败
        ConfirmCallback nackCallback = (deliveryTag,multiple)->{
            System.out.println("未确认的消息：" + deliveryTag);
            nack.add(deliveryTag + "");
        };

        // 消息监听器,监听成功的消息和失败的消息
        channel.addConfirmListener(ackCallback,nackCallback);

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = "消息：" + i;
            channel.basicPublish("",queueName,null,message.getBytes("UTF-8"));
        }


        long endTime = System.currentTimeMillis();
        System.out.println("异步确认发布 " + MESSAGE_COUNT + " 次工作时间： " + (endTime - beginTime) + "  ms");
    }
}
