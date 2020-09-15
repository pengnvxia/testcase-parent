package edu.jiahui.testcase.constants;

public class BaseConstans {


    /**
     * 业务编码
     */
    public enum BUSI_CODE {
        PROJECT_NOT_EXIT("PRO001","项目不存在"),
        PROJECT_NAME_EXIT("PRO002","项目名称存在"),
        MODULE_NOT_EXIT("MO001","模块不存在"),
        MODULE_NAME_EXIT("MO002","模块名称存在"),
        INTERFACE_NOT_EXIT("INTER001","接口不存在"),
        CASE_EXIT("CASE001","存在用例"),
        NOT_RUN_CASE("CASE002","没有要运行的用例"),
        CASEDETAIL_NOT_EXIT("CASEDETAIL001","用例详细信息不存在"),
        ;


        private String code;
        private String msg;

        public String getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

        BUSI_CODE(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }
}

