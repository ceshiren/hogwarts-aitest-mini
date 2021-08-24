package com.hogwartstest.aitestmini.dao;

import com.hogwartstest.aitestmini.common.MySqlExtensionMapper;
import com.hogwartstest.aitestmini.entity.HogwartsTestHis;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface HogwartsTestHisMapper extends MySqlExtensionMapper<HogwartsTestHis> {

    /**
     * 统计总数
     * @param params
     * @return
     */
    Integer count(@Param("params") Map<String, Object> params);

    /**
     * 列表分页查询
     * @param params
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<HogwartsTestHis> list(@Param("params") Map<String, Object> params,
                                   @Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize);

}
