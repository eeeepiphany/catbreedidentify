package com.aida.cat.model;

import lombok.Data;

@Data
public class User {
    private String loginId;
    private String loginPwd;
    private String nickname;
    private String tel;
    private String depId;
}
