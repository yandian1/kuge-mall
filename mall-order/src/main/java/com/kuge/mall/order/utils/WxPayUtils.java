package com.kuge.mall.order.utils;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.*;
import com.kuge.mall.common.constant.PayStatusEnum;
import com.kuge.mall.common.utils.CustomException;
import com.kuge.mall.common.utils.HttpUtils;
import com.kuge.mall.order.component.WxPayProperties;
import org.springframework.stereotype.Component;
import com.wechat.pay.java.service.payments.model.Transaction;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * created by xbxie on 2024/6/1
 */
@Component
public class WxPayUtils {
    @Resource
    private WxPayProperties wxPayProperties;

    @Resource
    private NativePayService nativePayService;

    @Resource
    private Config config;

    // 创建 native 订单
    // 合并支付、单独支付
    public String createNativeOrder(Integer total, String orderNo, String desc, String attach) {
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();

        amount.setTotal(total);
        request.setAmount(amount);
        request.setAppid(wxPayProperties.getAppId());
        request.setMchid(wxPayProperties.getMchId());
        request.setDescription(desc);
        request.setNotifyUrl(wxPayProperties.getNotifyDomain());
        request.setOutTradeNo(orderNo);
        request.setAttach(attach);

        try {
            PrepayResponse response = nativePayService.prepay(request);
            return response.getCodeUrl();
        } catch (ServiceException exception) {
            throw new CustomException(exception.getErrorMessage());
        } catch (Exception exception) {
            throw new CustomException(exception.getMessage());
        }

    }

    // 查询 native 状态
    public PayStatusEnum queryNativeOrder(String orderNo) {
        QueryOrderByOutTradeNoRequest queryRequest = new QueryOrderByOutTradeNoRequest();
        queryRequest.setMchid(wxPayProperties.getMchId());
        queryRequest.setOutTradeNo(orderNo);

        PayStatusEnum status = null;
        Transaction transaction = nativePayService.queryOrderByOutTradeNo(queryRequest);
        Transaction.TradeStateEnum tradeState = transaction.getTradeState();

        switch (tradeState) {
            case SUCCESS:
                status = PayStatusEnum.PAID;
                break;
            case REFUND:
                status = PayStatusEnum.REFUNDED;
                break;
            case NOTPAY:
                status = PayStatusEnum.UN_PAY;
                break;
            case CLOSED:
                status = PayStatusEnum.CLOSED;
                break;
        }

        return status;
    }

    // 解析支付通知
    public Transaction parsePayNotify(HttpServletRequest request) {
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

        NotificationParser parser = new NotificationParser((NotificationConfig)config);

        return parser.parse(requestParam, Transaction.class);
    }
}
