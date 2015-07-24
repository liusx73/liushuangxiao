package com.cmcc.dmp.taglib.manager.imp;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import tools.JSONUtils;
import tools.URLConnector;

import com.cmcc.dmp.taglib.entity.Category;
import com.cmcc.dmp.taglib.entity.Page;
import com.cmcc.dmp.taglib.manager.CrawlTask;

public class TaobaoCrawlTask extends CrawlTask {
	// https://s.taobao.com/search?q=&js=1&stats_click=search_radio_all%3A1&initiative_id=staobaoz_20150719&ie=utf8&cps=yes&cat=
	private Category category;
	private String url;
	private String coding;

	public static FileOutputStream fos;
	public static int total = 0;
	public static Set<Integer> categoryIds = new HashSet<Integer>();
	public static Set<String> errorUrl = new HashSet<String>();

	public TaobaoCrawlTask(String url, String coding) {
		super();
		this.url = url;
		this.coding = coding;
	}

	public TaobaoCrawlTask(String url) {
		super();
		this.url = url;
		this.coding = UTF_8;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	protected void crawl() {
		if (this.category == null) {
			this.category = new Category("所有分类", null);
		}
		crawl(url, coding, category, 0);
		Monitor.stop();
	}

	void crawl(final String url, final String coding, Category category,
			int level) {
		level ++;
		if(level < 3){
			String info = getPageConfig(url, category);
			if (!StringUtils.isBlank(info)) {
				category.setLevel(level);
				System.out.println(category.getName() + "--" + category.getId()
						+ "--" + category.getLevel());
				analyze(category, info);
				List<Category> childs = category.getChilds();
				for (int index = 0; index < childs.size(); index++) {
					crawl(url, coding, childs.get(index), level);
				}
			}
		}else{
			category.setLevel(level);
			System.out.println(category.getName() + "--" + category.getId()
					+ "--" + category.getLevel());
			total ++;
		}
		

	}

	private void analyze(Category category, String info) {
		List<Category> categories = getCategorys(info);
		int countItems = getTotalCount(info);
		category.setCountItems(countItems);
		category.setChilds(categories);
		
	}

	private void startDetail(double lowPrise, double hightPrise,
			final String url, Category category) {
		String urlc = url + "&filter=reserve_price%5B" + lowPrise + "%2C"
				+ hightPrise + "%5D";
		String info = getPageConfigItems(urlc, category);
		if (!StringUtils.isBlank(info)) {
			int totalCount = getTotalCount(info);
			if (totalCount <= 4400) {
				total += totalCount;
				Page page = getPage(info);
//				startDetail(page, urlc + "&cat=" + category.getId());
			} else {
				double temp = hightPrise - lowPrise;
				if (temp <= 1) {
					Page page = getPage(info);
//					startDetail(page, urlc + "&cat=" + category.getId());
				} else {
					startDetail(lowPrise, lowPrise + temp / 2, urlc, category);
					startDetail(lowPrise + temp / 2, hightPrise, urlc, category);
				}
			}
		}

	}

//	private void startDetail(Page page, String url) {
//		TaobaoDetailCrawlTask t = new TaobaoDetailCrawlTask(url, page);
//		Thread thread = new Thread(t);
//		TaobaoCrawlTask.addThread(thread);
//		startThread(waitThreads, threads);
//	}

	private List<Category> getCategorys(String info) {
		List<Category> categories = new ArrayList<Category>();
		int commonIndex = info.indexOf("\"common\"");
		if (commonIndex > -1) {
			String categoryString = JSONUtils.getValue(info, "\"sub\"",
					commonIndex, "[{", "}]");
			JSONArray categoriesJA = JSONArray.fromObject(categoryString);
			Category category = null;
			JSONObject categoryJO = null;
			for (int i = 0; i < categoriesJA.size(); i++) {
				category = new Category();
				categoryJO = categoriesJA.getJSONObject(i);
				category.setId(categoryJO.getInt("value"));
				category.setName(categoryJO.getString("text"));
				categories.add(category);
			}
		}else{
			total++;
		}
		return categories;
	}

	private int getTotalCount(String info) {
		return JSONUtils.getIntValue(info, "totalCount");
	}

	private double getMaxPrise(String info) {
		return JSONUtils.getDoubleValue(info, "view_price");
	}

	private Page getPage(String info) {
		int pageSize = JSONUtils.getIntValue(info, "pageSize");
		int currentPage = JSONUtils.getIntValue(info, "totalPage");
		Page page = new Page(pageSize, currentPage);
		return page;
	}

	private String getPageConfig(String url, Category category) {
		Integer categoryId = category.getId();
		boolean isNotExist = categoryIds.add(categoryId);
		String info = null;
		if (isNotExist) {
			url = categoryId == null ? url : url + categoryId.toString();
			HttpURLConnection urlCon = URLConnector.getHttpConnection(url,
					UTF_8);
			// urlCon.setReadTimeout(30000);
			InputStreamReader in = null;
			BufferedReader br = null;
			try {
				urlCon.connect();
				in = new InputStreamReader(urlCon.getInputStream(), UTF_8);
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
				errorUrl.add(url);
				System.out.println(url + "-" + e.getMessage());
			} catch (IOException e) {
				info = null;
				System.out.println(url + "-" + e.getMessage());
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		return info;
	}

	private String getPageConfigItems(String url, Category category) {
		Integer categoryId = category.getId();
		String info = null;
		url = categoryId == null ? url : url + "&cat=" + categoryId.toString();
		HttpURLConnection urlCon = URLConnector.getHttpConnection(url, UTF_8);
		// urlCon.setReadTimeout(30000);
		InputStreamReader in = null;
		BufferedReader br = null;
		try {
			urlCon.connect();
			System.out.println(urlCon.getHeaderField("cookie"));
			in = new InputStreamReader(urlCon.getInputStream(), UTF_8);
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
			errorUrl.add(url);
			System.out.println(url + "-" + e.getMessage());
		} catch (IOException e) {
			info = null;
			System.out.println(url + "-" + e.getMessage());
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return info;
	}

	// private String readStream(InputStream inputStream) {
	// StringBuffer sb = new StringBuffer();
	//
	// return sb.toString();
	// }
	//
	// private final static char[] G_PAGE_CONFIG =
	// "g_page_config".toCharArray();

	public static List<Thread> threads = new ArrayList<Thread>();
	public static List<Thread> waitThreads = new ArrayList<Thread>();
	public final static String detailUrl = "https://s.taobao.com/search?q=&js=1&stats_click=search_radio_all%3A1&initiative_id=staobaoz_20150719&ie=utf8&cps=yes&bcoffset=1&sort=price-desc";
	public final static String UTF_8 = "UTF-8";

	public static void main(String[] args) {
//		String url = "http://s.taobao.com/list?spm=a217f.7278017.1997728653.2.Rf50JH&style=grid&seller_type=taobao&cps=yes&cat=";
		String url = "https://list.taobao.com/itemlist/default.htm?_input_charset=utf-8&json=on&sort=biz30daydata-key=s&cat=51712001&s=0&pSize=96&data-value=96&callback=jsonp20&_ksTS=1437731744965_19";

//		Category category = new Category("所有分类", null);
//		TaobaoCrawlTask tct = new TaobaoCrawlTask(url);
//		tct.setCategory(category);
//		Thread t = new Thread(tct);
//		t.start();
//		
//		Monitor m = new Monitor();
//		Thread mt = new Thread(m);
//		mt.start();
		
		HttpURLConnection urlCon = URLConnector.getHttpConnection(url, UTF_8);
		urlCon.setRequestProperty("Cookie", "t="+getUUID()+";v=0;isg="+ getUUID() + ";cookie2="+getUUID());
		urlCon.setRequestProperty("Host","list.taobao.com");
		Map<String, List<String>> a = urlCon.getHeaderFields();
		
		Iterator<Entry<String, List<String>>> i = a.entrySet().iterator();
		while (i.hasNext()) {
			Entry<String, List<String>> e = i.next();
			System.out.print(e.getKey());
			for (int j = 0; j < e.getValue().size(); j++) {
				System.out.print("--" + e.getValue().get(j));
			}
			System.out.println();
		}
		
		
		InputStreamReader in = null;
		BufferedReader br = null;
		try {
			in = new InputStreamReader(urlCon.getInputStream(), "gbk");
			br = new BufferedReader(in);
			String readLine = null;
			int index = 0;
			while ((readLine = br.readLine()) != null && index++ < 30) {
				System.out.println(readLine);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		
		
		
		
		
		
		
		
		
		
		
		
		
//		String info = tct.getPageConfig(url, category);
//		tct.analyze(category, info);
//		System.out.println(category.getName() + "--" + category.getId() + "--"
//				+ category.getCountItems());
//		for (Category c : category.getChilds()) {
//			TaobaoCrawlTask tctThread = new TaobaoCrawlTask(url);
//			tctThread.setCategory(c);
//			Thread t = new Thread(tctThread);
//			addThread(t);
//			startThread(waitThreads, threads);
//		}
	}
	
	static String getUUID(){
		return  UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public static void startThread(List<Thread> waitThreads,
			List<Thread> threads) {
		if (threads.size() < 10) {
			synchronized (threads) {
				if (threads.size() < 10 && waitThreads.size() > 0) {
					Thread thread = waitThreads.remove(0);
					thread.start();
					threads.add(thread);
				}
			}
		}
	}

	private static void addThread(Thread t) {
		synchronized (waitThreads) {
			waitThreads.add(t);
		}
	}
}

class Monitor implements Runnable {
	private static boolean stop = false;
	
	public static boolean isStop() {
		return stop;
	}


	public static void stop() {
		Monitor.stop = true;
	}


	@Override
	public void run() {
		while ( !stop ) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("总数：" + TaobaoCrawlTask.total 
//					+ "--解析过的：" + TaobaoDetailCrawlTask.totalItem 
//					+  "--解析成功的：" 	+ TaobaoDetailCrawlTask.alreadyItem 
					+  "--errorurl:" + TaobaoCrawlTask.errorUrl.size()
//					+  "--size:" + TaobaoCrawlTask.waitThreads.size()
					);
			
		}

	}
}