package xyz.lizhaorong.queoj.entity;

import lombok.Data;

import java.util.List;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/13 15:47
 */
@Data
public class PageInfo<T> {

    public static final Integer DEFAULT_PAGE_SIZE = 20;

    private Integer pageSize;
    private Integer pageNumber;
    private List<T> data;

}
