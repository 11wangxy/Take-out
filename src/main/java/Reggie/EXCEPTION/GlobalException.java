package Reggie.EXCEPTION;

import Reggie.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(Exception.class)//捕获所有异常
    public Result<String> ex(Exception exception){
        exception.printStackTrace();
        return Result.error("操作失败");
    }
    @ExceptionHandler(CustomException.class)
    public Result<String> ex(CustomException exception){
        return Result.error(exception.getMessage());
    }
}
