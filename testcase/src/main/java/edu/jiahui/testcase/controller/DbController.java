package edu.jiahui.testcase.controller;

import edu.jiahui.framework.exceptions.ClientException;
import edu.jiahui.framework.util.ResultCode;
import edu.jiahui.testcase.domain.request.DatabaseReq;
import edu.jiahui.testcase.domain.request.SearchDatabaseReq;
import edu.jiahui.testcase.domain.response.DatabaseRes;
import edu.jiahui.testcase.domain.response.SearchDatabaseRes;
import edu.jiahui.testcase.service.DbService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/db")
@Slf4j
public class DbController {
    @Autowired
    private DbService dbService;

    @RequestMapping(method = RequestMethod.POST, value = "/createDb")
    public ResultCode createDb(@RequestBody @Valid DatabaseReq req){
        try{
            dbService.createDb(req);
        }catch (ClientException c){
            return ResultCode.getFailure(c.getCode(),c.getMessage());
        }catch (Exception e){
            log.error("插入数据库信息异常:{}",e.getMessage());
            return ResultCode.getFailure(null,"服务器繁忙，请稍后重试！");
        }
        return ResultCode.getSuccessReturn(null,"添加成功！",null);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/dbDetail/{id}")
    public ResultCode<DatabaseRes> dbDetail(@PathVariable("id")Integer id){
        DatabaseRes res= new DatabaseRes();
        try{
            res=dbService.dbDetail(id);
        }catch (Exception e){
            log.error("查询数据库信息异常:{}",e.getMessage());
            return ResultCode.getFailure(null,"服务器繁忙，请稍后重试！");
        }
        return ResultCode.getSuccessReturn(null,"查询成功!",res);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/updateDb")
    public ResultCode updateDb(@RequestBody @Valid DatabaseReq req){
        try{
            dbService.updateDb(req);
        }catch (ClientException c){
            return ResultCode.getFailure(c.getCode(),c.getMessage());
        }catch (Exception e){
            log.error("更新数据库信息异常:{}",e.getMessage());
            return ResultCode.getFailure(null,"服务器繁忙，请稍后重试！");
        }
        return ResultCode.getSuccessReturn(null,"更新成功!",null);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/list")
    public ResultCode<SearchDatabaseRes> searchDatabase(@RequestBody @Valid SearchDatabaseReq req){
        SearchDatabaseRes res= new SearchDatabaseRes();
        try{
            res=dbService.dbList(req);
        }catch (Exception e){
            log.error("查询数据库信息异常:{}",e.getMessage());
            return ResultCode.getFailure(null,"服务器繁忙，轻稍后重试！");
        }

        return ResultCode.getSuccessReturn(null,null,res);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/dbDel")
    public ResultCode dbDel(@RequestBody List<Integer> req){
        try{
            dbService.deleteDb(req.get(0));
        }catch (Exception e){
            log.error("删除数据库信息异常:{}",e.getMessage());
            return ResultCode.getFailure(null,"服务器繁忙，轻稍后重试！");
        }
        return ResultCode.getSuccessReturn(null,"删除成功！",null);
    }
}
