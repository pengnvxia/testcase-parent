package edu.jiahui.testcase.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.annotations.JsonAdapter;
import edu.jiahui.framework.exceptions.ClientException;
import edu.jiahui.testcase.constants.BaseConstans;
import edu.jiahui.testcase.domain.*;
import edu.jiahui.testcase.domain.request.CreateTestcaseGroupReq;
import edu.jiahui.testcase.domain.request.SearchGroupReq;
import edu.jiahui.testcase.domain.response.GroupRes;
import edu.jiahui.testcase.domain.response.SearchGroupRes;
import edu.jiahui.testcase.mapper.*;
import edu.jiahui.testcase.utils.JDBCUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Service
public class GroupService {

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private TestcaseConfigDetailMapper testcaseConfigDetailMapper;

    @Resource
    private DatabaseMapper databaseMapper;

    @Resource
    private TestcaseGroupMapper testcaseGroupMapper;

    @Resource
    private TestcaseGroupDetailMapper testcaseGroupDetailMapper;

    @Resource
    private ReportMapper reportMapper;

    @Resource
    private TestcaseConfigMapper testcaseConfigMapper;

    @Resource
    private TestcaseMapper testcaseMapper;

    @Resource
    private InterfacesMapper interfacesMapper;

    @Autowired
    private CaseService caseService;

    public SearchGroupRes searchGroupList(SearchGroupReq req){
        SearchGroupRes res = new SearchGroupRes();
        PageHelper.startPage(req.currentifPage(),req.sizeif());
        List<SearchGroupRes.Group> testcaseGroups=testcaseGroupMapper.select(req);
        PageInfo<SearchGroupRes.Group> pageInfo= new PageInfo<SearchGroupRes.Group>(testcaseGroups);
        res.setGroupList(testcaseGroups);
        res.setTotal(pageInfo.getTotal());

        return res;
    }


    public void createTestcaseGroup(CreateTestcaseGroupReq req){
        if(testcaseGroupMapper.exitName(req.getGroupName(),null)>0){
            throw (new ClientException(BaseConstans.BUSI_CODE.GROUP_NAME_EXIT.getCode(),BaseConstans.BUSI_CODE.GROUP_NAME_EXIT.getMsg()));
        }

        TestcaseGroup testcaseGroup=TestcaseGroup.builder()
                .groupName(req.getGroupName())
                .configIds(req.getConfigIds())
                .testcaseIds(req.getTestcaseIds())
                .envId(req.getEnvId())
                .projectId(req.getProjectId())
                .build();
        testcaseGroupMapper.insert(testcaseGroup);
        List<TestcaseGroupDetail> testcaseGroupDetails=new ArrayList<>();
        if(req.getVariables()!=null && req.getVariables().size()>0){
            for(CreateTestcaseGroupReq.Variable crv: req.getVariables()){
                TestcaseGroupDetail td=TestcaseGroupDetail.builder()
                        .name(crv.getName())
                        .type(crv.getType())
                        .value(crv.getValue())
                        .databaseId(crv.getDatabaseId())
                        .groupId(testcaseGroup.getId())
                        .scope("variables")
                        .build();
                testcaseGroupDetails.add(td);
            }

        }

        if(req.getParameters()!=null && req.getParameters().size()>0){
            for(CreateTestcaseGroupReq.Parameter crp: req.getParameters()){
                TestcaseGroupDetail td = TestcaseGroupDetail.builder()
                        .name(crp.getName())
                        .value(crp.getValue())
                        .groupId(testcaseGroup.getId())
                        .scope("parameters")
                        .build();
                testcaseGroupDetails.add(td);
            }
        }

        if(req.getSetuphooks()!=null && req.getSetuphooks().size()>0){
            for(CreateTestcaseGroupReq.Setuphook crs: req.getSetuphooks()){
                TestcaseGroupDetail td = TestcaseGroupDetail.builder()
                        .value(crs.getSql())
                        .databaseId(crs.getDatabaseId())
                        .groupId(testcaseGroup.getId())
                        .scope("setupHooks")
                        .build();
                testcaseGroupDetails.add(td);
            }
        }
        if(testcaseGroupDetails.size()>0){
            testcaseGroupDetailMapper.insert(testcaseGroupDetails);
        }
    }

    public void editTestcaseGroup(CreateTestcaseGroupReq req){
        if(testcaseGroupMapper.exitName(req.getGroupName(),req.getId())>0){
            throw (new ClientException(BaseConstans.BUSI_CODE.GROUP_NAME_EXIT.getCode(),BaseConstans.BUSI_CODE.GROUP_NAME_EXIT.getMsg()));
        }
        TestcaseGroup testcaseGroup=TestcaseGroup.builder()
                .id(req.getId())
                .envId(req.getEnvId())
                .projectId(req.getProjectId())
                .configIds(req.getConfigIds())
                .testcaseIds(req.getTestcaseIds())
                .groupName(req.getGroupName())
                .build();
        testcaseGroupMapper.update(testcaseGroup);
        List<Integer> ids=new ArrayList<>();
        if(req.getVariables()!=null && req.getVariables().size()>0){
            for(CreateTestcaseGroupReq.Variable crv: req.getVariables()){
                TestcaseGroupDetail testcaseGroupDetail= TestcaseGroupDetail.builder()
                        .id(crv.getId())
                        .name(crv.getName())
                        .type(crv.getType())
                        .value(crv.getValue())
                        .databaseId(crv.getDatabaseId())
                        .groupId(req.getId())
                        .scope("variables")
                        .build();
                if(testcaseGroupDetail.getId()!=null){
                    testcaseGroupDetailMapper.update(testcaseGroupDetail);
                }else {
                    testcaseGroupDetailMapper.insertOne(testcaseGroupDetail);
                }
                ids.add(testcaseGroupDetail.getId());
            }
        }
        if(req.getParameters()!=null && req.getParameters().size()>0){
            for(CreateTestcaseGroupReq.Parameter crp: req.getParameters()){
                TestcaseGroupDetail testcaseGroupDetail= TestcaseGroupDetail.builder()
                        .id(crp.getId())
                        .name(crp.getName())
                        .value(crp.getValue())
                        .groupId(req.getId())
                        .scope("parameters")
                        .build();
                if(testcaseGroupDetail.getId()!=null){
                    testcaseGroupDetailMapper.update(testcaseGroupDetail);
                }else {
                    testcaseGroupDetailMapper.insertOne(testcaseGroupDetail);
                }
                ids.add(testcaseGroupDetail.getId());
            }
        }
        if(req.getSetuphooks()!=null && req.getSetuphooks().size()>0){
            for(CreateTestcaseGroupReq.Setuphook crs: req.getSetuphooks()){
                TestcaseGroupDetail testcaseGroupDetail = TestcaseGroupDetail.builder()
                        .id(crs.getId())
                        .name(crs.getSql())
                        .databaseId(crs.getDatabaseId())
                        .groupId(req.getId())
                        .scope("setupHooks")
                        .build();
                if(testcaseGroupDetail.getId()!=null){
                    testcaseGroupDetailMapper.update(testcaseGroupDetail);
                }else {
                    testcaseGroupDetailMapper.insertOne(testcaseGroupDetail);
                }
                ids.add(testcaseGroupDetail.getId());
            }
        }
        if(ids.size()>0){
            testcaseGroupDetailMapper.deleteNotIn(ids,req.getId());
        }
    }

    public GroupRes groupDetail(Integer id) {
        TestcaseGroup testcaseGroup=testcaseGroupMapper.selectByPrimaryKey(id);
        List<TestcaseGroupDetail> testcaseGroupDetails= testcaseGroupDetailMapper.selectByGroupId(id);
        List<GroupRes.Variable> grv = new ArrayList<>();
        List<GroupRes.Parameter> grp = new ArrayList<>();
        List<GroupRes.Setuphook> grs = new ArrayList<>();
        if(testcaseGroupDetails.size()>0){
            for(TestcaseGroupDetail tgd: testcaseGroupDetails){
                if(tgd.getScope().equals("variables")){
                    GroupRes.Variable variable = GroupRes.Variable.builder()
                            .id(tgd.getId())
                            .name(tgd.getName())
                            .type(tgd.getType())
                            .value(tgd.getValue())
                            .databaseId(tgd.getDatabaseId())
                            .build();
                    grv.add(variable);
                }
                if(tgd.getScope().equals("parameters")){
                    GroupRes.Parameter parameter = GroupRes.Parameter.builder()
                            .id(tgd.getId())
                            .name(tgd.getName())
                            .value(tgd.getValue())
                            .build();
                    grp.add(parameter);
                }
                if(tgd.getScope().equals("setupHooks")){
                    GroupRes.Setuphook setuphook = GroupRes.Setuphook.builder()
                            .id(tgd.getId())
                            .sql(tgd.getValue())
                            .databaseId(tgd.getDatabaseId())
                            .build();
                    grs.add(setuphook);
                }
            }
        }

        List<GroupRes.Config> gcList = new ArrayList<>();
        if(testcaseGroup.getConfigIds()!=null && testcaseGroup.getTestcaseIds().length()>0){
            List<Integer> configIds=JSON.parseArray(testcaseGroup.getConfigIds(),Integer.class);
            for(Integer configId: configIds){
                if(testcaseConfigMapper.selectByPrimaryKey(configId)!=null){
                    GroupRes.Config gc=GroupRes.Config.builder().id(configId)
                            .configName(testcaseConfigMapper.selectByPrimaryKey(configId).getConfigName()).build();
                    gcList.add(gc);
                }
            }
        }

        List<GroupRes.Testcase> gtList = new ArrayList<>();
        if(testcaseGroup.getTestcaseIds()!=null && testcaseGroup.getTestcaseIds().length()>0){
            List<Integer> testcaseIds= JSON.parseArray(testcaseGroup.getTestcaseIds(),Integer.class);
            for(Integer testcaseId: testcaseIds){
                Testcase testcase=testcaseMapper.selectByPrimaryKey(testcaseId);
                GroupRes.Testcase gt=GroupRes.Testcase.builder().id(testcase.getInterfaceId())
                        .interfaceName(interfacesMapper.selectByPrimaryKey(testcase.getInterfaceId()).getName())
                        .caseId(testcaseId)
                        .caseName(testcase.getTestcaseName()).build();
                gtList.add(gt);
            }
        }

        GroupRes res = GroupRes.builder()
                .id(id)
                .envId(testcaseGroup.getEnvId())
                .projectId(testcaseGroup.getProjectId())
                .configIds(gcList)
                .testcaseIds(gtList)
                .groupName(testcaseGroup.getGroupName())
                .variables(grv)
                .parameters(grp)
                .setuphooks(grs)
                .build();

        return res;
    }

    public void runGroups(List<Integer> groupIds){
        if(groupIds.size()<=0){
            throw (new ClientException(BaseConstans.BUSI_CODE.NOT_RUN_GROUP.getCode(),BaseConstans.BUSI_CODE.NOT_RUN_GROUP.getMsg()));
        }
        for(Integer groupId: groupIds){
            TestcaseGroup testcaseGroup=testcaseGroupMapper.selectByPrimaryKey(groupId);
            runTestcaseGroup(groupId,testcaseGroup.getProjectId(),testcaseGroup.getEnvId());
        }
    }

    public void runTestcaseGroup(Integer id,Integer projectId,Integer envId){
        //拼接变量等数据
        List<TestcaseGroupDetail> testcaseGroupDetails = testcaseGroupDetailMapper.selectByGroupId(id);
        TestcaseGroup testcaseGroup = testcaseGroupMapper.selectByPrimaryKey(id);
        Project project = projectMapper.selectByPrimaryKey(projectId);
        String baseUrl = envId==1 ? project.getDevAddress():project.getProdAddress();
        JSONObject totalVariableJson = new JSONObject();
        if(testcaseGroup.getConfigIds()!=null && !testcaseGroup.getConfigIds().equals("")){
            List<Integer> configIds = JSON.parseArray(testcaseGroup.getConfigIds(),Integer.class);
            if(configIds.size()>0){
                for(Integer configId: configIds){
                    List<TestcaseConfigDetail> testcaseConfigDetails= testcaseConfigDetailMapper.selectByConfigId(configId);
                    for(TestcaseConfigDetail tcd: testcaseConfigDetails){
                        totalVariableJson.put(tcd.getName(),tcd.getValue());
                    }
                }
            }
        }



//        JSONObject variablesJson= new JSONObject();
        JSONObject configParametersJson= new JSONObject();

        if(testcaseGroupDetails.size()>0){
            for(TestcaseGroupDetail tgd: testcaseGroupDetails){
                if(tgd.getScope().equals("setupHooks")){
                    //执行sql,只执行添加删除sql
                    DatabaseWithBLOBs databaseInfo =databaseMapper.selectByPrimaryKey(tgd.getDatabaseId());
                    String databaseAddress = databaseInfo.getHost()+":"+databaseInfo.getPort();
                    Connection conn = JDBCUtil.getConnection(databaseAddress,databaseInfo.getDbName(),databaseInfo.getUsername(),databaseInfo.getPassword());
                    try {
                        PreparedStatement pst = conn.prepareStatement(tgd.getValue());
                        pst.execute();
                        JDBCUtil.close(pst, conn);
                    }catch (SQLException e){
                        throw (new ClientException(BaseConstans.BUSI_CODE.SQL_ERROR.getCode(),e.getMessage()));
                    }catch (Exception e){
                        throw (new ClientException(BaseConstans.BUSI_CODE.RUN_SQL_ERROR.getCode(),e.getMessage()));

                    }
                }

                if(tgd.getScope().equals("variables")){
                    if(tgd.getType().equals("Sql")) {
                        List keyList = JSON.parseArray(tgd.getName(), String.class);
                        DatabaseWithBLOBs databaseInfo = databaseMapper.selectByPrimaryKey(tgd.getDatabaseId());
                        String databaseAddress = databaseInfo.getHost() + ":" + databaseInfo.getPort();
                        Connection conn = JDBCUtil.getConnection(databaseAddress, databaseInfo.getDbName(), databaseInfo.getUsername(), databaseInfo.getPassword());
                        ResultSet rs = null;
                        try {
                            PreparedStatement pst = conn.prepareStatement(tgd.getValue());
                            rs = pst.executeQuery();
                            while (rs.next()) {
                                for (Object k : keyList) {
                                    totalVariableJson.put(k.toString(), rs.getString(k.toString()));
                                }
                            }

                            JDBCUtil.close(pst, conn);
                        } catch (SQLException e) {
                            throw (new ClientException(BaseConstans.BUSI_CODE.SQL_ERROR.getCode(), e.getMessage()));
                        } catch (Exception e) {
                            throw (new ClientException(BaseConstans.BUSI_CODE.RUN_SQL_ERROR.getCode(), e.getMessage()));
                        }
                    }else {
                        totalVariableJson.put(tgd.getName(),tgd.getValue());
                    }
                }

                if(tgd.getScope().equals("parameters")){
                    configParametersJson.put(tgd.getName(),tgd.getValue());
                }
            }
        }

        List testList= new ArrayList();
        if(testcaseGroup.getTestcaseIds()!=null && !testcaseGroup.getTestcaseIds().equals("")){
            List<Integer> testcaseIds = JSON.parseArray(testcaseGroup.getTestcaseIds(),Integer.class);
            for(Integer testcaseId: testcaseIds){
                List testcaseList = caseService.createTestcaseJson(testcaseGroup.getEnvId(),testcaseGroup.getProjectId(),testcaseId);
                testList.add(testcaseList.get(1));
            }
        }

        JSONObject configJson= new JSONObject();
        configJson.put("id",testcaseGroup.getGroupName());
        configJson.put("name",testcaseGroup.getGroupName());
        configJson.put("variables",totalVariableJson);
        configJson.put("parameters",configParametersJson);

        JSONObject testcaseConfigJson= new JSONObject();
        testcaseConfigJson.put("config",configJson);

        List testcaseGroupList= new ArrayList();
        testcaseGroupList.add(testcaseConfigJson);
        for(Object obj: testList){
            testcaseGroupList.add(obj);
        }
        //创建可执行的yaml文件
        caseService.createTestcaseYaml(project.getProjectName(),testcaseGroup.getGroupName(),testcaseGroupList);
        //执行测试用例
        String reportContent = caseService.execTestcase(project.getProjectName(),testcaseGroup.getGroupName());
        Boolean status = (Boolean) JSON.parseObject(reportContent).get("status");
        Integer result = status ? 1 : 0;
        Report report = Report.builder().groupId(id).content(reportContent).result(result).isGroup(1).build();
        //报告数据落表
        reportMapper.insert(report);

    }




    public void deleteGroup(Integer id){
        testcaseGroupMapper.delete(id);
        testcaseGroupDetailMapper.delete(id);
    }
}
