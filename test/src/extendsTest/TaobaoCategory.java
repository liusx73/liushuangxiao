package extendsTest;

public class TaobaoCategory extends Category {

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
		return true;
	}

}
