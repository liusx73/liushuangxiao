package extendsTest;

import java.util.Date;

public class Category implements Cloneable,Equalsable {

	protected Integer categoryId; // 鑷id
	protected String categoryName;// 鍒嗙被鍚?
	protected String categoryCode;// 鐖彇鐨刬d
	protected String parentCategoryCode;// 鐖彇浼忓嚮鍒嗙被id
	protected Integer categoryLevel;// 灞傜骇
	protected String categoryUrl;// url
	protected String categoryDesc;// 鎻忚堪
	protected String websiteName;// 缃戠珯鍚?
	protected String categoryCondition;// 绛涢?夋潯浠?
	protected int isLeaf;// 绛涢?夋潯浠?
	protected Date createTime;
	protected Date updateTime;
	protected Integer parseStatus;
	protected Integer tagId;
	protected String tagCode;
	protected String tagType;

	public Object clone() {
		Category o = null;
		try {
			o = (Category) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}
	
	public Category() {
		super();
	}

	public Category(String categoryCode, Integer categoryLevel) {
		super();
		this.categoryCode = categoryCode;
		this.categoryLevel = categoryLevel;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getParentCategoryCode() {
		return parentCategoryCode;
	}

	public void setParentCategoryCode(String parentCategoryCode) {
		this.parentCategoryCode = parentCategoryCode;
	}

	public Integer getCategoryLevel() {
		return categoryLevel;
	}

	public void setCategoryLevel(Integer categoryLevel) {
		this.categoryLevel = categoryLevel;
	}

	public String getCategoryUrl() {
		return categoryUrl;
	}

	public void setCategoryUrl(String categoryUrl) {
		this.categoryUrl = categoryUrl;
	}

	public String getCategoryDesc() {
		return categoryDesc;
	}

	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}

	public String getWebsiteName() {
		return websiteName;
	}

	public void setWebsiteName(String websiteName) {
		this.websiteName = websiteName;
	}

	public String getCategoryCondition() {
		return categoryCondition;
	}

	public void setCategoryCondition(String categoryCondition) {
		this.categoryCondition = categoryCondition;
	}

	public int getIsLeaf() {
		return isLeaf;
	}

	public void setIsLeaf(int isLeaf) {
		this.isLeaf = isLeaf;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getParseStatus() {
		return parseStatus;
	}

	public void setParseStatus(Integer parseStatus) {
		this.parseStatus = parseStatus;
	}

	public Integer getTagId() {
		return tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

	public String getTagCode() {
		return tagCode;
	}

	public void setTagCode(String tagCode) {
		this.tagCode = tagCode;
	}

	public String getTagType() {
		return tagType;
	}

	public void setTagType(String tagType) {
		this.tagType = tagType;
	}

	@Override
	public String toString() {
		return "鍒嗙被鍚?:" + categoryName + ";鍒嗙被code:" + categoryCode + ";鍒嗙被level:" + categoryLevel + ";鐖剁被鍚?:" + parentCategoryCode + "鍙跺瓙:" + isLeaf + ",desc:" + this.categoryDesc + ",url:" + this.categoryUrl;
	}

	public boolean equals(Category categoryCompare) {
		
		return specialEquals(categoryCompare);
	}

	@Override
	public boolean specialEquals(Object object) {
		
		if (object == null) {
			return false;
		}
		
		if(object instanceof Category){
			
		}else{
			return false;
		}
		Category categoryCompare = (Category) object;
		

		if (this.categoryDesc == null && categoryCompare.categoryDesc != null) {
			return false;
		}
		if (this.categoryDesc != null && !this.categoryDesc.equals(categoryCompare.categoryDesc)) {
			return false;
		}

		if (this.categoryCode == null && categoryCompare.categoryCode != null) {
			return false;
		}

		if (this.categoryCode != null && !this.categoryCode.equals(categoryCompare.categoryCode)) {
			return false;
		}

		if (this.categoryName == null && categoryCompare.categoryName != null) {
			return false;
		}
		if (this.categoryName != null && !this.categoryName.equals(categoryCompare.categoryName)) {
			return false;
		}

		if (this.categoryLevel == null && categoryCompare.categoryLevel != null) {
			return false;
		}
		if (this.categoryLevel != null && !this.categoryLevel.equals(categoryCompare.categoryLevel)) {
			return false;
		}

		if (this.categoryUrl == null && categoryCompare.categoryUrl != null) {
			return false;
		}
		if (this.categoryUrl != null && !this.categoryUrl.equals(categoryCompare.categoryUrl)) {
			return false;
		}

		if (this.websiteName == null && categoryCompare.websiteName != null) {
			return false;
		}
		if (this.websiteName != null && !this.websiteName.equals(categoryCompare.websiteName)) {
			return false;
		}

		if (this.parentCategoryCode == null && categoryCompare.parentCategoryCode != null) {
			return false;
		}
		if (this.parentCategoryCode != null && !this.parentCategoryCode.equals(categoryCompare.parentCategoryCode)) {
			return false;
		}

		if (this.isLeaf != categoryCompare.isLeaf) {
			return false;
		}

		return true;
	}

}
