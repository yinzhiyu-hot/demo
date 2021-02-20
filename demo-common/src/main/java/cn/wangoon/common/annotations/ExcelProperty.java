package cn.wangoon.common.annotations;


import java.lang.annotation.*;

/**
 * @author rolker
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelProperty {

    /**
     * 表头名字
     */
    String excelTitle();

}
