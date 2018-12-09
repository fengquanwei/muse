package com.fengquanwei.muse.web.attack.xss;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 登录
 *
 * @author fengquanwei
 * @create 2018/12/9 21:57
 **/
@Controller
@RequestMapping("/login")
public class LoginController {
    @RequestMapping("")
    public String login(@RequestParam(required = false, defaultValue = "") String username, Model model) {
        model.addAttribute("username", username);
        return "login";
    }

    @RequestMapping("/check")
    public String check(String username, String password, Model model) {
        System.out.println("check: [" + username + "/" + password + "]");

        model.addAttribute("username", username);
        return "welcome";
    }
}
