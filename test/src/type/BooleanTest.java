package type;

public class BooleanTest {

	public static void main(String[] args) {
		String trueStr = "true";
		
		Boolean b1 = new Boolean(trueStr);
		Boolean b2 = new Boolean(trueStr);
		
		System.out.println(Boolean.getBoolean(b1.toString()));
		System.out.println(b1 == b2);
	}

}
