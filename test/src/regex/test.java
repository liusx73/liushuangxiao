package regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {
	public static void main(String[] args) {
		Pattern pattern = Pattern.compile("(http//:)|(https//:)|(ftp//:){0,1}.*[a-z]*");
		Matcher matcher1 = pattern.matcher("HTTP/s/:192.168.1.24:8080/imag/index.jsp?abc=xxx");
		Matcher matcher2 = pattern.matcher("https//:192.168.1.24:8080/imag/index.jsp?abc=xxx");
		Matcher matcher3 = pattern.matcher("www.baidu.com");
		Matcher matcher4 = pattern.matcher("baidu.com");
		System.out.println(matcher1.matches());
		System.out.println(matcher2.matches());
		System.out.println(matcher3.matches());
		System.out.println(matcher4.matches());
		// System.out.println(isUrl("http//:192.168.1.24:8080/imag/index.jsp?abc=xxx"));
	}

	private static Pattern urlPattern;

	public static boolean isUrl(String s) {
		if (urlPattern == null) {
			synchronized (s) {
				if (urlPattern == null) {
					urlPattern = Pattern
							.compile("^[a-zA-z]+:\\/\\/(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*(\\:([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]{1}|6553[0-5]))?(\\/\\w*)*(\\.\\w*)?(\\?\\S*)?$/");
				}
			}

		}
		return urlPattern.matcher(s).matches();
	}
}
