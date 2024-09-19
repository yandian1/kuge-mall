package com.kuge.mall.order.utils;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.BusinessParams;
import com.alipay.api.domain.ExtendParams;
import com.alipay.api.domain.GoodsDetail;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * created by xbxie on 2024/6/1
 */
@Component
public class AliPayUtils {
    @Resource
    private AlipayClient alipayClient;

    public String createPreOrder() {
        // 构造请求参数以调用接口
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        // model.setSubject("Iphone6 16G"); // xxx
        // model.setOutTradeNo("0987205320100101"); // xxx
        // model.setTotalAmount("88.88"); // xxx

        // 设置商户门店编号
        model.setStoreId("NJ_001");

        // 设置业务扩展参数
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088511833207846");
        extendParams.setSpecifiedSellerName("XXX的跨境小铺");
        extendParams.setCardType("S0JP0000");
        model.setExtendParams(extendParams);

        // 设置订单标题
        model.setSubject("Iphone6 16G"); // xxx

        // 设置商户操作员编号
        model.setOperatorId("yx_001");

        // 设置产品码
        model.setProductCode("FACE_TO_FACE_PAYMENT");

        // 设置订单附加信息
        model.setBody("Iphone6 16G");

        // 设置订单包含的商品列表信息
        List<GoodsDetail> goodsDetail = new ArrayList<GoodsDetail>();
        GoodsDetail goodsDetail0 = new GoodsDetail();
        goodsDetail0.setGoodsName("ipad");
        goodsDetail0.setQuantity(1L);
        goodsDetail0.setPrice("2000");
        goodsDetail0.setGoodsId("apple-01");
        goodsDetail0.setGoodsCategory("34543238");
        goodsDetail0.setCategoriesTree("124868003|126232002|126252004");
        goodsDetail0.setShowUrl("http://www.alipay.com/xxx.jpg");
        goodsDetail.add(goodsDetail0);
        model.setGoodsDetail(goodsDetail);

        // 设置商户的原始订单号
        model.setMerchantOrderNo("20161008001");

        // 设置可打折金额
        model.setDiscountableAmount("80.00");

        // 设置商户订单号
        model.setOutTradeNo("20150320010101001"); // xxx

        // 设置订单总金额
        model.setTotalAmount("88.88"); // xxx

        // 设置商户传入业务信息
        BusinessParams businessParams = new BusinessParams();
        // businessParams.setMcCreateTradeIp("127.0.0.1");
        model.setBusinessParams(businessParams);

        // 设置卖家支付宝用户ID
        model.setSellerId("2088102146225135");

        // 设置商户机具终端编号
        model.setTerminalId("NJ_T_001");

        request.setBizModel(model);
        // 第三方代调用模式下请设置app_auth_token
        // request.putOtherTextParam("app_auth_token", "<-- 请填写应用授权令牌 -->");

        AlipayTradePrecreateResponse response;
        try {
            response = alipayClient.execute(request);
            System.out.println(response.getBody());

            return response.getBody();
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
    }
}
