package testDate;

import java.time.LocalDate;
import java.time.chrono.IsoChronology;

public class Test {
	public static void main(String[] args) {
		LocalDate ld = IsoChronology.INSTANCE.dateNow();
		System.out.println(hideInfo("ab",""));
		System.out.println(ld);
	}
	private static String hideString = "*****";
	public static String hideInfo(String str,String separator){
		StringBuffer returnString = new StringBuffer(str);
		int length;
		if(separator == null){
			length = returnString.length();
		}else{
			length = returnString.indexOf(separator);
		}
		if(length < 1){
			length = returnString.length();
		}

		if(length == 1){
			returnString.replace(0, 1, hideString);
		}else if(length == 2){
			returnString.replace(1, 2, hideString);
		}else{
			int hideLength = length/2;
			int startHidePoint = (length - hideLength)/2;
			int endHidePoint = startHidePoint + hideLength;
			returnString.replace(startHidePoint, endHidePoint, hideString);
		}
		return returnString.toString();
	}
}
