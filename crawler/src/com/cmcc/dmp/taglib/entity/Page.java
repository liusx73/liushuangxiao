package com.cmcc.dmp.taglib.entity;

public class Page {
	private Integer pageSize;
	private Integer totalPage;
	private Integer currentPage;
	private Integer totalCount;

	public Page(Integer pageSize, Integer totalPage) {
		super();
		this.pageSize = pageSize;
		this.totalPage = totalPage;
	}

	public Page(Integer pageSize, Integer totalPage, Integer totalCount,
			Integer currentPage) {
		super();
		this.pageSize = pageSize;
		this.totalPage = totalPage;
		this.currentPage = currentPage;
		this.totalCount = totalCount;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

}
