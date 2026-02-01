package com.hjm.item.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hjm.item.domin.dto.OrderDetailDTO;
import com.hjm.item.domin.po.Item;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author cyw
 * @since 2026-01-30
 */
public interface ItemMapper extends BaseMapper<Item> {

    @Update("UPDATE item SET stock = stock - #{num} WHERE id = #{itemId}")
    void updateStock(OrderDetailDTO orderDetail);
}
