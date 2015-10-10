package test;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;


public class Test {
	static int[] iArray = new int[5];
	public static void main(String[] args) {
//		int index = 80;
//		
//		Pattern regex = Pattern.compile(\"^(?![^a-zA_Z]+$)(?![^0-9]+$)[a-zA-Z0-9_]{6,16}$\");
//		System.out.println(regex.matcher(\"aaaa1_\").matches());
//		System.out.println(\"1\".compareTo(\"2\"));
//		
//		for (int i = 0; i < index; i++) {
//			for (int j = 0; j < index; j++) {
//				System.out.print((j+i)%2 + \" \");
//			}
//			System.out.println();
//		}
//		Random r = new Random();
//		for (int i = 0; i <100; i++) {
//			System.out.println(r.nextInt()%100);
//		}
//		
//		Set<String> s = new HashSet<String>();
//		System.out.println(s.add("a"));
//		System.out.println(s.add("a"));
//		System.out.println("1155fb6fb320bb54b94fe5ed6cd69823" != null);
//		System.out.println(System.getProperty("user.dir") + System.getProperty("file.separator") + "proxy");
//		String pubTime = "";
//		if (pubTime.equals("")||equals(null)||equals("null")){
			
//		}
//		List<String> stringList = new ArrayList<String>();
//		System.out.println(stringList.size());
//		stringList.add(null);
		
//		String a = "https://sec.taobao.com/query.htm?smApp=list&smPolicy=list-list_itemlist_jsonapi-anti_Spider-checklogin&smCharset=utf-8&smTag=MjIzLjk1Ljc2LjEzNywsMmVmN2E4MzcwYzJkNDM5NDk2MjBmZTg5ZjBlMjU2OTA%3D&smReturn=https%3A%2F%2Flist.taobao.com%2Fitemlist%2Fdefault.htm%3F_input_charset%3Dutf-8%26json%3Don%26sort%3Dbiz30daydata-key%3Ds%26cat%3D51712001%26s%3D0%26pSize%3D96%26data-value%3D96%26callback%3Djsonp20%26_ksTS%3D1437731744965_19&smSign=wt4O9AKpvuhfPk7vdwKjHA%3D%3D";
//		String b = "https://sec.taobao.com/query.htm?smApp=list&smPolicy=list-list_itemlist_jsonapi-anti_Spider-checklogin&smCharset=utf-8&smTag=MjIzLjk1Ljc2LjEzNywsNDhkMmU3NDExMTMxNDMxMWE3NWVmN2VhZGVmMGIxOGY%3D&smReturn=https%3A%2F%2Flist.taobao.com%2Fitemlist%2Fdefault.htm%3F_input_charset%3Dutf-8%26json%3Don%26sort%3Dbiz30daydata-key%3Ds%26cat%3D51712001%26s%3D0%26pSize%3D96%26data-value%3D96%26callback%3Djsonp20%26_ksTS%3D1437731744965_19&smSign=jpnGIqa9o4%2FZZFY4yhKe9A%3D%3D";
//		System.out.println(a.equals(b));
//		for (int i = 0; i < 100; i++) {
//			try {
//				Thread.sleep(100l);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			System.out.println(new Date().getTime());
//		}
//		
//		Integer i = 9;
//		Integer i2 = 9;
//		System.out.println(i == i2);
//		
//		System.out.println("http://mobilecdn.kugou.com/new/app/i/krc.php?type=1&cmd=200&hash=E1D476EC62531E885AF72442D4E8AD05&keyword=I'm Yours - Jason Mraz&timelength=287000".replaceAll(" ", "%20"));
//		int a = 2;
//		System.out.println(a == 1 && a == 2);
//		System.out.println(Integer.valueOf("I3r2E", 36));
//		byte[] b = "I3r2E".getBytes();
//		String s = "";
//		for (int i = 0; i < b.length; i++) {
//			String temp = Long.toBinaryString(Byte.toUnsignedLong(b[i]));
//			if(temp.length() < 8){
//				temp = "0" + temp;
//			}
//			s += temp;
//		}
//		System.out.println(Long.parseLong(s, 2));
//		
//		long l = Long.valueOf("I4IMz", 36);
//		byte[] b2 = "I4IMz".getBytes();
//		String s2 = "";
//		for (int i = 0; i < b2.length; i++) {
//			String temp = Long.toBinaryString(Byte.toUnsignedLong(b2[i]));
//			if(temp.length() < 8){
//				temp = "0" + temp;
//			}
//			s2 += temp;
//		}
//		System.out.println(Long.parseLong(s2, 2));
//		39480413006
//		System.out.println(15742626 + 30408134);
		
//		
//		try {
//			System.out.println(URLEncoder.encode("https://www.baidu.com/s?ie=utf-8&f=3&rsv_bp=1&rsv_idx=1&tn=baidu&wd=c%20long%20int&oq=c%20string&rsv_pq=e758e12e0001bef1&rsv_t=9fdeY4SQFFeGALIZQQO6EU11%2FRos7AG%2BsGe1FNeAulbunBCsw1iAb3n65uc&rsv_enter=1&rsv_sug3=14&rsv_sug1=19&rsv_sug2=0&prefixsug=c%20long%20&rsp=2&inputT=111099&rsv_sug4=115053", "UTF-8"));
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println((int)'\n');
		new Test().aa();
		
//		Set<String> s = new HashSet<String>();
//		
//		System.out.println(s.add(new String("a")));
//		System.out.println(s.add(new String("a")));
//		System.out.println(s.size());
		
		System.out.println(Math.pow(2, 16));
	}
	
	private void aa(){
		System.out.println("aasdasdasd" == bb());
	}
	
	private String bb(){
		return "aasdasdasd";
	}
	
}
//1438739949142
//1438740008742