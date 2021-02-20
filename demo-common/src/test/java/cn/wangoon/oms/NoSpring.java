package cn.wangoon.oms;

import cn.wangoon.common.exception.BusinessException;
import org.junit.jupiter.api.Test;

public class NoSpring {

    @Test
    public void test123() {

        String text="6232040986911 \n" +
                "6242040499931 \n" +
                "6232040589502 \n" +
                "6242041707528 \n" +
                "6242041403745 \n" +
                "6242041707528 \n" +
                "6242041900596 \n" +
                "6242041307506 \n" +
                "6242041209708 \n" +
                "6242041207056 \n" +
                "6242041713872 \n" +
                "6242041113925 \n" +
                "6242041015956 \n" +
                "6242041729652 \n" +
                "6242041725607 \n" +
                "6242041921141 \n" +
                "6242041229002 \n" +
                "6242041733732 \n" +
                "6242041925027 \n" +
                "6242041029034 \n" +
                "6242041423795 \n" +
                "6242041529613 \n" +
                "6242041837718 \n" +
                "6242041031815 \n" +
                "6242041444653 \n" +
                "6242041339151 \n" +
                "6242041832974 \n" +
                "6232039827655";
       String[] cccc= text.split("\n");
       StringBuilder builder=new StringBuilder();
       for (String aaa:cccc){
           builder.append("'"+aaa+"',\n");
       }
    System.out.println(builder);

    }

    @Test
    public void test1223() {
        try {
            int i = 1 / 0;
        } catch (Exception e) {
            throw new BusinessException("除数不能大于0");
        } finally {
            System.out.println("记录保存");
        }
    }
}
