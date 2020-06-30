package com.changhong.sei.report.expression.model.expr;

import com.changhong.sei.report.builds.Context;
import com.changhong.sei.report.exception.ReportComputeException;
import com.changhong.sei.report.expression.model.data.ExpressionData;
import com.changhong.sei.report.expression.model.data.ObjectExpressionData;
import com.changhong.sei.report.expression.model.expr.set.CellExpression;
import com.changhong.sei.report.model.Cell;

import java.util.List;
import java.util.Map;

/**
 * @desc：单元格位置
 * @author：zhaohz
 * @date：2020/6/30 13:49
 */
public class CellPositionExpression extends CellExpression {
	private static final long serialVersionUID = 6881039873078990276L;

	public CellPositionExpression(String cellName) {
		super(cellName);
	}
	@Override
	public boolean supportPaging(){
		return false;
	}
	@Override
	protected ExpressionData<?> compute(Cell cell, Cell currentCell, Context context) {
		List<Cell> targetCells=fetchCellsByLeftParent(context,cell,cellName);
		if(targetCells==null || targetCells.size()==0){
			targetCells=fetchCellsByTopParent(context,cell,cellName);
		}
		if(targetCells==null || targetCells.size()==0){
			targetCells=context.getReport().getCellsMap().get(cellName);
		}
		if(targetCells==null){
			throw new ReportComputeException("Cell ["+cellName+"] not exist.");
		}
		int index=-1;
		int rowNumber=cell.getRow().getRowNumber(),colNumber=cell.getColumn().getColumnNumber();
		for(int i=0;i<targetCells.size();i++){
			Cell target=targetCells.get(i);
			if(target.getRow()==cell.getRow()){
				index=i;
				break;
			}
			int rowSpan=target.getRowSpan();
			if(rowSpan>0){
				int targetRowStart=target.getRow().getRowNumber();
				int targetRowEnd=targetRowStart+rowSpan-1;
				if(targetRowStart<=rowNumber && targetRowEnd>=rowNumber){
					index=i;
					break;
				}
			}
		}
		if(index>-1){
			index++;
			return new ObjectExpressionData(index);
		}
		for(int i=0;i<targetCells.size();i++){
			Cell target=targetCells.get(i);
			if(target.getColumn()==cell.getColumn()){
				index=i;
				break;
			}
			int colSpan=target.getColSpan();
			if(colSpan>0){
				int targetColStart=target.getColumn().getColumnNumber();
				int targetColEnd=targetColStart+colSpan-1;
				if(targetColStart<=colNumber && targetColEnd>=colNumber){
					index=i;
					break;
				}
			}
		}
		index++;
		return new ObjectExpressionData(index);
	}
	
	private List<Cell> fetchCellsByLeftParent(Context context, Cell cell, String cellName){
		Cell leftParentCell=cell.getLeftParentCell();
		if(leftParentCell==null){
			return null;
		}
		Map<String,List<Cell>> childrenCellsMap=leftParentCell.getRowChildrenCellsMap();
		List<Cell> targetCells=childrenCellsMap.get(cellName);
		if(targetCells!=null){
			return targetCells;
		}
		return fetchCellsByLeftParent(context,leftParentCell,cellName);
	}
	
	private List<Cell> fetchCellsByTopParent(Context context, Cell cell, String cellName){
		Cell topParentCell=cell.getTopParentCell();
		if(topParentCell==null){
			return null;
		}
		Map<String,List<Cell>> childrenCellsMap=topParentCell.getColumnChildrenCellsMap();
		List<Cell> targetCells=childrenCellsMap.get(cellName);
		if(targetCells!=null){
			return targetCells;
		}
		return fetchCellsByTopParent(context,topParentCell,cellName);
	}
}
