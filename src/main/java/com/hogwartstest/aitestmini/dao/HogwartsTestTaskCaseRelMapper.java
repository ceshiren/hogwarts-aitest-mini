package com.hogwartstest.aitestmini.dao;

import com.hogwartstest.aitestmini.common.MySqlExtensionMapper;
import com.hogwartstest.aitestmini.dto.testcase.HogwartsTestTaskCaseRelDetailDto;
import com.hogwartstest.aitestmini.dto.testcase.QueryHogwartsTestTaskCaseRelListDto;
import com.hogwartstest.aitestmini.entity.HogwartsTestTaskCaseRel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HogwartsTestTaskCaseRelMapper extends MySqlExtensionMapper<HogwartsTestTaskCaseRel> {

    /**
     * 列表分页查询
     * @param params
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<HogwartsTestTaskCaseRelDetailDto> listDetail(@Param("params") QueryHogwartsTestTaskCaseRelListDto params, @Param("pageNum") Integer pageNum,
                                                      @Param("pageSize") Integer pageSize);


}