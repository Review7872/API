package online.fadai.api.tencent.wxLogin.service;

import com.alibaba.fastjson2.JSONObject;

public interface WxLoginService {
    /**
     * 获取用户信息，传递前端拿到的用户code，返回JSONObject对象
     * 具体返回信息可见官方文档
     * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-login/code2Session.html
     */
    public JSONObject getUserInfo(String code);
}
