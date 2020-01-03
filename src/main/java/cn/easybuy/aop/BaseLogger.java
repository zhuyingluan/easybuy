package cn.easybuy.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 记录log信息
 */
@Aspect
public class BaseLogger {
    private static final Logger logger = Logger.getLogger(BaseLogger.class);

    /*@Pointcut("execution(* cn.easybuy.service..*.*(..))")
    public void pointcut(){}

    @AfterReturning(pointcut = "pointcut()", returning = "result")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, Object result) throws Throwable{
        Object[] objects = proceedingJoinPoint.getArgs();
        try{
            Object object = proceedingJoinPoint.proceed(objects);
            return object;
        } catch (Throwable e) {
            throw e;
        } finally {

        }
    }*/
}
