package com.weiyi.reader.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.util.EncodingUtils;

import android.content.Context;

/**
 * 字符串操作工具
 * 
 * @author 魏艺荣
 * @version 1.0
 * */
public class StringUtil {
	/**
	 * 半角转换为全角
	 * 
	 * @param input
	 * @return
	 */
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	/**
	 * 去除特殊字符或将所有中文标号替换为英文标号
	 * 
	 * @param str
	 * @return
	 */
	public static String stringFilter(String str) {
		str = str.replaceAll("【", "[").replaceAll("】", "]")
				.replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
		String regEx = "[『』]"; // 清除掉特殊字符
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}
	/**
	 * 从asset目录读取文件
	 * @param fileName 文件名
	 * */
	public static String getContentFromAsset(String fileName,Context context){
		String resultStr = "";
		try {
			InputStreamReader isr = new InputStreamReader(context.getResources().getAssets().open(fileName),"GB2312");
			BufferedReader br = new BufferedReader(isr);
			String line="";
			while((line = br.readLine())!=null){
				resultStr+=line;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return resultStr;
	}
}
