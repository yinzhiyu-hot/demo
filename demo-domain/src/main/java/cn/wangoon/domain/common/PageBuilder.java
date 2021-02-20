package cn.wangoon.domain.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageBuilder<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分页对象
     */
    private Pager pager;

    /**
     * 结果集
     */
    private List<T> records;

    public static <T> PageBuilder<T> buildPage(IPage<T> iPage) {
        return buildPage(iPage.getRecords(), iPage);
    }

    public static <T> PageBuilder<T> buildPage(List<T> records, IPage<T> iPage) {
        return new PageBuilder<>(records,
                new Pager(iPage.getCurrent(), iPage.getSize(), iPage.getPages(), iPage.getTotal()));
    }

    private PageBuilder(List<T> records, Pager pager) {
        this.records = records;
        this.pager = pager;
    }
}
