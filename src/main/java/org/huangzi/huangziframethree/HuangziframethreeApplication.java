package org.huangzi.huangziframethree;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: guohao
 */
@SpringBootApplication
@MapperScan("org.huangzi.huangziframethree.mapper")
public class HuangziframethreeApplication {

    public static void main(String[] args) {
        SpringApplication.run(HuangziframethreeApplication.class, args);
    }

}

