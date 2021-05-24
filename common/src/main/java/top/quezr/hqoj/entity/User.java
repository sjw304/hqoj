package top.quezr.hqoj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import top.quezr.hqoj.enums.UserRole;

import java.io.Serializable;

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
@ToString
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private UserRole role;

    private String nickname;

    private String password;

    private String favicon;

    private String phone;

    private String email;

    private Integer coins;

    private Integer point;

    private String introduction;

    private String github;

    private String website;

    private String wechat;


}
