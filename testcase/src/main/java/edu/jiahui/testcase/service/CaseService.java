package edu.jiahui.testcase.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import edu.jiahui.framework.exceptions.ClientException;
import edu.jiahui.testcase.constants.BaseConstans;
import edu.jiahui.testcase.constants.PythonConstants;
import edu.jiahui.testcase.domain.*;
import edu.jiahui.testcase.mapper.*;
import edu.jiahui.testcase.utils.JDBCUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Service
public class CaseService {

    @Resource
    private PythonConstants pythonConstants;

    @Resource
    private ProjectMapper projectMapper;

    @Resource
    private TestcaseMapper testcaseMapper;

    @Resource
    private TestcaseDetailMapper testcaseDetailMapper;

    @Resource
    private TestcaseConfigMapper testcaseConfigMapper;

    @Resource
    private TestcaseConfigDetailMapper testcaseConfigDetailMapper;

    @Resource
    private DatabaseMapper databaseMapper;

    public List createTestcaseJson(Integer envId,Integer projectId,Integer testcaseId){
        //初始化数据库
//        String testcaseDb= testcaseDbMapper.selectByTestcaseId(testcaseId);
//        List dbIds = JSON.parseArray(testcaseDb,String.class);
//        initDatabase(dbIds); 先注掉，数据库方案还未确定
        //拼接用例数据
        Project project = projectMapper.selectByPrimaryKey(projectId);
        String baseUrl = envId==1 ? project.getDevAddress():project.getProdAddress();
        Testcase testcase = testcaseMapper.selectByPrimaryKey(testcaseId);
//        JSONObject totalVariableJson = new JSONObject();
        JSONObject variablesJson= new JSONObject();

        if(testcase.getConfigIds()!=null && !testcase.getConfigIds().equals("")){
            List<Integer> configIds = JSON.parseArray(testcase.getConfigIds(),Integer.class);
            if(configIds.size()>0){
                for(Integer configId: configIds){
                    List<TestcaseConfigDetail> testcaseConfigDetailList = testcaseConfigDetailMapper.selectByConfigId(configId);
                    for(TestcaseConfigDetail testcaseConfigDetail: testcaseConfigDetailList){
                        variablesJson.put(testcaseConfigDetail.getName(),testcaseConfigDetail.getValue());
                    }
                }
            }
        }
//        if(testcase.getConfigIds()!=null && testcase.getConfigIds().length()>0){
//            List<String> configIdsList = Arrays.asList(testcase.getConfigIds());
//            for(String configId: configIdsList){
//                List<TestcaseConfigDetail> testcaseConfigDetailList = testcaseConfigDetailMapper.selectByConfigId(Integer.parseInt(configId));
//                for(TestcaseConfigDetail testcaseConfigDetail: testcaseConfigDetailList){
//                    totalVariableJson.put(testcaseConfigDetail.getName(),testcaseConfigDetail.getValue());
//                }
//            }
//        }

        List<TestcaseDetail> testcaseDetailList = testcaseDetailMapper.selectByTestcaseId(testcaseId);
        JSONObject reqHeadersJson= new JSONObject();
        JSONObject reqParamsJson= new JSONObject();
        JSONObject requestBodyJson= new JSONObject();
        JSONArray requestBodyJsonArray= new JSONArray();
        JSONObject configParametersJson= new JSONObject();
        List extractList= new ArrayList();
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
                        throw (new ClientException(BaseConstans.BUSI_CODE.SQL_ERROR.getCode(),BaseConstans.BUSI_CODE.SQL_ERROR.getMsg()));
                    }catch (Exception e){
                        throw (new ClientException(BaseConstans.BUSI_CODE.RUN_SQL_ERROR.getCode(),BaseConstans.BUSI_CODE.RUN_SQL_ERROR.getMsg()));

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

                if(testcaseDetail.getScope().equals("extract")){
                    JSONObject item = new JSONObject();
                    item.put(testcaseDetail.getName(),testcaseDetail.getValue());
                    extractList.add(item);
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
                            case "notcontain":
                                type="notcontains";
                                break;
                        }
                    }

                    List actualExceptedList =new ArrayList();
                    List <String> resultList = nameJoint(testcaseDetail.getId());
                    if(!resultList.get(0).equals("Object") && !resultList.get(0).equals("Array")){
                        actualExceptedList.add(resultList.get(1));
                        switch (resultList.get(0)){
                            //处理整数和小数
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
//        configJson.put("variables",totalVariableJson);
//        JSONObject baseUrlJson = new JSONObject();
//        baseUrlJson.put("base_url",baseUrl);
//        configJson.put("request",baseUrlJson);
        configJson.put("parameters",configParametersJson);

        JSONObject requestJson = new JSONObject();
        requestJson.put("headers",reqHeadersJson);
        requestJson.put("json",requestBodyJson.isEmpty()?requestBodyJsonArray:requestBodyJson);
        requestJson.put("method",testcase.getMethod());
        requestJson.put("url",baseUrl+testcase.getUrl());
        requestJson.put("params",reqParamsJson);



        JSONObject testJson = new JSONObject();
        testJson.put("name", testcase.getTestcaseName());
        testJson.put("request",requestJson);
        testJson.put("validate",validateList);
        testJson.put("variables",variablesJson);
        testJson.put("extract",extractList);


        JSONObject testcaseConfigJson= new JSONObject();
        testcaseConfigJson.put("config",configJson);

        JSONObject testcaseTestJson= new JSONObject();
        testcaseTestJson.put("test",testJson);

        List testcaseList= new ArrayList();
        testcaseList.add(testcaseConfigJson);
        testcaseList.add(testcaseTestJson);
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

}
