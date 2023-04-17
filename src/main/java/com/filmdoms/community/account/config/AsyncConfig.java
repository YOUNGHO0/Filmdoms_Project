package com.filmdoms.community.account.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Slf4j
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Bean
    public Executor mailAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3); // 기본 실행 대기 중 스레드 개수
        executor.setMaxPoolSize(10); // 동시 동작하는 최대 스레드 개수
        executor.setQueueCapacity(100); // CorePool 초과 시 Queue에 저장해 둔 후 꺼내서 실행
        executor.setThreadNamePrefix("MailAsyncExecutor-");
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) ->
                log.error("Exception handler for async method '" + method.toGenericString()
                        + "' threw unexpected exception itself. message={}", ex.getMessage());
    }
}
