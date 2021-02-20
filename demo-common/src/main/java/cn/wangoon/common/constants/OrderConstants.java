package cn.wangoon.common.constants;

/**
 * @Description 订单常量
 * @PackagePath cn.wangoon.common.constants.OrderConstants
 * @Author YINZHIYU
 * @Date 2020/5/20 10:18
 * @Version 1.0.0.0
 **/
public interface OrderConstants {
    /**
     * 订单前缀
     */
    String prefix = "sb";

    /**
     * 订单规则
     */
    String format = "yyMMddHHmmssSSS";

    /**
     * FBA订单发运状态同步默认天数[16天前开始]
     */
    Integer ORDER_SHIPMENT_SYNC_DAY_OFFSET = -16;

    /**
     * 自发货订单结果状态同步默认天数[16天前开始]
     */
    Integer ORDER_RESULT_SYNC_DAY_OFFSET = -16;

    /**
     * 订单同步默认天数[2天前开始]
     */
    Integer ORDER_SYNC_DAY_OFFSET = -2;

    /**
     * 手动订单同步默认天数[2天前开始]
     */
    Integer HAN_ORDER_SYNC_DAY_OFFSET = -15;
}
