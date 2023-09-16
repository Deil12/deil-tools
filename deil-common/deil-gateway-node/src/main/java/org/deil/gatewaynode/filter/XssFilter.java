package org.deil.gatewaynode.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 服务器请求参数过滤器
 *
 * @DATE 2023/01/28
 * @CODE Deil
 * @see Filter
 */
public class XssFilter implements Filter {
    private Logger log = LoggerFactory.getLogger(XssFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        //log.info("\033[0;32;4m控制层>当前MVC过滤请求IP:{}\033[0m", IPUtil.getIpAddress(request));

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //region cookie 添加 HttpOnly
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie != null) {
                    String value = cookie.getValue();
                    StringBuilder builder = new StringBuilder();
                    builder.append(value);
                    // set-cookie后，在ie浏览器下session失效
                    builder.append("JSESSIONID=<script>alert(\"XSS\");</script>" + value + "; ");
                    builder.append("Secure; ");
                    builder.append("HttpOnly; ");
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.HOUR, 1);
                    Date date = cal.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.CHINA);
                    builder.append("Expires=" + sdf.format(date));
                    response.setHeader("SET-COOKIE", builder.toString());
                    response.setHeader("x-frame-options", "SAMEORIGIN");

                    //cookie.setValue(builder.toString());
                }
            }
        }

        HttpSession session = request.getSession();
        if (session != null) {
            //设置HttpOnly属性，避免攻击者利用跨站脚本漏洞进行Cookie劫持攻击
            response.setHeader("Set-Cookie", "JSESSIONID=<script>alert(\"XSS\");</script>" + session.getId() + "; HttpOnly");
            response.setHeader("x-frame-options", "SAMEORIGIN");
            // 拦截请求，处理XSS过滤
        }
        //endregion
        filterChain.doFilter(new XssRequestWrapper((HttpServletRequest) request), response);
    }
}
