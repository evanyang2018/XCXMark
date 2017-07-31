package group.yf.dao;

import com.zap.miniapp.action.vo.MiniappSimpleVO;
import com.zap.miniapp.action.vo.MiniappVO;
import com.zap.miniapp.po.Miniapp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public abstract interface MiniappMapper extends BaseDao<Miniapp> {

    public MiniappVO selectByAppId(@Param("appId") int appId);

    public List<MiniappSimpleVO> selectByCreateTime();

    public List<MiniappSimpleVO> selectNewByCreateTime(@Param("startNo") int paramInt1, @Param("perPageNum") int paramInt2);

    public List<MiniappSimpleVO> selectByRecommend();

    public List<MiniappSimpleVO> selectByCId(@Param("cId") int paramInt1, @Param("startNo") int paramInt2, @Param("perPageNum") int paramInt3);

    public List<MiniappSimpleVO> selectBrowseByUserId(@Param("userId") int paramInt1, @Param("startNo") int paramInt2, @Param("perPageNum") int paramInt3);

    public List<MiniappSimpleVO> selectByKeyword(@Param("keyword") String paramString, @Param("startNo") int paramInt1, @Param("perPageNum") int paramInt2);

    public Miniapp selectByName(@Param("appName") String appName);

    public int udpateQrcode(Miniapp miniapp);

}
