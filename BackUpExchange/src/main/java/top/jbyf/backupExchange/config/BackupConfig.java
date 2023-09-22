package top.jbyf.backupExchange.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class BackupConfig {
    // 交换机名称
    public static final String EXCHANGE_NAME = "backup_exchange";

    @Bean
    public FanoutExchange backupExchange(){
        return new FanoutExchange(EXCHANGE_NAME,true,false);
    }

    // 报警队列
    public static final String WARNING_QUEUE = "warning_queue";
    @Bean
    public Queue warningQueue(){
        return QueueBuilder.durable(WARNING_QUEUE).build();
    }

    @Bean
    public Binding warningQueueBindBackupExchange(@Qualifier("backupExchange") FanoutExchange backupExchange,
                                                  @Qualifier("warningQueue") Queue warningQueue){
        return BindingBuilder
                .bind(warningQueue)
                .to(backupExchange);
    }

    // 备份队列
    public static final String BACKUP_QUEUE = "backup_queue";
    @Bean
    public Queue backupQueue(){
        return QueueBuilder.durable(BACKUP_QUEUE).build();
    }

    @Bean
    public Binding backupQueueBindBackupExchange(@Qualifier("backupExchange") FanoutExchange backupExchange,
                                                 @Qualifier("backupQueue") Queue backupQueue){
        return BindingBuilder
                .bind(backupQueue)
                .to(backupExchange);
    }

}
