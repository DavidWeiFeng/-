package com.sky.aspect;

import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import com.sky.annotation.AutoFill;

import java.lang.annotation.Annotation;
import java.time.LocalDateTime;

@Aspect
@Component

public class AutoFillAspect {
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill) ")
    public void AutoFillPoincut() {
    }

    @Before("AutoFillPoincut()")
    public void AutoFill(JoinPoint joinPoint) {
        System.out.println("AutoFillAspect");

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();
        Object[] args = joinPoint.getArgs();
        if (args.length == 0 || args == null) {
            return;
        }
        Object entity = args[0];
        LocalDateTime now = LocalDateTime.now();
        Long currentId= BaseContext.getCurrentId();

        if (operationType == OperationType.INSERT) {
            try {
                entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class).invoke(entity, now);
                entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class).invoke(entity, currentId);
                entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, LocalDateTime.class).invoke(entity, now);
                entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class).invoke(entity, currentId);

            }
            catch (Exception e) {
                e.printStackTrace();
            }
            //插入操作
        }
        else if (operationType == OperationType.UPDATE) {
            try {
                entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class).invoke(entity, now);
                entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class).invoke(entity, currentId);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
