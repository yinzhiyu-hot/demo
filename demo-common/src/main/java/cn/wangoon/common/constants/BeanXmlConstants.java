package cn.wangoon.common.constants;

/**
 * JavaBean,xml 互转常量类
 *
 * @author : yinzhiyu
 * @version V1.0
 * @date Date : 2019年08月13日 11:36
 */
public interface BeanXmlConstants {
    //xml转javabean 时，xml中取节点的匹配规则
    String FIELD_NAME = "FIELD_NAME";//javabean 字段名称
    String FIELD_ANNOTATION_NAME = "FIELD_ANNOTATION_NAME";//javabean @XmlElement注解里配置的名称,配合@XmlElementAnno注解
}
