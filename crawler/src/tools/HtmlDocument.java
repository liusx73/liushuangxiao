package tools;

import org.apache.commons.lang.StringUtils;

public class HtmlDocument {
	

	public HtmlDocument(String htmlDocument) {
		super();
		HtmlDocument = htmlDocument;
	}

	public String findValue(String variableName) {
		String value = null;
		if (!StringUtils.isBlank(variableName)) {
			int index = HtmlDocument.indexOf(variableName);
			if (index >= 0) {
				value = HtmlStringUtils.getValue(HtmlDocument, index);
			}
		}
		return value;
	}
	
	public HtmlElement findElement(String attribute,String value) {
		HtmlElement element = null;
		if (!StringUtils.isBlank(attribute) && !StringUtils.isBlank(value)) {
			int index = HtmlDocument.indexOf(attribute);
			if (index >= 0) {
				element = new HtmlElement(HtmlStringUtils.getValue(HtmlDocument, index));
			}
		}
		return element;
	}
	
	private String HtmlDocument;
	
	public String html(){
		return HtmlDocument;
	}
	
	private String innerHtml(){
		String innerHtml = null;
		return innerHtml;
	}
}
