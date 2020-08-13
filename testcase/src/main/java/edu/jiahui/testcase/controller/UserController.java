package edu.jiahui.testcase.controller;
import edu.jiahui.framework.util.ResultCode;
import edu.jiahui.testcase.domain.request.UserReq;
import edu.jiahui.testcase.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ResultCode<List<String>> register(@RequestBody @Valid UserReq req){
        userService.createUser(req);
        return ResultCode.getSuccessReturn(null, "注册成功！", null);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResultCode<Map<String, String>> login(@RequestBody @Valid UserReq req, HttpServletResponse response){
        //        解决跨域问题
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Cache-Control","no-cache");
//        解决跨域问题
        return userService.login(req);
    }

}
