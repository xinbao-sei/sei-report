package com.changhong.sei.report.expression.function.math;

import com.changhong.sei.report.builds.Context;
import com.changhong.sei.report.expression.model.data.ExpressionData;
import com.changhong.sei.report.model.Cell;

import java.math.BigDecimal;
import java.util.List;

/**
 * @desc：求e底数的参数次方
 * @author：zhaohz
 * @date：2020/7/6 9:08
 */
public class ExpFunction extends MathFunction {

	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
		BigDecimal data=buildBigDecimal(dataList);
		return Math.exp(data.doubleValue());
	}

	@Override
	public String name() {
		return "exp";
	}
}
