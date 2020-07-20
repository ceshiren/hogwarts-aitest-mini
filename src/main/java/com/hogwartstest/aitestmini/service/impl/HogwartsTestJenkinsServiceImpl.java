package com.hogwartstest.aitestmini.service.impl;

import com.hogwartstest.aitestmini.common.TokenDb;
import com.hogwartstest.aitestmini.dao.HogwartsTestJenkinsMapper;
import com.hogwartstest.aitestmini.dao.HogwartsTestUserMapper;
import com.hogwartstest.aitestmini.dto.PageTableRequest;
import com.hogwartstest.aitestmini.dto.PageTableResponse;
import com.hogwartstest.aitestmini.dto.TokenDto;
import com.hogwartstest.aitestmini.dto.jenkins.QueryHogwartsTestJenkinsListDto;
import com.hogwartstest.aitestmini.dto.ResultDto;
import com.hogwartstest.aitestmini.entity.HogwartsTestJenkins;
import com.hogwartstest.aitestmini.entity.HogwartsTestUser;
import com.hogwartstest.aitestmini.service.HogwartsTestJenkinsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class HogwartsTestJenkinsServiceImpl implements HogwartsTestJenkinsService {

	@Autowired
	private HogwartsTestJenkinsMapper hogwartsTestJenkinsMapper;

	@Autowired
	private HogwartsTestUserMapper hogwartsTestUserMapper;

	@Autowired
	private TokenDb tokenDb;


	/**
	 * 新增Jenkins信息
	 *
	 * @param hogwartsTestJenkins
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultDto<HogwartsTestJenkins> save(TokenDto tokenDto, HogwartsTestJenkins hogwartsTestJenkins) {

		hogwartsTestJenkins.setCreateTime(new Date());
		hogwartsTestJenkins.setUpdateTime(new Date());
		hogwartsTestJenkinsMapper.insertUseGeneratedKeys(hogwartsTestJenkins);

		Integer defaultJenkinsFlag = hogwartsTestJenkins.getDefaultJenkinsFlag();

		if(Objects.nonNull(defaultJenkinsFlag) && defaultJenkinsFlag==1){

			Integer createUserId = hogwartsTestJenkins.getCreateUserId();
			HogwartsTestUser hogwartsTestUser = new HogwartsTestUser();
			hogwartsTestUser.setId(createUserId);
			hogwartsTestUser.setDefaultJenkinsId(hogwartsTestJenkins.getId());

			//更新token信息中的默认JenkinsId
			tokenDto.setDefaultJenkinsId(hogwartsTestJenkins.getId());
			tokenDb.addTokenDto(tokenDto.getToken(), tokenDto);

			hogwartsTestUserMapper.updateByPrimaryKeySelective(hogwartsTestUser);

		}

		return ResultDto.success("成功", hogwartsTestJenkins);
	}

	/**
	 * 删除Jenkins信息
	 *
	 * @param jenkinsId
	 * @return createUserId
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultDto<HogwartsTestJenkins> delete(Integer jenkinsId,TokenDto tokenDto) {

		HogwartsTestJenkins queryHogwartsTestJenkins = new HogwartsTestJenkins();

		queryHogwartsTestJenkins.setId(jenkinsId);
		queryHogwartsTestJenkins.setCreateUserId(tokenDto.getUserId());

		HogwartsTestJenkins result = hogwartsTestJenkinsMapper.selectOne(queryHogwartsTestJenkins);

		//如果为空，则提示，也可以直接返回成功
		if(Objects.isNull(result)){
			return ResultDto.fail("未查到Jenkins信息");
		}

		HogwartsTestUser queryHogwartsTestUser = new HogwartsTestUser();
		queryHogwartsTestUser.setId(tokenDto.getUserId());

		HogwartsTestUser reslutHogwartsTestUser = hogwartsTestUserMapper.selectOne(queryHogwartsTestUser);

		Integer defaultJenkinsId = reslutHogwartsTestUser.getDefaultJenkinsId();

		if(Objects.nonNull(defaultJenkinsId) && defaultJenkinsId.equals(jenkinsId)){

			HogwartsTestUser hogwartsTestUser = new HogwartsTestUser();
			hogwartsTestUser.setId(tokenDto.getUserId());
			hogwartsTestUser.setDefaultJenkinsId(null);

			//更新token信息中的默认JenkinsId
			tokenDto.setDefaultJenkinsId(null);
			tokenDb.addTokenDto(tokenDto.getToken(), tokenDto);

			hogwartsTestUserMapper.updateByPrimaryKeySelective(hogwartsTestUser);

		}

		hogwartsTestJenkinsMapper.deleteByPrimaryKey(jenkinsId);

		return ResultDto.success("成功");
	}

	/**
	 * 修改Jenkins信息
	 *
	 * @param hogwartsTestJenkins
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultDto<HogwartsTestJenkins> update(TokenDto tokenDto, HogwartsTestJenkins hogwartsTestJenkins) {

		HogwartsTestJenkins queryHogwartsTestJenkins = new HogwartsTestJenkins();

		queryHogwartsTestJenkins.setId(hogwartsTestJenkins.getId());
		queryHogwartsTestJenkins.setCreateUserId(hogwartsTestJenkins.getCreateUserId());

		HogwartsTestJenkins result = hogwartsTestJenkinsMapper.selectOne(queryHogwartsTestJenkins);

		//如果为空，则提示，也可以直接返回成功
		if(Objects.isNull(result)){
			return ResultDto.fail("未查到Jenkins信息");
		}

		hogwartsTestJenkins.setCreateTime(result.getCreateTime());
		hogwartsTestJenkins.setUpdateTime(new Date());

		hogwartsTestJenkinsMapper.updateByPrimaryKey(hogwartsTestJenkins);

		Integer defaultJenkinsFlag = hogwartsTestJenkins.getDefaultJenkinsFlag();

		if(Objects.nonNull(defaultJenkinsFlag) && defaultJenkinsFlag==1){

			Integer createUserId = hogwartsTestJenkins.getCreateUserId();
			HogwartsTestUser hogwartsTestUser = new HogwartsTestUser();
			hogwartsTestUser.setId(createUserId);
			hogwartsTestUser.setDefaultJenkinsId(hogwartsTestJenkins.getId());

			//更新token信息中的默认JenkinsId
			tokenDto.setDefaultJenkinsId(hogwartsTestJenkins.getId());
			tokenDb.addTokenDto(tokenDto.getToken(), tokenDto);

			hogwartsTestUserMapper.updateByPrimaryKeySelective(hogwartsTestUser);

		}

		return ResultDto.success("成功");
	}

	/**
	 * 根据id查询Jenkins信息
	 *
	 * @param jenkinsId
	 * @return createUserId
	 */
	@Override
	public ResultDto<HogwartsTestJenkins> getById(Integer jenkinsId,Integer createUserId) {

		HogwartsTestJenkins queryHogwartsTestJenkins = new HogwartsTestJenkins();

		queryHogwartsTestJenkins.setId(jenkinsId);
		queryHogwartsTestJenkins.setCreateUserId(createUserId);

		HogwartsTestJenkins result = hogwartsTestJenkinsMapper.selectOne(queryHogwartsTestJenkins);

		//如果为空，则提示，也可以直接返回成功
		if(Objects.isNull(result)){
			ResultDto.fail("未查到Jenkins信息");
		}

		HogwartsTestUser queryHogwartsTestUser = new HogwartsTestUser();
		queryHogwartsTestUser.setId(createUserId);

		HogwartsTestUser resultHogwartsTestUser = hogwartsTestUserMapper.selectOne(queryHogwartsTestUser);
		Integer defaultJenkinsId = resultHogwartsTestUser.getDefaultJenkinsId();

		if(result.getId().equals(defaultJenkinsId)){
			result.setDefaultJenkinsFlag(1);
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
	public ResultDto<PageTableResponse<HogwartsTestJenkins>> list(PageTableRequest<QueryHogwartsTestJenkinsListDto> pageTableRequest) {

		QueryHogwartsTestJenkinsListDto params = pageTableRequest.getParams();
		Integer pageNum = pageTableRequest.getPageNum();
		Integer pageSize = pageTableRequest.getPageSize();
		Integer createUserId = params.getCreateUserId();

		HogwartsTestUser queryHogwartsTestUser = new HogwartsTestUser();
		queryHogwartsTestUser.setId(createUserId);

		HogwartsTestUser resultHogwartsTestUser = hogwartsTestUserMapper.selectOne(queryHogwartsTestUser);
		Integer defaultJenkinsId = resultHogwartsTestUser.getDefaultJenkinsId();

		//总数
		Integer recordsTotal =  hogwartsTestJenkinsMapper.count(params);

		//分页查询数据
		List<HogwartsTestJenkins>  hogwartsTestJenkinsList = hogwartsTestJenkinsMapper.list(params, (pageNum - 1) * pageSize, pageSize);

		//查找默认Jenkins
		for (HogwartsTestJenkins hogwartsTestJenkins:hogwartsTestJenkinsList) {
			if(hogwartsTestJenkins.getId().equals(defaultJenkinsId)){
				hogwartsTestJenkins.setDefaultJenkinsFlag(1);
			}
		}

		PageTableResponse<HogwartsTestJenkins> hogwartsTestJenkinsPageTableResponse = new PageTableResponse<>();
		hogwartsTestJenkinsPageTableResponse.setRecordsTotal(recordsTotal);
		hogwartsTestJenkinsPageTableResponse.setData(hogwartsTestJenkinsList);

		return ResultDto.success("成功", hogwartsTestJenkinsPageTableResponse);
	}
}
