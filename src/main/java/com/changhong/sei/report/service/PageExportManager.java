package com.changhong.sei.report.service;

import com.changhong.sei.report.export.ExportConfigure;
import com.changhong.sei.report.model.PageReport;
import com.changhong.sei.report.model.Page;

import javax.servlet.ServletException;
import java.sql.SQLException;
import java.util.Map;

public interface PageExportManager {
    public static final String BEAN_ID="ureport.exportManager";
    /**
     * 导出Html报表
     * @param file 报表模版文件名
     * @param contextPath 当前项目的context path
     * @param parameters 参数
     * @return 返回一个HtmlReport对象，里面有报表产生的HTML及相关CSS
     */
    PageReport exportHtml(String file, String contextPath, Map<String, Object> parameters);
    /**
     * 导出指定页码的物理分页Html报表
     * @param file 报表模版文件名
     * @param contextPath 当前项目的context path
     * @param parameters 参数
     * @param pageIndex 当前页码
     * @return 返回一个HtmlReport对象，里面有报表产生的HTML及相关CSS
     */
    PageReport exportHtml(String file, String contextPath, Map<String, Object> parameters, int pageIndex);
    /**
     * 导出指定页码的物理分页Html报表
     * @param file 报表模版文件名
     * @param contextPath 当前项目的context path
     * @param parameters 参数
     * @param page  物理分页实体
     * @return 返回一个HtmlReport对象，里面有报表产生的HTML及相关CSS
     */
    PageReport exportHtml(String file, String contextPath, Map<String, Object> parameters, Page page) throws ServletException, SQLException;
    /**
     * 导出PDF报表
     * @param config 包含报表模版文件名、参数等信息的配置对象
     */
    void exportPdf(ExportConfigure config);
    /**
     * 不分页导出Excel
     * @param config 包含报表模版文件名、参数等信息的配置对象
     */
    void exportExcel(ExportConfigure config);
    /**
     * 不分页导出Excel97格式文件
     * @param config 包含报表模版文件名、参数等信息的配置对象
     */
    void exportExcel97(ExportConfigure config);
    /**
     * 分页导出Excel
     * @param config 包含报表模版文件名、参数等信息的配置对象
     */
    void exportExcelWithPaging(ExportConfigure config);
    /**
     * 分页导出Excel
     * @param config 包含报表模版文件名、参数等信息的配置对象
     */
    void exportExcel97WithPaging(ExportConfigure config);
    /**
     * 分页分Sheet导出Excel
     * @param config 包含报表模版文件名、参数等信息的配置对象
     */
    void exportExcelWithPagingSheet(ExportConfigure config);
    /**
     * 分页分Sheet导出Excel
     * @param config 包含报表模版文件名、参数等信息的配置对象
     */
    void exportExcel97WithPagingSheet(ExportConfigure config);
    /**
     * 导出Word
     * @param config 包含报表模版文件名、参数等信息的配置对象
     */
    void exportWord(ExportConfigure config);
}
