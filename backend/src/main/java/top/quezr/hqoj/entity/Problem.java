package top.quezr.hqoj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import top.quezr.hqoj.enums.ProblemLevel;

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
public class Problem implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Id
    private Integer id;

    private String name;

    private Integer point;

    private ProblemLevel level;

    private String defaultCode;

    private String description;

    private String tags;

    @TableField("`like`")
    private Integer like;

    private Integer timeLimit;

    private Integer spaceLimit;

    public static Problem fromEs(ProblemSearch search){
        Problem p = new Problem();
        p.setId(search.getId());
        p.setName(search.getName());
        p.setLevel(ProblemLevel.of(search.getLevel()));
        p.setTags(search.getTags());
        p.setPoint(search.getPoint());
        return p;
    }


}
