package edu.jiahui.testcase.mapper;

import edu.jiahui.testcase.domain.Users;
import edu.jiahui.testcase.domain.response.UserRes;

public interface UsersMapper {
    Users selectByPrimaryKey(Integer id);
    void insert(Users users);
    UserRes selectByNameAndPassword(Users users);
    void updateToken(Users users);
    Integer selectByName(Users users);
}
