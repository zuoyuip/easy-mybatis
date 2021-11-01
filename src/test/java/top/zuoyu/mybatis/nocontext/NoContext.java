package top.zuoyu.mybatis.nocontext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import top.zuoyu.mybatis.data.model.Table;

/**
 * 无环境测试 .
 *
 * @author: zuoyu
 * @create: 2021-11-01 16:27
 */
public class NoContext {
    
    @Test
    public void testClassName(){
        List<Table> tables = new ArrayList<Table>();
        tables.add(new Table());
        System.out.println(tables.getClass().getTypeName());
    }
}
