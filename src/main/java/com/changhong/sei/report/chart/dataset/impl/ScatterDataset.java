package com.changhong.sei.report.chart.dataset.impl;

import com.changhong.sei.report.builds.Context;
import com.changhong.sei.report.chart.dataset.BaseDataset;
import com.changhong.sei.report.chart.dataset.ScatterData;
import com.changhong.sei.report.model.Cell;
import com.changhong.sei.report.utils.DataUtils;
import com.changhong.sei.report.utils.Utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @desc：散点图数据集
 * @author：zhaohz
 * @date：2020/6/30 16:28
 */
public class ScatterDataset extends BaseDataset {
	private String datasetName;
	private String categoryProperty;
	private String xProperty;
	private String yProperty;
	
	private boolean fill=true;
	private double lineTension=0.2;
	
	@Override
	public String buildDataJson(Context context, Cell cell) {
		List<?> dataList= DataUtils.fetchData(cell, context, datasetName);
		Map<Object,List<ScatterData>> map=new LinkedHashMap<Object,List<ScatterData>>();
		for(Object obj:dataList){
			Object category= Utils.getProperty(obj, categoryProperty);
			if(category==null){
				continue;
			}
			Object xValue= Utils.getProperty(obj, xProperty);
			Object yValue= Utils.getProperty(obj, yProperty);
			if(xValue==null || yValue==null){
				continue;
			}
			double x= Utils.toBigDecimal(xValue).doubleValue();
			double y= Utils.toBigDecimal(yValue).doubleValue();
			List<ScatterData> list=null;
			if(map.containsKey(category)){
				list=map.get(category);
			}else{
				list=new ArrayList<ScatterData>();
				map.put(category, list);
			}
			list.add(new ScatterData(x,y));
		}
		StringBuilder sb=new StringBuilder();
		sb.append("{");
		sb.append("\"datasets\":[");
		int index=0;
		for(Object obj:map.keySet()){
			if(index>0){
				sb.append(",");
			}
			sb.append("{");
			sb.append("\"label\":\"").append(obj).append("\",");
			sb.append("\"fill\":").append(fill).append(",");
			sb.append("\"lineTension\":").append(lineTension).append(",");
			sb.append("\"borderColor\":\"rgb(").append(getRgbColor(index)).append(")\",");
			sb.append("\"backgroundColor\":\"rgba(").append(getRgbColor(index)).append(",0.5)\",");
			sb.append("\"data\":[");
			List<ScatterData> list=map.get(obj);
			int i=0;
			for(ScatterData data:list){
				if(i>0){
					sb.append(",");
				}
				i++;
				sb.append("{");				
				sb.append("\"x\":").append(data.getX()).append(",");
				sb.append("\"y\":").append(data.getY()).append("");
				sb.append("}");				
			}
			sb.append("]");
			sb.append("}");
			index++;
		}
		sb.append("]");
		sb.append("}");
		return sb.toString();
	}
	@Override
	public String getType() {
		return "scatter";
	}
	
	public String getDatasetName() {
		return datasetName;
	}
	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}
	public String getCategoryProperty() {
		return categoryProperty;
	}
	public void setCategoryProperty(String categoryProperty) {
		this.categoryProperty = categoryProperty;
	}
	public String getxProperty() {
		return xProperty;
	}
	public void setxProperty(String xProperty) {
		this.xProperty = xProperty;
	}
	public String getyProperty() {
		return yProperty;
	}
	public void setyProperty(String yProperty) {
		this.yProperty = yProperty;
	}
	public boolean isFill() {
		return fill;
	}
	public void setFill(boolean fill) {
		this.fill = fill;
	}
	public double getLineTension() {
		return lineTension;
	}
	public void setLineTension(double lineTension) {
		this.lineTension = lineTension;
	}
}
