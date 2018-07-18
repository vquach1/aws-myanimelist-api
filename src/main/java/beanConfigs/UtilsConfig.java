package beanConfigs;

import hibernateUtils.daos.GenericDao;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import utils.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilsConfig {
    @Bean
    public MiscUtils miscUtils() {
        return new MiscUtils();
    }

    @Bean
    public S3Utils s3Utils() {
        return new S3Utils("mal-scrape", 200);
    }

    @Bean
    public Downloader downloader() {
        return new Downloader(12000, "https://myanimelist.net/");
    }

    @Bean
    public GenericDao hibernateUtils() {
        return new GenericDao();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        return executor;
    }

    @Bean
    public TaskListener taskListener() {
        return new TaskListener();
    }
}
