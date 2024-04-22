package org.deil.login.controller;

import lombok.extern.slf4j.Slf4j;
import org.deil.login.common.login.LoginConfigManager;
import org.deil.login.common.domain.LoginProperties;
import org.deil.login.common.utils.IPUtil;
import org.deil.login.common.domain.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        model.addAttribute(LoginProperties.CONFIG, LoginConfigManager.getConfig().setIp(IPUtil.getClientAddress(request)));
        Object attribute = request.getSession().getAttribute(LoginProperties.ACCOUNT);
        if (attribute != null) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/doLogin")
    @ResponseBody
    public Result doLogin(
            // RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            HttpServletResponse response,
            String name,
            String pwd
    ) throws IOException {
        log.info("name:{}, pwd:{}", name, pwd);
        if (!StringUtils.isEmpty(name) && pwd.equals(LoginConfigManager.getConfig().getPwd())) {
            request.getSession().setAttribute(LoginProperties.ACCOUNT, name);
            return Result.get(200, "账号密码匹配", name + ":" + pwd);
        }
        // redirectAttributes.addFlashAttribute(LoginProperties.MESSAGE, "账号密码错误");
        return Result.get(400, "账号密码错误", name + ":" + pwd);
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute(LoginProperties.ACCOUNT);
        return "redirect:login";
    }

}
