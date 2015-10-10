package proxy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ProxyServerWriteToFile implements Runnable{
	private static final Log logger = LogFactory.getLog(ProxyServerWriteToFile.class);
	private List<Proxy> proxyList;
	
	public List<Proxy> getProxyList() {
		return proxyList;
	}

	public void setProxyList(List<Proxy> proxyList) {
		this.proxyList = proxyList;
	}

	@Override
	public void run() {
		obtainProxy(proxyList);
		writeProxy.threadList.remove(Thread.currentThread());
		if(writeProxy.ThreadSize() == 0){
			writeProxy.close();
		}
	}
	
	public void obtainProxy(List<Proxy> proxyList){
		boolean isAvailable = false;
		Proxy proxy = null;
		int size = proxyList.size();
		for (int i = 0; i < size ; i++) {
			proxy = proxyList.get(i);
			isAvailable = isAvailable(proxy, 0);
			if(isAvailable){
				logger.info("success--proxyNum:" + size + ";currentNum:" + (i+1) + ";proxy:" + proxy.address());
				writeProxy.write(proxy);
			}else{
				logger.info("failure--proxyNum:" + size + ";currentNum:" + (i+1) + ";proxy:" + proxy.address());
			}
		}
	}
	
	private String urlStr = "http://www.baidu.com";
	
	private boolean isAvailable(Proxy proxy, int times){
		HttpURLConnection urlCon = null;
		try {
			URL url;
			url = new URL(urlStr);
			urlCon = (HttpURLConnection) url.openConnection(proxy);
			urlCon.setReadTimeout(2000);
			urlCon.setConnectTimeout(2000);
			if (HttpURLConnection.HTTP_OK != urlCon.getResponseCode()) {
				if (times < 3) {
					return isAvailable(proxy, ++times);
				}else{
					return false;
				}
			}
			
			urlCon.disconnect();
			urlCon = null;
		} catch (SocketTimeoutException e) {
//			logger.error(proxy.address() + "-" + e.getMessage());
			if(times < 3){
				return isAvailable(proxy, ++times);
			}else{
				return false;
			}
		} catch (Exception e) {
//			logger.error(proxy.address() + "-" + e.getMessage());
			if(times < 3){
				return isAvailable(proxy, ++times);
			}else{
				return false;
			}
		} finally {
			if(urlCon != null){
				urlCon.disconnect();
			}
		}
		return true;
	}
	
	
	
	private static List<Proxy> crawlProxy2() {
		Document document = null;
		Elements elements = null;
		List<Proxy> list = new LinkedList<Proxy>();
			document = getDocument("http://www.kuaidaili.com/free/inha/");
			if(document == null){
				return list;
			}
			elements = document.select("li >a");
			String pageStr = elements.last().text();
			int page = Integer.parseInt(pageStr);
			for (int i = 1; i <= page; i++) {
				String url = "http://www.kuaidaili.com/free/inha/" + i;
				Document document1 = getDocument(url);
				System.out.println("page:" + i + ";url:" + url);
				if(document == null){
					continue;
				}
				list.addAll(getProxyKuaidaili(document1));
			}
		return list;
	}
	
	private static List<Proxy> getProxyKuaidaili(Document document) {
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

	
	private static List<Proxy> crawlProxyXiciNN() {
		String url = "http://www.xicidaili.com/nn/";
		return crawlProxyXici(url);
	}
	
	
	private static List<Proxy> crawlProxyXici(String url){
		Document document = null;
		List<Proxy> list = new LinkedList<Proxy>();
		document = getDocument(url);
		if(document == null){
			return list;
		}
		Elements pager = document.select("div[class*=pagination] > a");
		String totalPageStr = pager.get(pager.size() -2).text();
		int totalPage = Integer.parseInt(totalPageStr);
		for (int i = 1; i <= totalPage; i++) {
			String urlStr = url + i;
			Document document1 = getDocument(urlStr);
			if(document == null){
				continue;
			}
			System.out.println("page:" + i + ";url:" + url);
			getProxyXici(document1,list);
		}
		return list;
	}
	
	private static List<Proxy> crawlProxyXiciNt() {
		String url = "http://www.xicidaili.com/nt/";
		return crawlProxyXici(url);
	}
	
	private static Document getDocument(String url){
		Document document = null;
		int tryTimes = 0;
		while(document == null && tryTimes < 5){
			try {
				document = Jsoup.connect(url).userAgent(BrowserUtil.getUserAgent()).timeout(6000).get();
			} catch (Exception e) {
				e.printStackTrace();
			}
			tryTimes ++;
		}
		
		return document;
	}

	private static List<Proxy> getProxyXici(Document document,List<Proxy> list) {
		String ipRegex = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
		Elements elements = document.select("table[id=ip_list] > tbody > tr");
		Proxy proxy = null;
		for (Element e : elements) {
			Elements nodes = e.select("td");
			if (nodes.size() > 3 && Pattern.matches(ipRegex, nodes.get(2).text().trim())) {
				proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(nodes.get(2).text().trim(), Integer.parseInt(nodes.get(3).text().trim())));
				if(proxy != null && !list.contains(proxy)){
					list.add(proxy);
				}
			}
		}
		return list;
	}
	private static List<Proxy> loadProxy() {
		List<Proxy> list = new ArrayList<Proxy>();
		BufferedReader br = null;
		Proxy proxy = null;
		try {
			br = new BufferedReader(new FileReader(new File(ProxyServerUtil.getClassPath() + "proxy/proxy1.txt")));
			String line = null;
			int count = 0;
			while ((line = br.readLine()) != null) {
				count ++;
				String[] s = line.split(":");
				proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(s[0].trim(), Integer.parseInt(s[1].trim())));
				if(proxy != null && !list.contains(proxy)){
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
	
	private static List<Proxy> getPangchong(){
		List<Proxy> list = new LinkedList<Proxy>();
		String[] url = {
				"http://pachong.org/area/short/name/cn.html",
				"http://pachong.org/area/city/name/内江�?.html"
		};
		getPachong(url,list);
		return list;
	}
	
	private static List<Proxy> getPachong(String[] url, List<Proxy> list) {
		Document document = null;
		
		for (int i = 0; i < url.length; i++) {
			document = getDocument(url[i]);
			if(document != null){
				getPachong(document, list);
			}
		}
		return list;
	}
	private static void getPachong(Document document,List<Proxy> list){
		String ipRegex = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
		Proxy proxy = null;
		Elements elements = document.select("table[class=tb] > tbody > tr");
		for (Element e : elements) {
			Elements nodes = e.select("td");
			if (nodes.size() > 3 && Pattern.matches(ipRegex, nodes.get(1).text().trim())) {
				System.out.println(nodes.get(2));
				proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(nodes.get(1).text().trim(), Integer.parseInt(nodes.get(2).ownText().trim())));
				if(proxy != null && !list.contains(proxy)){
					System.out.println(proxy);
					list.add(proxy);
				}
			}
		}
	}
	
	private static List<Proxy> ip004(){
		String url = "http://ip004.com/proxycate_0.html";
		Document document = null;
		List<Proxy> list = new LinkedList<Proxy>();
		document = getDocument(url);
		if(document == null){
			return list;
		}
		Elements pager = document.select("div[class*=pagination]").select("a");
		String totalPageStr = pager.get(pager.size() -2).text();
		int totalPage = getInt(totalPageStr);
		int count = 0;
		while( count < 100 && document != null){
			ip004proxy(document, list);
			totalPage --;
			url = "http://ip004.com/proxycate_"+(totalPage)+".html"; 
			document =  getDocument(url);
			System.out.println(url);
			count ++;
		}
		return list;
	}
	
	private static int getInt(String str){
		String regex="[^0-9]";
		str=str.replaceAll(regex, "");
		if(StringUtils.isBlank(str)){
			return 0;
		}
		int d=Integer.valueOf(str);
		return d;
	}
	
	private static void ip004proxy(Document document, List<Proxy> list){
		Elements items = document.select("table[id=proxytable]").select("input[type=hidden]");
		Proxy proxy = null;
		for (int i = 0; i < items.size();i += 2) {
			proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(items.get(i).attr("value"), Integer.parseInt(items.get(i + 1).attr("value"))));
			if(proxy != null && !list.contains(proxy)){
				list.add(proxy);
			}
		}
	}
	
	public static void main(String[] args) {
//		FIXME
		List<Proxy> proxyList =  null;
//		proxyList =  loadProxy();
		proxyList = ip004();
//		proxyList = getPangchong();
		int currentThreadNum = 20;
		int count = proxyList.size();
		if(count < 100){
			currentThreadNum = 4;
		}
		int threadItemsSize = count/currentThreadNum;
		
		int start = 0;
		int end = 0;
		logger.info("count:" + count);
		ProxyServerWriteToFile proxyServerWriteToFile  = null;
		for (int i = 0; i < currentThreadNum; i++) {
			start = i * threadItemsSize;
			if(i == (currentThreadNum -1)){
				end = count;
			}else{
				 end = (i + 1) * threadItemsSize;
			}
			proxyServerWriteToFile = new ProxyServerWriteToFile();
			proxyServerWriteToFile.setProxyList(proxyList.subList(start, end));
			Thread t = new Thread(proxyServerWriteToFile);
			t.start();
			writeProxy.threadList.add(t);
		}
		
//		System.out.println(ProxyServerWriteToFile.class.getClassLoader().getResource("").getPath());
	}
}

class writeProxy{
	
	public static List<Thread> threadList = new ArrayList<Thread>();
	
	public static BufferedWriter bufferedWriter = null;
	private static FileWriter fileWriter = null;
	
	static{
		try {
			String path = ProxyServerUtil.getClassPath() + "/proxy/proxy4.txt";
			File file = new File(path);
			if(!file.exists()){
				file.createNewFile();
			}
			System.out.println(path);
			fileWriter = new FileWriter(file,true);
			bufferedWriter = new BufferedWriter(fileWriter);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized static void write(Proxy proxy) {
		String address = proxy.address().toString();
		write(address.replaceFirst("/", ""));
	}

	public synchronized static void write(String s) {
		try {
			if(s != null){
				bufferedWriter.write(s);
				bufferedWriter.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized static int ThreadSize(){
		return threadList.size();
	}
	
	public static void close(){
		try {
			if (bufferedWriter != null) {
				bufferedWriter.close();
				bufferedWriter = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if (bufferedWriter != null) {
					bufferedWriter.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
