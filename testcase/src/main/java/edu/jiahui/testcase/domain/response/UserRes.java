package edu.jiahui.testcase.domain.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRes {

    private Integer userId;

    private String username;

    private String token;
}
