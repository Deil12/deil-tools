package org.deil.navigation.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class NavigationController {

    @GetMapping("/")
    public String navigation(Model model, HttpSession session) {
        return "navigation";
    }

    @GetMapping("/rain")
    public String rain(Model model, HttpSession session) {
        return "rain_flower";
    }

    @GetMapping("/firework")
    public String firework(Model model) {
        return "firework";
    }

}
