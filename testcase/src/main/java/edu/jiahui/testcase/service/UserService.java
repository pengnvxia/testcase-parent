package edu.jiahui.testcase.service;

import edu.jiahui.framework.util.ResultCode;
import edu.jiahui.testcase.domain.request.UserReq;
import edu.jiahui.testcase.domain.Users;

import edu.jiahui.testcase.mapper.UsersMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.*;

import static edu.jiahui.testcase.utils.JWTUtil.createToken;

@Service
public class UserService {

    @Resource
    private UsersMapper usersMapper;

    public void createUser(UserReq req){
        Users user = new Users();
        user.setFullname(req.getFullname());
        user.setPassword(req.getPassword());
        user.setEmail(req.getEmail());
        usersMapper.insert(user);
    }

    public ResultCode<Map<String, String>> login(UserReq req){
        Users user = new Users();
        user.setFullname(req.getFullname());
        user.setPassword(req.getPassword());
        if(usersMapper.selectByNamePassword(user)<=0){
            return ResultCode.getFailure("0","用户名或密码错误！");
        }else {
            String userToken=createToken(user.getFullname());
            user.setToken(userToken);
            usersMapper.updateToken(user);
            Map<String,String> userTokenMap = new HashMap<String,String>();
            userTokenMap.put("token",createToken(user.getFullname()));

            return ResultCode.getSuccessReturn(null, "登录成功", userTokenMap);
        }
    }
}
