package edu.jiahui.testcase.service;

import java.util.ArrayList;
import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import edu.jiahui.framework.exceptions.ClientException;
import edu.jiahui.framework.threadlocal.ParameterThreadLocal;
import edu.jiahui.testcase.constants.BaseConstans;
import edu.jiahui.testcase.domain.Database;
import edu.jiahui.testcase.domain.DatabaseWithBLOBs;
import edu.jiahui.testcase.domain.request.DatabaseReq;
import edu.jiahui.testcase.domain.request.SearchDatabaseReq;
import edu.jiahui.testcase.domain.response.DatabaseRes;
import edu.jiahui.testcase.domain.response.SearchDatabaseRes;
import edu.jiahui.testcase.model.bo.DbBo;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import edu.jiahui.testcase.mapper.*;


@Service
public class DbService {

    @Resource
    private DatabaseMapper databaseMapper;



    public SearchDatabaseRes dbList(SearchDatabaseReq req){
        SearchDatabaseRes res = new SearchDatabaseRes();
        List<DatabaseWithBLOBs> dbs = databaseMapper.select(req);
        PageHelper.startPage(req.currentifPage(),req.sizeif());
        PageInfo<DatabaseWithBLOBs> pageInfo= new PageInfo<DatabaseWithBLOBs>(dbs);
        List<SearchDatabaseRes.Databases> databases=new ArrayList<>();
        for(DatabaseWithBLOBs dbItem: dbs){
            SearchDatabaseRes.Databases item=SearchDatabaseRes.Databases.builder()
                    .id(dbItem.getId())
                    .dbName(dbItem.getDbName())
                    .host(dbItem.getHost())
                    .port(dbItem.getPort())
                    .username(dbItem.getUsername())
                    .envId(dbItem.getEnvId())
                    .updatedBy(dbItem.getUpdatedBy())
                    .updatedAt(dbItem.getUpdatedAt())
                    .build();
            databases.add(item);
        }
        res.setDatabaseList(databases);
        res.setTotal(pageInfo.getTotal());
        return res;
    }

    //插入数据库信息
    public void createDb(DatabaseReq req){
        if(databaseMapper.selectByDbnameEnvid(req)>0){
            throw (new ClientException(BaseConstans.BUSI_CODE.DB_EXIT.getCode(),BaseConstans.BUSI_CODE.DB_EXIT.getMsg()));
        }
        req.setCreatedBy(ParameterThreadLocal.getUid());
        req.setUpdatedBy(ParameterThreadLocal.getUid());
        databaseMapper.insert(req);
    }

    //查询数据库信息详情
    public DatabaseRes dbDetail(Integer id){
        DatabaseWithBLOBs dbDetail = databaseMapper.selectByPrimaryKey(id);
        DatabaseRes res= DatabaseRes.builder()
                .id(dbDetail.getId())
                .dbName(dbDetail.getDbName())
                .host(dbDetail.getHost())
                .port(dbDetail.getPort())
                .username(dbDetail.getUsername())
                .password(dbDetail.getPassword())
                .envId(dbDetail.getEnvId())
                .createTableSql(dbDetail.getCreateTableSql())
                .insertSql(dbDetail.getInsertSql())
                .build();
        return res;
    }

    //更新数据库
    public void updateDb(DatabaseReq req){
        if(databaseMapper.selectByDbnameEnvidAndId(req)>0){
            throw (new ClientException(BaseConstans.BUSI_CODE.DB_EXIT.getCode(),BaseConstans.BUSI_CODE.DB_EXIT.getMsg()));
        }
        req.setUpdatedBy(ParameterThreadLocal.getUid());
        databaseMapper.update(req);
    }

    //删除数据库信息
    public void deleteDb(Integer id){
        databaseMapper.delete(id);
    }

}
