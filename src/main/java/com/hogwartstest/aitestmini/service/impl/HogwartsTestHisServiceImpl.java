package com.hogwartstest.aitestmini.service.impl;

import com.hogwartstest.aitestmini.common.TokenDb;
import com.hogwartstest.aitestmini.dao.HogwartsTestHisMapper;
import com.hogwartstest.aitestmini.dto.*;
import com.hogwartstest.aitestmini.entity.HogwartsTestHis;
import com.hogwartstest.aitestmini.service.HogwartsTestHisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class HogwartsTestHisServiceImpl implements HogwartsTestHisService {

	@Autowired
	private HogwartsTestHisMapper hogwartsTestHisMapper;


	/**
	 * 新增测试记录信息
	 *
	 * @param hogwartsTestHis
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultDto<HogwartsTestHis> save(HogwartsTestHis hogwartsTestHis) {

		hogwartsTestHis.setCreateTime(new Date());
		hogwartsTestHis.setUpdateTime(new Date());
		hogwartsTestHisMapper.insertUseGeneratedKeys(hogwartsTestHis);
		return ResultDto.success("成功", hogwartsTestHis);
	}

	/**
	 * 修改测试记录信息
	 *
	 * @param hogwartsTestHis
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultDto<HogwartsTestHis> update(HogwartsTestHis hogwartsTestHis) {

		HogwartsTestHis queryHogwartsTestHis = new HogwartsTestHis();

		queryHogwartsTestHis.setId(hogwartsTestHis.getId());
		queryHogwartsTestHis.setCreateUserId(hogwartsTestHis.getCreateUserId());

		HogwartsTestHis result = hogwartsTestHisMapper.selectOne(queryHogwartsTestHis);

		//如果为空，则提示，也可以直接返回成功
		if(Objects.isNull(result)){
			return ResultDto.fail("未查到测试记录信息");
		}

		//先赋值原创建时间
		result.setCreateTime(result.getCreateTime());
		//再赋值
		BeanUtils.copyProperties(hogwartsTestHis, result);
		//重新设置更新时间为当前时间
		result.setUpdateTime(new Date());

		hogwartsTestHisMapper.updateByPrimaryKey(result);

		return ResultDto.success("成功");
	}

	/**
	 * 查询测试记录信息列表
	 *
	 * @param pageTableRequest
	 * @return
	 */
	@Override
	public ResultDto<PageTableResponse<HogwartsTestHis>> list(PageTableRequest1 pageTableRequest) {

		Map<String, Object> params = pageTableRequest.getParams();
		Integer pageNum = pageTableRequest.getPageNum();
		Integer pageSize = pageTableRequest.getPageSize();

		//总数
		Integer recordsTotal =  hogwartsTestHisMapper.count(params);

		//分页查询数据
		List<HogwartsTestHis>  hogwartsTestJenkinsList = hogwartsTestHisMapper
				.list(params, (pageNum - 1) * pageSize, pageSize);


		PageTableResponse<HogwartsTestHis> hogwartsTestJenkinsPageTableResponse = new PageTableResponse<>();
		hogwartsTestJenkinsPageTableResponse.setRecordsTotal(recordsTotal);
		hogwartsTestJenkinsPageTableResponse.setData(hogwartsTestJenkinsList);

		return ResultDto.success("成功", hogwartsTestJenkinsPageTableResponse);
	}
}
