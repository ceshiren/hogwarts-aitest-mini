package com.hogwartstest.aitestmini.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.hogwartstest.aitestmini.dto.PageTableRequest;
import com.hogwartstest.aitestmini.dto.PageTableRequest1;
import com.hogwartstest.aitestmini.dto.jenkins.QueryHogwartsTestJenkinsListDto;
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

		return cla.isAssignableFrom(PageTableRequest.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

		PageTableRequest tableRequest = new PageTableRequest();
		Map<String, String[]> param = request.getParameterMap();
		if (param.containsKey("pageNum")) {
			tableRequest.setPageNum(Integer.parseInt(request.getParameter("pageNum")));
		}

		if (param.containsKey("pageSize")) {
			tableRequest.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
		}

		JSONObject json = new JSONObject();

		param.forEach((k, v) -> {
			if (v.length == 1) {
                json.put(k, v[0]);
			} else {
                json.put(k, Arrays.asList(v));
			}
		});

        String requestUri = request.getRequestURI();
		if(requestUri.contains("/jenkins/list")){
            QueryHogwartsTestJenkinsListDto queryHogwartsTestJenkinsListDto
                    = JSONObject.parseObject(json.toJSONString(),QueryHogwartsTestJenkinsListDto.class);
            tableRequest.setParams(queryHogwartsTestJenkinsListDto);
        }
		return tableRequest;
	}

}
