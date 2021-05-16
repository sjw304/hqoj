package top.quezr.hqoj.util.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/12 10:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginEvent {

    private Integer userId;

}
