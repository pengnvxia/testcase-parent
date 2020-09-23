//package edu.jiahui.testcase.service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//import edu.jiahui.testcase.model.bo.DbBo;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//
//
//@Service
//public class DbService {
//
//    @Resource
//    private DatabaseMapper databaseMapper;
//
//    public List<DbBo> list(){
//        List<Database> dbs = databaseMapper.select();
//        List<DbBo> rsp = new ArrayList<DbBo>();
//        if(dbs.size()>0){
//            for(Database database : dbs){
//                DbBo db = new DbBo();
//                db.setId(database.getId());
//                db.setDbname(database.getDbName());
//                rsp.add(db);
//            }
//        }
//        return rsp;
//    }
//
//}
