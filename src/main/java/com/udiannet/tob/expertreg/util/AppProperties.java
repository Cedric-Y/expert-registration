package com.udiannet.tob.expertreg.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 加载配置文件的工具类
 */
public class AppProperties
{
	/**
	 * 加载配置文件里的配置信息
	 */
	public static String getPropertiesValueByKey(String key)
	{
		//获取 jar 包所在目录
		String jarPath = (new File(AppProperties.class.getProtectionDomain().getCodeSource().getLocation().getFile())).getParent();
		String filePath = jarPath + System.getProperty("file.separator") + "app.properties";

		Properties pps = new Properties();
		InputStream in = null;
		try
		{
			//这个方式是获取在 jar 包内的根目录下的配置文件方式
			in = AppProperties.class.getClassLoader().getResourceAsStream("app.properties");

			//这个方式是获取与 jar 包（在 jar 包外）同路径下的配置文件方式
//			in = new BufferedInputStream(new FileInputStream(filePath));

			//这个方法可以把配置文件放在工程的 resources 目录下
//			in = new BufferedInputStream(new FileInputStream(AppProperties.class.getResource("/").getPath() + "app.properties"));
			
			pps.load(in);
			return pps.getProperty(key);
		}
		catch (Exception e)
		{
			try
			{
				if (in != null)
					in.close();
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
				in = null;
			}
			return null;
		}
		finally
		{
			try
			{
				if (in != null)
					in.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
				in = null;
			}
		}
	}
}
