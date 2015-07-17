package tools;

public class HtmlElement {
	private String elementStr ;
	private String innerHTML ;

	public HtmlElement(String elementStr) {
		super();
		this.elementStr = elementStr == null ? "" : elementStr;;
	}

	public String html() {
		return elementStr;
	}
	
	
	public String innerHtml(){
		if(innerHTML == null){
			HtmlStringUtils.getInnerhtml(elementStr, 0);
		}
		return innerHTML;
	}
}
