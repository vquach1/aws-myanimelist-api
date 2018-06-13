package config;

import awsUtils.MiscUtils;
import awsUtils.S3Utils;
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
}
