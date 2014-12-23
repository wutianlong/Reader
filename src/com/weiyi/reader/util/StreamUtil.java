package com.weiyi.reader.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * ���ܣ��������������ĸ������
 * 
 * @author κ����
 * @version 1.0
 * */
public class StreamUtil {

	public static byte[] getByteByStream(InputStream is) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int length = 0;
		while (length != -1) {
			try {
				length = is.read(b);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (length != -1) {
				baos.write(b, 0, length);
			}
		}
		if (is != null) {
			try {
				is.close();
				is = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return baos.toByteArray();
	}

	/**
	 * ͨ��URL����ȡ������InputStream
	 * 
	 * @param url
	 *            URL��ַ
	 * @return InputStream ��ȡ������InputStream
	 * */
	public static InputStream getInputStreamByUrl(String url) {
		InputStream is = null;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);// ����ʽΪget
		try {
			HttpResponse response = httpClient.execute(request);// �������Ӧ
			is = response.getEntity().getContent();// ��ȡ��ӦInputStream
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return is;

	}
}
