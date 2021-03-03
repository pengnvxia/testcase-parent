package edu.jiahui.testcase.service;

import edu.jiahui.framework.exceptions.ClientException;
import edu.jiahui.framework.util.ResultCode;
import edu.jiahui.testcase.constants.BaseConstans;
import edu.jiahui.testcase.domain.request.UserReq;
import edu.jiahui.testcase.domain.Users;

import edu.jiahui.testcase.domain.response.UserRes;
import edu.jiahui.testcase.mapper.UsersMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.*;

import static edu.jiahui.testcase.utils.JWTUtil.createToken;

@Service
public class UserService {

    @Resource
    private UsersMapper usersMapper;

//    public void createUser(UserReq req){
//        Users user = new Users();
//        user.setFullname(req.getFullname());
//        user.setPassword(req.getPassword());
//        user.setEmail(req.getEmail());
//        usersMapper.insert(user);
//    }

    public UserRes login(UserReq req){
        UserRes res=new UserRes();
        Users user = Users.builder()
                .username(req.getUsername())
                .password(req.getPassword())
                .build();
        Integer userId=usersMapper.selectByNameAndPassword(user);
        if(userId==null || userId<=0){
            throw (new ClientException(BaseConstans.BUSI_CODE.USER_NOT_EXIT.getCode(),BaseConstans.BUSI_CODE.USER_NOT_EXIT.getMsg()));
        }else {
            String userToken=createToken(user.getUsername());
            user.setToken(userToken);
            user.setId(userId);
            usersMapper.updateToken(user);
            res.setToken(userToken);
        }
        return res;
    }

    public void register(UserReq req){
        Users users=Users.builder()
                .username(req.getUsername())
                .password(req.getPassword())
                .email(req.getEmail())
                .build();
        if(usersMapper.selectByName(users)>0){
            throw (new ClientException(BaseConstans.BUSI_CODE.USER_EXIT.getCode(),BaseConstans.BUSI_CODE.USER_EXIT.getMsg()));
        }else {
            usersMapper.insert(users);
        }
    }
}
