package com.shop.manager;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class TaskPoolConfiguration implements AsyncConfigurer {

    // Declaring constants
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskPoolConfiguration.class);
    private static final int MAX_POOL_SIZE = 2;
    private static final int CORE_POOL_SIZE = 2;
    private static final int QUEUE_CAPACITY = 25;

    /**
     * 
     * Executor for managing Async functions
     */
    @Bean(name = "taskExecutor")
    @Override
    public Executor getAsyncExecutor() {
        LOGGER.debug("Creating Custom Executor");
        final ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setMaxPoolSize(MAX_POOL_SIZE);
        threadPoolTaskExecutor.setCorePoolSize(CORE_POOL_SIZE);
        threadPoolTaskExecutor.setQueueCapacity(QUEUE_CAPACITY);
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolTaskExecutor.setThreadNamePrefix("Custom-Executor");
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
    
}