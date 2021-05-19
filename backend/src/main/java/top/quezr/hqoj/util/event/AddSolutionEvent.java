package top.quezr.hqoj.util.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.quezr.hqoj.entity.Solution;

/**
 * @author que
 * @version 1.0
 * @date 2021/5/19 17:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddSolutionEvent {
    Solution solution;
}
