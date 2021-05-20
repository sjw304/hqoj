package top.quezr.hqoj.controller.dto;

import lombok.Data;
import top.quezr.hqoj.enums.ItemType;
import top.quezr.hqoj.enums.LikeType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/20 11:18
 */
@Data
public class LikeDto {

    @NotNull
    private ItemType itemType;

    @NotNull
    private LikeType type;

    @Min(1)
    private Integer itemId;
}
