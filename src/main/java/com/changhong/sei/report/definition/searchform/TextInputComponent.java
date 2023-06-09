package com.changhong.sei.report.definition.searchform;

/**
 * @desc：为本输入框
 * @author：zhaohz
 * @date：2020/6/30 11:17
 */
public class TextInputComponent extends InputComponent {
	@Override
	String inputHtml(RenderContext context) {
		String name=getBindParameter();
		Object pvalue=context.getParameter(name)==null ? "" : context.getParameter(name);
		return "<input type='text' value=\""+pvalue+"\" style=\"padding:3px;height:28px\" id='"+context.buildComponentId(this)+"' name='"+getBindParameter()+"' class='form-control'>";	
	}
	@Override
	public String initJs(RenderContext context) {
		String name=getBindParameter();
		StringBuilder sb=new StringBuilder();
		sb.append("formElements.push(");
		sb.append("function(){");
		sb.append("if(''==='").append(name).append("'){");
		sb.append("alert('文本框未绑定查询参数名，不能进行查询操作!');");
		sb.append("throw '文本框未绑定查询参数名，不能进行查询操作!'");
		sb.append("}");
		sb.append("return {");
		sb.append("\"").append(name).append("\":");
		sb.append("$('#").append(context.buildComponentId(this)).append("').val()");
		sb.append("}");
		sb.append("}");
		sb.append(");");
		return sb.toString();
	}
}
