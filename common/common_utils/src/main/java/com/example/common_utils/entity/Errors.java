package com.example.common_utils.entity;

public enum Errors {
    AuthenticationError(-201, "登录失效，请重新登录"),
    NullPointerError(-404, "未找到资源"),

    PasswordError(-200,"密码格式错误"),

    AccessDeniedError(-200,"权限不足"),

    NoneUserError(-200,"没有这个用户"),
    RegisterEmailError(-200,"邮箱格式错误"),
    RegisterExpireEmailError(-200,"邮箱输入错误或已过期"),

    RegisterPasswordError(-200,"密码格式错误"),
    RegisterCodeError(-200,"验证码错误"),

    RegisterNicknameError(-200,"昵称格式错误"),

    ExistError(-200,"邮箱已经注册"),
    FrequentOperationsError(-200,"操作太频繁"),

    InviteCodeError(-200,"邀请码错误");


    private final String msg;
    private final int code;

    Errors(int code, String msg){
        this.code=code;
        this.msg=msg;
    }

    public String getMsg() {
        return this.msg;
    }
    public int getCode() {
        return this.code;
    }

}
