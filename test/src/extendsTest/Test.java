package extendsTest;

public class Test {
	public static void main(String[] args) {
		Category c1 = new TaobaoCategory();
		c1.setCategoryDesc("1");
		c1.setCategoryCode("2");
		Category c2 = new Category();
		c2.setCategoryDesc("1");
		c1.setCategoryCode("3");
		System.out.println(c1.equals(c2));
		System.out.println(c2.equals(c1));
		save(c1);
		save(c2);
	}
	
	public static void save(TaobaoCategory c){
		System.out.println("do something");
	}
	public static void save(Category c){
		System.out.println(c instanceof TaobaoCategory);
		System.out.println("just save");
	}
}
