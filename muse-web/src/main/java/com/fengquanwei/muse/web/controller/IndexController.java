package com.fengquanwei.muse.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 首页
 *
 * @author fengquanwei
 * @create 2018/12/8 22:58
 **/
@Controller
@RequestMapping("")
public class IndexController {
    @RequestMapping({"", "/", "/index"})
    public String index() {
        return "index";
    }
}
