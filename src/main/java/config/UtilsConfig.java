package config;

import utils.Downloader;
import utils.MiscUtils;
import utils.S3Utils;
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
}
