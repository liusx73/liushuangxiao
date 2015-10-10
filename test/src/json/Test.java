package json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Date;

import json.org.JSONObject;


public class Test {
	private static String getPageConfigItems(String urlStr) {
		String info = null;
		
		// urlCon.setReadTimeout(30000);
		InputStreamReader in = null;
		BufferedReader br = null;
		URL url = null;
		HttpURLConnection urlCon = null;
		try {
			
			url = new URL(urlStr);
			urlCon = (HttpURLConnection) url.openConnection();
			urlCon.connect();
			System.out.println(urlCon.getHeaderField("cookie"));
			in = new InputStreamReader(urlCon.getInputStream(), "utf-8");
			br = new BufferedReader(in);
			String readLine = null;
			while ((readLine = br.readLine()) != null && info == null) {
				int index = readLine.indexOf("g_page_config");
				if (index > -1) {
					readLine = readLine.trim();
					info = readLine.substring(readLine.indexOf("=") + 1,
							readLine.length() - 1);
				}
			}
			in.close();
			in = null;
			br.close();
			br = null;
			urlCon.disconnect();
			urlCon = null;
		} catch (SocketTimeoutException e) {
			info = null;
			e.printStackTrace();
		} catch (IOException e) {
			info = null;
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (br != null) {
					br.close();
				}
				if (urlCon != null) {
					urlCon.disconnect();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return info;
	}
	public static void main(String[] args) {
		
		String url = "https://s.taobao.com/search?q=%E5%86%AC%E4%B8%8A%E6%96%B0&commend=all&ssid=s5-e&search_type=item&sourceId=tb.index&spm=a21bo.7724922.8452-taobao-item.2&initiative_id=tbindexz_20151009";
//		url = "https://s.taobao.com/search?q=%E5%86%AC%E4%B8%8A%E6%96%B0&commend=all&ssid=s5-e&search_type=item&sourceId=tb.index&spm=a21bo.7724922.8452-taobao-item.2&initiative_id=tbindexz_20151009&filter=reserve_price%5B%2C0%5D&cps=yes&cat=50456025&loc=%E5%B9%BF%E8%A5%BF";
		String s = getPageConfigItems(url);
		s = "{\"sk\\\"dfk\"\" : \"111\"}";
		JSONObject j = new JSONObject(s);
//		JSONObject j = JSONObject.fromObject(s);
		Date start = new Date();
		System.out.println(j.getJSONObject("mods")
				.getJSONObject("itemlist")
				.getJSONObject("data")
//				.getJSONArray("auctions").size()
				
		);
		
		System.out.println(j.getJSONObject("mods")
				.getJSONObject("pager")
//				.containsKey("data")
				);
		System.out.println(new Date().getTime() - start.getTime());
	}
}
