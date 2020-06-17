package com.hogwartstest.aitestmini.dao;

import com.hogwartstest.aitestmini.common.MySqlExtensionMapper;
import com.hogwartstest.aitestmini.dto.task.QueryHogwartsTestTaskListDto;
import com.hogwartstest.aitestmini.dto.task.TaskDataDto;
import com.hogwartstest.aitestmini.entity.HogwartsTestTask;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HogwartsTestTaskMapper extends MySqlExtensionMapper<HogwartsTestTask> {

    /**
     * 统计总数
     * @param params
     * @return
     */
    Integer count(@Param("params") QueryHogwartsTestTaskListDto params);

    /**
     * 列表分页查询
     * @param params
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<HogwartsTestTask> list(@Param("params") QueryHogwartsTestTaskListDto params, @Param("pageNum") Integer pageNum,
                                @Param("pageSize") Integer pageSize);

    List<TaskDataDto> getTaskByType(@Param("createUserId") Integer createUserId);

    List<TaskDataDto> getTaskByStatus(@Param("createUserId") Integer createUserId);

    List<HogwartsTestTask> getCaseCountByTask(@Param("createUserId") Integer createUserId, @Param("start") Integer start,
                                   @Param("end") Integer end);

}