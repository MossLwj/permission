package com.lwj.common;

import com.lwj.exception.PermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created By lwj
 * 2018/2/12 0012 15:15
 */
@Slf4j
public class SpringExceptionResolver implements HandlerExceptionResolver {

    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        String url = httpServletRequest.getRequestURL().toString();
        ModelAndView modelAndView;
        String defaultMsg = "System error";

        //请求规范1：.json(数据类的请求)；2：.page(页面类的请求)
        //要求所有请求json数据的都是用.json结尾
        if (url.endsWith(".json")) {
            if (e instanceof PermissionException) {//如果抛出的异常为自定义的权限部分的异常
                JsonData result = JsonData.fail(e.getMessage());
                modelAndView = new ModelAndView("jsonView", result.toMap());//对应spring-servlet.xml中的jsonView
            } else {
                log.error("unknown json exception, url:" + url, e);
                JsonData result = JsonData.fail(defaultMsg);
                modelAndView = new ModelAndView("jsonView", result.toMap());//对应spring-servlet.xml中的jsonView
            }
        } else if (url.endsWith(".page")) {//所有请求page页面，都是用.page结尾
            log.error("unknown page exception, url:" + url, e);
            JsonData result = JsonData.fail(defaultMsg);
            modelAndView = new ModelAndView("exception", result.toMap());//对应spring-servlet.xml中的jsonView
        } else {
            log.error("unknown exception, url:" + url, e);
            JsonData result = JsonData.fail(defaultMsg);
            modelAndView = new ModelAndView("jsonView", result.toMap());//对应spring-servlet.xml中的jsonView
        }
        return modelAndView;
    }
}
