package edu.jiahui.testcase.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.sun.org.apache.xpath.internal.operations.Bool;
import edu.jiahui.framework.exceptions.ClientException;
import edu.jiahui.framework.httpclient.HttpClientTemplate;
import edu.jiahui.framework.threadlocal.ParameterThreadLocal;
import edu.jiahui.testcase.constants.BaseConstans;
import edu.jiahui.testcase.constants.DockerConstants;
import edu.jiahui.testcase.constants.PythonConstants;
import edu.jiahui.testcase.domain.*;
import edu.jiahui.testcase.domain.request.CaseListReq;
import edu.jiahui.testcase.domain.request.RunTestcaseReq;
import edu.jiahui.testcase.domain.request.TestcaseReq;
import edu.jiahui.testcase.domain.response.TestcaseRes;
import edu.jiahui.testcase.mapper.*;
import edu.jiahui.testcase.utils.JDBCUtil;
import edu.jiahui.testcase.utils.ShellUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ReportDetailMapper reportDetailMapper;

    @Resource
    private HttpClientTemplate httpClientTemplate;

    @Autowired
    private CaseService caseService;


    public void addTestcase(Integer id,TestcaseReq req){
        Testcase testcase = new Testcase();
        testcase.setTestcaseName(req.getTestcaseName());
        testcase.setEnvId(req.getEnvId());
        testcase.setConfigIds(req.getConfigIds().toString());
        testcase.setInterfaceId(id);
        testcase.setUrl(req.getPath());
        testcase.setMethod(req.getMethod());
        testcase.setCreatedBy(Integer.parseInt(ParameterThreadLocal.getUid()));
        testcase.setUpdatedBy(Integer.parseInt(ParameterThreadLocal.getUid()));
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

        if(req.getExtracts()!=null && req.getExtracts().size()>0){
            for(TestcaseReq.Extract extract:req.getExtracts()){
                TestcaseDetail testcaseDetail = new TestcaseDetail();
                testcaseDetail.setScope("extract");
                testcaseDetail.setName(extract.getName());
                testcaseDetail.setValue(extract.getResponseKey());
                testcaseDetail.setTestcaseId(testcaseId);
                testcaseDetailList.add(testcaseDetail);
            }
        }

        testcaseDetailMapper.insert(testcaseDetailList);

        if(req.getResponses()!=null && req.getResponses().size()>0){
            recursionResponse(req.getResponses(),testcaseId,null);
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

    public void recursionResponse(List<TestcaseReq.Response> responseItemList,Integer testcaseId,Integer parentId){
        Integer i=0;
        for(TestcaseReq.Response responseItem: responseItemList){
            TestcaseDetail testcaseDetail=TestcaseDetail.builder()
                    .arrayIndex(responseItem.getIndexValue())
                    .name(responseItem.getName())
                    .type(responseItem.getType())
                    .comparator(responseItem.getComparator())
                    .expectedValue(responseItem.getExpectedValue())
                    .scope("response")
                    .testcaseId(testcaseId).parentId(parentId).build();
            testcaseDetailMapper.insertOne(testcaseDetail);
//            testcaseDetailList.add(testcaseDetail);
            if(responseItem.getChildren()!=null){
                recursionResponse(responseItem.getChildren(),testcaseId,testcaseDetail.getId());
            }
            i=i+1;
        }
    }

    public TestcaseRes testcaseInfo(Integer testcaseId){

//        TestcaseRes testcaseRes = new TestcaseRes();

        Testcase testcase=testcaseMapper.selectByPrimaryKey(testcaseId);
//        testcaseRes.setEnvId(testcase.getEnvId());
//        testcaseRes.setPath(testcase.getUrl());
//        testcaseRes.setConfigIds(JSON.parseArray(testcase.getConfigIds(),Integer.class));
//        testcaseRes.setTestcaseName(testcase.getTestcaseName());
        TestcaseRes testcaseRes=TestcaseRes.builder()
                .envId(testcase.getEnvId())
                .path(testcase.getUrl())
                .method(testcase.getMethod())
                .configIds(JSON.parseArray(testcase.getConfigIds(),Integer.class))
                .testcaseName(testcase.getTestcaseName())
                .build();
        List<TestcaseDetail> testcaseDetailList = testcaseDetailMapper.selectByTestcaseId(testcaseId);
        List<TestcaseRes.Variable> variableList = new ArrayList<>();
        List<TestcaseRes.Parameter> parameterList = new ArrayList<>();
        List<TestcaseRes.Setuphook> setuphookList = new ArrayList<>();
        List<TestcaseRes.ReqHeader> reqHeaderList = new ArrayList<>();
        List<TestcaseRes.ReqParam> reqParamList = new ArrayList<>();
        List<TestcaseRes.Extract> extractList = new ArrayList<>();
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
                    case "extract":
                        TestcaseRes.Extract extract = new TestcaseRes.Extract();
                        extract.setId(testcaseDetail.getId());
                        extract.setName(testcaseDetail.getName());
                        extract.setResponseKey(testcaseDetail.getValue());
                        extractList.add(extract);
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
        testcaseRes.setExtracts(extractList);
        testcaseRes.setResponses(responseList);
        testcaseRes.setReqBody(reqBodyMap);
        return testcaseRes;

    }

    public TestcaseRes.Response recursionSearchResponse(TestcaseDetail testcaseDetail){
//        TestcaseRes.Response  response = new TestcaseRes.Response();
//        response.setId(testcaseDetail.getId());
//        response.setName(testcaseDetail.getName());
//        response.setType(testcaseDetail.getType());
//        response.setComparator(testcaseDetail.getComparator()==null?"":testcaseDetail.getComparator());
//        response.setExpectedValue(testcaseDetail.getExpectedValue());
        TestcaseRes.Response response= TestcaseRes.Response.builder()
                .id(testcaseDetail.getId())
                .indexValue(testcaseDetail.getArrayIndex())
                .name(testcaseDetail.getName())
                .type(testcaseDetail.getType())
                .comparator(testcaseDetail.getComparator())
                .expectedValue(testcaseDetail.getExpectedValue())
                .build();
        List<TestcaseDetail> testcaseDetails= testcaseDetailMapper.selectByParentId(testcaseDetail.getId());
        if(testcaseDetails.size()>=0){
            List<TestcaseRes.Response> responses= new ArrayList<>();
            for(TestcaseDetail td: testcaseDetails){
//                TestcaseRes.Response res= new TestcaseRes.Response();
//                res=recursionSearchResponse(td);
                responses.add(recursionSearchResponse(td));
            }
            if(responses.size()>0){
                response.setChildren(responses);
            }
        }
        return response;
    }

    public void updateTestcase(Integer id,TestcaseReq req){
        Testcase testcase= Testcase.builder()
                .testcaseName(req.getTestcaseName())
                .envId(req.getEnvId())
                .url(req.getPath())
                .configIds(req.getConfigIds().toString())
                .method(req.getMethod())
                .id(id)
                .updatedBy(Integer.parseInt(ParameterThreadLocal.getUid()))
                .build();
        testcaseMapper.updateByPrimaryKey(testcase);
        if(req.getVariables()!=null && req.getVariables().size()>0){
            List<Integer> ids = new ArrayList<>();
            for(TestcaseReq.Variable variable:req.getVariables()){
                TestcaseDetail testcaseDetail= TestcaseDetail.builder()
                        .scope("variables")
                        .name(variable.getName())
                        .value(variable.getValue())
                        .type(variable.getType())
                        .databaseId(variable.getDatabaseId())
                        .updatedBy(Integer.parseInt(ParameterThreadLocal.getUid()))
                        .build();
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
                TestcaseDetail testcaseDetail= TestcaseDetail.builder()
                        .scope("parameters")
                        .name(parameter.getKeyName())
                        .value(parameter.getValue())
                        .updatedBy(Integer.parseInt(ParameterThreadLocal.getUid()))
                        .build();
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
                TestcaseDetail testcaseDetail= TestcaseDetail.builder()
                        .scope("setupHooks")
                        .value(setuphook.getSql())
                        .databaseId(setuphook.getDatabaseId())
                        .updatedBy(Integer.parseInt(ParameterThreadLocal.getUid()))
                        .build();
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
                TestcaseDetail testcaseDetail= TestcaseDetail.builder()
                        .scope("reqHeaders")
                        .name(reqHeader.getKeyName())
                        .value(reqHeader.getValue())
                        .updatedBy(Integer.parseInt(ParameterThreadLocal.getUid()))
                        .build();
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
                TestcaseDetail testcaseDetail= TestcaseDetail.builder()
                        .scope("reqParams")
                        .name(reqParam.getKeyName())
                        .value(reqParam.getValue())
                        .updatedBy(Integer.parseInt(ParameterThreadLocal.getUid()))
                        .build();
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

        if(req.getExtracts()!=null && req.getExtracts().size()>0){
            List<Integer> ids = new ArrayList<>();
            for(TestcaseReq.Extract et : req.getExtracts()){
                TestcaseDetail testcaseDetail= TestcaseDetail.builder()
                        .scope("extract")
                        .name(et.getName())
                        .value(et.getResponseKey())
                        .updatedBy(Integer.parseInt(ParameterThreadLocal.getUid()))
                        .build();
                if(et.getId()!=null){
                    testcaseDetail.setId(et.getId());
                    testcaseDetailMapper.updateByPrimaryKey(testcaseDetail);
                    ids.add(et.getId());
                }else {
                    testcaseDetail.setTestcaseId(id);
                    testcaseDetailMapper.insertOne(testcaseDetail);
                    ids.add(testcaseDetail.getId());
                }
            }
            testcaseDetailMapper.deleteNotIn(ids,id,null,"extract");
        }else {
            testcaseDetailMapper.deleteNotIn(null,id,null,"extract");
        }

        if(req.getResponses()!=null && req.getResponses().size()>0){
            recursionEditResponse(req.getResponses(),id,null);
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

        if(req.getReqBody().size()>0){
            TestcaseDetail testcaseDetail= TestcaseDetail.builder()
                    .value(req.getReqBody().get("requestBody").toString())
                    .scope("requestBody")
                    .updatedBy(Integer.parseInt(ParameterThreadLocal.getUid()))
                    .build();
            if(req.getReqBody().containsKey("id")){
                testcaseDetail.setId(Integer.parseInt(req.getReqBody().get("id").toString()));
                testcaseDetailMapper.updateByPrimaryKey(testcaseDetail);
            }else {
                testcaseDetail.setTestcaseId(id);
                testcaseDetailMapper.insertOne(testcaseDetail);
            }
        }
    }

    public void recursionEditResponse(List<TestcaseReq.Response> responseItemList,Integer testcaseId,Integer parentId){
        Integer i=0;
        List<Integer> ids = new ArrayList<>();
        for(TestcaseReq.Response responseItem: responseItemList){
            TestcaseDetail testcaseDetail=TestcaseDetail.builder()
                    .arrayIndex(responseItem.getIndexValue())
                    .name(responseItem.getName())
                    .type(responseItem.getType())
                    .comparator(responseItem.getComparator())
                    .expectedValue(responseItem.getExpectedValue())
                    .scope("response")
                    .testcaseId(testcaseId)
                    .parentId(parentId)
                    .updatedBy(Integer.parseInt(ParameterThreadLocal.getUid()))
                    .build();
//            if(flag){
//                testcaseDetail.setArrayIndex(i);
//            }
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
//                if(responseItem.getType().equals("Array")){
//                    flag=true;
//                }
                recursionEditResponse(responseItem.getChildren(),testcaseId,testcaseDetail.getId());
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

//
    public void runTestcase(Integer envId,Integer projectId,Integer testcaseId,Boolean flag){
        Project project = projectMapper.selectByPrimaryKey(projectId);
        Testcase testcase = testcaseMapper.selectByPrimaryKey(testcaseId);
        List testcaseList = caseService.createTestcaseJson(envId,projectId,testcaseId);
        //创建可执行的yaml文件
        caseService.createTestcaseYaml(project.getProjectName(),testcase.getTestcaseName(),testcaseList);
        //执行测试用例
        String reportContent = caseService.execTestcase(project.getProjectName(),testcase.getTestcaseName());
        JSONObject reportContentJson= JSON.parseObject(JSON.parseArray(reportContent).getString(0));
        Report report= Report.builder().testcaseId(testcaseId).
                result(reportContentJson.getBoolean("success") ? 1 : 0)
                .createdBy(ParameterThreadLocal.getUid())
                .updatedBy(ParameterThreadLocal.getUid())
                .build();
        //Boolean status = (Boolean) JSON.parseObject(reportContent).get("status");
        //Integer result = status ? 1 : 0;
//        Report report = Report.builder().testcaseId(testcaseId).content(reportContent).result(result).build();
        //报告数据落表
        reportMapper.insert(report);
        //只有一个用例取一个
//        JSONObject recordsJson= JSON.parseObject(JSON.parseArray(reportContentJson.getString("records")).getString(0));
//        ReportDetail reportDetail= ReportDetail.builder()
//                .reportId(report.getId())
//                .result(recordsJson.getString("status"))
//                .url(JSON.parseObject(JSON.parseObject(recordsJson.getString("meta_data")).getString("request")).getString("url"))
//                .method(JSON.parseObject(JSON.parseObject(recordsJson.getString("meta_data")).getString("request")).getString("method"))
//                .statusCode(JSON.parseObject(JSON.parseObject(recordsJson.getString("meta_data")).getString("response")).getString("status_code"))
//                .requestHeaders(JSON.parseObject(JSON.parseObject(recordsJson.getString("meta_data")).getString("request")).getString("headers"))
//                .requestBody(JSON.parseObject(JSON.parseObject(recordsJson.getString("meta_data")).getString("request")).getString("json"))
//                .validators(JSON.parseObject(recordsJson.getString("meta_data")).getString("validators"))
//                .response(JSON.parseObject(JSON.parseObject(recordsJson.getString("meta_data")).getString("response")).getString("content"))
//                .createdBy(ParameterThreadLocal.getUid())
//                .updatedBy(ParameterThreadLocal.getUid())
//                .build();
//        reportDetailMapper.insert(reportDetail);

        JSONArray recordsArray= JSON.parseArray(reportContentJson.getString("records"));
        for(int i=0;i<recordsArray.size();i++){
            JSONObject recordsJson= JSON.parseObject(recordsArray.getString(i));
            ReportDetail reportDetail= ReportDetail.builder()
                    .reportId(report.getId())
                    .result(recordsJson.getString("status"))
                    .url(JSON.parseObject(JSON.parseObject(recordsJson.getString("meta_data")).getString("request")).getString("url"))
                    .method(JSON.parseObject(JSON.parseObject(recordsJson.getString("meta_data")).getString("request")).getString("method"))
                    .statusCode(JSON.parseObject(JSON.parseObject(recordsJson.getString("meta_data")).getString("response")).getString("status_code"))
                    .requestHeaders(JSON.parseObject(JSON.parseObject(recordsJson.getString("meta_data")).getString("request")).getString("headers"))
                    .requestBody(JSON.parseObject(JSON.parseObject(recordsJson.getString("meta_data")).getString("request")).getString("json"))
                    .validators(JSON.parseObject(recordsJson.getString("meta_data")).getString("validators"))
                    .response(JSON.parseObject(JSON.parseObject(recordsJson.getString("meta_data")).getString("response")).getString("content"))
                    .attachment(recordsJson.getString("attachment"))
                    .createdBy(ParameterThreadLocal.getUid())
                    .updatedBy(ParameterThreadLocal.getUid())
                    .build();
            reportDetailMapper.insert(reportDetail);
        }
        //return testcaseList;
        //删除镜像 先注掉，数据库方案还未确定
//        if(flag){
//            deleteDatabase();
//        }
    }




    //调用接口方式创建容器，最终使用脚本方式创建容器
//    public String createMysql(String imageName){

//          String res= httpClientTemplate.doPost("http://127.0.0.1:2375/containers/create?name=mysql:5.7",(Object)null);
//        String res= httpClientTemplate.doGet("http://127.0.0.1:2375/containers/json");
//        DockerUtil dockerUtil = new DockerUtil();
//        String containerId = dockerUtil.createContainer(imageName);
//        return containerId;
//        return res;
//        String aaa = dockerUtil.inspect(imageName).toString();
//        return "aaa";
//    }

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
//        List<DatabaseWithBLOBs> databaseList= databaseMapper.selectByIds(ids);
//        FileOutputStream outputStream = null;
//        try{
//            File file = new File(dockerConstants.dockerInitSqlPath);
//            if(file.exists()){
//                file.delete();
//            }
//            file.createNewFile();
//            outputStream = new FileOutputStream(file,true);
//            for(DatabaseWithBLOBs database : databaseList){
//                if(database.getDbName()==null){
//                    throw (new ClientException(BaseConstans.BUSI_CODE.DBNAME_NOT_EXIT.getCode(),BaseConstans.BUSI_CODE.DBNAME_NOT_EXIT.getMsg()));
//                }else {
//                    outputStream.write(("CREATE DATABASE " + database.getDbName() + ";\n").getBytes());
//                    outputStream.write(("USE " + database.getDbName() + ";\n").getBytes());
//                }
//                if(database.getCreateTableSql()!=null){
//                    outputStream.write((database.getCreateTableSql()+ "\n").getBytes());
//                }
//                if(database.getInsertSql()!=null){
//                    outputStream.write((database.getInsertSql()+"\n").getBytes());
//                }
//            }
//        } catch (Exception e){
//            throw (new ClientException(null,e.getMessage()));
//        }finally {
//            try{
//                outputStream.close();
//            }catch (IOException e){
//                throw (new ClientException(null,e.getMessage()));
//            }
//        }
//        //启动数据库
//        String shellUrl ="sh " + dockerConstants.shellPath;
//        Integer pid = ShellUtil.executeShellReturnexitValue(shellUrl);

    }

    //删除数据库镜像等操作
//    public void deleteDatabase(){
//        httpClientTemplate.doPost(dockerConstants.dockerIpPort + "/containers/mysqlc/stop",(Object)null);
//        httpClientTemplate.doDelete(dockerConstants.dockerIpPort + "/containers/mysqlc");
//        httpClientTemplate.doDelete(dockerConstants.dockerIpPort + "/images/mysql:5.7c");
//    }

    public List<Testcase> caseList(CaseListReq req){
        List<Testcase> testcaseList = testcaseMapper.selectByIds(req);

        return testcaseList;
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
