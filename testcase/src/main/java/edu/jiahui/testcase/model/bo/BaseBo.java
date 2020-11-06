package edu.jiahui.testcase.model.bo;


import lombok.Data;


@Data
public class BaseBo {

    /**
     * 每页显示条数，默认 10
     */
    private Integer size;

    /**
     * 当前页
     */
    private Integer current;

    /**
     * 总数
     */
    private Integer total;

    public Integer currentifPage(){
        return current == null ? 0 : current;
    }

    public Integer sizeif(){
        return size == null ? 10 : size;
    }


}
