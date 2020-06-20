package com.hogwartstest.aitestmini.util;

import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @Author tlibn
 * @Date 2020/6/15 16:52
 **/
public class StrUtil {

    /**
     *  将存储id的list转为字符串
     *
     *  转换前=[2, 12, 22, 32]
     *  转换后= 2, 12, 22, 32
     * @param caseIdList
     * @return
     */
    public static String list2IdsStr(List<Integer> caseIdList){

        if(Objects.isNull(caseIdList)){
            return null;
        }

        return caseIdList.toString()
                .replace("[","")
                .replace("]","");

    }

    public static String getHostAndPort(String requestUrl) {

        if(StringUtils.isEmpty(requestUrl)){
            return "";
        }

        String http = "";
        String tempUrl = "";
        if(requestUrl.contains("://")){
            http = requestUrl.substring(0,requestUrl.indexOf("://")+3);
            tempUrl = requestUrl.substring(requestUrl.indexOf("://")+3);
        }
        if(tempUrl.contains("/")){
            tempUrl = tempUrl.substring(0,tempUrl.indexOf("/"));
        }
        return http+tempUrl;
    }

}
