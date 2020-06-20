package com.hogwartstest.aitestmini.dao;

import com.hogwartstest.aitestmini.common.MySqlExtensionMapper;
import com.hogwartstest.aitestmini.dto.testcase.QueryHogwartsTestCaseListDto;
import com.hogwartstest.aitestmini.entity.HogwartsTestCase;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HogwartsTestCaseMapper extends MySqlExtensionMapper<HogwartsTestCase> {

    /**
     * 根据用户id修改delFlag为0
     * @param createUserId
     * @return
     */
    int updateByCreateUserId(@Param("createUserId") Integer createUserId);

    List<HogwartsTestCase> getByIdList(@Param("createUserId") Integer createUserId, @Param("list") List<Integer> list);

    /**
     * 统计总数
     * @param params
     * @return
     */
    Integer count(@Param("params") QueryHogwartsTestCaseListDto params);

    /**
     * 列表分页查询
     * @param params
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<HogwartsTestCase> list(@Param("params") QueryHogwartsTestCaseListDto params, @Param("pageNum") Integer pageNum,
                                   @Param("pageSize") Integer pageSize);
}