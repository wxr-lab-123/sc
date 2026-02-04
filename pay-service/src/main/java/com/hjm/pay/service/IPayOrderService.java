package com.hjm.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hjm.pay.domin.dto.PayApplyDTO;
import com.hjm.pay.domin.dto.PayOrderFormDTO;
import com.hjm.pay.domin.po.PayOrder;


/**
 * <p>
 * 支付订单 服务类
 * </p>
 *
 * @author 虎哥
 * @since 2023-05-16
 */
public interface IPayOrderService extends IService<PayOrder> {

    String applyPayOrder(PayApplyDTO applyDTO);

    void tryPayOrderByBalance(PayOrderFormDTO payOrderFormDTO);
}
