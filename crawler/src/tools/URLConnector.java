package tools;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class URLConnector {
	private final static int TIME_OUT = 6000;

	public static HttpURLConnection getHttpConnection(String urlStr,
			String coding, String contentType, String pageLanguage,
			boolean useCaches, boolean outPut, boolean inPut,
			String connection, String userAgent, String accept,
			String cpuDataBit) {
		URL url = null;
		HttpURLConnection urlCon = null;
		try {
			url = new URL(urlStr);
			urlCon = (HttpURLConnection) url.openConnection();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (userAgent != null) {
			urlCon.setRequestProperty("User-Agent", userAgent);
		}

		if (accept != null) {
			urlCon.setRequestProperty("Accept", accept);
		}
		if (pageLanguage != null) {
			urlCon.setRequestProperty("Accept-Language", pageLanguage);
		}
		if (cpuDataBit != null) {
			urlCon.setRequestProperty("UA-CPU", cpuDataBit);
		}
		if (coding != null) {
			urlCon.setRequestProperty("Accept-Encoding", coding);// 为什么没有deflate呢
		}
		if (contentType != null) {
			urlCon.setRequestProperty("Content-type", contentType);
		}
		if (connection != null) {
			urlCon.setRequestProperty("Connection", connection); // keep-Alive，有什么用呢，你不是在访问网站，你是在采集。嘿嘿。减轻别人的压力，也是减轻自己。
		}
		urlCon.setUseCaches(useCaches);// 不要用cache，用了也没有什么用，因为我们不会经常对一个链接频繁访问。（针对程序）
		urlCon.setConnectTimeout(TIME_OUT);
		urlCon.setReadTimeout(TIME_OUT);
		urlCon.setDoOutput(outPut);
		urlCon.setDoInput(inPut);

		return urlCon;
	}

	public static HttpURLConnection getHttpConnection(String urlStr,
			String coding, String pageLanguage) {
		return getHttpConnection(
				urlStr,
				coding,
				"text/html",
				pageLanguage,
				false,
				true,
				true,
				"close",
				"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)",
				"image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/vnd.ms-powerpoint, application/vnd.ms-excel, application/msword, */*",
				null);
	}
}
