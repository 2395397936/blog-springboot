package com.example.blog.handler;



import com.example.common_utils.entity.Errors;
import com.example.common_utils.entity.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class AllExceptionHandler {
//    404
    @ExceptionHandler(NullPointerException.class)
    public R doException(NullPointerException e) {
        e.printStackTrace();
        return R.fail(Errors.NullPointerError.getCode(), Errors.NullPointerError.getMsg());
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public R doException(IllegalArgumentException e){
        e.printStackTrace();
        return R.fail(Errors.NullPointerError.getCode(), Errors.NullPointerError.getMsg());
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public R doException(MethodArgumentTypeMismatchException e){
        e.printStackTrace();
        return R.fail(Errors.NullPointerError.getCode(), Errors.NullPointerError.getMsg());
    }
//    全局异常
    @ExceptionHandler(Exception.class)
    public R doException(Exception e){
        e.printStackTrace();
        return R.fail(-200,"系统出错了");
    }
}
