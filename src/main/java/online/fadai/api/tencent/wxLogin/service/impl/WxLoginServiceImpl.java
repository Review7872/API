package online.fadai.api.tencent.wxLogin.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import online.fadai.api.tencent.wxLogin.config.WxLoginProperties;
import online.fadai.api.tencent.wxLogin.service.WxLoginService;
import online.fadai.api.tencent.wxLogin.utils.HttpClientUtil;
import org.springframework.stereotype.Service;

@Service
public class WxLoginServiceImpl implements WxLoginService {
    @Resource
    private WxLoginProperties wxLoginProperties;
    @Override
    public JSONObject getUserInfo(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + wxLoginProperties.getAppId() + "&secret=" + wxLoginProperties.getAppSecret() + "&js_code=" + code + "&grant_type=authorization_code";
        return JSON.parseObject(HttpClientUtil.doGet(url));
    }
}
