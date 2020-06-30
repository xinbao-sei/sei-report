package com.changhong.sei.report.expression.model.expr.ifelse;

import com.changhong.sei.report.builds.Context;
import com.changhong.sei.report.expression.model.data.ExpressionData;
import com.changhong.sei.report.expression.model.data.ObjectExpressionData;
import com.changhong.sei.report.expression.model.expr.BaseExpression;
import com.changhong.sei.report.expression.model.expr.ExpressionBlock;
import com.changhong.sei.report.model.Cell;

import java.util.List;

/**
 * @desc：if表达式
 * @author：zhaohz
 * @date：2020/6/30 15:35
 */
public class IfExpression extends BaseExpression {
	private static final long serialVersionUID = -514395376408127087L;
	private ExpressionConditionList conditionList;
	private ExpressionBlock expression;
	private List<ElseIfExpression> elseIfExpressions;
	private ElseExpression elseExpression;
	@Override
	protected ExpressionData<?> compute(Cell cell, Cell currentCell, Context context) {
		if(conditionList!=null){
			boolean result=conditionList.eval(context, cell,currentCell);
			if(result){
				return expression.execute(cell, currentCell,context);
			}
		}
		if(elseIfExpressions!=null){				
			for(ElseIfExpression elseIfExpr:elseIfExpressions){
				if(elseIfExpr.conditionsEval(cell, currentCell,context)){
					return elseIfExpr.execute(cell,currentCell, context);
				}
			}
		}
		if(elseExpression!=null){
			return elseExpression.execute(cell,currentCell, context);
		}
		return new ObjectExpressionData(null);
	}
	public void setConditionList(ExpressionConditionList conditionList) {
		this.conditionList = conditionList;
	}
	public ExpressionConditionList getConditionList() {
		return conditionList;
	}
	public void setElseExpression(ElseExpression elseExpression) {
		this.elseExpression = elseExpression;
	}
	public void setElseIfExpressions(List<ElseIfExpression> elseIfExpressions) {
		this.elseIfExpressions = elseIfExpressions;
	}
	public ExpressionBlock getExpression() {
		return expression;
	}
	public void setExpression(ExpressionBlock expression) {
		this.expression = expression;
	}
	public ElseExpression getElseExpression() {
		return elseExpression;
	}
	public List<ElseIfExpression> getElseIfExpressions() {
		return elseIfExpressions;
	}
}
