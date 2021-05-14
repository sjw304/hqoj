package top.quezr.hqoj.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
public class PageInfo {

    public static final Integer DEFAULT_PAGE_SIZE = 20;
    public static final Integer DEFAULT_PAGE_NUMBER = 0;
    public static final Integer DEFAULT_LAST_ID = 0;

    @ApiModelProperty(value = "每页大小",example = "20")
    private Integer pageSize;
    @ApiModelProperty(value = "第几页",example = "0")
    private Integer pageNumber;
    @ApiModelProperty(value = "上一页最后一项的id",example = "0")
    private Integer lastId;

    /**
     * 返回一个绝对属性正常的
     * @param pageInfo
     * @return
     */
    public static  PageInfo normalizing(PageInfo pageInfo) {
        if (Objects.isNull(pageInfo)){
            return new PageInfo(DEFAULT_PAGE_SIZE,DEFAULT_PAGE_NUMBER,DEFAULT_LAST_ID);
        }
        pageInfo.pageNumber = normalizingPageNumber(pageInfo.pageNumber);
        pageInfo.pageSize = normalizingPageSize(pageInfo.pageSize);
        return pageInfo;
    }

    public static Integer normalizingPageSize(Integer pageSize){
        if (Objects.isNull(pageSize) || pageSize<1){
            log.info("change pageSize to 20");
            pageSize = PageInfo.DEFAULT_PAGE_SIZE;
        }
        return pageSize;
    }

    public static Integer normalizingPageNumber(Integer pageNumber){
        if(Objects.isNull(pageNumber) || pageNumber<1){
            log.info("change pageNumber to 0");
            pageNumber = PageInfo.DEFAULT_PAGE_NUMBER;
        }
        return pageNumber;
    }

    public static Integer normalizingLastId(Integer lastId){
        if (Objects.isNull(lastId) || lastId < 0){
            lastId = 0;
        }
        return lastId;
    }
}
