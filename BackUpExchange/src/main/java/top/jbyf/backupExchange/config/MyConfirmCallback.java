package top.jbyf.backupExchange.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class MyConfirmCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {
    // 注入
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private ConcurrentHashMap<String,String> sentMessage;

    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    // 交换机确认回调的方法
    // 1. 发消息，交换机收到信息了，回调
    // 1.1 correlationData 保存回调消息的id及相关信息
    // 1.2 交换机收到消息 ack = true
    // 1.3 cause null
    // 2. 发消息，交换机接受失败，回调
    // 2.1 correlationData 保存回调消息的id及相关信息
    // 2.2 交换机收到消息 ack = false
    // 2.3 cause 失败原因

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = null ==correlationData ? "" : correlationData.getId();
        if (ack){
            log.info("交换机已经收到 id 为 :{} 的消息:{}",id,sentMessage.get(id));
        }
        else {
            log.info("交换机没有收到 id 为 :{} 的消息:{}， 原因:{}",id,sentMessage.get(id),cause);
        }
    }


    //交换机无法将消息进行路由时，会将该消息返回给生产者
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.error("消息:{} ， 被交换机：{} 退回，原因：{}, 路由Key：{} ",
                new String(message.getBody()),
                exchange,
                replyText,
                routingKey
        );
    }


}
