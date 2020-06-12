package com.hogwartstest.aitestmini;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan("com.hogwartstest.aitestmini.dao")
@SpringBootApplication
public class AitestMiniApplication {

    public static void main(String[] args) {
        SpringApplication.run(AitestMiniApplication.class, args);
    }

}
