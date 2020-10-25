package edu.jiahui.testcase.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import edu.jiahui.framework.exceptions.ClientException;
import edu.jiahui.framework.httpclient.HttpClientTemplate;
import edu.jiahui.testcase.constants.BaseConstans;
import edu.jiahui.testcase.constants.DockerConstants;
import edu.jiahui.testcase.constants.PythonConstants;
import edu.jiahui.testcase.domain.*;
import edu.jiahui.testcase.domain.request.RunTestcaseReq;
import edu.jiahui.testcase.domain.request.TestcaseReq;
import edu.jiahui.testcase.domain.response.TestcaseRes;
import edu.jiahui.testcase.mapper.*;
import edu.jiahui.testcase.utils.JDBCUtil;
import edu.jiahui.testcase.utils.ShellUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public class TestcaseService {

    @Resource
    private DockerConstants dockerConstants;

    @Resource
    private PythonConstants pythonConstants;

    @Resource
    private TestcaseMapper testcaseMapper;

    @Resource
    private TestcaseDetailMapper testcaseDetailMapper;

    @Resource
    private TestcaseConfigMapper testcaseConfigMapper;

    @Resource
    private TestcaseConfigDetailMapper testcaseConfigDetailMapper;

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private ReportMapper reportMapper;

    @Resource
    private DatabaseMapper databaseMapper;

    @Resource
    private TestcaseDbMapper testcaseDbMapper;

    @Resource
    private HttpClientTemplate httpClientTemplate;


    public void addTestcase(Integer id,TestcaseReq req){
        Testcase testcase = new Testcase();
        testcase.setTestcaseName(req.getTestcaseName());
        testcase.setEnvId(req.getEnvId());
        testcase.setConfigIds(req.getConfigIds());
        testcase.setInterfaceId(id);
        testcase.setUrl(req.getPath());
        testcaseMapper.insert(testcase);
        Integer testcaseId = testcase.getId();
        List<TestcaseDetail> testcaseDetailList = new ArrayList<>();

        if(req.getVariables()!=null && req.getVariables().size()>0){
            for(TestcaseReq.Variable variable : req.getVariables()){
                TestcaseDetail testcaseDetail = new TestcaseDetail();
                testcaseDetail.setName(variable.getName());
                testcaseDetail.setType(variable.getType());
                testcaseDetail.setValue(variable.getValue());
                testcaseDetail.setDatabaseId(variable.getDatabaseId());
                testcaseDetail.setScope("variables");
                testcaseDetail.setTestcaseId(testcaseId);
                testcaseDetailList.add(testcaseDetail);
            }
        }

        if(req.getParameters()!=null && req.getParameters().size()>0){
            for(TestcaseReq.Parameter parameter : req.getParameters()){
                TestcaseDetail testcaseDetail = new TestcaseDetail();
                testcaseDetail.setName(parameter.getKeyName());
                testcaseDetail.setValue(parameter.getValue());
                testcaseDetail.setScope("parameters");
                testcaseDetail.setTestcaseId(testcaseId);
                testcaseDetailList.add(testcaseDetail);
            }
        }
        if(req.getSetuphooks()!=null && req.getSetuphooks().size()>0){
            for(TestcaseReq.Setuphook setuphook: req.getSetuphooks()){
                TestcaseDetail testcaseDetail = new TestcaseDetail();
                testcaseDetail.setValue(setuphook.getSql());
                testcaseDetail.setDatabaseId(setuphook.getDatabaseId());
                testcaseDetail.setScope("setupHooks");
                testcaseDetail.setType("sql");
                testcaseDetail.setTestcaseId(testcaseId);
                testcaseDetailList.add(testcaseDetail);
            }
        }
        if(req.getReqHeaders()!=null && req.getReqHeaders().size()>0){
            for(TestcaseReq.ReqHeader reqHeader:req.getReqHeaders()){
                TestcaseDetail testcaseDetail = new TestcaseDetail();
                testcaseDetail.setName(reqHeader.getKeyName());
                testcaseDetail.setValue(reqHeader.getValue());
                testcaseDetail.setScope("reqHeaders");
                testcaseDetail.setTestcaseId(testcaseId);
                testcaseDetailList.add(testcaseDetail);
            }
        }
        if(req.getReqParams()!=null && req.getReqParams().size()>0){
            for(TestcaseReq.ReqParam reqParam:req.getReqParams()){
                TestcaseDetail testcaseDetail = new TestcaseDetail();
                testcaseDetail.setName(reqParam.getKeyName());
                testcaseDetail.setValue(reqParam.getValue());
                testcaseDetail.setScope("reqParams");
                testcaseDetail.setTestcaseId(testcaseId);
                testcaseDetailList.add(testcaseDetail);
            }
        }

        if(req.getRequestBody()!=null){
            String aa = req.getRequestBody().getClass().toString();
            TestcaseDetail testcaseDetail = new TestcaseDetail();
            testcaseDetail.setValue(req.getRequestBody().toString());
            testcaseDetail.setScope("requestBody");
            testcaseDetail.setTestcaseId(testcaseId);
            testcaseDetailList.add(testcaseDetail);
        }

        testcaseDetailMapper.insert(testcaseDetailList);

        if(req.getResponses()!=null && req.getResponses().size()>0){
            recursionResponse(req.getResponses(),testcaseId,null,false);
//            for(TestcaseReq.Response response:req.getResponses()){
//                TestcaseDetail testcaseDetail = new TestcaseDetail();
//                testcaseDetail.setName(response.getName());
//                testcaseDetail.setType(response.getType());
//                testcaseDetail.setComparator(response.getComparator());
//                testcaseDetail.setExpectedValue(response.getExpectedValue());
//                testcaseDetail.setScope("response");
//                testcaseDetail.setTestcaseId(testcaseId);
//                testcaseDetailList.add(testcaseDetail);
//            }
        }
    }

    public void recursionResponse(List<TestcaseReq.Response> responseItemList,Integer testcaseId,Integer parentId,Boolean flag){
        Integer i=0;
        for(TestcaseReq.Response responseItem: responseItemList){
            TestcaseDetail testcaseDetail = new TestcaseDetail();
            testcaseDetail.setName(responseItem.getName());
            testcaseDetail.setType(responseItem.getType());
            testcaseDetail.setComparator(responseItem.getComparator().equals("")? null:responseItem.getComparator());
            testcaseDetail.setExpectedValue(responseItem.getExpectedValue());
            testcaseDetail.setScope("response");
            testcaseDetail.setTestcaseId(testcaseId);
            testcaseDetail.setParentId(parentId);
            if(flag){
                testcaseDetail.setArrayIndex(i);
            }
            testcaseDetailMapper.insertOne(testcaseDetail);
//            testcaseDetailList.add(testcaseDetail);
            if(responseItem.getChildren()!=null){
                if(responseItem.getType().equals("Array")){
                    flag=true;
                }
                recursionResponse(responseItem.getChildren(),testcaseId,testcaseDetail.getId(),flag);
            }
            i=i+1;
        }
    }

    public TestcaseRes testcaseInfo(Integer testcaseId){

        TestcaseRes testcaseRes = new TestcaseRes();

        Testcase testcase=testcaseMapper.selectByPrimaryKey(testcaseId);
        testcaseRes.setEnvId(testcase.getEnvId());
        testcaseRes.setPath(testcase.getUrl());
        testcaseRes.setConfigIds(JSON.parseArray(testcase.getConfigIds(),Integer.class));
        testcaseRes.setTestcaseName(testcase.getTestcaseName());
        List<TestcaseDetail> testcaseDetailList = testcaseDetailMapper.selectByTestcaseId(testcaseId);
        List<TestcaseRes.Variable> variableList = new ArrayList<>();
        List<TestcaseRes.Parameter> parameterList = new ArrayList<>();
        List<TestcaseRes.Setuphook> setuphookList = new ArrayList<>();
        List<TestcaseRes.ReqHeader> reqHeaderList = new ArrayList<>();
        List<TestcaseRes.ReqParam> reqParamList = new ArrayList<>();
        List<TestcaseRes.Response> responseList = new ArrayList<>();
        Map reqBodyMap = new HashMap();
        if(testcaseDetailList.size()>0){
            for(TestcaseDetail testcaseDetail:testcaseDetailList){
                String scope=testcaseDetail.getScope();
                switch (scope){
                    case "variables":
                        TestcaseRes.Variable variable= new TestcaseRes.Variable();
                        variable.setId(testcaseDetail.getId());
                        variable.setName(testcaseDetail.getName());
                        variable.setValue(testcaseDetail.getValue());
                        variable.setType(testcaseDetail.getType());
                        variable.setDatabaseId(testcaseDetail.getDatabaseId());
                        variableList.add(variable);
                        break;
                    case "parameters":
                        TestcaseRes.Parameter parameter = new TestcaseRes.Parameter();
                        parameter.setId(testcaseDetail.getId());
                        parameter.setKeyName(testcaseDetail.getName());
                        parameter.setValue(testcaseDetail.getValue());
                        parameterList.add(parameter);
                        break;
                    case "setupHooks":
                        TestcaseRes.Setuphook setuphook = new TestcaseRes.Setuphook();
                        setuphook.setId(testcaseDetail.getId());
                        setuphook.setSql(testcaseDetail.getValue());
                        setuphook.setDatabaseId(testcaseDetail.getDatabaseId());
                        setuphookList.add(setuphook);
                        break;
                    case "reqHeaders":
                        TestcaseRes.ReqHeader reqHeader = new TestcaseRes.ReqHeader();
                        reqHeader.setId(testcaseDetail.getId());
                        reqHeader.setKeyName(testcaseDetail.getName());
                        reqHeader.setValue(testcaseDetail.getValue());
                        reqHeaderList.add(reqHeader);
                        break;
                    case "reqParams":
                        TestcaseRes.ReqParam reqParam = new TestcaseRes.ReqParam();
                        reqParam.setId(testcaseDetail.getId());
                        reqParam.setKeyName(testcaseDetail.getName());
                        reqParam.setValue(testcaseDetail.getValue());
                        reqParamList.add(reqParam);
                        break;
                    case "response":
                        if(testcaseDetail.getParentId()==null || testcaseDetail.getParentId().equals("")){
                            responseList.add(recursionSearchResponse(testcaseDetail));
                        }
                        break;
                    case "requestBody":
                        reqBodyMap.put("id",testcaseDetail.getId());
                        reqBodyMap.put("requestBody",testcaseDetail.getValue());
                }
            }

        }
        testcaseRes.setVariables(variableList);
        testcaseRes.setParameters(parameterList);
        testcaseRes.setSetuphooks(setuphookList);
        testcaseRes.setReqHeaders(reqHeaderList);
        testcaseRes.setReqParams(reqParamList);
        testcaseRes.setResponses(responseList);
        testcaseRes.setReqBody(reqBodyMap);
        return testcaseRes;

    }

    public TestcaseRes.Response recursionSearchResponse(TestcaseDetail testcaseDetail){
        TestcaseRes.Response  response = new TestcaseRes.Response();
        response.setId(testcaseDetail.getId());
        response.setName(testcaseDetail.getName());
        response.setType(testcaseDetail.getType());
        response.setComparator(testcaseDetail.getComparator()==null?"":testcaseDetail.getComparator());
        response.setExpectedValue(testcaseDetail.getExpectedValue());
        List<TestcaseDetail> testcaseDetails= testcaseDetailMapper.selectByParentId(testcaseDetail.getId());
        if(testcaseDetails.size()>=0){
            List<TestcaseRes.Response> responses= new ArrayList<>();
            for(TestcaseDetail td: testcaseDetails){
                TestcaseRes.Response res= new TestcaseRes.Response();
                res=recursionSearchResponse(td);
                responses.add(res);
            }
            if(responses.size()>0){
                response.setChildren(responses);
            }
        }
        return response;
    }

    public void updateTestcase(Integer id,TestcaseReq req){
        Testcase testcase= new Testcase();
        testcase.setTestcaseName(req.getTestcaseName());
        testcase.setEnvId(req.getEnvId());
        testcase.setUrl(req.getPath());
        testcase.setConfigIds(req.getConfigIds());
        testcase.setId(id);
        testcaseMapper.updateByPrimaryKey(testcase);
        if(req.getVariables()!=null && req.getVariables().size()>0){
            List<Integer> ids = new ArrayList<>();
            for(TestcaseReq.Variable variable:req.getVariables()){
                TestcaseDetail testcaseDetail= new TestcaseDetail();
                testcaseDetail.setScope("variables");
                testcaseDetail.setName(variable.getName());
                testcaseDetail.setValue(variable.getValue());
                testcaseDetail.setType(variable.getType());
                testcaseDetail.setDatabaseId(variable.getDatabaseId());
                if(variable.getId()!=null){
                    testcaseDetail.setId(variable.getId());
                    testcaseDetailMapper.updateByPrimaryKey(testcaseDetail);
                    ids.add(variable.getId());
                }else {
                    testcaseDetail.setTestcaseId(id);
                    testcaseDetailMapper.insertOne(testcaseDetail);
                    ids.add(testcaseDetail.getId());
                }
            }
            testcaseDetailMapper.deleteNotIn(ids,id,null,"variables");
        }else {
            testcaseDetailMapper.deleteNotIn(null,id,null,"variables");
        }

        if(req.getParameters()!=null && req.getParameters().size()>0){
            List<Integer> ids = new ArrayList<>();
            for(TestcaseReq.Parameter parameter:req.getParameters()){
                TestcaseDetail testcaseDetail= new TestcaseDetail();
                testcaseDetail.setScope("parameters");
                testcaseDetail.setName(parameter.getKeyName());
                testcaseDetail.setValue(parameter.getValue());
                if(parameter.getId()!=null){
                    testcaseDetail.setId(parameter.getId());
                    testcaseDetailMapper.updateByPrimaryKey(testcaseDetail);
                    ids.add(parameter.getId());
                }else {
                    testcaseDetail.setTestcaseId(id);
                    testcaseDetailMapper.insertOne(testcaseDetail);
                    ids.add(testcaseDetail.getId());
                }
            }
            testcaseDetailMapper.deleteNotIn(ids,id,null,"parameters");
        }else {
            testcaseDetailMapper.deleteNotIn(null,id,null,"parameters");
        }

        if(req.getSetuphooks()!=null && req.getSetuphooks().size()>0){
            List<Integer> ids = new ArrayList<>();
            for(TestcaseReq.Setuphook setuphook:req.getSetuphooks()){
                TestcaseDetail testcaseDetail= new TestcaseDetail();
                testcaseDetail.setScope("setupHooks");
                testcaseDetail.setValue(setuphook.getSql());
                testcaseDetail.setDatabaseId(setuphook.getDatabaseId());
                if(setuphook.getId()!=null){
                    testcaseDetail.setId(setuphook.getId());
                    testcaseDetailMapper.updateByPrimaryKey(testcaseDetail);
                    ids.add(setuphook.getId());
                }else {
                    testcaseDetail.setTestcaseId(id);
                    testcaseDetailMapper.insertOne(testcaseDetail);
                    ids.add(testcaseDetail.getId());
                }
            }
            testcaseDetailMapper.deleteNotIn(ids,id,null,"setupHooks");
        }else {
            testcaseDetailMapper.deleteNotIn(null,id,null,"setupHooks");
        }

        if(req.getReqHeaders()!=null && req.getReqHeaders().size()>0){
            List<Integer> ids = new ArrayList<>();
            for(TestcaseReq.ReqHeader reqHeader:req.getReqHeaders()){
                TestcaseDetail testcaseDetail= new TestcaseDetail();
                testcaseDetail.setScope("reqHeaders");
                testcaseDetail.setName(reqHeader.getKeyName());
                testcaseDetail.setValue(reqHeader.getValue());
                if(reqHeader.getId()!=null){
                    testcaseDetail.setId(reqHeader.getId());
                    testcaseDetailMapper.updateByPrimaryKey(testcaseDetail);
                    ids.add(reqHeader.getId());
                }else {
                    testcaseDetail.setTestcaseId(id);
                    testcaseDetailMapper.insertOne(testcaseDetail);
                    ids.add(testcaseDetail.getId());
                }
            }
            testcaseDetailMapper.deleteNotIn(ids,id,null,"reqHeaders");
        }else {
            testcaseDetailMapper.deleteNotIn(null,id,null,"reqHeaders");
        }

        if(req.getReqParams()!=null && req.getReqParams().size()>0){
            List<Integer> ids = new ArrayList<>();
            for(TestcaseReq.ReqParam reqParam:req.getReqParams()){
                TestcaseDetail testcaseDetail= new TestcaseDetail();
                testcaseDetail.setScope("reqParams");
                testcaseDetail.setName(reqParam.getKeyName());
                testcaseDetail.setValue(reqParam.getValue());
                if(reqParam.getId()!=null){
                    testcaseDetail.setId(reqParam.getId());
                    testcaseDetailMapper.updateByPrimaryKey(testcaseDetail);
                    ids.add(reqParam.getId());
                }else {
                    testcaseDetail.setTestcaseId(id);
                    testcaseDetailMapper.insertOne(testcaseDetail);
                    ids.add(testcaseDetail.getId());
                }
            }
            testcaseDetailMapper.deleteNotIn(ids,id,null,"reqParams");
        }else {
            testcaseDetailMapper.deleteNotIn(null,id,null,"reqParams");
        }

        if(req.getResponses()!=null && req.getResponses().size()>0){
            recursionEditResponse(req.getResponses(),id,null,false);
//            List<Integer> ids = new ArrayList<>();
//            for(TestcaseReq.Response response:req.getResponses()){
//                TestcaseDetail testcaseDetail= new TestcaseDetail();
//                testcaseDetail.setScope("response");
//                testcaseDetail.setName(response.getName());
//                testcaseDetail.setType(response.getType());
//                testcaseDetail.setExpectedValue(response.getExpectedValue());
//                testcaseDetail.setComparator(response.getComparator());
//                if(response.getId()!=null){
//                    testcaseDetail.setId(response.getId());
//                    testcaseDetailMapper.updateByPrimaryKey(testcaseDetail);
//                    ids.add(response.getId());
//                }else {
//                    testcaseDetail.setTestcaseId(id);
//                    testcaseDetailMapper.insertOne(testcaseDetail);
//                    ids.add(testcaseDetail.getId());
//                }
//            }
//            testcaseDetailMapper.deleteNotIn(ids,id,"response");
        }else {
            testcaseDetailMapper.deleteNotIn(null,id,null,"response");
        }

        if(req.getReqBody()!=null){
            TestcaseDetail testcaseDetail= new TestcaseDetail();
            testcaseDetail.setValue(req.getReqBody().get("requestBody").toString());
            testcaseDetail.setScope("requestBody");
            if(req.getReqBody().containsKey("id")){
                testcaseDetail.setId(Integer.parseInt(req.getReqBody().get("id").toString()));
                testcaseDetailMapper.updateByPrimaryKey(testcaseDetail);
            }else {
                testcaseDetail.setTestcaseId(id);
                testcaseDetailMapper.insertOne(testcaseDetail);
            }
        }
    }

    public void recursionEditResponse(List<TestcaseReq.Response> responseItemList,Integer testcaseId,Integer parentId,Boolean flag){
        Integer i=0;
        List<Integer> ids = new ArrayList<>();
        for(TestcaseReq.Response responseItem: responseItemList){
            TestcaseDetail testcaseDetail = new TestcaseDetail();
            testcaseDetail.setName(responseItem.getName());
            testcaseDetail.setType(responseItem.getType());
            testcaseDetail.setComparator(responseItem.getComparator().equals("")?null:responseItem.getComparator());
            testcaseDetail.setExpectedValue(responseItem.getExpectedValue());
            testcaseDetail.setScope("response");
            testcaseDetail.setTestcaseId(testcaseId);
            testcaseDetail.setParentId(parentId);
            if(flag){
                testcaseDetail.setArrayIndex(i);
            }
            if(responseItem.getId()!=null){
                testcaseDetail.setId(responseItem.getId());
                testcaseDetailMapper.updateByPrimaryKey(testcaseDetail);
                ids.add(responseItem.getId());
            }else {
                testcaseDetail.setTestcaseId(testcaseId);
                testcaseDetailMapper.insertOne(testcaseDetail);
                ids.add(testcaseDetail.getId());
            }
            if(responseItem.getChildren()!=null){
                if(responseItem.getType().equals("Array")){
                    flag=true;
                }
                recursionEditResponse(responseItem.getChildren(),testcaseId,testcaseDetail.getId(),flag);
            }
            i=i+1;
        }
        testcaseDetailMapper.deleteNotIn(ids,testcaseId,parentId,"response");
    }

    public void deleteTestcase(Integer id){
        testcaseDetailMapper.deleteByTestcaseId(id);
        testcaseMapper.deleteByPrimaryKey(id);
    }

    public List runTestcaseList(RunTestcaseReq req){
        List lista=new ArrayList();
        if(req.getTestcaseIds().size()<=0){
            throw (new ClientException(BaseConstans.BUSI_CODE.NOT_RUN_CASE.getCode(),BaseConstans.BUSI_CODE.NOT_RUN_CASE.getMsg()));
        }else {
            for(Integer testcaseId:req.getTestcaseIds()){
                runTestcase(req.getEnvId(),req.getProjectId(),testcaseId,req.getFlag());
            }
        }
        return lista;
    }


    public void runTestcase(Integer envId,Integer projectId,Integer testcaseId,Boolean flag){
        //初始化数据库
//        String testcaseDb= testcaseDbMapper.selectByTestcaseId(testcaseId);
//        List dbIds = JSON.parseArray(testcaseDb,String.class);
//        initDatabase(dbIds);
        //拼接用例数据
        Project project = projectMapper.selectByPrimaryKey(projectId);
        String baseUrl = envId==1 ? project.getDevAddress():project.getProdAddress();
        Testcase testcase = testcaseMapper.selectByPrimaryKey(testcaseId);
        JSONObject totalVariableJson = new JSONObject();
        if(testcase.getConfigIds()!=null && testcase.getConfigIds().length()>0){
            List<String> configIdsList = Arrays.asList(testcase.getConfigIds());
            for(String configId: configIdsList){
                List<TestcaseConfigDetail> testcaseConfigDetailList = testcaseConfigDetailMapper.selectByConfigId(Integer.parseInt(configId));
                for(TestcaseConfigDetail testcaseConfigDetail: testcaseConfigDetailList){
                    totalVariableJson.put(testcaseConfigDetail.getName(),testcaseConfigDetail.getValue().toString());
                }
            }
        }

        List<TestcaseDetail> testcaseDetailList = testcaseDetailMapper.selectByTestcaseId(testcaseId);
        JSONObject variablesJson= new JSONObject();
        JSONObject reqHeadersJson= new JSONObject();
        JSONObject reqParamsJson= new JSONObject();
        JSONObject requestBodyJson= new JSONObject();
        JSONArray requestBodyJsonArray= new JSONArray();
        JSONObject configParametersJson= new JSONObject();
        List validateList = new ArrayList();
        if(testcaseDetailList.size()<=0){
            throw (new ClientException(BaseConstans.BUSI_CODE.CASEDETAIL_NOT_EXIT.getCode(),BaseConstans.BUSI_CODE.CASEDETAIL_NOT_EXIT.getMsg()));
        }else {
            for(TestcaseDetail testcaseDetail: testcaseDetailList){
                if(testcaseDetail.getScope().equals("setupHooks")){
                    //执行sql,只执行添加删除sql
                    DatabaseWithBLOBs databaseInfo =databaseMapper.selectByPrimaryKey(testcaseDetail.getDatabaseId());
                    String databaseAddress = databaseInfo.getHost()+":"+databaseInfo.getPort();
                    Connection conn = JDBCUtil.getConnection(databaseAddress,databaseInfo.getDbName(),databaseInfo.getUsername(),databaseInfo.getPassword());
                    try {
                        PreparedStatement pst = conn.prepareStatement(testcaseDetail.getValue());
                        pst.execute();
                        JDBCUtil.close(pst, conn);
                    }catch (SQLException e){
                        throw (new ClientException(BaseConstans.BUSI_CODE.SQL_ERROR.getCode(),e.getMessage()));
                    }catch (Exception e){
                        throw (new ClientException(BaseConstans.BUSI_CODE.RUN_SQL_ERROR.getCode(),e.getMessage()));

                    }
                }

                if(testcaseDetail.getScope().equals("parameters")){
                    //设置多个用例

//                    configParametersJson.put(testcaseDetail.getName(),JSON.parseArray(testcaseDetail.getValue(),String.class));
                    configParametersJson.put(testcaseDetail.getName(),testcaseDetail.getValue());
                }

                if(testcaseDetail.getScope().equals("variables")){
                    if(testcaseDetail.getType().equals("Sql")){
//                        List<String> keyList = Arrays.asList(testcaseDetail.getName());
                        List keyList =JSON.parseArray(testcaseDetail.getName(),String.class);
//                        执行后赋值
                        DatabaseWithBLOBs databaseInfo =databaseMapper.selectByPrimaryKey(testcaseDetail.getDatabaseId());
                        String databaseAddress = databaseInfo.getHost()+":"+databaseInfo.getPort();
                        Connection conn = JDBCUtil.getConnection(databaseAddress,databaseInfo.getDbName(),databaseInfo.getUsername(),databaseInfo.getPassword());
                        ResultSet rs = null;
                        try {
                            PreparedStatement pst = conn.prepareStatement(testcaseDetail.getValue());
                            rs = pst.executeQuery();
                            while (rs.next()){
                                for(Object k: keyList){
                                    variablesJson.put(k.toString(),rs.getString(k.toString()));
                                }
                            }

                            JDBCUtil.close(pst, conn);
                        }catch (SQLException e){
                            throw (new ClientException(BaseConstans.BUSI_CODE.SQL_ERROR.getCode(),e.getMessage()));
                        }catch (Exception e){
                            throw (new ClientException(BaseConstans.BUSI_CODE.RUN_SQL_ERROR.getCode(),e.getMessage()));
                        }

                    }else {
                        variablesJson.put(testcaseDetail.getName(),testcaseDetail.getValue());
                    }
                }

                if(testcaseDetail.getScope().equals("reqHeaders")){
                    reqHeadersJson.put(testcaseDetail.getName(),testcaseDetail.getValue());
                }

                if(testcaseDetail.getScope().equals("reqParams")){
                    reqParamsJson.put(testcaseDetail.getName(),testcaseDetail.getValue());
                }

                if(testcaseDetail.getScope().equals("response")){
                    JSONObject item = new JSONObject();
                    String type = null;
                    if(testcaseDetail.getComparator()!=null){
                        switch (testcaseDetail.getComparator()){
                            case "=":
                                type="eq";
                                break;
                            case ">":
                                type="gt";
                                break;
                            case "<":
                                type="lt";
                                break;
                            case ">=":
                                type="ge";
                                break;
                            case "<=":
                                type="le";
                                break;
                            case "!=":
                                type="ne";
                                break;
                            case "contain":
                                type="contains";
                                break;
                        }
                    }

                    List actualExceptedList =new ArrayList();
                    List <String> resultList = nameJoint(testcaseDetail.getId());
                    if(!resultList.get(0).equals("Object") && !resultList.get(0).equals("Array")){
                        actualExceptedList.add(resultList.get(1));
                        switch (resultList.get(0)){
                            case "Number":
                                actualExceptedList.add(Integer.parseInt(testcaseDetail.getExpectedValue()));
                                break;
                            default:
                                actualExceptedList.add(testcaseDetail.getExpectedValue());
                        }

                        item.put(type,actualExceptedList);
                        validateList.add(item);
                    }
                }
                if(testcaseDetail.getScope().equals("requestBody")){
                    if(testcaseDetail.getValue().startsWith("[")){
                        requestBodyJsonArray = JSON.parseArray(testcaseDetail.getValue());
                    }else {
                        requestBodyJson = JSON.parseObject(testcaseDetail.getValue());
                    }
                }
            }
        }

        JSONObject configJson = new JSONObject();
        configJson.put("name",testcase.getTestcaseName());
        configJson.put("id",testcase.getTestcaseName());
        configJson.put("variables",totalVariableJson);
        JSONObject baseUrlJson = new JSONObject();
        baseUrlJson.put("base_url",baseUrl);
        configJson.put("request",baseUrlJson);
        configJson.put("parameters",configParametersJson);

        JSONObject requestJson = new JSONObject();
        requestJson.put("headers",reqHeadersJson);
        requestJson.put("json",requestBodyJson.isEmpty()?requestBodyJsonArray:requestBodyJson);
        requestJson.put("method",testcase.getMethod());
        requestJson.put("url",testcase.getUrl());
        requestJson.put("params",reqParamsJson);



        JSONObject testJson = new JSONObject();
        testJson.put("name", testcase.getTestcaseName());
        testJson.put("request",requestJson);
        testJson.put("validate",validateList);
        testJson.put("variables",variablesJson);


        JSONObject testcaseConfigJson= new JSONObject();
        testcaseConfigJson.put("config",configJson);

        JSONObject testcaseTestJson= new JSONObject();
        testcaseTestJson.put("test",testJson);

        List testcaseList= new ArrayList();
        testcaseList.add(testcaseConfigJson);
        testcaseList.add(testcaseTestJson);
        //创建可执行的yaml文件
        createTestcaseYaml(project.getProjectName(),testcase.getTestcaseName(),testcaseList);
        //执行测试用例
        String reportContent = execTestcase(project.getProjectName(),testcase.getTestcaseName());
        Boolean status = (Boolean) JSON.parseObject(reportContent).get("status");
        Integer result = status ? 1 : 0;
        Report report = Report.builder().testcaseId(testcaseId).content(reportContent).result(result).build();
        //报告数据落表
        reportMapper.insert(report);
        //return testcaseList;
        //删除镜像
//        if(flag){
//            deleteDatabase();
//        }
    }

    //逐级拼接response中的名称
    public List<String> nameJoint(Integer id){
        TestcaseDetail testcaseDetail = testcaseDetailMapper.selectByPrimaryKey(id);

        if(testcaseDetail.getParentId()!=null){
            if(testcaseDetail.getArrayIndex()!=null){
                List resultList = new ArrayList();
                String type = testcaseDetail.getType();
                String name = nameJoint(testcaseDetail.getParentId()).get(1)+"."+testcaseDetail.getArrayIndex().toString()+"."+testcaseDetail.getName();
                resultList.add(type);
                resultList.add(name);
                return resultList;
            }else {
                List resultList = new ArrayList();
                String type = testcaseDetail.getType();
                String name = nameJoint(testcaseDetail.getParentId()).get(1)+"."+testcaseDetail.getName();
                resultList.add(type);
                resultList.add(name);
                return resultList;
            }
        }else {
            List resultList = new ArrayList();
            String type = testcaseDetail.getType();
            String name = "content."+testcaseDetail.getName();
            resultList.add(type);
            resultList.add(name);
            return resultList;
        }
    }

    public void createTestcaseYaml(String projectName,String testcaseName,List caseList){
        try {
            String[] args = new String[]{"python3", pythonConstants.pythonCreateYaml, projectName, testcaseName, JSON.toJSONString(caseList)};
            Process proc = Runtime.getRuntime().exec(args);

            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//                if (String.valueOf(in.readLine()).equals("ok")) {
//                    result = "0";
//                }
            in.close();
            proc.waitFor();
        }catch (IOException e){
            e.printStackTrace();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public String execTestcase(String projectName,String testcaseName){
        String reportContent = null;
        try{
            String[] args = new String[]{"python3", pythonConstants.pythonRunTest, projectName, testcaseName};
            Process proc = Runtime.getRuntime().exec(args);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while((line=in.readLine())!=null){
                if(line.startsWith("{")){
                    reportContent=line;
                }
            }
            in.close();
            proc.waitFor();
        }catch (IOException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return reportContent;
    }

    public String createMysql(String imageName){

//          String res= httpClientTemplate.doPost("http://127.0.0.1:2375/containers/create?name=mysql:5.7",(Object)null);
//        String res= httpClientTemplate.doGet("http://127.0.0.1:2375/containers/json");
//        DockerUtil dockerUtil = new DockerUtil();
//        String containerId = dockerUtil.createContainer(imageName);
//        return containerId;
//        return res;
//        String aaa = dockerUtil.inspect(imageName).toString();
        return "aaa";
    }

    public void initDatabase(List<Integer> ids){
//        DatabaseWithBLOBs database= databaseMapper.selectByPrimaryKey(id);
//        try{
//            createDatabase(database.getDbName());
//        } catch (SQLException e){
//            e.printStackTrace();
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        try{
//            if(database.getCreateTableSql()!=null){
//                createTable(database.getDbName(),database.getCreateTableSql());
//            }
//        }catch (SQLException e){
//            e.printStackTrace();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        //创建初始化sql脚本
        List<DatabaseWithBLOBs> databaseList= databaseMapper.selectByIds(ids);
        FileOutputStream outputStream = null;
        try{
            File file = new File(dockerConstants.dockerInitSqlPath);
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();
            outputStream = new FileOutputStream(file,true);
            for(DatabaseWithBLOBs database : databaseList){
                if(database.getDbName()==null){
                    throw (new ClientException(BaseConstans.BUSI_CODE.DBNAME_NOT_EXIT.getCode(),BaseConstans.BUSI_CODE.DBNAME_NOT_EXIT.getMsg()));
                }else {
                    outputStream.write(("CREATE DATABASE " + database.getDbName() + ";\n").getBytes());
                    outputStream.write(("USE " + database.getDbName() + ";\n").getBytes());
                }
                if(database.getCreateTableSql()!=null){
                    outputStream.write((database.getCreateTableSql()+ "\n").getBytes());
                }
                if(database.getInsertSql()!=null){
                    outputStream.write((database.getInsertSql()+"\n").getBytes());
                }
            }
        } catch (Exception e){
            throw (new ClientException(null,e.getMessage()));
        }finally {
            try{
                outputStream.close();
            }catch (IOException e){
                throw (new ClientException(null,e.getMessage()));
            }
        }
        //启动数据库
        String shellUrl ="sh " + dockerConstants.shellPath;
        Integer pid = ShellUtil.executeShellReturnexitValue(shellUrl);

    }

    //删除数据库镜像等操作
    public void deleteDatabase(){
        httpClientTemplate.doPost(dockerConstants.dockerIpPort + "/containers/mysqlc/stop",(Object)null);
        httpClientTemplate.doDelete(dockerConstants.dockerIpPort + "/containers/mysqlc");
        httpClientTemplate.doDelete(dockerConstants.dockerIpPort + "/images/mysql:5.7c");
    }

//    public void createDatabase(String dbName) throws SQLException {
//        Connection conn = JDBCUtil.getConnection("");
//        String createDbSql = "create database " + dbName;
//        PreparedStatement pst = conn.prepareStatement(createDbSql);
//        pst.executeUpdate();
//        JDBCUtil.close(pst, conn);
//    }

//    public void createTable(String dbName,String createTableSql) throws SQLException {
//        Connection conn = JDBCUtil.getConnection(dbName);
//        PreparedStatement pst = conn.prepareStatement(createTableSql);
//        pst.executeUpdate();
//        JDBCUtil.close(pst, conn);
//    }



//    @Resource
//    private TestcaseMapper testcaseMapper;


//    public void createTestcase(CreateTestcaseReq req){
//        TestcaseWithBLOBs testcaseWithBLOBs = new TestcaseWithBLOBs();
//        testcaseWithBLOBs.setProjectId(req.getProjectId());
//        testcaseWithBLOBs.setBody(JSON.toJSONString(req.getBody()));
//        testcaseWithBLOBs.setExtractVariables(JSON.toJSONString(req.getExtractVariables()));
//        testcaseWithBLOBs.setParameters(JSON.toJSONString(req.getParameters()));
//        testcaseWithBLOBs.setParams(JSON.toJSONString(req.getParams()));
//        testcaseWithBLOBs.setSetupHooks(JSON.toJSONString(req.getSetupHooks()));
//        testcaseWithBLOBs.setSqlSentence(JSON.toJSONString(req.getSqlSentence()));
//        testcaseWithBLOBs.setValidate(JSON.toJSONString(req.getValidate()));
//        testcaseWithBLOBs.setVariables(JSON.toJSONString(req.getVariables()));
//        testcaseWithBLOBs.setConfigIds(JSON.toJSONString(req.getConfigIds()));
//        testcaseWithBLOBs.setDescription(req.getDescription());
//        testcaseWithBLOBs.setEnvId(req.getEnvId());
//        testcaseWithBLOBs.setHeaders(JSON.toJSONString(req.getHeaders()));
//        testcaseWithBLOBs.setPath(req.getPath());
//        testcaseWithBLOBs.setTestcaseName(req.getTestcaseName());
//        testcaseWithBLOBs.setRequestType(req.getRequestType());
//        testcaseMapper.insert(testcaseWithBLOBs);
//    }
//
//
////    运行单个用例
//    public String runTestcase(List<Integer> req){
//        String result = "-1";
//        for(Integer testcaseId : req){
//            String project= "test_paper_paper"; //临时参数
//            String patha = "paper_user_login"; //临时参数
//
//            List<Object> testcase_list = JSON.parseArray("[{\"test\": {\"request\": " +
//                    "{\"url\": \"/paper/user/login\", \"method\": \"POST\", \"json\": " +
//                    "{\"username\": \"testQuiz2\", \"password\": \"e10adc3949ba59abbe56e057f20f883e\"}}, " +
//                    "\"name\": \"case_paper_login\", \"extract\": [{\"token\": \"content.retval\"}]}}]");//临时参数
//            try {
//                String[] args = new String[]{"python3", "/Users/pengyishuang/Desktop/mypro01/create_yaml.py", project, patha, JSON.toJSONString(testcase_list)};
//                Process proc = Runtime.getRuntime().exec(args);
//
//                BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//                if (String.valueOf(in.readLine()).equals("ok")) {
//                    result = "0";
//                }
//                in.close();
//                proc.waitFor();
//            }catch (IOException e){
//                e.printStackTrace();
//            }catch(InterruptedException e){
//                e.printStackTrace();
//            }
//        }
//
//        return result;
//
//    }
}
