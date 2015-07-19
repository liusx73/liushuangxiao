package com.cmcc.dmp.taglib.entity;

import java.util.List;

public class Category {
	private String name;
	private Integer id;
	private List<Category> childs;
	//当前类别的总商品/条目数
	private Integer countItems;
	public Category() {
		super();
	}

	public Category(String name, Integer id, List<Category> childs) {
		super();
		this.name = name;
		this.id = id;
		this.childs = childs;
	}
	
	public Category(String name, Integer id) {
		super();
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Category> getChilds() {
		return childs;
	}

	public void setChilds(List<Category> childs) {
		this.childs = childs;
	}

	public Integer getCountItems() {
		return countItems;
	}

	public void setCountItems(Integer countItems) {
		this.countItems = countItems;
	}
}
