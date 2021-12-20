package top.zuoyu.mybatis.ssist;

import java.util.HashMap;
import java.util.Map;

import org.springframework.lang.NonNull;

/**
 * Mapper文件初始化 .
 *
 * @author: zuoyu
 * @create: 2021-12-15 15:04
 */
public final class MapperStructureInit {

    @NonNull
    public static Map<String, Class<?>> register(@NonNull String[] tableNames) {
        Map<String, Class<?>> mappers = new HashMap<>(tableNames.length);
        for (String tableName : tableNames) {
            Map<String, Class<?>> mapper = MapperStructure.registerMapper(tableName);
            mappers.putAll(mapper);
        }
        return mappers;
    }
}
