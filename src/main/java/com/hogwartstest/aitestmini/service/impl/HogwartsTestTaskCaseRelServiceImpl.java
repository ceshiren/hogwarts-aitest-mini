package com.hogwartstest.aitestmini.service.impl;

import com.hogwartstest.aitestmini.dao.HogwartsTestTaskCaseRelMapper;
import com.hogwartstest.aitestmini.dto.PageTableRequest;
import com.hogwartstest.aitestmini.dto.PageTableResponse;
import com.hogwartstest.aitestmini.dto.ResultDto;
import com.hogwartstest.aitestmini.dto.testcase.HogwartsTestTaskCaseRelDetailDto;
import com.hogwartstest.aitestmini.dto.testcase.QueryHogwartsTestTaskCaseRelListDto;
import com.hogwartstest.aitestmini.service.HogwartsTestTaskCaseRelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class HogwartsTestTaskCaseRelServiceImpl implements HogwartsTestTaskCaseRelService {


    @Autowired
    private HogwartsTestTaskCaseRelMapper hogwartsTestTaskCaseRelMapper;

    /**
     * 查询任务关联的详细信息列表
     *
     * @param pageTableRequest
     * @return
     */
    @Override
    public ResultDto<PageTableResponse<HogwartsTestTaskCaseRelDetailDto>> listDetail(PageTableRequest<QueryHogwartsTestTaskCaseRelListDto> pageTableRequest) {

        QueryHogwartsTestTaskCaseRelListDto params = pageTableRequest.getParams();

        List<HogwartsTestTaskCaseRelDetailDto> hogwartsTestTaskCaseRelDetailDtoList = hogwartsTestTaskCaseRelMapper.listDetail(params,null,null);

        PageTableResponse<HogwartsTestTaskCaseRelDetailDto> hogwartsTestJenkinsPageTableResponse = new PageTableResponse<>();
        hogwartsTestJenkinsPageTableResponse.setData(hogwartsTestTaskCaseRelDetailDtoList);

        return ResultDto.success("成功", hogwartsTestJenkinsPageTableResponse);

    }
}
