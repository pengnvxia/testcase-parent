package edu.jiahui.testcase.constants;

public class BaseConstans {


    /**
     * 业务编码
     */
    public enum BUSI_CODE {
        PROJECT_NOT_EXIT("PRO001","项目不存在"),
        PROJECT_NAME_EXIT("PRO002","项目名称存在");


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

