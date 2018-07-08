package beanConfigs;

import org.springframework.core.task.SyncTaskExecutor;
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
        return new S3Utils();
    }

    @Bean
    public Downloader downloader() {
        return new Downloader(12000, "https://myanimelist.net/");
    }

    @Bean
    public HibernateUtils hibernateUtils() {
        return new HibernateUtils();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        return executor;
    }

    @Bean
    public Graph<Runnable> dependencyGraph() {
        return new Graph<>();
    }
}
