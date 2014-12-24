package com.weiyi.reader.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class StreamUtil {

	public static byte[] getByteByStream(InputStream is) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int length = 0;
		while (length != -1) {
			try {
				length = is.read(b);
			} catch (IOException e) {
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
				e.printStackTrace();
			}

		}
		return baos.toByteArray();
	}

	public static InputStream getInputStreamByUrl(String url) {
		InputStream is = null;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);// ����ʽΪget
		try {
			HttpResponse response = httpClient.execute(request);// �������Ӧ
			is = response.getEntity().getContent();// ��ȡ��ӦInputStream
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is;

	}
}
