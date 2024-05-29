package online.fadai.api.tencent.wxPay.pojo;

/**
 * 微信预支付参数
 * 实际上，
 * com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest
 * 这个类才是库封装的预支付请求类，只是其内部参数过多，故对其二次封装
 */
public class WxPayOrderReqVO {
    /**
     *  附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用，实际情况下只有支付完成状态才会返回该字段。
     */
    private String orderType;
    /**
     * 金额
     */
    private Integer totalPrice;
    /**
     * 商品描述
     */
    private String goodsName;
    /**
     * 用户在普通商户AppID下的唯一标识。 下单前需获取到用户的OpenID，详见OpenID获取
     */
    private String openId;
    /**
     * 商户系统内部订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一。
     *  一般这里与项目订单id对应，可在回调接口中拿到此字段从而对订单库进行进一步操作
     */
    private Long orderSn;

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Long getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(Long orderSn) {
        this.orderSn = orderSn;
    }

    public WxPayOrderReqVO(String orderType, Integer totalPrice, String goodsName, String openId, Long orderSn) {
        this.orderType = orderType;
        this.totalPrice = totalPrice;
        this.goodsName = goodsName;
        this.openId = openId;
        this.orderSn = orderSn;
    }

    public WxPayOrderReqVO() {
    }
}

