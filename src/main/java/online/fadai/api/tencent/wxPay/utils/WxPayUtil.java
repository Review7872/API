package online.fadai.api.tencent.wxPay.utils;

import com.wechat.pay.java.core.util.PemUtil;
import org.springframework.util.Base64Utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Random;

/**
 * 获取支付信息
 */
public class WxPayUtil {

    private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final Random RANDOM = new SecureRandom();

    /**
     * 获取paySign
     */
    public static String getSign(String signatureStr,String privateKey) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException, IOException, URISyntaxException {
        //replace 根据实际情况，不一定都需要
        String replace = privateKey.replace("\\n", "\n");
        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKeyFromPath(replace);
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(merchantPrivateKey);
        sign.update(signatureStr.getBytes(StandardCharsets.UTF_8));
        return Base64Utils.encodeToString(sign.sign());
    }

    /**
     * 获取随机字符串 Nonce Str
     */
    public static String generateNonceStr() {
        char[] nonceChars = new char[32];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }
}
