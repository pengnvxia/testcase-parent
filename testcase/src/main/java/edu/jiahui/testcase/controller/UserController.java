package edu.jiahui.testcase.controller;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.jiahui.framework.exceptions.ClientException;
import edu.jiahui.framework.threadlocal.ParameterThreadLocal;
import edu.jiahui.framework.util.ResultCode;
import edu.jiahui.testcase.domain.request.LogoutReq;
import edu.jiahui.testcase.domain.request.UserReq;
import edu.jiahui.testcase.domain.response.UserRes;
import edu.jiahui.testcase.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ResultCode<List<String>> register(@RequestBody @Valid UserReq req){
        try{
            userService.register(req);
        }catch (ClientException c){
            return ResultCode.getFailure(c.getCode(),c.getMessage());
        }catch (Exception e){
            log.error("注册:{}",e.getMessage());
            return ResultCode.getFailure(null,"服务器繁忙，请稍后重试！");
        }
        return ResultCode.getSuccessReturn(null, "注册成功！", null);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResultCode login(@RequestBody @Valid UserReq req){
        UserRes res= new UserRes();
        try{
            res=userService.login(req);
        }catch (ClientException c){
            return ResultCode.getFailure(c.getCode(),c.getMessage());
        }catch (Exception e){
            log.error("登录异常:{}",e.getMessage());
            return ResultCode.getFailure(null,"服务器繁忙，请稍后重试！");
        }
        return ResultCode.getSuccessReturn(null,null,res);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/logout")
    public ResultCode logout(){

        try {
            userService.logout(Integer.parseInt(ParameterThreadLocal.getUid()));
        }catch (Exception e){
            log.error("登出异常:{}",e.getMessage());
            return ResultCode.getFailure(null,"服务器繁忙，请稍后重试！");
        }
        return ResultCode.getSuccessReturn(null,"登出成功！",null);
    }
}
