package online.fadai.api.tencent.wxPay.service;

import com.wechat.pay.java.service.partnerpayments.jsapi.model.Transaction;
import jakarta.servlet.http.HttpServletRequest;
import online.fadai.api.tencent.wxPay.pojo.WxPayOrderReqVO;
import online.fadai.api.tencent.wxPay.pojo.WxPayRespVO;

public interface WxPayService {

    /**
     * 功能描述:
     *      传递预支付信息，拉起微信预支付，返回完整的微信支付参数
     *      预支付具体参数可见微信小程序官方文档：
     *
     */
    WxPayRespVO createOrder(WxPayOrderReqVO req) throws Exception;

    /**
     * 功能描述:
     *      微信支付回调，微信支付成功才会触发，需要开放在公网，返回参数里面封装了支付的具体信息（退款用到的订单号等等）
     *      该返回值具体信息可见微信支付官方文档：
     *      https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7&index=8
     *
     */
    Transaction payNotify(HttpServletRequest request);

    /**
     *  微信小程序退款，需要提供订单号与退款金额，订单号封装在 payNotify() 返回体中，退款金额不能超过订单金额
     */
    Boolean refund(String orderId, Integer money);

    /**
     *  退款回调
     */
    void refundNotify(HttpServletRequest request);

}
