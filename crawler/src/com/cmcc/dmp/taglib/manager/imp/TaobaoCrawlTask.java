package com.cmcc.dmp.taglib.manager.imp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import tools.HtmlStringUtils;
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
	private Page page;

	public TaobaoCrawlTask(String url, String coding) {
		super();
		this.url = url;
		this.coding = coding;
	}

	public TaobaoCrawlTask(String url) {
		super();
		this.url = url;
		this.coding = "utf-8";
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
		crawl(url, coding, category);
	}

	void crawl(final String url, final String coding, Category category) {
		String info = getPageConfig(url, coding, category);
		analyze(category, info);
		System.out.println(category.getName() + "--" + category.getId() + "--"
				+ category.getCountItems());
		
		if(page == null){
			page = getPage(info);
		}
		
		List<Category> childs = category.getChilds();
		for (int index = 0; index < childs.size(); index++) {
			crawl(url, coding, childs.get(index));
		}
	}

	private void analyze(Category category, String info) {
		category.setChilds(getCategorys(info));
		category.setCountItems(getTotalCount(info));
	}

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
		}
		return categories;
	}
	
	private int getTotalCount(String info){
		return JSONUtils.getIntValue(info, "totalCount");
	}
	
	private Page getPage(String info) {
		int pageSize = JSONUtils.getIntValue(info, "pageSize");
		int currentPage = JSONUtils.getIntValue(info, "currentPage");
		Page page = new Page(pageSize, currentPage);
		return page;
	}

	private String getPageConfig(String url, String coding, Category category) {
		Integer categoryId = category.getId();
		url = categoryId == null ? url : url + categoryId.toString();
		HttpURLConnection urlCon = URLConnector.getHttpConnection(url, coding);
		JSONObject pageConfig = null;
		InputStreamReader in = null;
		BufferedReader br = null;
		String info = null;
		try {
			urlCon.connect();
			in = new InputStreamReader(urlCon.getInputStream(), "UTF-8");
			br = new BufferedReader(in);
			String readLine = null;
			while ((readLine = br.readLine()) != null && info == null) {
				if (readLine.trim().startsWith("g_page_config")) {
					info = readLine.split("=")[1];
					info = info.substring(0, info.length() - 1);
					info = HtmlStringUtils.trimQuotes(info);
				}
			}
			in.close();
			in = null;
			br.close();
			br = null;
			urlCon.disconnect();
			urlCon = null;
		} catch (IOException e) {
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
		String url = "https://s.taobao.com/search?q=&js=1&stats_click=search_radio_all%3A1&initiative_id=staobaoz_20150719&ie=utf8&cps=yes&cat=";
		
		//https://s.taobao.com/search?q=&js=1&stats_click=search_radio_all%3A1&initiative_id=staobaoz_20150719&ie=utf8&cps=yes&cat=50000072&bcoffset=-4&s=
		Category c = new Category("考试/教材/论文", 50000072);
		//Category c = new Category("德语考试", 50004603);
		TaobaoCrawlTask tct = new TaobaoCrawlTask(url, "utf8");
		tct.setCategory(c);
		tct.crawl();
		Category ca = tct.getCategory();
		System.out.println(ca);
	}
}
