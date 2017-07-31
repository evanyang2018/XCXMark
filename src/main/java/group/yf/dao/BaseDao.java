package group.yf.dao;

import org.apache.ibatis.annotations.Param;

import java.io.Serializable;


public interface BaseDao<Entity extends Serializable> {

    public int insert(Entity entity);

    public Entity selectById(@Param("id") int id);

    public int udpate(Entity entity);

    public int deleteById(int id);
}
