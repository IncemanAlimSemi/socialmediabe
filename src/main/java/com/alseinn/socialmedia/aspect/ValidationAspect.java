package com.alseinn.socialmedia.aspect;

import com.alseinn.socialmedia.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Component
@Aspect
@RestControllerAdvice(annotations = RestController.class)
@RequiredArgsConstructor
public class ValidationAspect {

    public final ResponseUtils responseUtils;

    @Around("execution(* *(@org.springframework.web.bind.annotation.RequestBody (*), org.springframework.validation.BindingResult, ..))" +
            " || execution(* *(@org.springframework.web.bind.annotation.ModelAttribute (*), org.springframework.validation.BindingResult, ..))")
    public Object validateInput(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();

        for (Object arg : args) {
            if (arg instanceof BindingResult bindingResult) {
                if (bindingResult.hasErrors()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                            responseUtils.createGeneralInformationResponse(false, bindingResult.getAllErrors().stream()
                                    .map(ObjectError::getDefaultMessage).collect(Collectors.joining(" "))
                            ));
                }
            }
        }

        return joinPoint.proceed();
    }
}