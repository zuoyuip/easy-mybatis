package top.zuoyu.mybatis.ssist;

import org.springframework.lang.NonNull;

/**
 * Mapper文件初始化 .
 *
 * @author: zuoyu
 * @create: 2021-12-15 15:04
 */
public final class MapperStructureInit {

    public static void register(@NonNull String[] tableNames) {
        for (String tableName : tableNames) {
            MapperStructure.registerMapper(tableName);
        }
    }
}
