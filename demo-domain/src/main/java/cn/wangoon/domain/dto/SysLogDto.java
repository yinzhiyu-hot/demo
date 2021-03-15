package cn.wangoon.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description 系统日志
 * @PackagePath cn.wangoon.domain.dto.SysLogDto
 * @Author YINZHIYU
 * @Date 2020/5/25 13:54
 * @Version 1.0.0.0
 **/
@Data
@ApiModel(description = "系统日志")
public class SysLogDto implements Serializable {

    /**
     * hashKey
     */
    @ApiModelProperty(value = "hashKey")
    private String hashKey;

    /**
     * 订单号
     */
    @ApiModelProperty(value = "订单号")
    private String orderNumber;

    /**
     * 平台
     */
    @ApiModelProperty(value = "平台")
    private String platform;

    /**
     * 站点
     */
    @ApiModelProperty(value = "站点")
    private String site;

    /**
     * 商户
     */
    @ApiModelProperty(value = "商户")
    private String merchant;

    /**
     * 平台同步到的商品编码
     */
    @ApiModelProperty(value = "平台同步到的商品编码")
    private String originalFbmSku;

    /**
     * 平台同步到的商品编码（清洗后缀的）
     */
    @ApiModelProperty(value = "平台同步到的商品编码（清洗后缀的）")
    private String fbmSku;

    /**
     * wms对应的商品编码sku
     */
    @ApiModelProperty(value = "站点列表")
    private String platSku;

    /**
     * wms对应的商品编码sku
     */
    @ApiModelProperty(value = "wms对应的商品编码platsku")
    private String sku;

    /**
     * wms对应的商品编码upc
     */
    @ApiModelProperty(value = "wms对应的商品编码upc")
    private String upc;

    /**
     * wms对应的商品编码fnsku
     */
    @ApiModelProperty(value = "wms对应的商品编码fnsku")
    private String fnSku;

    /**
     * 消息
     */
    @ApiModelProperty(value = "消息")
    private String message;

    /**
     * 记录时间
     */
    @ApiModelProperty(value = "记录时间")
    private Date recordDate;

    /**
     * 查询日期
     */
    @ApiModelProperty(value = "查询日期")
    private String queryDate;

    public SysLogDto() {
    }

    /**
     * 构造
     *
     * @param orderNumber
     * @param platform
     * @param site
     * @param fbmSku
     * @param platSku
     * @param sku
     * @param upc
     * @param fnSku
     * @param message
     */
    public SysLogDto(String platform, String site, String orderNumber, String fbmSku, String platSku, String sku, String upc, String fnSku, String message) {
        this.orderNumber = orderNumber;
        this.platform = platform;
        this.site = site;
        this.fbmSku = fbmSku;
        this.platSku = platSku;
        this.sku = sku;
        this.upc = upc;
        this.fnSku = fnSku;
        this.message = message;
        this.recordDate = new Date();
    }

    /*
     * @Description 构造
     * @Params ==>
     * @Param platform
     * @Param site
     * @Param merchant
     * @Param orderNumber
     * @Param fbmSku
     * @Param platSku
     * @Param sku
     * @Param upc
     * @Param fnSku
     * @Param message
     * @Return
     * @Date 2020/6/5 18:11
     * @Auther YINZHIYU
     */
    public SysLogDto(String platform, String site, String merchant, String orderNumber, String fbmSku, String platSku, String sku, String upc, String fnSku, String message) {
        this.orderNumber = orderNumber;
        this.platform = platform;
        this.site = site;
        this.merchant = merchant;
        this.fbmSku = fbmSku;
        this.platSku = platSku;
        this.sku = sku;
        this.upc = upc;
        this.fnSku = fnSku;
        this.message = message;
        this.recordDate = new Date();
    }

    /**
     * 构造
     *
     * @param message
     */
    public SysLogDto(String message) {
        this.message = message;
        this.recordDate = new Date();
    }

    /**
     * 构造
     *
     * @param platform
     * @param site
     * @param message
     */
    public SysLogDto(String platform, String site, String message) {
        this.platform = platform;
        this.site = site;
        this.message = message;
        this.recordDate = new Date();
    }

    /**
     * 构造
     *
     * @param platform
     * @param site
     * @param merchant
     * @param message
     */
    public SysLogDto(String platform, String site, String merchant, String message) {
        this.platform = platform;
        this.site = site;
        this.merchant = merchant;
        this.message = message;
        this.recordDate = new Date();
    }

    /*
     * @Description 构造
     * @Params ==>
     * @Param platform
     * @Param site
     * @Param merchant
     * @Param orderNumber
     * @Param message
     * @Return
     * @Date 2020/6/5 18:13
     * @Auther YINZHIYU
     */
    public SysLogDto(String platform, String site, String merchant, String orderNumber, String message) {
        this.orderNumber = orderNumber;
        this.platform = platform;
        this.site = site;
        this.merchant = merchant;
        this.message = message;
        this.recordDate = new Date();
    }

    /*
     * @Description 构造
     * @Params ==>
     * @Param orderNumber
     * @Param message
     * @Return
     * @Date 2020/6/8 15:37
     * @Auther YINZHIYU
     */
    public SysLogDto(String orderNumber, String message) {
        this.orderNumber = orderNumber;
        this.message = message;
        this.recordDate = new Date();
    }
}
