package com.hogwartstest.aitestmini.config;

import com.hogwartstest.aitestmini.common.TokenDb;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author tlibn
 * @Date 2020/6/12 16:46
 **/

//@Configuration
public class TokenDbConfig {

    //单例bean
    //@Bean("tokenDb")
    public TokenDb cacheManager() {
        TokenDb tokenDb = new TokenDb();
        return tokenDb;
    }

}
