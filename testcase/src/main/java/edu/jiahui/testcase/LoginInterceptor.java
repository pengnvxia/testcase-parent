package edu.jiahui.testcase;

import com.alibaba.fastjson.JSON;
import edu.jiahui.framework.exceptions.ClientException;
import edu.jiahui.framework.exceptions.HttpClientException;
import edu.jiahui.framework.threadlocal.ParameterThreadLocal;
import edu.jiahui.framework.util.ResultCode;
import edu.jiahui.testcase.constants.BaseConstans;
import edu.jiahui.testcase.domain.Users;
import edu.jiahui.testcase.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) {

        String token = ParameterThreadLocal.getToken();
        String uid = ParameterThreadLocal.getUid();
        if(StringUtils.isBlank(token)){
            if(StringUtils.isBlank(uid)){
                isTimeOut(response,BaseConstans.BUSI_CODE.USER_INFO_NOT_EXIT.getCode(),BaseConstans.BUSI_CODE.USER_INFO_NOT_EXIT.getMsg());
                return false;
            }
            isTimeOut(response,BaseConstans.BUSI_CODE.USER_TOKEN_INVALID.getCode(),BaseConstans.BUSI_CODE.USER_TOKEN_INVALID.getMsg());
            return false;
        }
        Users users=Users.builder()
                .token(token)
                .id(uid==null?null:Integer.parseInt(uid)).build();
        try {
            Users user = userService.authentication(users);
            if(user==null){
                isTimeOut(response,BaseConstans.BUSI_CODE.USER_TOKEN_INVALID.getCode(),BaseConstans.BUSI_CODE.USER_TOKEN_INVALID.getMsg());
                return false;
            }else {
                ParameterThreadLocal.setUid(user.getId().toString());
                return true;
            }
        } catch (ClientException e) {
            isTimeOut(response,e.getCode(),e.getMessage());
            return false;
        } catch (HttpClientException e){
            if(e.getErrorResponse()!=null){
                isTimeOut(response,e.getErrorResponse().getCode(),e.getErrorResponse().getMessage());
            }else{
                isTimeOut(response,BaseConstans.BUSI_CODE.USER_TOKEN_INVALID.getCode(),BaseConstans.BUSI_CODE.USER_TOKEN_INVALID.getMsg());
            }
            log.error("鉴权失败",e);
            return false;
        }

    }

    /**
     * token为空时返回
     *
     * @param response
     * @throws Exception
     */
    private void isTimeOut(HttpServletResponse response, String errcode, String errMsg) {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(JSON.toJSONString(ResultCode.getSuccess(errcode, errMsg)));

        } catch (IOException e) {
            log.error("response error", e);
        } finally {
            if (writer != null)
                writer.close();
        }
    }
}
