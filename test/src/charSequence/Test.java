package charSequence;

public class Test {
	public static void main(String[] args) {
		String s = "1234567890abcdefghijklmnopqrstuvwxyz";
		
		StringBuffer sb = new StringBuffer(s);
		System.out.println(sb.lastIndexOf("1"));
		sb.replace(sb.lastIndexOf("1"), 1, "*");
		System.out.println(sb);
		
		
		System.out.println(s.substring(0, s.length()-1));
//		System.out.println(s.subSequence(0, 2));
//		System.out.println(s.substring(4));
//		System.out.println(s.codePoints());
	}
}
