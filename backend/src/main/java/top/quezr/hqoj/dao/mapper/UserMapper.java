package top.quezr.hqoj.dao.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.quezr.hqoj.entity.User;
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

    @Update("update user set coins=coins+#{coins} where id=#{id}")
    void updateUserCoins(@Param("id") Integer userId, @Param("coins") int coins);

    void updateSelective(@Param("u") User u);
}
