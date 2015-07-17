package com.cmcc.dmp.taglib.manager;


/**
 *
 *
 * @Project: 
 * @File: CrawlTask.java
 * @Date: 2015年7月17日
 * @Author: liushuangxiao
 * @Copyright: 版权所有 (C) 2015 中国移动 杭州研发中心.
 *
 * @注意：本内容仅限于中国移动内部传阅，禁止外泄以及用于其他的商业目的
 */

public abstract class CrawlTask<T> implements Runnable{

	protected abstract void crawl();

	@Override
	public final void run() {
		crawl();
	}
}
