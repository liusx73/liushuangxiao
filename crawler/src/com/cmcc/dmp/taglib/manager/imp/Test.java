package com.cmcc.dmp.taglib.manager.imp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;

import tools.URLConnector;

public class Test {
	private String getPageConfigItems(String url) {
		String info = null;
		HttpURLConnection urlCon = URLConnector.getHttpConnection(url, "utf-8");
		// urlCon.setReadTimeout(30000);
		InputStreamReader in = null;
		BufferedReader br = null;
		try {
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
	}
}
