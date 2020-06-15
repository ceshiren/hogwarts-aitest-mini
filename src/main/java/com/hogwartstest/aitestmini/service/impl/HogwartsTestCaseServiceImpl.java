package com.hogwartstest.aitestmini.service.impl;

import com.hogwartstest.aitestmini.constants.Constants;
import com.hogwartstest.aitestmini.dao.HogwartsTestCaseMapper;
import com.hogwartstest.aitestmini.dto.PageTableRequest;
import com.hogwartstest.aitestmini.dto.PageTableResponse;
import com.hogwartstest.aitestmini.dto.ResultDto;
import com.hogwartstest.aitestmini.dto.testcase.AddHogwartsTestCaseDto;
import com.hogwartstest.aitestmini.dto.testcase.QueryHogwartsTestCaseListDto;
import com.hogwartstest.aitestmini.dto.testcase.SaveTestCaseListDto;
import com.hogwartstest.aitestmini.entity.HogwartsTestCase;
import com.hogwartstest.aitestmini.service.HogwartsTestCaseService;
import com.hogwartstest.aitestmini.util.CopyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class HogwartsTestCaseServiceImpl implements HogwartsTestCaseService {

    @Autowired
    private HogwartsTestCaseMapper hogwartsTestCaseMapper;

    /**
     * saveTestCaseListDto
     *
     * @param saveTestCaseListDto
     * @return
     */
    @Override
    public ResultDto<HogwartsTestCase> saveList(SaveTestCaseListDto saveTestCaseListDto) {

        Integer createUserId = saveTestCaseListDto.getCreateUserId();
        List<AddHogwartsTestCaseDto> testCaseList = saveTestCaseListDto.getTestCaseList();

        HogwartsTestCase queryHogwartsTestCase = new HogwartsTestCase();

        queryHogwartsTestCase.setCreateUserId(createUserId);
        queryHogwartsTestCase.setDelFlag(Constants.DEL_FLAG_ONE);

        //思考？为什么不直接删除
        hogwartsTestCaseMapper.updateByCreateUserId(createUserId);

        List<HogwartsTestCase> hogwartsTestCaseList = new ArrayList<>();

        for (AddHogwartsTestCaseDto addHogwartsTestCaseDto : testCaseList) {

            HogwartsTestCase hogwartsTestCase = new HogwartsTestCase();
            CopyUtil.copyPropertiesCglib(addHogwartsTestCaseDto,hogwartsTestCase);
            hogwartsTestCase.setCreateTime(new Date());
            hogwartsTestCase.setUpdateTime(new Date());
            hogwartsTestCase.setCreateUserId(createUserId);
            hogwartsTestCase.setDelFlag(Constants.DEL_FLAG_ONE);

            hogwartsTestCaseList.add(hogwartsTestCase);

        }

        hogwartsTestCaseMapper.insertList(hogwartsTestCaseList);
        return ResultDto.success("成功");
    }

    /**
     * 删除测试用例信息
     *
     * @param caseId
     * @return createUserId
     */
    @Override
    public ResultDto<HogwartsTestCase> delete(Integer caseId,Integer createUserId) {

        HogwartsTestCase queryHogwartsTestCase = new HogwartsTestCase();

        queryHogwartsTestCase.setId(caseId);
        queryHogwartsTestCase.setCreateUserId(createUserId);
        queryHogwartsTestCase.setDelFlag(Constants.DEL_FLAG_ONE);

        HogwartsTestCase result = hogwartsTestCaseMapper.selectOne(queryHogwartsTestCase);

        //如果为空，则提示
        if(Objects.isNull(result)){
            return ResultDto.fail("未查到测试用例信息");
        }
        result.setDelFlag(Constants.DEL_FLAG_ZERO);
        hogwartsTestCaseMapper.updateByPrimaryKey(result);

        return ResultDto.success("成功");
    }

    /**
     * 修改测试用例信息
     *
     * @param hogwartsTestCase
     * @return
     */
    @Override
    public ResultDto<HogwartsTestCase> update(HogwartsTestCase hogwartsTestCase) {

        HogwartsTestCase queryHogwartsTestCase = new HogwartsTestCase();

        queryHogwartsTestCase.setId(hogwartsTestCase.getId());
        queryHogwartsTestCase.setCreateUserId(hogwartsTestCase.getCreateUserId());
        queryHogwartsTestCase.setDelFlag(Constants.DEL_FLAG_ONE);

        HogwartsTestCase result = hogwartsTestCaseMapper.selectOne(queryHogwartsTestCase);

        //如果为空，则提示
        if(Objects.isNull(result)){
            return ResultDto.fail("未查到测试用例信息");
        }

        hogwartsTestCase.setCreateTime(result.getCreateTime());
        hogwartsTestCase.setUpdateTime(new Date());
        hogwartsTestCase.setDelFlag(Constants.DEL_FLAG_ONE);

        hogwartsTestCaseMapper.updateByPrimaryKey(hogwartsTestCase);

        return ResultDto.success("成功");
    }

    /**
     * 根据id查询测试用例信息
     *
     * @param jenkinsId
     * @return createUserId
     */
    @Override
    public ResultDto<HogwartsTestCase> getById(Integer jenkinsId,Integer createUserId) {

        HogwartsTestCase queryHogwartsTestCase = new HogwartsTestCase();

        queryHogwartsTestCase.setId(jenkinsId);
        queryHogwartsTestCase.setCreateUserId(createUserId);
        queryHogwartsTestCase.setDelFlag(Constants.DEL_FLAG_ONE);

        HogwartsTestCase result = hogwartsTestCaseMapper.selectOne(queryHogwartsTestCase);

        //如果为空，则提示，也可以直接返回成功
        if(Objects.isNull(result)){
            return ResultDto.fail("未查到测试用例信息");
        }

        return ResultDto.success("成功",result);
    }

    /**
     * 查询Jenkins信息列表
     *
     * @param pageTableRequest
     * @return
     */
    @Override
    public ResultDto<PageTableResponse<HogwartsTestCase>> list(PageTableRequest<QueryHogwartsTestCaseListDto> pageTableRequest) {

        QueryHogwartsTestCaseListDto params = pageTableRequest.getParams();
        Integer pageNum = pageTableRequest.getPageNum();
        Integer pageSize = pageTableRequest.getPageSize();

        //总数
        Integer recordsTotal =  hogwartsTestCaseMapper.count(params);

        //分页查询数据
        List<HogwartsTestCase> hogwartsTestJenkinsList = hogwartsTestCaseMapper.list(params, (pageNum - 1) * pageSize, pageSize);

        PageTableResponse<HogwartsTestCase> hogwartsTestJenkinsPageTableResponse = new PageTableResponse<>();
        hogwartsTestJenkinsPageTableResponse.setRecordsTotal(recordsTotal);
        hogwartsTestJenkinsPageTableResponse.setData(hogwartsTestJenkinsList);

        return ResultDto.success("成功", hogwartsTestJenkinsPageTableResponse);
    }

}
