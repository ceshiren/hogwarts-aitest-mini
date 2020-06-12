package com.hogwartstest.aitestmini.common;

import com.hogwartstest.aitestmini.dto.ResultDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 全局异常捕获类，主要分业务异常和其他异常两种
 * ，其中业务异常为使用ServiceException类包装的异常，其他异常则为java原生异常
 * @Author tlibn
 * @Date 2019/8/2 18:02
 **/
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    //业务异常
    @ExceptionHandler(ServiceException.class)
    public ResultDto serviceExceptionHandler(ServiceException ex) {
        return resultFormat(ex);
    }

    //其他异常
    @ExceptionHandler({Exception.class})
    public ResultDto exception(Exception ex) {
        return resultFormat( ex);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultDto exception(Throwable throwable) {
        log.error("服务暂不可用", throwable);
        return resultFormat(throwable);

    }

    private <T extends Throwable> ResultDto resultFormat(T ex) {
        log.error("全局异常捕获 == ", ex);
        ResultDto resultDto = ResultDto.newInstance();
        resultDto.setAsFailure();
        if(ex instanceof ServiceException){
            ServiceException serviceException = (ServiceException)ex;
            resultDto.setMessage(serviceException.getMessage());
        }else{
            resultDto.setMessage("服务暂不可用-" + ex.getMessage());
        }
        return resultDto;
    }

}