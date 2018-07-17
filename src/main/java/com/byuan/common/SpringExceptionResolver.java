package com.byuan.common;

import com.byuan.exception.PermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 捕捉全局异常，进行异常处理
 * Created by Administrator on 2018/7/16 0016.
 */
@Slf4j
public class SpringExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {
        String url = request.getRequestURL().toString();
        ModelAndView mv;
        String defaultMsg = "System error";

        // 默认接口请求.json
        // 默认页面请求.page
        if (url.endsWith(".json")) {
            // 当异常为自定义异常时，返回的msg为定义的msg
            if (e instanceof PermissionException) {
                JsonData result = JsonData.fail(e.getMessage());
                // jsonView 为 spring-servlet.xml配置的json处理
                mv = new ModelAndView("jsonView",result.toMap());
            } else {
                // 当异常为普通异常时，返回msg为默认msg
                log.error("unknow json exception, url:" + url,e);
                JsonData result = JsonData.fail(defaultMsg);
                mv = new ModelAndView("jsonView",result.toMap());
            }
        } else if (url.endsWith(".page")) {
            log.error("unknow page exception, url:" + url,e);
            JsonData result = JsonData.fail(defaultMsg);
            mv = new ModelAndView("exception",result.toMap());
        } else {
            log.error("unknow exception, url:" + url,e);
            JsonData result = JsonData.fail(defaultMsg);
            mv = new ModelAndView("jsonView",result.toMap());
        }

        return mv;
    }
}
