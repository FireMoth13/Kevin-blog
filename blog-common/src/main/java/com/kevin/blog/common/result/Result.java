package com.kevin.blog.common.result;

import lombok.Data;

@Data
public class Result<T> {
    private int code;
    private String message;
    private T data;

    private Result(){}

    //成功-带数据
    public static<T> Result<T> success(T data){
        Result<T> r=new Result<>();
        r.code=ResultCode.SUCCESS.getCode();
        r.message=ResultCode.SUCCESS.getMessage();
        r.data=data;
        return r;
    }

    //成功-不带数据(如删除)
    public static<T> Result<T> success(){
        return success(null);
    }
    
    //失败-用枚举
    public static<T> Result<T> fail(ResultCode resultCode){
        Result<T> r=new Result<>();
        r.code=resultCode.getCode();
        r.message=resultCode.getMessage();
        return r;
    }

    //失败-自定义提示文字
    public static<T> Result<T> fail(ResultCode code,String message){
        Result<T> r=new Result<>();
        r.code=code.getCode();
        r.message=message;
        return r;
    }

    public static <T> Result<T> fail(int code, String message) {
    Result<T> r = new Result<>();
    r.code = code;
    r.message = message;
    return r;
}
}
