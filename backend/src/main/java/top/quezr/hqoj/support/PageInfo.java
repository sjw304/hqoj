package top.quezr.hqoj.support;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
@ToString
@ApiModel(description = "出入参通用实体")
public class PageInfo<T> {

    @ApiModelProperty(value = "(入参)每页大小",example = "20")
    private Integer pageSize;
    @ApiModelProperty(value = "(入参)第几页",example = "0")
    private Integer pageNumber;
    @ApiModelProperty(value = "(入参)上一页最后一项的id，如果这个参数>0，则pageNumber失效。",example = "0")
    private Integer lastId;
    @ApiModelProperty(value = "(入参)是否包含返回的数据总数（分页前）",example = "false")
    private Boolean hasCount;
    @ApiModelProperty(value = "(出参)返回的数据的总数（分页前）",example = "0")
    private Integer totalCount;
    @ApiModelProperty(value = "(出参)返回的数据列表",example = "0")
    private List<T> data;

    public static final Integer DEFAULT_PAGE_SIZE = 20;
    public static final Integer DEFAULT_PAGE_NUMBER = 0;
    public static final Integer DEFAULT_LAST_ID = 0;
    public static final Integer DEFAULT_TOTAL_COUNT = 0;
    public static final Boolean DEFAULT_HAS_COUNT = false;

    private static final PageInfo<Object> EMPTY_PAGE_INFO = new PageInfo<>(DEFAULT_PAGE_SIZE, DEFAULT_PAGE_NUMBER, DEFAULT_LAST_ID, DEFAULT_HAS_COUNT,DEFAULT_TOTAL_COUNT,Collections.emptyList());

    /**
     * 返回一个绝对属性正常的pageInfo
     * @param pageInfo pageInfo
     * @return pageInfo
     */
    public static <T>  PageInfo<T> normalizing(PageInfo<T> pageInfo) {
        if (Objects.isNull(pageInfo)){
            return new PageInfo<>(DEFAULT_PAGE_SIZE, DEFAULT_PAGE_NUMBER, DEFAULT_LAST_ID, DEFAULT_HAS_COUNT,DEFAULT_TOTAL_COUNT,Collections.emptyList());
        }
        pageInfo.pageNumber = normalizingPageNumber(pageInfo.pageNumber);
        pageInfo.pageSize = normalizingPageSize(pageInfo.pageSize);
        pageInfo.lastId = normalizingLastId(pageInfo.lastId);
        pageInfo.totalCount = normalizingTotalCount(pageInfo.totalCount);
        pageInfo.hasCount = normalizingHasCount(pageInfo.hasCount);
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

    public static Boolean normalizingHasCount(Boolean hasCount){
        if (Objects.isNull(hasCount)){
            hasCount = PageInfo.DEFAULT_HAS_COUNT;
        }
        return hasCount;
    }

    public static boolean isFirstPage(PageInfo pageInfo){
        return Objects.isNull(pageInfo)||(pageInfo.getPageNumber()==0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageInfo<?> pageInfo = (PageInfo<?>) o;
        return Objects.equals(pageSize, pageInfo.pageSize) && Objects.equals(pageNumber, pageInfo.pageNumber) && Objects.equals(lastId, pageInfo.lastId) && Objects.equals(hasCount, pageInfo.hasCount) && Objects.equals(totalCount, pageInfo.totalCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageSize, pageNumber, lastId, hasCount, totalCount);
    }
}
