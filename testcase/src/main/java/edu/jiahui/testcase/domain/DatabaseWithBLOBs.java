package edu.jiahui.testcase.domain;

import lombok.Data;

@Data
public class DatabaseWithBLOBs extends Database {
    private String createTableSql;

    private String insertSql;
}
