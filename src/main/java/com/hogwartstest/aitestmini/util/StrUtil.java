package com.hogwartstest.aitestmini.util;

import java.util.ArrayList;
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

}
