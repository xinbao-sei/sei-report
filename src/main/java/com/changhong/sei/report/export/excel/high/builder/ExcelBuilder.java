package com.changhong.sei.report.export.excel.high.builder;

import com.changhong.sei.report.definition.Paper;
import com.changhong.sei.report.enums.Orientation;
import com.changhong.sei.report.enums.PaperType;
import com.changhong.sei.report.model.Column;
import com.changhong.sei.report.model.Image;
import com.changhong.sei.report.model.Row;
import com.changhong.sei.report.utils.UnitUtils;
import org.apache.poi.ss.usermodel.PaperSize;
import org.apache.poi.ss.usermodel.PrintOrientation;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFPrintSetup;

import java.util.List;

/**
 * @desc：excel抽象类
 * @author：zhaohz
 * @date：2020/7/6 11:40
 */
public abstract class ExcelBuilder {
	
	protected int getWholeWidth(List<Column> columns, int colNumber, int colSpan){
		Column col=columns.get(colNumber);
		int start=colNumber+1,end=colNumber+colSpan;
		int w=col.getWidth();
		for(int i=start;i<end;i++){
			Column c=columns.get(i);
			w+=c.getWidth();
		}
		w= UnitUtils.pointToPixel(w);
		return w;
	}
	
	protected int getWholeHeight(List<Row> rows, int rowNumber, int rowSpan){
		Row row=rows.get(rowNumber);
		int start=rowNumber+1,end=rowNumber+rowSpan;
		int h=row.getRealHeight();
		for(int i=start;i<end;i++){
			Row r=rows.get(i);
			h+=r.getRealHeight();
		}
		h= UnitUtils.pointToPixel(h);
		return h;
	}
	
	protected Sheet createSheet(SXSSFWorkbook wb, Paper paper, String name){
		Sheet sheet = null;
		if(name==null){
			sheet=wb.createSheet();
		}else{			
			sheet=wb.createSheet(name);
		}
		PaperType paperType=paper.getPaperType();
		XSSFPrintSetup printSetup=(XSSFPrintSetup)sheet.getPrintSetup();
		Orientation orientation=paper.getOrientation();
		if(orientation.equals(Orientation.landscape)){
			printSetup.setOrientation(PrintOrientation.LANDSCAPE);
		}
		setupPaper(paperType, printSetup);
		int leftMargin=paper.getLeftMargin();
		int rightMargin=paper.getRightMargin();
		int topMargin=paper.getTopMargin();
		int bottomMargin=paper.getBottomMargin();
		sheet.setMargin(Sheet.LeftMargin, UnitUtils.pointToInche(leftMargin));
		sheet.setMargin(Sheet.RightMargin, UnitUtils.pointToInche(rightMargin));
		sheet.setMargin(Sheet.TopMargin, UnitUtils.pointToInche(topMargin));
		sheet.setMargin(Sheet.BottomMargin, UnitUtils.pointToInche(bottomMargin));
		return sheet;
	}

	protected int buildImageFormat(Image img){
		int type= Workbook.PICTURE_TYPE_PNG;
		String path=img.getPath();
		if(path==null){
			return type;
		}
		path=path.toLowerCase();
		if(path.endsWith("jpg") || path.endsWith("jpeg")){
			type= Workbook.PICTURE_TYPE_JPEG;
		}
		return type;
	}

	protected boolean setupPaper(PaperType paperType, XSSFPrintSetup printSetup) {
		boolean setup=false;
		switch(paperType){
			case A0:
				printSetup.setPaperSize(PaperSize.A4_PAPER);
				break;
			case A1:
				printSetup.setPaperSize(PaperSize.A4_PAPER);
				break;
			case A2:
				printSetup.setPaperSize(PaperSize.A4_PAPER);
				break;
			case A3:				
				printSetup.setPaperSize(PaperSize.A3_PAPER);
				setup=true;
				break;
			case A4:
				printSetup.setPaperSize(PaperSize.A4_PAPER);
				setup=true;
				break;
			case A5:
				printSetup.setPaperSize(PaperSize.A5_PAPER);
				setup=true;
				break;
			case A6:
				printSetup.setPaperSize(PaperSize.A4_PAPER);
				break;
			case A7:
				printSetup.setPaperSize(PaperSize.A4_PAPER);
				break;
			case A8:
				printSetup.setPaperSize(PaperSize.A4_PAPER);
				break;
			case A9:
				printSetup.setPaperSize(PaperSize.A4_PAPER);
				break;
			case A10:
				printSetup.setPaperSize(PaperSize.A4_PAPER);
				break;
			case B0:
				printSetup.setPaperSize(PaperSize.A4_PAPER);
				break;
			case B1:
				printSetup.setPaperSize(PaperSize.A4_PAPER);
				break;
			case B2:
				printSetup.setPaperSize(PaperSize.A4_PAPER);
				break;
			case B3:
				printSetup.setPaperSize(PaperSize.A4_PAPER);
				break;
			case B4:
				printSetup.setPaperSize(PaperSize.B4_PAPER);
				setup=true;
				break;
			case B5:
				printSetup.setPaperSize(PaperSize.B4_PAPER);
				setup=true;
				break;
			case B6:
				printSetup.setPaperSize(PaperSize.A4_PAPER);
				break;
			case B7:
				printSetup.setPaperSize(PaperSize.A4_PAPER);
				break;
			case B8:
				printSetup.setPaperSize(PaperSize.A4_PAPER);
				break;
			case B9:
				printSetup.setPaperSize(PaperSize.A4_PAPER);
				break;
			case B10:
				printSetup.setPaperSize(PaperSize.A4_PAPER);
				break;
			case CUSTOM:
				printSetup.setPaperSize(PaperSize.A4_PAPER);
				break;
		}
		return setup;
	}
}
