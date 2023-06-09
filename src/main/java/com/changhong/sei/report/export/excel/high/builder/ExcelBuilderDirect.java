package com.changhong.sei.report.export.excel.high.builder;

import com.changhong.sei.core.log.LogUtil;
import com.changhong.sei.report.chart.ChartData;
import com.changhong.sei.report.definition.Paper;
import com.changhong.sei.report.exception.ReportComputeException;
import com.changhong.sei.report.export.excel.high.CellStyleContext;
import com.changhong.sei.report.expression.model.Report;
import com.changhong.sei.report.model.Cell;
import com.changhong.sei.report.model.Column;
import com.changhong.sei.report.model.Image;
import com.changhong.sei.report.model.Row;
import com.changhong.sei.report.utils.ImageUtils;
import com.changhong.sei.report.utils.UnitUtils;
import com.changhong.sei.report.utils.Utils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFShape;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @desc：
 * @author：zhaohz
 * @date：2020/7/6 13:31
 */
public class ExcelBuilderDirect extends ExcelBuilder {
	public void build(Report report, OutputStream outputStream) {
		CellStyleContext cellStyleContext=new CellStyleContext();
		SXSSFWorkbook wb = new SXSSFWorkbook(100000);
		CreationHelper creationHelper=wb.getCreationHelper();
		Paper paper=report.getPaper();
		try{
			List<Column> columns=report.getColumns();
			Map<Row, Map<Column, Cell>> cellMap=report.getRowColCellMap();
			int columnSize=columns.size();
			Sheet sheet=createSheet(wb, paper, null);
			Drawing<?> drawing=sheet.createDrawingPatriarch();
			List<Row> rows=report.getRows();
			AtomicInteger rowNumber = new AtomicInteger(0);
			rows.stream().forEach(r -> {
				int rowNo = rowNumber.getAndIncrement();
				int realHeight=r.getRealHeight();
				if(realHeight<1){
					return;
				}
				if(r.isForPaging()){
					return;
				}
				org.apache.poi.ss.usermodel.Row row = sheet.getRow(rowNo);
				if(row==null){
					row=sheet.createRow(rowNo);
				}
				Map<Column, Cell> colCell=cellMap.get(r);
				int skipCol=0;
				for(int i=0;i<columnSize;i++){
					Column col=columns.get(i);
					int w=col.getWidth();
					if(w<1){
						skipCol++;
						continue;
					}
					double colWidth= UnitUtils.pointToPixel(w)*37.5;
					int colNum=i-skipCol;
					sheet.setColumnWidth(colNum,(short)colWidth);
					org.apache.poi.ss.usermodel.Cell cell = row.getCell(colNum);
					if(cell!=null){
						continue;
					}
					cell=row.createCell(colNum);
					Cell cellInfo=null;
					if(colCell!=null){
						cellInfo=colCell.get(col);
					}
					if(cellInfo==null){
						continue;
					}
					if(cellInfo.isForPaging()){
						continue;
					}
					XSSFCellStyle style = cellStyleContext.produceXSSFCellStyle(wb,cellInfo);
					int colSpan=cellInfo.getColSpan();
					int rowSpan=cellInfo.getRowSpan();
					int rowStart= rowNo;
					int rowEnd=rowSpan;
					if(rowSpan==0){
						rowEnd++;
					}
					rowEnd+= rowNo;
					int colStart=i;
					int colEnd=colSpan;
					if(colSpan==0){
						colEnd++;
					}
					colEnd+=i;
					for(int j=rowStart;j<rowEnd;j++){
						org.apache.poi.ss.usermodel.Row rr=sheet.getRow(j);
						if(rr==null){
							rr=sheet.createRow(j);
						}
						for(int c=colStart;c<colEnd;c++){
							org.apache.poi.ss.usermodel.Cell cc=rr.getCell(c-skipCol);
							if(cc==null){
								cc=rr.createCell(c-skipCol);
							}
							cc.setCellStyle(style);
						}
					}
					if(colSpan>1 || rowSpan>1){
						if(rowSpan>1){
							rowSpan--;
						}
						if(colSpan>1){
							colSpan--;
						}
						CellRangeAddress cellRegion=new CellRangeAddress(rowNo,(rowNo +rowSpan),i-skipCol,(i-skipCol+colSpan));
						sheet.addMergedRegion(cellRegion);
					}
					Object obj=cellInfo.getFormatData();
					if(obj!=null){
						if(obj instanceof String){
							cell.setCellValue((String)obj);
						}else if(obj instanceof Number){
							BigDecimal bigDecimal= Utils.toBigDecimal(obj);
							cell.setCellValue(bigDecimal.doubleValue());
						}else if(obj instanceof Boolean){
							cell.setCellValue((Boolean)obj);
						}else if(obj instanceof Image){
							Image img=(Image)obj;
							int width = 0;
							int height = 0;
							InputStream inputStream = null;
							try {
								inputStream= ImageUtils.base64DataToInputStream(img.getBase64Data());
								BufferedImage bufferedImage = ImageIO.read(inputStream);
								width = bufferedImage.getWidth();
								height = bufferedImage.getHeight();
							} catch (IOException e) {
								LogUtil.error(e.toString(), e);
								if (Objects.nonNull(inputStream)){
									try {
										inputStream.close();
									}catch (Exception ce){

									}
								}
							}
							try {
								inputStream= ImageUtils.base64DataToInputStream(img.getBase64Data());
								int leftMargin=0,topMargin=0;
								int wholeWidth=getWholeWidth(columns, i, cellInfo.getColSpan());
								int wholeHeight=getWholeHeight(rows, rowNo, cellInfo.getRowSpan());
								HorizontalAlignment align=style.getAlignmentEnum();
								if(align.equals(HorizontalAlignment.CENTER)){
									leftMargin=(wholeWidth-width)/2;
								}else if(align.equals(HorizontalAlignment.RIGHT)){
									leftMargin=wholeWidth-width;
								}
								VerticalAlignment valign=style.getVerticalAlignmentEnum();
								if(valign.equals(VerticalAlignment.CENTER)){
									topMargin=(wholeHeight-height)/2;
								}else if(valign.equals(VerticalAlignment.BOTTOM)){
									topMargin=wholeHeight-height;
								}

								XSSFClientAnchor anchor=(XSSFClientAnchor)creationHelper.createClientAnchor();
								byte[] bytes= IOUtils.toByteArray(inputStream);
								int pictureFormat=buildImageFormat(img);
								int pictureIndex=wb.addPicture(bytes, pictureFormat);
								anchor.setCol1(i);
								anchor.setCol2(i+colSpan);
								anchor.setRow1(rowNo);
								anchor.setRow2(rowNo +rowSpan);
								anchor.setDx1(leftMargin * Units.EMU_PER_PIXEL);
								anchor.setDx2(width * Units.EMU_PER_PIXEL);
								anchor.setDy1(topMargin * Units.EMU_PER_PIXEL);
								anchor.setDy2(height * Units.EMU_PER_PIXEL);
								drawing.createPicture(anchor, pictureIndex);
							} catch (IOException e) {
								LogUtil.error(e.toString(), e);
								if (Objects.nonNull(inputStream)){
									try {
										inputStream.close();
									}catch (Exception ce){

									}
								}
							}
						}else if(obj instanceof ChartData){
							ChartData chartData=(ChartData)obj;
							String base64Data=chartData.retriveBase64Data();
							if(base64Data!=null){
								Image img=new Image(base64Data,chartData.getWidth(),chartData.getHeight());
								int width = 0;
								int height = 0;
								InputStream inputStream = null;
								try {
									inputStream= ImageUtils.base64DataToInputStream(img.getBase64Data());
									BufferedImage bufferedImage = ImageIO.read(inputStream);
									width = bufferedImage.getWidth();
									height = bufferedImage.getHeight();
								} catch (IOException e) {
									LogUtil.error(e.toString(), e);
									if (Objects.nonNull(inputStream)){
										try {
											inputStream.close();
										}catch (Exception ce){

										}
									}
								}
								try {
									inputStream= ImageUtils.base64DataToInputStream(img.getBase64Data());
									int leftMargin=0,topMargin=0;
									int wholeWidth=getWholeWidth(columns, i, cellInfo.getColSpan());
									int wholeHeight=getWholeHeight(rows, rowNo, cellInfo.getRowSpan());
									HorizontalAlignment align=style.getAlignmentEnum();
									if(align.equals(HorizontalAlignment.CENTER)){
										leftMargin=(wholeWidth-width)/2;
									}else if(align.equals(HorizontalAlignment.RIGHT)){
										leftMargin=wholeWidth-width;
									}
									VerticalAlignment valign=style.getVerticalAlignmentEnum();
									if(valign.equals(VerticalAlignment.CENTER)){
										topMargin=(wholeHeight-height)/2;
									}else if(valign.equals(VerticalAlignment.BOTTOM)){
										topMargin=wholeHeight-height;
									}

									XSSFClientAnchor anchor=(XSSFClientAnchor)creationHelper.createClientAnchor();
									byte[] bytes= IOUtils.toByteArray(inputStream);
									int pictureFormat=buildImageFormat(img);
									int pictureIndex=wb.addPicture(bytes, pictureFormat);
									anchor.setCol1(i);
									anchor.setCol2(i+colSpan);
									anchor.setRow1(rowNo);
									anchor.setRow2(rowNo +rowSpan);
									anchor.setDx1(leftMargin * Units.EMU_PER_PIXEL);
									anchor.setDx2(width * Units.EMU_PER_PIXEL);
									anchor.setDy1(topMargin * Units.EMU_PER_PIXEL);
									anchor.setDy2(height * Units.EMU_PER_PIXEL);
									drawing.createPicture(anchor, pictureIndex);
								} catch (IOException e) {
									LogUtil.error(e.toString(), e);
									if (Objects.nonNull(inputStream)){
										try {
											inputStream.close();
										}catch (Exception ce){

										}
									}
								}
							}
						}else if(obj instanceof Date){
							cell.setCellValue((Date)obj);
						}
					}
				}
				row.setHeight((short) UnitUtils.pointToTwip(r.getRealHeight()));
			});
			sheet.setRowBreak(rowNumber.get() - 1);
			wb.write(outputStream);			
		}catch(Exception ex){
			throw new ReportComputeException(ex);
		}finally{
			wb.dispose();			
		}
	}
}
