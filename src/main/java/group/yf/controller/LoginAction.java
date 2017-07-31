package group.yf.controller;

import com.alibaba.fastjson.JSONObject;
import com.zap.component.config.PropertiesConfig;
import com.zap.component.storage.StorageService;
import com.zap.miniapp.dao.UserMapper;
import com.zap.miniapp.po.User;
import com.zap.miniapp.util.DownUploadFileUtil;
import com.zap.miniapp.util.SecurityUtil;
import com.zap.miniapp.util.UrlConnUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.util.Date;

/**
 * 登陆微信公众
 *
 * @author Yang Fei
 */
@Controller
public class LoginAction {

    Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private PropertiesConfig weixinConnectConfig;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StorageService storageService;

    @RequestMapping(value = "/getWebCode.jspa")
    public ModelAndView getWebEntry(HttpServletRequest req, HttpSession session) {
        logger.info("进入小程汇后台");
        String state = RandomStringUtils.randomAlphanumeric(10);
        session.setAttribute("state", state);
        logger.info("state>>>>" + state);
        String url = weixinConnectConfig.getConfig("url_web_code").replace("APPID", weixinConnectConfig.getConfig("appid"))
                .replace("REDIRECT_URI", weixinConnectConfig.getConfig("url_redirect")).replace("SCOPE", "snsapi_userinfo")
                .replace("STATE", state);
        logger.info("请求 code 的url>>>" + url);
        return new ModelAndView("redirect:" + url);
    }

    /**
     * 获取公众号页面许可code 重定向的�?
     *
     * @author Yang Fei
     */
    @RequestMapping(value = "/getWebEntry.jspa")
    public ModelAndView getWebEntry(HttpServletRequest req, HttpSession session,
                                    @RequestParam(value = "code", required = true) String code,
                                    @RequestParam(value = "state", required = true) String state) {
        logger.info("getWebEntry>>>code>>" + code + ">>>state>>" + state);

        ModelAndView mav = new ModelAndView();
        if (!state.equals(session.getAttribute("state")) || code == null) {
            logger.info("state 验证错误�? code为null ！！");
            mav.setViewName("error");
            return mav;
        }

        String url = weixinConnectConfig.getConfig("url_web_accesstoken").replace("APPID", weixinConnectConfig.getConfig("appid"))
                .replace("SECRET", weixinConnectConfig.getConfig("secret")).replace("CODE", code);
        logger.info("请求accesstoken的url>>" + url);
        JSONObject json = JSONObject.parseObject(UrlConnUtils.getConnStr(url));
        String accessToken = json.getString("access_token");
        String openId = json.getString("openid");
        // 判断用户是否存在
        User user = userMapper.selectByOpenId(openId);

        if (user == null) { // 未注�?
            JSONObject userInfo = new JSONObject();
            url = weixinConnectConfig.getConfig("url_web_userinfo").replace("ACCESS_TOKEN", accessToken).replace("OPENID",
                    openId);
            logger.info("获取用户信息的地�?>>>" + url);
            String str_userInfo = UrlConnUtils.getConnStr(url);
            userInfo = JSONObject.parseObject(str_userInfo);
            if (userInfo != null && userInfo.getString("errcode") == null) {
                user = new User();
                user.setOpenId(openId);
                user.setUsername(userInfo.getString("nickname"));
                user.setGender(userInfo.getInteger("sex"));
                user.setPortrait(DownUploadFileUtil.downLoadImageUrl(userInfo.getString("headimgurl"), storageService));
                Date date = new Date();
                user.setCreatetime(date);
                user.setUpdatetime(date);
                user.setProvince(userInfo.getString("province"));
                user.setCity(userInfo.getString("city"));
                userMapper.insert(user);
            } else {
                mav.setViewName("error");
                return mav;
            }
        }
        int userId = user.getUserId();
        try {
            mav.setViewName("redirect:getIndexList.jspa?userId="
                    + URLEncoder.encode(SecurityUtil.encrypt(userId + ""), "utf-8"));
        } catch (Exception e) {
            logger.error("redirect index.vm fail !");
            mav.setViewName("error");
            return mav;
        }
        return mav;
    }

}
