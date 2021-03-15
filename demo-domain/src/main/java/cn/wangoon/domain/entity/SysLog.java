package cn.wangoon.domain.entity;

import cn.hutool.core.date.DateUtil;
import cn.wangoon.common.constants.RedisConstants;
import cn.wangoon.common.utils.StringUtils;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description 系统日志
 * @Remark
 * @PackagePath cn.wangoon.domain.entity.SysLog
 * @Author YINZHIYU
 * @Date 2020/9/27 10:32
 * @Version 1.0.0.0
 **/
@ApiModel(description = "系统日志")
@Data
@TableName(value = "sys_log")
public class SysLog implements Serializable {
    /**
     * 日志主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "日志主键")
    private Long id;

    /**
     * 平台
     */
    @TableField(value = "platform")
    @ApiModelProperty(value = "平台")
    private String platform;

    /**
     * 站点
     */
    @TableField(value = "site")
    @ApiModelProperty(value = "站点")
    private String site;

    /**
     * 商户
     */
    @TableField(value = "merchant")
    @ApiModelProperty(value = "商户")
    private String merchant;

    /**
     * 业务键(订单号等等)
     */
    @TableField(value = "business_key")
    @ApiModelProperty(value = "业务键(订单号等等)")
    private String businessKey;

    /**
     * 平台同步到的商品编码
     */
    @TableField(value = "original_fbm_sku")
    @ApiModelProperty(value = "平台同步到的商品编码")
    private String originalFbmSku;

    /**
     * 平台同步到的商品编码（清洗后缀的）
     */
    @TableField(value = "fbm_sku")
    @ApiModelProperty(value = "平台同步到的商品编码（清洗后缀的）")
    private String fbmSku;

    /**
     * wms对应的商品编码platsku
     */
    @TableField(value = "plat_sku")
    @ApiModelProperty(value = "wms对应的商品编码platsku")
    private String platSku;

    /**
     * wms对应的商品编码sku
     */
    @TableField(value = "sku")
    @ApiModelProperty(value = "wms对应的商品编码sku")
    private String sku;

    /**
     * wms对应的商品编码upc
     */
    @TableField(value = "upc")
    @ApiModelProperty(value = "wms对应的商品编码upc")
    private String upc;

    /**
     * wms对应的商品编码fnsku
     */
    @TableField(value = "fn_sku")
    @ApiModelProperty(value = "wms对应的商品编码fnsku")
    private String fnSku;

    /**
     * 日志消息
     */
    @TableField(value = "message")
    @ApiModelProperty(value = "日志消息")
    private String message;

    /**
     * 记录日期
     */
    @TableField(value = "record_date")
    @ApiModelProperty(value = "记录日期")
    private String recordDate;

    /**
     * 时间戳
     */
    @TableField(value = "ts")
    @ApiModelProperty(value = "时间戳")
    private Date ts;

    public SysLog() {
    }

    public SysLog(String businessKey, String message) {
        this.businessKey = businessKey;
        this.message = StringUtils.splitString(message, 10000)[0];
        this.recordDate = DateUtil.format(DateUtil.date(), RedisConstants.LOGS_FORMAT);
    }

    public SysLog(String platform, String site, String businessKey, String message) {
        this.platform = platform;
        this.site = site;
        this.businessKey = businessKey;
        this.message = StringUtils.splitString(message, 10000)[0];
        this.recordDate = DateUtil.format(DateUtil.date(), RedisConstants.LOGS_FORMAT);
    }

    public SysLog(String platform, String site, String merchant, String businessKey, String message) {
        this.platform = platform;
        this.site = site;
        this.merchant = merchant;
        this.businessKey = businessKey;
        this.message = StringUtils.splitString(message, 10000)[0];
        this.recordDate = DateUtil.format(DateUtil.date(), RedisConstants.LOGS_FORMAT);
    }

    public SysLog(String platform, String site, String merchant, String businessKey, String originalFbmSku, String fbmSku, String platSku, String sku, String upc, String fnSku, String message) {
        this.platform = platform;
        this.site = site;
        this.merchant = merchant;
        this.businessKey = businessKey;
        this.originalFbmSku = originalFbmSku;
        this.fbmSku = fbmSku;
        this.platSku = platSku;
        this.sku = sku;
        this.upc = upc;
        this.fnSku = fnSku;
        this.message = StringUtils.splitString(message, 10000)[0];
        this.recordDate = DateUtil.format(DateUtil.date(), RedisConstants.LOGS_FORMAT);
    }

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_PLATFORM = "platform";

    public static final String COL_SITE = "site";

    public static final String COL_MERCHANT = "merchant";

    public static final String COL_BUSINESS_KEY = "business_key";

    public static final String COL_ORIGINAL_FBM_SKU = "original_fbm_sku";

    public static final String COL_FBM_SKU = "fbm_sku";

    public static final String COL_PLAT_SKU = "plat_sku";

    public static final String COL_SKU = "sku";

    public static final String COL_UPC = "upc";

    public static final String COL_FN_SKU = "fn_sku";

    public static final String COL_MESSAGE = "message";

    public static final String COL_RECORD_DATE = "record_date";

    public static final String COL_TS = "ts";
}