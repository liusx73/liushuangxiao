package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 *
 * @Project: taglib-crawler-framework
 * @File: ProxyServerUtil.java
 * @Date: 2015�?6�?29�?
 * @Author: wangmeixi
 * @Copyright: 版权�?�? (C) 2015 中国移动 杭州研发中心.
 *
 * @注意：本内容仅限于中国移动内部传阅，禁止外泄以及用于其他的商业目�?
 */

public class ProxyServerUtil {

	private static final Log logger = LogFactory.getLog(ProxyServerUtil.class);

	private static List<Proxy> proxyList = null;

	private static List<Proxy> loadProxy() {
		List<Proxy> list = new LinkedList<Proxy>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "proxy")));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] s = line.split(";");
				list.add(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(s[0].trim(), Integer.parseInt(s[1].trim()))));
			}
		} catch (FileNotFoundException e) {
			logger.error(ProxyServerUtil.class, e);
		} catch (IOException e) {
			logger.error(ProxyServerUtil.class, e);
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				logger.error(ProxyServerUtil.class, e);
			}
		}
		logger.info("proxy list size :" + proxyList.size());
		return list;
	}

	private static List<Proxy> crawlProxy() {
		Document document = null;
		Elements elements = null;
		List<Proxy> list = new LinkedList<Proxy>();
		try {
			document = Jsoup.connect("http://www.proxy.com.ru/gaoni/").timeout(30000).get();
			elements = document.select("tr");
			for (Element e : elements) {
				Elements nodes = e.select("td");
				if (nodes.text().contains("�?") && nodes.text().contains("�?")) {
					Elements links = nodes.select("a");
					for (Element link : links) {
						if (link.text().contains("�?") && link.text().contains("�?")) {
							String url = "http://www.proxy.com.ru/gaoni" + link.attr("href");
							Document document1 = Jsoup.connect(url).timeout(30000).get();
							list.addAll(getProxy(document1));
						}
					}
					break;
				}
			}
		} catch (Exception e) {
			logger.error(ProxyServerUtil.class, e);
			return list;
		}
		return list;
	}

	private static List<Proxy> getProxy(Document document) {
		List<Proxy> list = new LinkedList<Proxy>();
		String ipRegex = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
		Elements elements = document.select("tr");
		for (Element e : elements) {
			Elements nodes = e.select("td");
			if (nodes.size() > 3 && Pattern.matches(ipRegex, nodes.get(1).text().trim())) {
				list.add(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(nodes.get(1).text().trim(), Integer.parseInt(nodes.get(2).text().trim()))));
			}
		}
		return list;
	}

	public static Proxy getProxy() {
		if (!proxyList.isEmpty()) {
			Random random = new Random(System.currentTimeMillis());
			return proxyList.get(Math.abs(random.nextInt()) % proxyList.size());
		}
		return null;
	}

	private static List<Proxy> crawlProxy2() {
		Document document = null;
		Elements elements = null;
		List<Proxy> list = new LinkedList<Proxy>();
		try {
			document = Jsoup.connect("http://www.kuaidaili.com/free/inha/").timeout(30000).get();
			elements = document.select("li >a");
			String pageStr = elements.last().text();
			int page = Integer.parseInt(pageStr);
			for (int i = 1; i <= page; i++) {
				String url = "http://www.kuaidaili.com/free/inha/" + i;
				Document document1 = Jsoup.connect(url).timeout(30000).get();
				list.addAll(getProxy2(document1));
			}
		} catch (IOException e) {
			logger.error(ProxyServerUtil.class, e);
			return list;
		}
		return list;
	}

	private static List<Proxy> getProxy2(Document document) {
		List<Proxy> list = new LinkedList<Proxy>();
		String ipRegex = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
		Elements elements = document.select("tr");
		for (Element e : elements) {
			Elements nodes = e.select("td");
			if (nodes.size() > 3 && Pattern.matches(ipRegex, nodes.get(0).text().trim())) {
				list.add(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(nodes.get(0).text().trim(), Integer.parseInt(nodes.get(1).text().trim()))));
			}
		}
		return list;
	}

	public static void removeProxy(Proxy proxy) {
		proxyList.remove(proxy);
		if (proxyList.isEmpty()) {
			proxyList = ProxyServerUtil.crawlProxy();
		}
	}
	
	private BufferedWriter bufferedWriter = initBufferedWriter();
	
	public void obtainProxy(){
		List<Proxy> proxyList = ProxyServerUtil.crawlProxy2();
		boolean isAvailable = false;
		for (Proxy proxy : proxyList) {
			isAvailable = isAvailable(proxy, 0);
			if(isAvailable){
				writeProxy(proxy);
			}
		}
	}
	
	private void writeProxy(Proxy proxy){
		try {
			bufferedWriter.write(proxy.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private BufferedWriter initBufferedWriter(){
		BufferedWriter bufferedWriter = null;
		FileWriter fileWriter = null;
		try {
			File file = new File(System.getProperty("user.dir") + System.getProperty("file.separator") + "proxy");
			if(file.exists()){
				file.delete();
			}else{
				file.createNewFile();
			}
			fileWriter = new FileWriter(file);
			bufferedWriter = new BufferedWriter(fileWriter);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bufferedWriter;
	}
	
	private String urlStr = "http://www.baidu.com";
	private boolean isAvailable(Proxy proxy, int times){
		InputStreamReader in = null;
		BufferedReader br = null;
		HttpURLConnection urlCon = null;
		try {
			URL url;
			url = new URL(urlStr);
			proxy = ProxyServerUtil.getProxy();
			urlCon = (HttpURLConnection) url.openConnection(proxy);
			
			urlCon.setReadTimeout(6000);
			
			if (HttpURLConnection.HTTP_OK != urlCon.getResponseCode()) {
				logger.info("链接无效�?" + urlStr);
				logger.info("http code : " + urlCon.getResponseCode());
				if (times < 5) {
					return isAvailable(proxy, times);
				}else{
					return false;
				}
			}
			urlCon.connect();
			in = new InputStreamReader(urlCon.getInputStream(), "utf-8");
			br = new BufferedReader(in);
			br.readLine();
			in.close();
			in = null;
			br.close();
			br = null;
			urlCon.disconnect();
			urlCon = null;
		} catch (Exception e) {
			logger.warn(urlStr + "-" + e.getMessage());
			if(times < 5){
				return isAvailable(proxy, times);
			}else{
				return false;
			}
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
				logger.warn(urlStr + "-" + e.getMessage());
			}
		}
		return true;
	}
	
	public static void changeProxy() {
		Proxy proxy = getProxy();
		System.setProperty("http.proxyHost", ((InetSocketAddress) proxy.address()).getHostName());
		System.setProperty("http.proxyPort", "" + ((InetSocketAddress) proxy.address()).getPort());
	}
	
	
	public static void main(String[] args) {
		System.out.println(System.getProperty("user.dir") + System.getProperty("file.separator") + "proxy");
//		ProxyServerUtil proxyServerUtil = new ProxyServerUtil();
//		proxyServerUtil.obtainProxy();
	}
}
