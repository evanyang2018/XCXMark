package group.yf.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zap.component.config.PropertiesConfig;
import com.zap.component.storage.StorageService;
import com.zap.miniapp.action.vo.BannerVO;
import com.zap.miniapp.action.vo.MiniappSimpleVO;
import com.zap.miniapp.action.vo.MiniappVO;
import com.zap.miniapp.dao.ClassifyMapper;
import com.zap.miniapp.dao.FavoritesMapper;
import com.zap.miniapp.dao.MiniappMapper;
import com.zap.miniapp.po.Classify;
import com.zap.miniapp.po.Favorites;
import com.zap.miniapp.util.ReadConfigFileUtil;
import com.zap.miniapp.util.SecurityUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MiniappAction {
    Logger logger = Logger.getLogger(getClass());
    @Autowired
    private MiniappMapper miniappMapper;
    @Autowired
    private FavoritesMapper favoritesMapper;
    @Autowired
    private ClassifyMapper classifyMapper;
    @Autowired
    private StorageService storageService;
    @Autowired
    private PropertiesConfig xiaochenghuiConfig;

    /**
     * 进入首页
     */
    @RequestMapping({"getIndexList.jspa"})
    public ModelAndView getHotListPull(HttpServletRequest req,
                                       @RequestParam(value = "userId", required = true) String userId) {
        this.logger.info("userId>>>" + userId);
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("userId", userId);

        List<Classify> classifyList = this.classifyMapper.selectAllFirstLevel();

        mav.addObject("classifyList", classifyList);

        List<MiniappSimpleVO> newAppList = new ArrayList<MiniappSimpleVO>();
        List<MiniappSimpleVO> hotAppList = new ArrayList<MiniappSimpleVO>();

        newAppList = this.miniappMapper.selectByCreateTime();
        hotAppList = this.miniappMapper.selectByRecommend();
        for (MiniappSimpleVO newApp : newAppList) {
            newApp.setLogo(this.storageService.getAbsolutePath(newApp.getLogo()));
        }
        for (MiniappSimpleVO hotApp : hotAppList) {
            hotApp.setLogo(this.storageService.getAbsolutePath(hotApp.getLogo()));
        }
        mav.addObject("newAppList", newAppList);
        mav.addObject("hotAppList", hotAppList);

        ReadConfigFileUtil fileUtil = new ReadConfigFileUtil();
        JSONObject json = fileUtil
                .readVMFile(req.getServletContext().getRealPath("/") + "WEB-INF/view/vm/fragment/banner.vm");
        JSONArray jsonArray = json.getJSONArray("bannerList");
        List<BannerVO> bannerlist = new ArrayList<BannerVO>();
        for (int i = 0; i < jsonArray.size(); i++) {
            String webUrl = jsonArray.getJSONObject(i).getString("webUrl");
            String webImage = jsonArray.getJSONObject(i).getString("webImage");
            BannerVO bannerVO = new BannerVO();
            bannerVO.setWebImage(webImage);
            bannerVO.setWebUrl(webUrl);
            bannerlist.add(bannerVO);
        }
        mav.addObject("bannerlist", bannerlist);

        return mav;
    }

    /**
     * 获取小程序详�?
     */
    @RequestMapping({"getMiniApp.jspa"})
    public ModelAndView getMiniApp(HttpServletRequest req,
                                   @RequestParam(value = "userId", required = true) String userId,
                                   @RequestParam(value = "appId", required = true) int appId) {
        this.logger.info("userId>>>" + userId + ">>appId>>>" + appId);
        int uId = Integer.parseInt(SecurityUtil.decrypt(userId));
        ModelAndView mav = new ModelAndView("detail");
        mav.addObject("userId", userId);
        MiniappVO miniapp = this.miniappMapper.selectByAppId(appId);
        if (miniapp != null) {
            Favorites favorites = this.favoritesMapper.selectByUIdAndAppId(uId, appId);
            if (favorites == null) {
                favorites = new Favorites();
                favorites.setAppId(appId);
                favorites.setUserId(uId);
                Date date = new Date();
                favorites.setCreateTime(date);
                favorites.setUpdateTime(date);
                this.favoritesMapper.insert(favorites);
            } else {
                this.favoritesMapper.updateByUIdAndAppId(uId, appId, new Date());
            }
            miniapp.setLogo(this.storageService.getAbsolutePath(miniapp.getLogo()));
            if (miniapp.getQrcode() != null) {
                miniapp.setQrcode(this.storageService.getAbsolutePath(miniapp.getQrcode()));
            } else {
                miniapp.setQrcode("/img/qrcode.jpg");
            }
            if (miniapp.getPic1() != null) {
                miniapp.setPic1(this.storageService.getAbsolutePath(miniapp.getPic1()));
            }
            if (miniapp.getPic2() != null) {
                miniapp.setPic2(this.storageService.getAbsolutePath(miniapp.getPic2()));
            }
            if (miniapp.getPic3() != null) {
                miniapp.setPic3(this.storageService.getAbsolutePath(miniapp.getPic3()));
            }
            if (miniapp.getPic4() != null) {
                miniapp.setPic4(this.storageService.getAbsolutePath(miniapp.getPic4()));
            }
            if (miniapp.getPic5() != null) {
                miniapp.setPic5(this.storageService.getAbsolutePath(miniapp.getPic5()));
            }
            mav.addObject("miniapp", miniapp);
            return mav;
        } else {
            mav.setViewName("index");
            mav.addObject("userId", userId);
            return mav;
        }
    }

    /**
     * 获取小程序分类列�?
     */
    @RequestMapping({"getMiniappList.jspa"})
    public ModelAndView getMiniappList(HttpServletRequest req,
                                       @RequestParam(value = "userId", required = true) String userId,
                                       @RequestParam(value = "cId", required = true) int cId) {
        logger.info("cId>>>" + cId);
        int perPageNum = Integer.parseInt(this.xiaochenghuiConfig.getConfig("perpagenum"));
        ModelAndView mav = new ModelAndView("category");
        Classify firstClassify = (Classify) this.classifyMapper.selectById(cId);
        if (firstClassify == null) {
            mav.setViewName("error");
            return mav;
        }
        List<MiniappSimpleVO> miniappList = new ArrayList<MiniappSimpleVO>();
        miniappList = this.miniappMapper.selectByCId(cId, 0, perPageNum);
        for (MiniappSimpleVO miniapp : miniappList) {
            miniapp.setLogo(this.storageService.getAbsolutePath(miniapp.getLogo()));
        }
        mav.addObject("miniappList", miniappList);
        mav.addObject("cId", Integer.valueOf(cId));
        mav.addObject("classify", firstClassify);
        mav.addObject("userId", userId);
        mav.addObject("perPageNum", Integer.valueOf(perPageNum));
        return mav;
    }

    /**
     * 获取小程序分类列表（ajax�?
     */
    @ResponseBody
    @RequestMapping(value = {"getMiniappListPull.jspa"}, produces = {"application/json;charset=UTF-8"})
    public String getMiniappListPull(HttpServletRequest req, @RequestParam(value = "cId", required = true) int cId,
                                     @RequestParam(value = "pageNo", required = true) int pageNo) {
        int perPageNum = Integer.parseInt(this.xiaochenghuiConfig.getConfig("perpagenum"));
        logger.info("cId>>>" + cId + ">>pageNo>>>" + pageNo);
        Classify c1 = (Classify) this.classifyMapper.selectById(cId);

        JSONObject resp = new JSONObject();
        if (c1 == null) {
            resp.put("ret", Integer.valueOf(-1));
            resp.put("info", "暂无该分类数据！");
            return resp.toString();
        }
        List<MiniappSimpleVO> miniappList = new ArrayList<MiniappSimpleVO>();
        miniappList = this.miniappMapper.selectByCId(cId, (pageNo - 1) * perPageNum, perPageNum);
        for (MiniappSimpleVO miniapp : miniappList) {
            miniapp.setLogo(this.storageService.getAbsolutePath(miniapp.getLogo()));
        }
        resp.put("ret", Integer.valueOf(0));
        resp.put("miniappList", miniappList);

        return resp.toString();
    }

    /**
     * 获取浏览记录
     */
    @RequestMapping({"getBrowseList.jspa"})
    public ModelAndView getBrowseList(HttpServletRequest req,
                                      @RequestParam(value = "userId", required = true) String userId) {
        this.logger.info("userId>>>" + userId);
        int perPageNum = Integer.parseInt(this.xiaochenghuiConfig.getConfig("perpagenum"));
        int uId = Integer.parseInt(SecurityUtil.decrypt(userId));
        ModelAndView mav = new ModelAndView("history");

        List<MiniappSimpleVO> miniappList = new ArrayList<MiniappSimpleVO>();
        miniappList = this.miniappMapper.selectBrowseByUserId(uId, 0, perPageNum);
        for (MiniappSimpleVO miniapp : miniappList) {
            miniapp.setLogo(this.storageService.getAbsolutePath(miniapp.getLogo()));
        }
        mav.addObject("miniappList", miniappList);
        mav.addObject("userId", userId);
        mav.addObject("perPageNum", Integer.valueOf(perPageNum));
        return mav;
    }

    /**
     * 获取浏览记录(ajax)
     */
    @ResponseBody
    @RequestMapping(value = {"getBrowseListPull.jspa"}, produces = {"application/json;charset=UTF-8"})
    public String getBrowseListPull(HttpServletRequest req,
                                    @RequestParam(value = "userId", required = true) String userId,
                                    @RequestParam(value = "pageNo", required = true) int pageNo) {
        this.logger.info("userId>>>" + userId + ">>pageNo>>>" + pageNo);
        int perPageNum = Integer.parseInt(this.xiaochenghuiConfig.getConfig("perpagenum"));
        int uId = Integer.parseInt(SecurityUtil.decrypt(userId));

        JSONObject resp = new JSONObject();
        List<MiniappSimpleVO> miniappList = new ArrayList<MiniappSimpleVO>();
        miniappList = this.miniappMapper.selectBrowseByUserId(uId, (pageNo - 1) * perPageNum, perPageNum);
        for (MiniappSimpleVO miniapp : miniappList) {
            miniapp.setLogo(this.storageService.getAbsolutePath(miniapp.getLogo()));
        }
        resp.put("miniappList", miniappList);
        return resp.toString();
    }

    /**
     * 小程序搜�?
     */
    @RequestMapping({"getSearchAppList.jspa"})
    public ModelAndView getSearchAppList(HttpServletRequest req,
                                         @RequestParam(value = "userId", required = true) String userId,
                                         @RequestParam(value = "keyword", required = true) String keyword) {
        ModelAndView mav = new ModelAndView("search");
        mav.addObject("userId", userId);
        try {
            keyword = URLDecoder.decode(keyword, "utf-8");
        } catch (UnsupportedEncodingException e) {
            this.logger.error("keyword decode fail !!!");
            mav.setViewName("index");
            return mav;
        }
        this.logger.info("userId>>>" + userId + ">>>decode keyword>>" + keyword);

        int perPageNum = Integer.parseInt(this.xiaochenghuiConfig.getConfig("perpagenum"));

        List<MiniappSimpleVO> miniappList = new ArrayList<MiniappSimpleVO>();
        miniappList = this.miniappMapper.selectByKeyword(keyword, 0, perPageNum);
        for (MiniappSimpleVO miniapp : miniappList) {
            miniapp.setLogo(this.storageService.getAbsolutePath(miniapp.getLogo()));
        }
        mav.addObject("miniappList", miniappList);
        mav.addObject("keyword", keyword);
        mav.addObject("perPageNum", Integer.valueOf(perPageNum));
        return mav;
    }

    /**
     * 小程序搜�?(ajax)
     */
    @ResponseBody
    @RequestMapping(value = {"getSearchAppListPull.jspa"}, produces = {"application/json;charset=UTF-8"})
    public String getSearchAppListPull(HttpServletRequest req,
                                       @RequestParam(value = "keyword", required = true) String keyword,
                                       @RequestParam(value = "pageNo", required = true) int pageNo) {
        JSONObject resp = new JSONObject();
        try {
            keyword = URLDecoder.decode(keyword, "utf-8");
        } catch (UnsupportedEncodingException e) {
            this.logger.error("keyword decode fail !!!");
            resp.put("ret", Integer.valueOf(-1));
            resp.put("info", "亲，这个关键词有问题哟！");
            return resp.toString();
        }
        this.logger.info("decode keyword>>" + keyword);

        int perPageNum = Integer.parseInt(this.xiaochenghuiConfig.getConfig("perpagenum"));

        List<MiniappSimpleVO> miniappList = new ArrayList<MiniappSimpleVO>();
        miniappList = this.miniappMapper.selectByKeyword(keyword, (pageNo - 1) * perPageNum, perPageNum);
        for (MiniappSimpleVO miniapp : miniappList) {
            miniapp.setLogo(this.storageService.getAbsolutePath(miniapp.getLogo()));
        }
        resp.put("ret", Integer.valueOf(0));
        resp.put("info", "查询成功");
        resp.put("miniappList", miniappList);

        return resp.toString();
    }

    /**
     * 获取�?级分�?
     */
    @ResponseBody
    @RequestMapping(value = {"getClassify.jspa"}, produces = {"application/json;charset=UTF-8"})
    public String getClassify(HttpServletRequest req) {
        List<Classify> classifyList = new ArrayList<Classify>();
        classifyList = this.classifyMapper.selectAllFirstLevel();
        JSONObject resp = new JSONObject();
        resp.put("classifyList", classifyList);
        return resp.toString();
    }

    /**
     * 获取�?新小程序列表
     */
    @ResponseBody
    @RequestMapping(value = {"getNewAppList.jspa"}, produces = {"application/json;charset=UTF-8"})
    public String getNewAppList(HttpServletRequest req, @RequestParam(value = "pageNo", required = true) int pageNo) {
        int perPageNum = Integer.parseInt(this.xiaochenghuiConfig.getConfig("perpagenum"));
        List<MiniappSimpleVO> newAppList = new ArrayList<MiniappSimpleVO>();
        newAppList = this.miniappMapper.selectNewByCreateTime((pageNo - 1) * perPageNum, perPageNum);
        for (MiniappSimpleVO newApp : newAppList) {
            newApp.setLogo(this.storageService.getAbsolutePath(newApp.getLogo()));
        }
        JSONObject resp = new JSONObject();
        resp.put("miniappList", newAppList);
        return resp.toString();
    }

    /**
     * 获取推荐小程序列�?
     */
    @ResponseBody
    @RequestMapping(value = {"getHotAppList.jspa"}, produces = {"application/json;charset=UTF-8"})
    public String getHotAppList(HttpServletRequest req, @RequestParam(value = "pageNo", required = true) int pageNo) {
        List<MiniappSimpleVO> hotAppList = new ArrayList<MiniappSimpleVO>();
        hotAppList = this.miniappMapper.selectByRecommend();
        for (MiniappSimpleVO newApp : hotAppList) {
            newApp.setLogo(this.storageService.getAbsolutePath(newApp.getLogo()));
        }
        JSONObject resp = new JSONObject();
        resp.put("miniappList", hotAppList);
        return resp.toString();
    }

    /**
     * 获取小程序详�?(接口)
     */
    @ResponseBody
    @RequestMapping(value = {"getMiniappDetail.jspa"}, produces = {"application/json;charset=UTF-8"})
    public String getMiniappDetail(HttpServletRequest req, @RequestParam(value = "appId", required = true) int appId) {
        this.logger.info("appId>>>" + appId);
        MiniappVO miniapp = this.miniappMapper.selectByAppId(appId);
        JSONObject resp = new JSONObject();
        if (miniapp == null) {
            resp.put("ret", Integer.valueOf(-1));
            resp.put("info", "还有没这个小程序哟！");
            return resp.toString();
        }
        miniapp.setLogo(this.storageService.getAbsolutePath(miniapp.getLogo()));
        if (miniapp.getQrcode() != null) {
            miniapp.setQrcode(this.storageService.getAbsolutePath(miniapp.getQrcode()));
        } else {
            miniapp.setQrcode("/img/qrcode.jpg");
        }
        if (miniapp.getPic1() != null) {
            miniapp.setPic1(this.storageService.getAbsolutePath(miniapp.getPic1()));
        }
        if (miniapp.getPic2() != null) {
            miniapp.setPic2(this.storageService.getAbsolutePath(miniapp.getPic2()));
        }
        if (miniapp.getPic3() != null) {
            miniapp.setPic3(this.storageService.getAbsolutePath(miniapp.getPic3()));
        }
        if (miniapp.getPic4() != null) {
            miniapp.setPic4(this.storageService.getAbsolutePath(miniapp.getPic4()));
        }
        if (miniapp.getPic5() != null) {
            miniapp.setPic5(this.storageService.getAbsolutePath(miniapp.getPic5()));
        }
        resp.put("ret", Integer.valueOf(0));
        resp.put("miniapp", miniapp);
        return resp.toString();
    }
}
