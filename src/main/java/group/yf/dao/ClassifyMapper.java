package group.yf.dao;

import com.zap.miniapp.po.Classify;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public abstract interface ClassifyMapper
        extends BaseDao<Classify> {
    public abstract List<Classify> selectAllFirstLevel();

    public abstract Classify selectByName(@Param("name") String paramString);
}
