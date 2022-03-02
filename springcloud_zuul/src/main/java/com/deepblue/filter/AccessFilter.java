package com.deepblue.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public class AccessFilter extends ZuulFilter {

    public static Logger logger = LoggerFactory.getLogger(AccessFilter.class);

    public static String aa = "";


    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        // 这里可以配置白名单相关的东西! 暂时不使用 过滤器拦截 设置为 false;
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String accessToken = request.getParameter("accessToken");

        logger.info("access token is :" + accessToken);

        if(StringUtils.isBlank(accessToken)) {
            logger.warn("access token is empty!");
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(401);
            return null;
        }

        logger.info("access token ok");
        return null;
    }

}
