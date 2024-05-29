package online.fadai.api.tencent.wxPay.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.partnerpayments.jsapi.model.Transaction;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.jsapi.model.Amount;
import com.wechat.pay.java.service.payments.jsapi.model.Payer;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayResponse;
import com.wechat.pay.java.service.refund.RefundService;
import com.wechat.pay.java.service.refund.model.*;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import online.fadai.api.tencent.wxPay.pojo.WxPayOrderReqVO;
import online.fadai.api.tencent.wxPay.utils.WxPayUtil;
import online.fadai.api.tencent.wxPay.pojo.WxPayRespVO;
import online.fadai.api.tencent.wxPay.config.WxPayProperties;
import online.fadai.api.tencent.wxPay.service.WxPayService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.wechat.pay.java.core.http.Constant.*;

@Service
public class WxPayServiceImpl implements WxPayService {

    @Resource
    private WxPayProperties wxPayProperties;
    @Resource
    private RSAAutoCertificateConfig config;

    @Override
    public WxPayRespVO createOrder(WxPayOrderReqVO req) throws Exception {
        try {
            JsapiService service = new JsapiService.Builder().config(config).build();
            PrepayRequest request = new PrepayRequest();
            Amount amount = new Amount();
            amount.setTotal(req.getTotalPrice());
            request.setAmount(amount);
            request.setAppid(wxPayProperties.getAppId());
            request.setMchid(wxPayProperties.getMchId());
            request.setDescription(req.getGoodsName());
            request.setNotifyUrl(wxPayProperties.getNotifyUrl());
            request.setOutTradeNo(req.getOrderSn().toString());
            request.setAttach(req.getOrderType());
            Payer payer = new Payer();
            payer.setOpenid(req.getOpenId());
            request.setPayer(payer);
            // 调用下单方法，得到应答，开始封装WxPayRespVO对象
            PrepayResponse response = service.prepay(request);
            WxPayRespVO vo = new WxPayRespVO();
            Long timeStamp = System.currentTimeMillis() / 1000;
            vo.setTimeStamp(timeStamp);
            String substring = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
            vo.setNonceStr(substring);
            String signatureStr = Stream.of(wxPayProperties.getAppId(), String.valueOf(timeStamp), substring, "prepay_id=" + response.getPrepayId())
                    .collect(Collectors.joining("\n", "", "\n"));
            String sign = WxPayUtil.getSign(signatureStr, wxPayProperties.getKeyPath());
            vo.setPaySign(sign);
            vo.setPrepayId("prepay_id=" + response.getPrepayId());
            // 前端拿到此对象即可调用微信支付接口
            return vo;
        } catch (ServiceException e) {
            JSONObject parse = JSONObject.parseObject(e.getResponseBody());
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Transaction payNotify(HttpServletRequest request) {
        try {
            //读取请求体的信息
            ServletInputStream inputStream = request.getInputStream();
            StringBuilder stringBuffer = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String s;
            //读取回调请求体
            while ((s = bufferedReader.readLine()) != null) {
                stringBuffer.append(s);
            }
            String s1 = stringBuffer.toString();
            String timestamp = request.getHeader(WECHAT_PAY_TIMESTAMP);
            String nonce = request.getHeader(WECHAT_PAY_NONCE);
            String signType = request.getHeader("Wechatpay-Signature-Type");
            String serialNo = request.getHeader(WECHAT_PAY_SERIAL);
            String signature = request.getHeader(WECHAT_PAY_SIGNATURE);
            // 如果已经初始化了 RSAAutoCertificateConfig，可直接使用
            // 初始化 NotificationParser
            NotificationParser parser = new NotificationParser(config);
            RequestParam requestParam = new RequestParam.Builder()
                    .serialNumber(serialNo)
                    .nonce(nonce)
                    .signature(signature)
                    .timestamp(timestamp)
                    // 若未设置signType，默认值为 WECHATPAY2-SHA256-RSA2048
                    .signType(signType)
                    .body(s1)
                    .build();
            return parser.parse(requestParam, Transaction.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean refund(String orderId, Integer money) {
        try {
            // 构建退款service
            RefundService service = new RefundService.Builder().config(config).build();
            //构建退款请求
            CreateRequest request = new CreateRequest();
            // request.setXxx(val)设置所需参数，具体参数可见Request定义
            //构建订单金额信息
            AmountReq amountReq = new AmountReq();
            //退款金额
            amountReq.setRefund(Long.valueOf(money));
            //原订单金额
            amountReq.setTotal(Long.valueOf(money));
            //货币类型(默认人民币)
            amountReq.setCurrency("CNY");
            request.setAmount(amountReq);
            request.setTransactionId(orderId);
            request.setReason("退款");
            //商户退款单号
            request.setOutRefundNo(orderId);
            //退款通知回调地址，开设可不设，我是没有设置的
            //如果要设置就在配置加上对应的参数和参数值即可
            // request.setNotifyUrl(wxPayV3Bean.getRefundNotify());

            // 调用微信sdk退款接口
            Refund refund = service.create(request);
            //接收退款返回参数
            if (!Status.SUCCESS.equals(refund.getStatus().SUCCESS)) {
                throw new RuntimeException();
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 退款回调
     *
     */
    @Override
    public void refundNotify(HttpServletRequest request) {
        try {
            //读取请求体的信息
            ServletInputStream inputStream = request.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String s;
            //读取回调请求体
            while ((s = bufferedReader.readLine()) != null) {
                stringBuffer.append(s);
            }
            String s1 = stringBuffer.toString();
            String timestamp = request.getHeader(WECHAT_PAY_TIMESTAMP);
            String nonce = request.getHeader(WECHAT_PAY_NONCE);
            String signType = request.getHeader("Wechatpay-Signature-Type");
            String serialNo = request.getHeader(WECHAT_PAY_SERIAL);
            String signature = request.getHeader(WECHAT_PAY_SIGNATURE);
            // 如果已经初始化了 RSAAutoCertificateConfig，可直接使用
            // 初始化 NotificationParser
            NotificationParser parser = new NotificationParser(config);
            RequestParam requestParam = new RequestParam.Builder()
                    .serialNumber(serialNo)
                    .nonce(nonce)
                    .signature(signature)
                    .timestamp(timestamp)
                    // 若未设置signType，默认值为 WECHATPAY2-SHA256-RSA2048
                    .signType(signType)
                    .body(s1)
                    .build();
            RefundNotification parse = parser.parse(requestParam, RefundNotification.class);
            System.out.println("parse = " + parse);
            //parse.getRefundStatus().equals("SUCCESS");说明退款成功

            //这里和上面退款返回差不多的处理，可以抽成一个公共的方法
            if (!Status.SUCCESS.equals(parse.getRefundStatus().SUCCESS)) {
                //你的业务代码，根据请求返回状态修改对应订单状态
                throw new RuntimeException();
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

}
