package proxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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

	private static List<Proxy> proxyList = ProxyServerUtil.loadProxy();
	private static int[] deleteProxySign = new int[proxyList.size()];// int 默认�?0 设为1 表示 下标�?�? proxy 不能�?
	// private static List<Proxy> proxyList = null;
	private static int index = 0;

	private static List<Proxy> loadProxy() {
		List<Proxy> list = new ArrayList<Proxy>();
		BufferedReader br = null;
		Proxy proxy = null;
		try {
			br = new BufferedReader(new FileReader(new File(getClassPath() + "proxy/proxy.txt")));
			String line = null;
			int count = 0;
			while ((line = br.readLine()) != null) {
				count++;
				String[] s = line.split(":");
				proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(s[0].trim(), Integer.parseInt(s[1].trim())));
				if (proxy != null && !list.contains(proxy)) {
					list.add(proxy);
				}
			}
			System.out.println(count);
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
		logger.info("proxy list size :" + list.size());
		return list;
	}

	public static int getProxyListSize() {
		return proxyList.size();
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

	private static void nextIndex() {
		index++;
		if (index >= proxyList.size()) {
			index = 0;
		}
	}

	private static List<Proxy> getProxy(Document document) {
		List<Proxy> list = new LinkedList<Proxy>();
		String ipRegex = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
		Proxy proxy = null;
		Elements elements = document.select("tr");
		for (Element e : elements) {
			Elements nodes = e.select("td");
			if (nodes.size() > 3 && Pattern.matches(ipRegex, nodes.get(1).text().trim())) {
				proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(nodes.get(1).text().trim(), Integer.parseInt(nodes.get(2).text().trim())));
				if (proxy != null && !list.contains(proxy)) {
					list.add(proxy);
				}
			}
		}
		return list;
	}

	private static int CAN_USE = 0;
	
	public static Proxy getProxy() {
		int count = 0;
		Proxy proxy = null;
		while (count < 5 && proxy == null) {
			proxy = proxy();
		}
		return proxy;
	}
	
	public synchronized static Proxy proxy() {
		if (!proxyList.isEmpty()) {
			int useIndex = -1;
			int traversed = 0;
			while (useIndex == -1 && traversed < deleteProxySign.length) {
				nextIndex();
				traversed++;
				if (deleteProxySign[index] == CAN_USE) {
					useIndex = index;
				}
			}
			if (useIndex != -1) {
				return proxyList.get(useIndex);
			}

		}

		if (proxyList.isEmpty()) {
			proxyList = ProxyServerUtil.loadProxy();
			deleteProxySign = new int[proxyList.size()];
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
		int index = proxyList.indexOf(proxy);
		deleteProxySign[index] = 1;
	}

	public static void changeProxy() {
		Proxy proxy = getProxy();
		System.setProperty("http.proxyHost", ((InetSocketAddress) proxy.address()).getHostName());
		System.setProperty("http.proxyPort", "" + ((InetSocketAddress) proxy.address()).getPort());
	}

	public static void main(String[] args) {
		Proxy proxy = getProxy();
		System.out.println(proxy);
	}
	
	public static String getClassPath(){
		String classPath = ProxyServerUtil.class.getClassLoader().getResource("").getPath();
		return classPath;
	}
}
