package com.changhong.sei.report.definition.searchform;

import java.util.Arrays;
import java.util.List;

/**
 * @desc：单选框组件
 * @author：zhaohz
 * @date：2020/6/30 11:13
 */
public class RadioInputComponent extends InputComponent {
	private boolean optionsInline;
	private List<Option> options;
	@Override
	String inputHtml(RenderContext context) {
		StringBuilder sb=new StringBuilder();
		String name=getBindParameter();
		Object pvalue=context.getParameter(name)==null ? "" : context.getParameter(name);
		String[] data=pvalue.toString().split(",");
		List<String> list=Arrays.asList(data);
		sb.append("<div>");
		for(Option option:options){
			String value=option.getValue();
			String label=option.getLabel();
			String checked=list.contains(value) ? "checked" : "";
			if(this.optionsInline){
				sb.append("<span class='checkbox-inline' style='padding-top:0px;padding-left:2px;padding-top:0px'><input value='"+value+"' "+checked+" type='radio' name='"+name+"'> "+label+"</span>");
			}else{
				sb.append("<span class='checkbox'><input value='"+value+"' type='radio' "+checked+" name='"+name+"' style='margin-left: auto'> <span style=\"margin-left:15px\">"+label+"</span></span>");
			}
		}
		sb.append("</div>");
		return sb.toString();
	}
	@Override
	public String initJs(RenderContext context) {
		String name=getBindParameter();
		StringBuilder sb=new StringBuilder();
		sb.append("formElements.push(");
		sb.append("function(){");
		sb.append("if(''==='"+name+"'){");
		sb.append("alert('单选框未绑定查询参数名，不能进行查询操作!');");
		sb.append("throw '单选框未绑定查询参数名，不能进行查询操作!'");
		sb.append("}");
		sb.append("return {");
		sb.append("\""+name+"\":");
		sb.append("$(\"input[name='"+getBindParameter()+"']:checked\").val()");
		sb.append("}");
		sb.append("}");
		sb.append(");");
		return sb.toString();
	}
	public void setOptionsInline(boolean optionsInline) {
		this.optionsInline = optionsInline;
	}
	public boolean isOptionsInline() {
		return optionsInline;
	}
	public void setOptions(List<Option> options) {
		this.options = options;
	}
	public List<Option> getOptions() {
		return options;
	}
}
