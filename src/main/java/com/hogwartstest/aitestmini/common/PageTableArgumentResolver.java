package com.hogwartstest.aitestmini.common;

import com.google.common.collect.Maps;
import com.hogwartstest.aitestmini.dto.PageTableRequest;
import com.hogwartstest.aitestmini.dto.PageTableRequest1;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 分页、查询参数解析
 *
 */
public class PageTableArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> cla = parameter.getParameterType();

		return cla.isAssignableFrom(PageTableRequest1.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

		PageTableRequest1 tableRequest = new PageTableRequest1();
		Map<String, String[]> param = request.getParameterMap();
		if (param.containsKey("pageNum")) {
			tableRequest.setPageNum(Integer.parseInt(request.getParameter("pageNum")));
		}

		if (param.containsKey("pageSize")) {
			tableRequest.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
		}

		Map<String, Object> map = Maps.newHashMap();
		tableRequest.setParams(map);

		param.forEach((k, v) -> {
			if (v.length == 1) {
				map.put(k, v[0]);
			} else {
				map.put(k, Arrays.asList(v));
			}
		});

		setOrderBy(tableRequest, map);
		removeParam(tableRequest);

		//postman模拟请求时进行的特殊处理
		Map<String, Object> params = tableRequest.getParams();
		if(tableRequest.getPageNum()==null){
			//前端传值以前端为准，否则取默认值
			if(params!=null&&params.get("pageNum")!=null){
				tableRequest.setPageNum(Integer.parseInt(params.get("pageNum").toString()));
			}else {
				tableRequest.setPageNum(0);
			}
		}
		if(tableRequest.getPageSize()==null){
			//前端传值以前端为准，否则取默认值
			if(params!=null&&params.get("pageSize")!=null){
				tableRequest.setPageNum(Integer.parseInt(params.get("pageSize").toString()));
			}else{
				tableRequest.setPageSize(10);
			}
		}
		return tableRequest;
	}

	/**
	 * 去除datatables分页带的一些复杂参数
	 *
	 * @param tableRequest
	 */
	private void removeParam(PageTableRequest1 tableRequest) {
		Map<String, Object> map = tableRequest.getParams();

		if (!CollectionUtils.isEmpty(map)) {
			Map<String, Object> param = new HashMap<>();
			map.forEach((k, v) -> {
				if (k.indexOf("[") < 0 && k.indexOf("]") < 0 && !"_".equals(k)) {
					param.put(k, v);
				}
			});

			tableRequest.setParams(param);
		}
	}

	/**
	 * 从datatables分页请求数据中解析排序
	 *
	 * @param tableRequest
	 * @param map
	 */
	private void setOrderBy(PageTableRequest1 tableRequest, Map<String, Object> map) {
		StringBuilder orderBy = new StringBuilder();
		int size = map.size();
		for (int i = 0; i < size; i++) {
			String index = (String) map.get("order[" + i + "][column]");
			if (StringUtils.isEmpty(index)) {
				break;
			}
			String column = (String) map.get("columns[" + index + "][data]");
			if (StringUtils.isBlank(column)) {
				continue;
			}
			String sort = (String) map.get("order[" + i + "][dir]");

			orderBy.append(column).append(" ").append(sort).append(", ");
		}
	}

}
