package top.quezr.hqoj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.quezr.hqoj.enums.ProblemLevel;

/**
 * <p>
 *
 * </p>
 *
 * @author que
 * @since 2021-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Solution implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer uid;

    private Integer pid;

    private Integer like;

    private LocalDateTime time;

    private String userNickName;

    private String tags;

    private String title;

    private String content;

    private String summary;

    public static Solution fromEs(SolutionSearch search){
        Solution p = new Solution();
        p.setId(search.getId());
        p.setPid(search.getPid());
        p.setSummary(search.getSummary());
        p.setTags(search.getTags());
        p.setTitle(search.getTitle());
        return p;
    }

}
