//package com.sky.aspect;
//
//import com.sky.annotation.AutoFill;
//import com.sky.enumeration.OperationType;
//import jdk.internal.classfile.MethodSignature;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.Signature;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Method;
//
//import static org.apache.commons.lang3.reflect.MethodUtils.getAnnotation;
//
//@Aspect
//@Component
//@Slf4j
//public class AutoFillAspect {
//
//    //切入点
//    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
//    public void autoFillPointcut(){}
//
//    /**
//     * 前置通知，为公共字段赋值
//     */
//    @Before("autoFillPointcut()")
//    public void autoFill(JoinPoint joinPoint){
//        log.info("开始公共字段自动填充");
//        //获取到当前被拦截的方法数据库操作类
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        signature.
//        Method method = signature.getMethod(); // 获取被拦截的方法
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//方法签名对象
//        AutoFill autoFill = signature.getClass().getMethod(getAnnotation(AutoFill.class));//获取方法上的注解对象
//        OperationType value = autoFill.value();
//
//        //获取到当前被拦截的方法的参数--实体对象
//        //准备赋值的数据
//        //根据当前不同的操作类型，为对应的属性通过反射来赋值
//    }
//}
