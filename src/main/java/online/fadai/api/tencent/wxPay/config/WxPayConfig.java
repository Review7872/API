package online.fadai.api.tencent.wxPay.config;

import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WxPayConfig {
    @Resource
    private WxPayProperties wxPayProperties;

    /**
     * 调用微信支付服务的所需配置
     * 全局只允许存在一个
     */
    @Bean("wxConfigInit")
    public RSAAutoCertificateConfig getWXConfig() {
        return new RSAAutoCertificateConfig.Builder()
                .merchantId(wxPayProperties.getMchId())
                .privateKeyFromPath(wxPayProperties.getKeyPath())
                .merchantSerialNumber(wxPayProperties.getMchSerialNo())
                .apiV3Key(wxPayProperties.getApiKey())
                .build();
    }
}
