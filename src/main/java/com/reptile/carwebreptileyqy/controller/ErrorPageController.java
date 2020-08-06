package com.reptile.carwebreptileyqy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorPageController {
    @RequestMapping("/999")
    public String toPage999(ModelMap modelMap) {
        return "error/loginError";
    }

}
