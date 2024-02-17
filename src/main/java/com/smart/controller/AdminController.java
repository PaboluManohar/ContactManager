package com.smart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController { // Corrected class name

    @RequestMapping("/index")
    public String dashboard() {
        return "admin/h"; // Corrected template path
    }

}
