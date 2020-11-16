package edu.jiahui.testcase.service;


import edu.jiahui.framework.exceptions.ClientException;
import edu.jiahui.testcase.constants.BaseConstans;
import edu.jiahui.testcase.domain.TestcaseGroup;
import edu.jiahui.testcase.domain.TestcaseGroupDetail;
import edu.jiahui.testcase.domain.request.CreateTestcaseGroupReq;
import edu.jiahui.testcase.domain.request.SearchGroupReq;
import edu.jiahui.testcase.domain.response.GroupRes;
import edu.jiahui.testcase.domain.response.SearchGroupRes;
import edu.jiahui.testcase.mapper.TestcaseGroupDetailMapper;
import edu.jiahui.testcase.mapper.TestcaseGroupMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class GroupService {

    @Resource
    private TestcaseGroupMapper testcaseGroupMapper;

    @Resource
    private TestcaseGroupDetailMapper testcaseGroupDetailMapper;

    public List<SearchGroupRes.Group> searchGroupList(SearchGroupReq req){

        return testcaseGroupMapper.select(req);
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
                        .scope("setuphooks")
                        .build();
                testcaseGroupDetails.add(td);
            }
        }
        testcaseGroupDetailMapper.insert(testcaseGroupDetails);
    }

    public void editTestcaseGroup(CreateTestcaseGroupReq req){
        if(testcaseGroupMapper.exitName(req.getGroupName(),req.getId())>0){
            throw (new ClientException(BaseConstans.BUSI_CODE.GROUP_NAME_EXIT.getCode(),BaseConstans.BUSI_CODE.GROUP_NAME_EXIT.getMsg()));
        }
        TestcaseGroup testcaseGroup=TestcaseGroup.builder()
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
                        .scope("setuphooks")
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
                if(tgd.getScope()=="variables"){
                    GroupRes.Variable variable = GroupRes.Variable.builder()
                            .id(tgd.getId())
                            .name(tgd.getName())
                            .type(tgd.getType())
                            .value(tgd.getValue())
                            .databaseId(tgd.getDatabaseId())
                            .build();
                    grv.add(variable);
                }
                if(tgd.getScope()=="parameters"){
                    GroupRes.Parameter parameter = GroupRes.Parameter.builder()
                            .id(tgd.getId())
                            .name(tgd.getName())
                            .value(tgd.getValue())
                            .build();
                    grp.add(parameter);
                }
                if(tgd.getScope()=="setuphooks"){
                    GroupRes.Setuphook setuphook = GroupRes.Setuphook.builder()
                            .id(tgd.getId())
                            .sql(tgd.getValue())
                            .databaseId(tgd.getDatabaseId())
                            .build();
                    grs.add(setuphook);
                }
            }
        }

        GroupRes res = GroupRes.builder()
                .id(id)
                .envId(testcaseGroup.getEnvId())
                .projectId(testcaseGroup.getProjectId())
                .configIds(testcaseGroup.getConfigIds())
                .testcaseIds(testcaseGroup.getTestcaseIds())
                .groupName(testcaseGroup.getGroupName())
                .variables(grv)
                .parameters(grp)
                .setuphooks(grs)
                .build();

        return res;
    }

    public void deleteGroup(Integer id){
    }
}
