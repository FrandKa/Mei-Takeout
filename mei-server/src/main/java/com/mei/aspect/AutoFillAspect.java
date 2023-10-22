package com.mei.aspect;

import com.mei.annotation.AutoFill;
import com.mei.constant.AutoFillConstant;
import com.mei.context.BaseContext;
import com.mei.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @program: sky-take-out
 * @description: 自定义切面类
 * @author: Mr.Ka
 * @create: 2023-10-22 16:16
 **/
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    @Pointcut("execution(* com.mei.mapper.*.*(..)) && @annotation(com.mei.annotation.AutoFill)")
    public void autoFilePointCut() {}

    @Before(value = "autoFilePointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始填充 joinPoint: {}", joinPoint.toString());
        // 1. 获取当前方法的操作类型;
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        OperationType type = signature.getMethod().getAnnotation(AutoFill.class).value();
        // 2. 获取方法的参数:
        Object[] args = joinPoint.getArgs();
        if(Objects.isNull(args) || args.length == 0) {
            return;
        }
        Object entity = args[0];

        // 3. 准备公共值:
        Long id = BaseContext.getCurrentId();
        LocalDateTime now = LocalDateTime.now();

        // 4. 为实体对象中的公共属性赋值
        if(OperationType.INSERT.equals(type)) {
            try {
                Method setCreateTime = entity.getClass().getMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                setCreateTime.invoke(entity, now);
                setCreateUser.invoke(entity, id);
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, id);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } else if (OperationType.UPDATE.equals(type)){
            try {
                Method setUpdateTime = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, id);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
