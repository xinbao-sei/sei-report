package com.changhong.sei.report.builds.aggregate;

import com.changhong.sei.report.builds.BindData;
import com.changhong.sei.report.builds.Context;
import com.changhong.sei.report.definition.value.Value;
import com.changhong.sei.report.exception.ReportComputeException;
import com.changhong.sei.report.expression.model.Condition;
import com.changhong.sei.report.expression.model.expr.DatasetExpression;
import com.changhong.sei.report.model.Cell;
import com.changhong.sei.report.utils.DataUtils;
import com.changhong.sei.report.utils.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @desc：平均值计算
 * @author：zhaohz
 * @date：2020/6/30 17:07
 */
public class AvgAggregate extends Aggregate {
	@Override
	public List<BindData> aggregate(DatasetExpression expr, Cell cell, Context context) {
		String datasetName=expr.getDatasetName();
		String property=expr.getProperty();
		Cell leftCell= DataUtils.fetchLeftCell(cell, context, datasetName);
		Cell topCell= DataUtils.fetchTopCell(cell, context, datasetName);
		List<Object> leftList=null,topList=null;
		if(leftCell!=null){
			leftList=leftCell.getBindData();
		}
		if(topCell!=null){
			topList=topCell.getBindData();
		}
		BigDecimal result=null;
		if(leftList==null && topList==null){
			List<?> data=context.getDatasetData(datasetName);
			result=buildAvg(data,property,cell,expr,context);
		}else if(leftList==null){
			result=buildAvg(topList,property,cell,expr,context);
		}else if(topList==null){
			result=buildAvg(leftList,property,cell,expr,context);
		}else{
			List<Object> list=null;
			Object data=null;
			String prop=null;
			if(leftList.size()>topList.size()){
				list=topList;
				data=leftCell.getData();
				Value value=leftCell.getValue();
				DatasetExpression de= DataUtils.fetchDatasetExpression(value);
				if(de==null){
					throw new ReportComputeException("Unsupport value : "+value);
				}
				prop=de.getProperty();
			}else{
				list=leftList;
				data=topCell.getData();
				Value value=topCell.getValue();
				DatasetExpression de= DataUtils.fetchDatasetExpression(value);
				if(de==null){
					throw new ReportComputeException("Unsupport value : "+value);
				}
				prop=de.getProperty();
			}
			result=new BigDecimal(0);
			int count=0;
			Condition condition=getCondition(cell);
			if(condition==null){
				condition=expr.getCondition();
			}
			for(Object obj:list){
				if(condition!=null && !condition.filter(cell, cell, obj, context)){
					continue;
				}
				Object o= Utils.getProperty(obj, prop);
				if((o!=null && data!=null && (o==data || o.equals(data))) || (o==null && data==null)){
					Object value= Utils.getProperty(obj, property);
					if(value==null || value.toString().equals("")){
						continue;
					}
					result=result.add(Utils.toBigDecimal(value));
					count++;
				}
			}
			if(count>0){
				result=result.divide(new BigDecimal(count),8,BigDecimal.ROUND_HALF_UP);				
			}
		}
		List<BindData> list=new ArrayList<BindData>();
		list.add(new BindData(result.doubleValue(),null));
		return list;
	}
	
	private BigDecimal buildAvg(List<?> list, String property, Cell cell, DatasetExpression expr, Context context){
		Condition condition=getCondition(cell);
		if(condition==null){
			condition=expr.getCondition();
		}
		BigDecimal result=new BigDecimal(0);
		int size=0;
		for(Object obj:list){
			if(condition!=null){
				boolean ok=condition.filter(cell, cell, obj, context);
				if(!ok){
					continue;
				}
			}
			Object value= Utils.getProperty(obj, property);
			if(value==null || value.toString().equals("")){
				continue;
			}
			result=result.add(Utils.toBigDecimal(value));
			size++;
		}
		result=result.divide(new BigDecimal(size),8,BigDecimal.ROUND_HALF_UP);
		return result;
	}
}
