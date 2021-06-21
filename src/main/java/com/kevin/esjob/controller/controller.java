package com.kevin.esjob.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * </p>
 *
 * @author zhaowenjian
 * @since 2021/6/17 8:54
 */
@RestController
@RequestMapping("/test")
public class controller {

    @RequestMapping("/test")
    public String Test1(){
        return "kevin";
    }
}
