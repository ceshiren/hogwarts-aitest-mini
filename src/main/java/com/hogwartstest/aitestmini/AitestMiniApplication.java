package com.hogwartstest.aitestmini;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan("com.hogwartstest.aitestmini.dao")
@SpringBootApplication
//@EnableSwagger2
public class AitestMiniApplication {

    public static void main(String[] args) {
        SpringApplication.run(AitestMiniApplication.class, args);
    }

}
