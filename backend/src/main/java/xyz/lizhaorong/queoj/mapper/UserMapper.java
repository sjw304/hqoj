package xyz.lizhaorong.queoj.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xyz.lizhaorong.queoj.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author que
 * @since 2021-05-11
 */
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user where username = #{username}")
    User getUserByUserName(String username);

    @Select("select * from user where email = #{email}")
    User getUserByEmal(String email);

    @Insert("insert into user(email,password) values(#{e},#{p})")
    Integer register(@Param("e") String email,@Param("p") String password);
}
