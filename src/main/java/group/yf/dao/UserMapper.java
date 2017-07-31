package group.yf.dao;

import com.zap.miniapp.po.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseDao<User> {

    User selectByOpenId(@Param("openId") String openId);

}