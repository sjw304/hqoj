package top.quezr.hqoj.controller.dto;

import lombok.Data;
import top.quezr.hqoj.enums.JudgeStauts;
import top.quezr.hqoj.enums.LanguageType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/21 17:19
 */
@Data
public class SubmitDto {

    @NotNull
    @Min(1)
    private Integer pid;

    @NotNull
    private LanguageType languageType;

    @NotBlank
    private String code;

}
