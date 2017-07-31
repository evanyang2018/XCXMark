package com.zap.miniapp.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.zap.miniapp.resp.BaseResp;
import com.zap.miniapp.resp.InvalidUserResp;
import com.zap.miniapp.util.SecurityUtil;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class AuthInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = Logger.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getParameter("userId");
        if (userId != null) {
            try {
                request.setAttribute("userId", SecurityUtil.decrypt(userId));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                InvalidUserResp invalidResp = new InvalidUserResp();
                invalidResp.setRet(BaseResp.RESP_ERROR);
                invalidResp.setInfo("无效的用户访问");
                PrintWriter respWriter = response.getWriter();
                respWriter.write(JSONObject.toJSONString(invalidResp));
                respWriter.flush();
                respWriter.close();
                return false;
            }
        }
        return super.preHandle(request, response, handler);
    }


}


