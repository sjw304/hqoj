package top.quezr.hqoj.controller.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.quezr.hqoj.enums.FavoriteItemType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/18 9:48
 */
@Data
public class AddFavoriteItemDto {

    @ApiModelProperty(value = "收藏项的类型")
    @NotNull
    private FavoriteItemType type;

    @ApiModelProperty(value = "收藏夹id",example = "1")
    @NotNull
    @Min(0)
    private Integer favritesId;

    @NotNull
    @Min(0)
    @ApiModelProperty(value = "收藏项id",example = "1")
    private Integer itemId;
}
