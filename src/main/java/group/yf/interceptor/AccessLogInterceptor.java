package com.zap.miniapp.interceptor;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class AccessLogInterceptor extends HandlerInterceptorAdapter {
    /**
     */
    private Logger logger = Logger.getLogger(this.getClass());
    long startTime = 0L;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        startTime = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        Enumeration params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String param = (String) params.nextElement();
            sb.append(param);
            sb.append("=");
            sb.append(request.getParameter(param));
            sb.append(",");
        }
        logger.info("url=" + request.getRequestURI().toString() + ",querystring=" + request.getQueryString() + ",params={" + sb.toString() + "}");
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        //logger.info("ModelAndView=" + modelAndView.toString() + ",resp=" + response.getClass());
        super.postHandle(request, response, handler, modelAndView);

    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;
        logger.info("executeTime:" + executeTime + "ms");
        super.afterCompletion(request, response, handler, ex);
    }


}
