package online.fadai.api.aliyun.oss.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OSSConfig {
    @Resource
    private OSSProperties ossProperties;
    @Bean
    public OSS ossConfig(){
        return new OSSClient(ossProperties.getPath(), ossProperties.getId(), ossProperties.getSecret());
    }
}
