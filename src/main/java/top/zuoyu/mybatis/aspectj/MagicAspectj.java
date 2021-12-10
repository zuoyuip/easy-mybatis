package top.zuoyu.mybatis.aspectj;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * 对Magic注解的注入 .
 *
 * @author: zuoyu
 * @create: 2021-12-10 17:35
 */
@Aspect
@Component
public class MagicAspectj {

    @Before("@annotation(org.springframework.stereotype.Component)")
    public void beforeEvent(JoinPoint joinPoint) {
        System.out.println("--------------------" + joinPoint.getThis());
    }
}
