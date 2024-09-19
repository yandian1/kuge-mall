package com.kuge.mall.admin.utils;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.refund.RefundService;
import com.wechat.pay.java.service.refund.model.*;
import com.kuge.mall.admin.component.WxPayProperties;
import com.kuge.mall.common.utils.CustomException;
import com.kuge.mall.common.utils.HttpUtils;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * created by xbxie on 2024/6/1
 */
@Component
public class WxPayUtils {

    @Resource
    private Config config;

    @Resource
    private WxPayProperties wxPayProperties;

    @Resource
    private RefundService refundService;


    public Status applyRefund(String orderSn, String afterSaleSn, BigDecimal totalAmount, BigDecimal actualAmount) {
        CreateRequest request = new CreateRequest();
        AmountReq amountReq = new AmountReq();


        amountReq.setRefund(actualAmount.multiply(BigDecimal.valueOf(100)).longValue());
        amountReq.setTotal(totalAmount.multiply(BigDecimal.valueOf(100)).longValue());
        amountReq.setCurrency("CNY");


        request.setOutTradeNo(orderSn);
        request.setOutRefundNo(afterSaleSn);
        request.setAmount(amountReq);
        request.setNotifyUrl(wxPayProperties.getRefundCallback());
        System.out.println("refundCallback" + wxPayProperties.getRefundCallback());


        try {
            Refund refund = refundService.create(request);
            return refund.getStatus();
        } catch (ServiceException exception) {
            throw new CustomException(exception.getErrorMessage());
        } catch (Exception exception) {
            throw new CustomException(exception.getMessage());
        }
    }

    // 解析退款通知
    public RefundNotification parseRefundNotify(HttpServletRequest request) {
        String serialNumber = request.getHeader("wechatpay-serial");
        String nonce = request.getHeader("wechatpay-nonce");
        String signature = request.getHeader("wechatpay-signature");
        String timestamp = request.getHeader("wechatpay-timestamp");
        String signType = request.getHeader("wechatpay-signature-type");
        String body = HttpUtils.getBody(request);

        RequestParam requestParam = new RequestParam.Builder()
            .serialNumber(serialNumber.trim())
            .nonce(nonce.trim())
            .signature(signature.trim())
            .timestamp(timestamp.trim())
            .signType(signType.trim())
            .body(body)
            .build();

        NotificationParser parser = new NotificationParser((NotificationConfig) config);


        return parser.parse(requestParam, RefundNotification.class);
    }
}
