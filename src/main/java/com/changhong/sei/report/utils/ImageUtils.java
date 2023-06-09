package com.changhong.sei.report.utils;

import com.changhong.sei.report.enums.ImageType;
import com.changhong.sei.report.exception.ReportComputeException;
import com.changhong.sei.report.image.ChartImageProcessor;
import com.changhong.sei.report.image.ImageProcessor;
import com.changhong.sei.report.image.StaticImageProcessor;
import org.apache.commons.io.IOUtils;
import org.springframework.util.Base64Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @desc：图片工具
 * @author：zhaohz
 * @date：2020/6/30 16:09
 */
public class ImageUtils {
	private static Map<ImageType, ImageProcessor<?>> imageProcessorMap=new HashMap<ImageType, ImageProcessor<?>>();
	static{
		StaticImageProcessor staticImageProcessor=new StaticImageProcessor();
		imageProcessorMap.put(ImageType.image, staticImageProcessor);
		ChartImageProcessor chartImageProcessor=new ChartImageProcessor();
		imageProcessorMap.put(ImageType.chart, chartImageProcessor);
	}
	
	public static InputStream base64DataToInputStream(String base64Data){
		byte[] bytes= Base64Utils.decodeFromString(base64Data);
		ByteArrayInputStream inputStream=new ByteArrayInputStream(bytes);
		return inputStream;
	}
	
	@SuppressWarnings("unchecked")
	public static String getImageBase64Data(ImageType type, Object data, int width, int height){
		ImageProcessor<Object> targetProcessor=(ImageProcessor<Object>)imageProcessorMap.get(type);
		if(targetProcessor==null){
			throw new ReportComputeException("Unknow image type :"+type);
		}
		try(InputStream inputStream = targetProcessor.getImage(data)){
			if(width>0 && height>0){
				BufferedImage inputImage=ImageIO.read(inputStream);
				BufferedImage outputImage = new BufferedImage(width,height,BufferedImage.TYPE_USHORT_565_RGB);
				Graphics2D g = outputImage.createGraphics();
		        g.drawImage(inputImage, 0, 0, width,height, null);
		        g.dispose();
		        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
		        ImageIO.write(outputImage, "png", outputStream);
				try(InputStream newInputStream = new ByteArrayInputStream(outputStream.toByteArray())){
					return Base64Utils.encodeToString(IOUtils.toByteArray(newInputStream));
				}
			}else {
				byte[] bytes = IOUtils.toByteArray(inputStream);
				return Base64Utils.encodeToString(bytes);
			}
		}catch(Exception ex){
			throw new ReportComputeException(ex);
		}
	}
}
