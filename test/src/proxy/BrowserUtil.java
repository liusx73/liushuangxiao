package proxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

public class BrowserUtil {

	private static final Log logger = LogFactory.getLog(BrowserUtil.class);

	private static List<String> browserList = BrowserUtil.loadBrowser();
	private static int size = browserList.size();
	private static int index = 0;
	private static List<String> loadBrowser() {
		List<String> list = new ArrayList<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("browser.txt"));
			String line = null;
			while ((line = br.readLine()) != null) {
				list.add(line);
			}
		} catch (FileNotFoundException e) {
			logger.error(BrowserUtil.class, e);
		} catch (IOException e) {
			logger.error(BrowserUtil.class, e);
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				logger.error(BrowserUtil.class, e);
			}
		}
		logger.info("browser list size :" + size);
		return list;
	}

	public static int getProxyListSize(){
		return size;
	}
	
	public static synchronized String getUserAgent() {
		index ++;
		if(index >= size){
			index = 0;
		}
		return browserList.get(index);
	}
	
	public static void main(String[] args) {
		
	}
}
