package com.aida.cat.controller;

import com.aida.cat.model.User;
import com.aida.cat.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("user")
public class UserController {
    @Resource
    private UserService userService;
    // @Resource是Java EE的注解，用于指示SpringIoC容器自动将容器里面的定义的UserService的实现类注入到这个字段。
    // 为什么不直接用@Resource修饰UserService的实现类呢？
    // @Resource修饰接口的好处在于，你不需要关心具体的实现类是什么，只要保证容器中有UserService类型的实现类即可。



    @RequestMapping(value = "login", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Map<String, Object> login(@RequestBody User user) {
        User data = userService.loginUser(user);
        Map<String, Object> map = new HashMap<>();
        if (data == null) {
            map.put("code", 500);
        } else {
            map.put("data", data);
            map.put("code", 200);
        }
        return map;
    }

    @RequestMapping(value = "reg", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public Map<String, Object> register(@RequestBody User user) {
        Map<String, Object> map = new HashMap<>();
        if (userService.regUser(user)) {
            map.put("code", 200);
        } else {
            map.put("code", 500);
        }
        return map;
    }
}
