package top.zuoyu.mybatis.ssist;

import java.util.Arrays;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;

/**
 * Mapper文件初始化 .
 *
 * @author: zuoyu
 * @create: 2021-12-15 15:04
 */
public final class MapperStructureInit {

    @NonNull
    public static Stream<Resource> register(@NonNull String[] tableNames) {
        return Arrays.stream(tableNames).map(MapperStructure::registerMapper);
    }
}
