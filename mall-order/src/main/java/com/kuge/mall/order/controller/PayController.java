package com.kuge.mall.order.controller;

import com.kuge.mall.common.utils.R;
import com.kuge.mall.order.service.PayService;
import com.kuge.mall.order.vo.*;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * created by xbxie on 2024/5/31
 */
@RestController
@RequestMapping("/pay")
public class PayController {

    @Resource
    private PayService payService;

    @PostMapping("/status")
    public R<String> wxPayStatus(@RequestBody PayStatusReqVo payStatusReqVo) {
        return payService.wxPayStatus(payStatusReqVo);
    }

    @PostMapping("/result")
    public R<PayResultResVo> wxPayResult(@RequestBody PayResultReqVo payResultReqVo) {
        return payService.wxPayResult(payResultReqVo);
    }

    @PostMapping("/pre/info")
    public R<PreInfoResVo> getPreInfo(@RequestBody PreInfoReqVo preInfoReqVo) {
        return payService.getPreInfo(preInfoReqVo);
    }

    @PostMapping("/codeUrl")
    public R<String> getCodeUrl(@RequestBody CodeUrlReqVo codeUrlReqVo) {
        return payService.getCodeUrl(codeUrlReqVo);
    }

    @PostMapping("/notify")
    public void notify(HttpServletRequest request) {
        payService.notify(request);
    }
}
