package top.quezr.hqoj.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/19 16:39
 */
@Data
public class WriteSolutionDto {
    @NotNull
    Integer pid;
    Integer[] tags;
    @NotBlank
    String title;
    @NotBlank
    String content;
}
