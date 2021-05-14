package top.quezr.hqoj.util.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.quezr.hqoj.entity.User;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/12 10:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterEvent {
    User user;
}
