package top.quezr.hqoj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.quezr.hqoj.enums.ItemType;

/**
 * <p>
 *
 * </p>
 *
 * @author que
 * @since 2021-05-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class FavoriteItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private ItemType type;

    /**
     * 收藏夹id
     */
    private Integer favritesId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer itemId;

    @TableField(exist = false)
    private String itemTitle;


}
