package cn.wangoon.common.utils;

import lombok.Data;
import org.apache.poi.hssf.usermodel.HSSFRow;


/**
 * @author rolker
 */
@Data
public class ExportAndRowVO {


    /**
     * 具体的数据
     */
    private Object t;

    /**
     * excel 行数
     */
    private HSSFRow row;


    private Object p;

    public ExportAndRowVO(Object t, Object p, HSSFRow row) {
        this.t = t;
        this.p = p;
        this.row = row;
    }

    public ExportAndRowVO() {

    }

}
