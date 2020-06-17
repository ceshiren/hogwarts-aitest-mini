package com.hogwartstest.aitestmini.constants;

/**
 * 通用相关常量
 */
public interface Constants {


	/**
	 * 未删除
	 */
	public final static Integer DEL_FLAG_ONE = 1;

	/**
	 * 已删除
	 */
	public final static Integer DEL_FLAG_ZERO = 0;

	/**
	 * 0 无效
	 */
	public final static Integer STATUS_ZERO = 0;

	/**
	 * 1 新建
	 */
	public final static Integer STATUS_ONE = 1;

	/**
	 * 2 执行中
	 */
	public final static Integer STATUS_TWO = 2;

	/**
	 * 3 执行完成
	 */
	public final static Integer STATUS_THREE = 3;

	/**
	 * 1 执行测试任务
	 */
	public final static Integer TASK_TYPE_ONE = 1;

	/**
	 * 2 一键执行测试的任务
	 */
	public final static Integer TASK_TYPE_TWO = 2;

	/**
	 * 90 生成用例的任务
	 */
	public final static Integer TASK_TYPE_90 = 90;


	/**
	 * Jenkins生成用例的Job
	 */
	public final static Integer JOB_TYPE_ONE = 1;

	/**
	 * Jenkins执行测试的Job
	 */
	public final static Integer JOB_TYPE_TWO = 2;

	/**
	 * 任务类型 普通测试任务
	 */
	public final static Integer TASK_TYPE_UNIT = 1;
	/**
	 * 任务类型 一键执行测试任务
	 */
	public final static Integer TASK_TYPE_QUICK = 2;


}
