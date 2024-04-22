package org.deil.login.common.login;

import lombok.extern.slf4j.Slf4j;
import org.deil.login.common.domain.LoginProperties;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object account = request.getSession().getAttribute(LoginProperties.ACCOUNT);
        if (account != null) {
            log.debug("拦截器访问:{}", request.getRequestURI());
            return true;
        }
        log.info("未授权访问:{}", request.getRequestURI());
        request.setAttribute(LoginProperties.MESSAGE, "请登录后访问");
        // request.getRequestDispatcher("/login").forward(request, response);
        response.sendRedirect("/login");
        return false;
    }

    /*@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
       // 获取request的cookie
        Cookie[] cookies = request.getCookies();
        if (null == cookies) {
            log.info("没有cookie==============");
        } else {
           // 遍历cookie如果找到登录状态则返回true执行原来controller的方法
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("isLogin")) {
                    return true;
                }
            }
        }
       // 没有找到登录状态则重定向到登录页，返回false，不执行原来controller的方法
        response.sendRedirect("/login");
        return false;
    }*/

}
