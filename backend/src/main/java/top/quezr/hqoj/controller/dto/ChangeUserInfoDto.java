package top.quezr.hqoj.controller.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.Email;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/17 9:45
 */
@ToString
@Data
@EqualsAndHashCode
public class ChangeUserInfoDto {

    public static final ChangeUserInfoDto EMPTY_DTO = new ChangeUserInfoDto();

    private String nickname;

    private String favicon;

    private String phone;

    private String introduction;

    private String github;

    private String website;

    private String wechat;
}
