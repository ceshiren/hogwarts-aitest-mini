package com.hogwartstest.aitestmini.dao;

import com.hogwartstest.aitestmini.common.MySqlExtensionMapper;
import com.hogwartstest.aitestmini.entity.HogwartsTestCase;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HogwartsTestCaseMapper extends MySqlExtensionMapper<HogwartsTestCase> {
}
