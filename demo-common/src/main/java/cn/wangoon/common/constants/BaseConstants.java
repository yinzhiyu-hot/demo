package cn.wangoon.common.constants;

/**
 * @Description 基础常量
 * @Remark
 * @PackagePath cn.wangoon.common.constants.BaseConstants
 * @Author Thomas
 * @Date 2021/2/20 17:31
 * @Version 1.0.0.0
 **/
public interface BaseConstants {

    /**
     * springboot环境的端口key
     */
    String LOCAL_SERVER_PORT = "local.server.port";

    /**
     * 拆分字符
     */
    char SPLIT_CHAR_DUN_HAO = ',';
    char SPLIT_CHAR_JIN_HAO = '#';
    char SPLIT_CHAR_SHU_XIAN = '|';
    char SPLIT_CHAR_UNDER_LNE = '_';
    CharSequence SPLIT_CHAR_SEQUENCE_DUN_HAO = ",";
    CharSequence SPLIT_CHAR_SEQUENCE_JIN_HAO = "#";
    CharSequence SPLIT_CHAR_SEQUENCE_SHU_XIAN = "|";
    CharSequence SPLIT_CHAR_SEQUENCE_UNDER_LNE = "_";

    /**
     * 格式化拼接常量
     */
    String STRING_FORMAT_TWO_CONCAT = "%s_%s";
    String STRING_FORMAT_THREE_CONCAT = "%s_%s_%s";

}
