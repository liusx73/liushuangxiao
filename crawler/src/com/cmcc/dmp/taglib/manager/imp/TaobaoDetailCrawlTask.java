package com.cmcc.dmp.taglib.manager.imp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import tools.HtmlStringUtils;
import tools.JSONUtils;
import tools.Transcoding;
import tools.URLConnector;

import com.cmcc.dmp.taglib.entity.Page;
import com.cmcc.dmp.taglib.manager.CrawlTask;

public class TaobaoDetailCrawlTask extends CrawlTask {
	private String url;
	private String coding;
	private Page page;
	private int pageSize;
	public static int totalItem = 0;
	public static int alreadyItem = 0;
	
	public synchronized void plusTotalItem(){
		totalItem ++;
	}
	public synchronized void plusAlreadyItem(){
		totalItem ++;
	}
	
	public TaobaoDetailCrawlTask(String url, String coding,
			Page page) {
		super();
		this.url = url ;
		this.coding = coding;
		this.page = page;
		this.pageSize = page.getPageSize();
	}
	
	public TaobaoDetailCrawlTask(String url,
			Page page) {
		super();
		this.url = url ;
		this.coding = TaobaoCrawlTask.UTF_8;
		this.page = page;
		this.pageSize = page.getPageSize();
	}


	@Override
	protected void crawl() {
		String info = null;
		List<String> items = null;
		Iterator<String> i = null;
		int indexPage = 0;
		while (indexPage < page.getTotalPage()) {
			info = getPageConfig(url +"&s=" + indexPage * pageSize, coding);
			if(!StringUtils.isBlank(info)){
				items = getItems(info);
				i = items.iterator();
				while (i.hasNext()) {
					crawlDetail(i.next());
				}
				indexPage ++ ;
			}
		}
		TaobaoCrawlTask.threads.remove(Thread.currentThread());
		TaobaoCrawlTask.startThread(TaobaoCrawlTask.waitThreads,TaobaoCrawlTask.threads);
	}
	
	
	private void crawlDetail(String url){
		try {
			url = "http:" + url.toString();
			url = Transcoding.decodeUnicode(url);
			Document doc = Jsoup.connect(url).get();
			if(doc.title().indexOf("出错啦！") >= 0){
				System.out.print("----出错啦!");
				return;
			}
			
			Elements e = doc.select("li[class*=section clearfix] > h5 > a");
			e.get(0).text();
			e.get(0).attr("htef");
			
			boolean isTmall = url.toLowerCase().indexOf("tmall") > -1;
			String title = null;
			String prise = null;
			String attribute = null;
			String brand = null;
			if(isTmall){
				Elements titles = doc.select("div[class=tb-detail-hd]");
				title = titles.get(0).getElementsByTag("h1").get(0).text();
				String prises = doc.select("script").toString();
				prise = getValue(prises, prises.indexOf(DEFAULT_ITEM_PRICE) + DEFAULT_ITEM_PRICE.length());
				Elements brands = doc.select("a[class=J_EbrandLogo]");
				if(brands .size() > 0){
					brand = brands.get(0).text();
				}
				Elements attributes = doc.select("li[class=attrwrap]");
				if(attributes.size()== 0){
					attributes = doc.select("ul[id=J_AttrUL]");
				}
				attribute = attributes.get(0).text();
			}else{
				Elements titles = doc.select("h3[class=tb-main-title]");
				title = titles.get(0).text();
				String prises = doc.select("script").toString();
				prise = getValue(prises, prises.indexOf(DEFAULT_ITEM_PRICE) + DEFAULT_ITEM_PRICE.length());
				Elements attributes = doc.select("ul[class=attributes-list]");
				attribute = attributes.get(0).text();
			}
			
			plusAlreadyItem();
		} catch (IOException e) {
			TaobaoCrawlTask.errorUrl.add(url);
			System.out.println(url + "----error----" + e.getMessage());
//			e.printStackTrace();
		} catch (Exception e) {
			TaobaoCrawlTask.errorUrl.add(url);
			System.out.println(url + "----error----" + e.getMessage());
		}
		plusTotalItem();
	}
	
	private List<String> getItems(final String info){
		List<String> items = new LinkedList<String>();
		int length = DETAIL_URL.length();
		int index = info.indexOf(DETAIL_URL);
		String item = null;
		while (index >-1) {
			item = getValue(info, index + length);
			if(!StringUtils.isBlank(item)){
				items.add(item);
			}
			index = info.indexOf(DETAIL_URL, index + length);
		}
		return items;
	}
	
	private String getValue(final String info, int index){
		return HtmlStringUtils.trimQuotes(JSONUtils.getValue(info, index, HtmlStringUtils.DOUBLE_QUOTES_STRING, HtmlStringUtils.DOUBLE_QUOTES_STRING));
	}
	
	private final static String DETAIL_URL = "\"detail_url\"";
	private final static String DEFAULT_ITEM_PRICE = "\"defaultItemPrice\"";
	
	private String getPageConfig(String url, String coding) {
		HttpURLConnection urlCon = URLConnector.getHttpConnection(url, coding);
//		urlCon.setReadTimeout(30000);
		InputStreamReader in = null;
		BufferedReader br = null;
		String info = null;
		try {
			urlCon.connect();
			in = new InputStreamReader(urlCon.getInputStream(), "UTF-8");
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return info;
	}
	
	public static void main(String[] args) {
		String url = "https://s.taobao.com/search?q=&js=1&stats_click=search_radio_all%3A1&initiative_id=staobaoz_20150719&ie=utf8&cps=yes&cat=50004603&bcoffset=1&s=";
		Page page = new Page(44, 47);
		TaobaoDetailCrawlTask t = new TaobaoDetailCrawlTask(url, "utf-8", page);
		long start = new Date().getTime();
		t.crawl();
		System.out.println(new Date().getTime() - start);
	}
}
