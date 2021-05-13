package xyz.lizhaorong.queoj.util.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.lizhaorong.queoj.entity.User;

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
