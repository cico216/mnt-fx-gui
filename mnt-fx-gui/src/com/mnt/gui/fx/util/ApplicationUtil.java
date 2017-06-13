package com.mnt.gui.fx.util;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

/**
 * 
 * <p>
 * 应用工具类
 * </p>
 * @author    mnt.cico
 * @version  2016年5月17日 下午7:41:16 mnt.cico .
 * @since   FX8.0
 */
public class ApplicationUtil {
	
	/**
	 * 桌面宽度
	 */
	private double desktopWidth;
	
	/**
	 * 桌面高度
	 */
	private double desktopHeight;
	
	/**
	 * 屏幕大小
	 */
	private Dimension screenSize;
	
	private Insets scrInsets;
	
	private ApplicationUtil()
	{
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		final Insets scrInsets = Toolkit.getDefaultToolkit().getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());
		this.scrInsets = scrInsets;
		desktopHeight = screenSize.getHeight() - scrInsets.top - scrInsets.bottom;
		desktopWidth = screenSize.getWidth() - scrInsets.left - scrInsets.right;
	}
	
	public double getDesktopWidth() {
		return desktopWidth;
	}

	public double getDesktopHeight() {
		return desktopHeight;
	}

	
	public Insets getScrInsets() {
		return scrInsets;
	}

	public void setScrInsets(Insets scrInsets) {
		this.scrInsets = scrInsets;
	}

	/**
	 * 
	 * <p>
	 * 获取当前屏幕截屏
	 * </p>
	 * @create mnt.cico
	 * @return
	 */
	public InputStream getCurrScreenshot(){
		InputStream result = null;
		Robot robot;
		ByteArrayOutputStream bs = new ByteArrayOutputStream();  
        ImageOutputStream imOut; 
		try {
			robot = new Robot();
			BufferedImage image =  robot.createScreenCapture(new Rectangle(screenSize));
			imOut = ImageIO.createImageOutputStream(bs); 
			ImageIO.write(image, "png", imOut); 
			result = new ByteArrayInputStream(bs.toByteArray()); 
		} catch (AWTException | IOException e) {
			e.printStackTrace();
		} 
		return result;
	}
	

	public static ApplicationUtil getInstance()
	{
		return SingleInit.INSTANCE;
	}
	
	private static class SingleInit
	{
		private static ApplicationUtil INSTANCE = new ApplicationUtil();
	}
}
