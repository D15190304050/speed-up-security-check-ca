package united.cn.suscc.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import united.cn.suscc.commons.ServiceResponse;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ServiceResponse<?> handleValidationException(Exception e)
    {
        log.error("Error in validation", e);
        return ServiceResponse.buildErrorResponse(-100, e.getMessage());
    }
}
