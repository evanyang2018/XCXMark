package group.yf.dao;


import com.zap.miniapp.po.Favorites;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @author Yang Fei
 */
public interface FavoritesMapper extends BaseDao<Favorites> {

    public Favorites selectByUIdAndAppId(@Param("userId") int userId, @Param("appId") int appId);

    public void updateByUIdAndAppId(@Param("userId") int userId, @Param("appId") int appId, @Param("updateTime") Date updateTime);

}
