package top.quezr.hqoj.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.expression.Lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/13 15:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
@Slf4j
public class PageInfo<T> {

    public static final Integer DEFAULT_PAGE_SIZE = 20;
    public static final Integer DEFAULT_PAGE_NUMBER = 0;
    public static final Integer DEFAULT_LAST_ID = 0;
    public static final Integer DEFAULT_TOTAL_COUNT = 0;

    @ApiModelProperty(value = "每页大小",example = "20")
    private Integer pageSize;
    @ApiModelProperty(value = "第几页",example = "0")
    private Integer pageNumber;
    @ApiModelProperty(value = "上一页最后一项的id",example = "0")
    private Integer lastId;
    @ApiModelProperty(value = "返回的数据的总数（分页前）",example = "0")
    private Integer totalCount;
    @ApiModelProperty(value = "返回的数据列表",example = "0")
    private List<T> data;




    /**
     * 返回一个绝对属性正常的pageInfo
     * @param pageInfo pageInfo
     * @return pageInfo
     */
    public static <T>  PageInfo<T> normalizing(PageInfo<T> pageInfo) {
        if (Objects.isNull(pageInfo)){
            return new PageInfo<>(DEFAULT_PAGE_SIZE, DEFAULT_PAGE_NUMBER, DEFAULT_LAST_ID, DEFAULT_TOTAL_COUNT, Collections.emptyList());
        }
        pageInfo.pageNumber = normalizingPageNumber(pageInfo.pageNumber);
        pageInfo.pageSize = normalizingPageSize(pageInfo.pageSize);
        pageInfo.lastId = normalizingLastId(pageInfo.lastId);
        pageInfo.totalCount = normalizingTotalCount(pageInfo.totalCount);
        return pageInfo;
    }

    public static Integer normalizingPageSize(Integer pageSize){
        if (Objects.isNull(pageSize) || pageSize<1){
            pageSize = PageInfo.DEFAULT_PAGE_SIZE;
        }
        return pageSize;
    }

    public static Integer normalizingPageNumber(Integer pageNumber){
        if(Objects.isNull(pageNumber) || pageNumber<1){
            pageNumber = PageInfo.DEFAULT_PAGE_NUMBER;
        }
        return pageNumber;
    }

    public static Integer normalizingLastId(Integer lastId){
        if (Objects.isNull(lastId) || lastId < 0){
            lastId = PageInfo.DEFAULT_LAST_ID;
        }
        return lastId;
    }

    public static Integer normalizingTotalCount(Integer totalCount){
        if (Objects.isNull(totalCount) || totalCount < 0){
            totalCount = PageInfo.DEFAULT_TOTAL_COUNT;
        }
        return totalCount;
    }
}
