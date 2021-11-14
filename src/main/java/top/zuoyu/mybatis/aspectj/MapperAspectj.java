package top.zuoyu.mybatis.aspectj;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 接口实现类注入 .
 *
 * @author: zuoyu
 * @create: 2021-11-14 14:01
 */
@Aspect
@Component
public class MapperAspectj {

    @Pointcut("@annotation(top.zuoyu.mybatis.annotation.Magic)")
    public void magic() {
    }


    @Before("magic()")
    public void doBefore(JoinPoint point) throws Throwable {
        System.out.println(point);
    }

}
