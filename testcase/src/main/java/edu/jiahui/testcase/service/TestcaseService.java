package edu.jiahui.testcase.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import edu.jiahui.framework.exceptions.ClientException;
import edu.jiahui.testcase.constants.BaseConstans;
import edu.jiahui.testcase.domain.*;
import edu.jiahui.testcase.domain.request.RunTestcaseReq;
import edu.jiahui.testcase.domain.request.TestcaseReq;
import edu.jiahui.testcase.domain.response.TestcaseRes;
import edu.jiahui.testcase.mapper.*;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.io.*;
import java.util.*;

@Service
public class TestcaseService {

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

    public void addTestcase(Integer id,TestcaseReq req){
        Testcase testcase = new Testcase();
        testcase.setTestcaseName(req.getTestcaseName());
        testcase.setEnvId(req.getEnvId());
        testcase.setConfigIds(req.getConfigIds());
        testcase.setInterfaceId(id);
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
        if(req.getResponses()!=null && req.getResponses().size()>0){
            for(TestcaseReq.Response response:req.getResponses()){
                TestcaseDetail testcaseDetail = new TestcaseDetail();
                testcaseDetail.setName(response.getName());
                testcaseDetail.setType(response.getType());
                testcaseDetail.setComparator(response.getComparator());
                testcaseDetail.setExpectedValue(response.getExpectedValue());
                testcaseDetail.setScope("response");
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
    }

    public TestcaseRes testcaseInfo(Integer testcaseId){

        TestcaseRes testcaseRes = new TestcaseRes();

        Testcase testcase=testcaseMapper.selectByPrimaryKey(testcaseId);
        testcaseRes.setEnvId(testcase.getEnvId());
        testcaseRes.setConfigIds(testcase.getConfigIds());
        testcaseRes.setTestcaseName(testcase.getTestcaseName());
        List<TestcaseDetail> testcaseDetailList = testcaseDetailMapper.selectByTestcaseId(testcaseId);
        if(testcaseDetailList.size()>0){
            List<TestcaseRes.Variable> variableList = new ArrayList<>();
            List<TestcaseRes.Parameter> parameterList = new ArrayList<>();
            List<TestcaseRes.Setuphook> setuphookList = new ArrayList<>();
            List<TestcaseRes.ReqHeader> reqHeaderList = new ArrayList<>();
            List<TestcaseRes.ReqParam> reqParamList = new ArrayList<>();
            List<TestcaseRes.Response> responseList = new ArrayList<>();
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
                        TestcaseRes.Response  response = new TestcaseRes.Response();
                        response.setId(testcaseDetail.getId());
                        response.setName(testcaseDetail.getName());
                        response.setType(testcaseDetail.getType());
                        response.setComparator(testcaseDetail.getComparator());
                        response.setExpectedValue(testcaseDetail.getExpectedValue());
                        responseList.add(response);
                        break;
                    case "requestBody":
                        Map reqBodyMap = new HashMap();
                        reqBodyMap.put("id",testcaseDetail.getId());
                        reqBodyMap.put("requestBody",testcaseDetail.getValue());
                        testcaseRes.setReqBody(reqBodyMap);
                }
            }
            testcaseRes.setVariables(variableList);
            testcaseRes.setParameters(parameterList);
            testcaseRes.setSetuphooks(setuphookList);
            testcaseRes.setReqHeaders(reqHeaderList);
            testcaseRes.setReqParams(reqParamList);
            testcaseRes.setResponses(responseList);
        }
        return testcaseRes;

    }

    public void updateTestcase(Integer id,TestcaseReq req){
        Testcase testcase= new Testcase();
        testcase.setTestcaseName(req.getTestcaseName());
        testcase.setEnvId(req.getEnvId());
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
            testcaseDetailMapper.deleteNotIn(ids,id,"variables");
        }else {
            testcaseDetailMapper.deleteNotIn(null,id,"variables");
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
            testcaseDetailMapper.deleteNotIn(ids,id,"parameters");
        }else {
            testcaseDetailMapper.deleteNotIn(null,id,"parameters");
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
            testcaseDetailMapper.deleteNotIn(ids,id,"setupHooks");
        }else {
            testcaseDetailMapper.deleteNotIn(null,id,"setupHooks");
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
            testcaseDetailMapper.deleteNotIn(ids,id,"reqHeaders");
        }else {
            testcaseDetailMapper.deleteNotIn(null,id,"reqHeaders");
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
            testcaseDetailMapper.deleteNotIn(ids,id,"reqParams");
        }else {
            testcaseDetailMapper.deleteNotIn(null,id,"reqParams");
        }

        if(req.getResponses()!=null && req.getResponses().size()>0){
            List<Integer> ids = new ArrayList<>();
            for(TestcaseReq.Response response:req.getResponses()){
                TestcaseDetail testcaseDetail= new TestcaseDetail();
                testcaseDetail.setScope("response");
                testcaseDetail.setName(response.getName());
                testcaseDetail.setType(response.getType());
                testcaseDetail.setExpectedValue(response.getExpectedValue());
                testcaseDetail.setComparator(response.getComparator());
                if(response.getId()!=null){
                    testcaseDetail.setId(response.getId());
                    testcaseDetailMapper.updateByPrimaryKey(testcaseDetail);
                    ids.add(response.getId());
                }else {
                    testcaseDetail.setTestcaseId(id);
                    testcaseDetailMapper.insertOne(testcaseDetail);
                    ids.add(testcaseDetail.getId());
                }
            }
            testcaseDetailMapper.deleteNotIn(ids,id,"response");
        }else {
            testcaseDetailMapper.deleteNotIn(null,id,"response");
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
                lista=runTestcase(req.getEnvId(),req.getProjectId(),testcaseId);
            }
        }
        return lista;
    }


    public List runTestcase(Integer envId,Integer projectId,Integer testcaseId){
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
        JSONObject configParametersJson= new JSONObject();
        List validateList = new ArrayList();
        if(testcaseDetailList.size()<=0){
            throw (new ClientException(BaseConstans.BUSI_CODE.CASEDETAIL_NOT_EXIT.getCode(),BaseConstans.BUSI_CODE.CASEDETAIL_NOT_EXIT.getMsg()));
        }else {
            for(TestcaseDetail testcaseDetail: testcaseDetailList){
                if(testcaseDetail.getScope().equals("setupHooks")){
                    //执行sql
                }

                if(testcaseDetail.getScope().equals("parameters")){
                    //设置多个用例

//                    configParametersJson.put(testcaseDetail.getName(),JSON.parseArray(testcaseDetail.getValue(),String.class));
                    configParametersJson.put(testcaseDetail.getName(),testcaseDetail.getValue());
                }

                if(testcaseDetail.getScope().equals("variables")){
                    if(testcaseDetail.getType()=="sql"){
//                        执行后赋值
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

                    List actualExceptedList =new ArrayList();
                    List <String> resultList = nameJoint(testcaseDetail.getId());
                    if(!resultList.get(0).equals("Object") && !resultList.get(0).equals("Array")){
                        actualExceptedList.add(resultList.get(1));
                        actualExceptedList.add(testcaseDetail.getExpectedValue());
                        item.put(type,actualExceptedList);
                        validateList.add(item);
                    }
                }
                if(testcaseDetail.getScope().equals("requestBody")){
                    requestBodyJson = JSON.parseObject(testcaseDetail.getValue());

                }
            }
        }

        JSONObject configJson = new JSONObject();
        configJson.put("name",testcase.getTestcaseName());
        configJson.put("id",testcase.getTestcaseName());
        configJson.put("variables",totalVariableJson);
        configJson.put("base_url",baseUrl);
        configJson.put("parameters",configParametersJson);

        JSONObject requestJson = new JSONObject();
        requestJson.put("headers",reqHeadersJson);
        requestJson.put("json",requestBodyJson);
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
//        createTestcaseYaml(project.getProjectName(),testcase.getTestcaseName(),testcaseList);
        String reportContent = execTestcase(project.getProjectName(),testcase.getTestcaseName());
        Report report = Report.builder().testcaseId(testcaseId).content(reportContent).build();
        reportMapper.insert(report);
        return testcaseList;


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
            String[] args = new String[]{"python3", "/Users/pengyishuang/Desktop/mypro01/create_yaml.py", projectName, testcaseName, JSON.toJSONString(caseList)};
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
            String[] args = new String[]{"python3", "/Users/pengyishuang/Desktop/mypro01/run_test.py", projectName, testcaseName};
            Process proc = Runtime.getRuntime().exec(args);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            reportContent = String.valueOf(in.readLine());
            in.close();
            proc.waitFor();
        }catch (IOException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return reportContent;
    }





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
