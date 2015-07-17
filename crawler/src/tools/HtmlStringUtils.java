package tools;


public class HtmlStringUtils {
	
	private static String EQUAL_SIGN = "=";
	private static String SEMICOLON = ";";
	private static String GREATER = ">";
	private static String LESS = "<";
	
	private static String trim(String str,char c) {
		char[] value = str.toCharArray();
        int len = value.length;
        int st = 0;
        char[] val = value;    /* avoid getfield opcode */

        while ((st < len) && (val[st] <= c)) {
            st++;
        }
        while ((st < len) && (val[len - 1] <= c)) {
            len--;
        }
        return ((st > 0) || (len < value.length)) ? str.substring(st, len) : str;
    }
	/**
	 * 去掉前后引号
	 * @param str
	 * @return
	 */
	public static String trimQuotes(String str) {
		str = str.trim();
		if(str.startsWith("'")){
			return trim(str, '\'');
		}else{
			return trim(str, '"');
		}
		
        
    }
	/**
	 * 例：
	 * 	str = id="id" name="name" 
	 * 		  0123456789....
	 *  index = 8;
	 *  return name
	 * @param str
	 * @param index 
	 * @return
	 */
	
	public static String getValue(String str,int index){
		int start = str.indexOf(EQUAL_SIGN, index) + 1;
		int end = str.indexOf(SEMICOLON, start);
		return trimQuotes(str.substring(start, end));
	}
	
	private static String getInnerHtml(String str,int index, String front, String behind){
		int start = str.indexOf(front, index) + 1;
		int end = str.lastIndexOf(behind, start);
		return str.substring(start, end);
	}
	
	public static String getInnerhtml(String str,int index){
		return getInnerHtml(str, index, LESS, GREATER);
	}
}
