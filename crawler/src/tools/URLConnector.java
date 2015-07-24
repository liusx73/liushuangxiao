package tools;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class URLConnector {
	private final static int TIME_OUT = 6000;
	private final static String contentType = "text/html";
	private final static String pageLanguage = "zh-cn";
	private final static String connection = "close";

	public static HttpURLConnection getHttpConnection(String urlStr,
			String coding, String contentType, String pageLanguage,
			boolean useCaches, boolean outPut, boolean inPut,
			String connection) {
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

		if (pageLanguage != null) {
			urlCon.setRequestProperty("Accept-Language", pageLanguage);
		}
		if (coding != null) {
			urlCon.setRequestProperty("Accept-Encoding", coding);
		}
		if (contentType != null) {
			urlCon.setRequestProperty("Content-type", contentType);
		}
		if (connection != null) {
			urlCon.setRequestProperty("Connection", connection);
		}
		urlCon.setUseCaches(useCaches);
		urlCon.setConnectTimeout(TIME_OUT);
		urlCon.setReadTimeout(TIME_OUT);
		urlCon.setDoOutput(outPut);
		urlCon.setDoInput(inPut);

		return urlCon;
	}

	public static HttpURLConnection getHttpConnection(String urlStr,
			String coding, String pageLanguage) {
		return getHttpConnection(urlStr, coding, contentType, pageLanguage,
				false, true, true, connection);
	}

	public static HttpURLConnection getHttpConnection(String urlStr,
			String coding) {
		return getHttpConnection(urlStr, coding, contentType, pageLanguage,
				false, true, true, "keep-alive");
	}
}
