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
        DBNAME_NOT_EXIT("DB001","数据库名称不存在"),
        SQL_ERROR("DB002","sql写错"),
        RUN_SQL_ERROR("DB003","执行sql是错误"),
        CONFIG_NAME_EXIT("CONFIG001","配置项名称已经存在"),
        REPORT_NOT_EXIT("REPORT001","报告不存在"),
        GROUP_NAME_EXIT("GROUP001","用例组名已存在！"),
        NOT_RUN_GROUP("GROUP002","没有要运行的用例组!"),
        DB_EXIT("DB001","数据库信息已经存在"),
        USER_NOT_EXIT("USER001","用户名或密码错误！"),
        USER_EXIT("USER002","用户已经存在！"),
        USER_INFO_NOT_EXIT("USER003","用户信息不存在！"),
        USER_TOKEN_INVALID("TOKEN001","token过期"),
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

