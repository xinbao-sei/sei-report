package com.changhong.sei.report.model;

import com.bstek.ureport.chart.ChartData;
import com.bstek.ureport.export.html.SearchFormData;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.Collection;

public class PageReportDto implements Serializable {
    /**
     * 当前页码
     */
    private Integer page;

    /**
     * 每页显示条数
     */
    private Integer rows;

    /**
     * 总条数
     */
    private Integer records;

    /**
     * 总页数
     */
    private Integer total;
    private String content;
    private String style;
    private int column;
    private String reportAlign;
    private Collection<ChartData> chartDatas;
    private int htmlIntervalRefreshValue;
    private SearchFormData searchFormData;
    private String type;//1-预览,2-分页预览,3-物理分页预览

    public Integer getPage() {
        return page;
    }

    public Integer getRows() {
        return rows;
    }

    public Integer getRecords() {
        return records;
    }

    public Integer getTotal() {
        return total;
    }

    public String getContent() {
        return content;
    }

    public String getStyle() {
        return style;
    }

    public int getColumn() {
        return column;
    }

    public String getReportAlign() {
        return reportAlign;
    }

    public Collection<ChartData> getChartDatas() {
        return chartDatas;
    }

    public int getHtmlIntervalRefreshValue() {
        return htmlIntervalRefreshValue;
    }

    public SearchFormData getSearchFormData() {
        return searchFormData;
    }

    public String getType() {
        return type;
    }

    public PageReportDto(){}

    public PageReportDto(PageReport report){
        this.page = ObjectUtils.isEmpty(report.getPage())?0:report.getPage().getPage();
        this.rows = ObjectUtils.isEmpty(report.getPage())?0:report.getPage().getRows();
        this.total = ObjectUtils.isEmpty(report.getPage())?0:report.getPage().getTotal();
        this.records = ObjectUtils.isEmpty(report.getPage())?0:report.getPage().getRecords();
        this.content = report.getContent();
        this.style = report.getStyle();
        this.column = report.getColumn();
        this.reportAlign = report.getReportAlign();
        this.chartDatas = report.getChartDatas();
        this.htmlIntervalRefreshValue = report.getHtmlIntervalRefreshValue();
        this.searchFormData = report.getSearchFormData();
        this.type = report.getType();
    }
}