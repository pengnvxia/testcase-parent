package edu.jiahui.testcase.mapper;

import edu.jiahui.testcase.domain.Users;

public interface UsersMapper {
    Users selectByPrimaryKey(Integer id);
    void insert(Users users);
    Integer selectByNamePassword(Users users);
    void updateToken(Users users);
}
