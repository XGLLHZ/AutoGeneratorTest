package org.huangzi.huangziframethree.util;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: guohao
 * @date: "2019/1/22"
 * @description: mybatis-plus分页工具类
 */
@Configuration
@MapperScan("org.huangzi.huangziframethree.mapper")
public class MybatisPageUtil {

    /**
     * mybatis-plus分页插件
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor;
    }

}
