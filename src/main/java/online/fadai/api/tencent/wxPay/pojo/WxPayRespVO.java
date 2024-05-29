package online.fadai.api.tencent.wxPay.pojo;

import java.io.Serial;
import java.io.Serializable;

/**
 * 微信支付请求发起所需参数，这里需要封装后传递给前端，
 */
public class WxPayRespVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 预支付交易会话标识小程序下单接口返回的prepay_id参数值
     */
    private String prepayId;
    /**
     * 随机字符串
     */
    private String nonceStr;
    /**
     * 时间戳
     */
    private Long timeStamp;
    /**
     * 签名
     */
    private String paySign;

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }

    public WxPayRespVO(String prepayId, String nonceStr, Long timeStamp, String paySign) {
        this.prepayId = prepayId;
        this.nonceStr = nonceStr;
        this.timeStamp = timeStamp;
        this.paySign = paySign;
    }

    public WxPayRespVO() {
    }
}
