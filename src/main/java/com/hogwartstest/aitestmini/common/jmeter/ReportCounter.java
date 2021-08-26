package com.hogwartstest.aitestmini.common.jmeter;

import lombok.Data;

import java.util.List;

@Data
public class ReportCounter {
    private int number;
    private List<String> reportIds;
}
