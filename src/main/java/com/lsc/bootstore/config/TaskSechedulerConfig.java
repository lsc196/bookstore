package com.lsc.bootstore.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 开启定时任务
 */
@Configuration
@EnableScheduling
@ComponentScan("com.lsc.bootstore.service")
public class TaskSechedulerConfig {
}
