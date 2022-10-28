package com.example.common_utils.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class R {

    private boolean success;

    private int code;

    private String msg;

    private Object data;


    public static R success(Object data){
        return new R(true,200,"success",data);
    }

    public static R success(Object data,String msg){
        return new R(true,200,msg,data);
    }
    public static R fail(Errors errors){
        return new R(false,errors.getCode(),errors.getMsg(),null);
    }

    public static R fail(int code,String msg){
        return new R(false,code,msg,null);
    }
}
