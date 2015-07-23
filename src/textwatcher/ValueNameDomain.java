package textwatcher;
/**
 * 一个Value(绑定值)-Name(显示名称)对象，如：1-汉族
 */
public class ValueNameDomain {

	private String Value;//绑定的值
	private String Name;//显示的选项名称
	
	public ValueNameDomain(){}
	
	public ValueNameDomain(String name,String value){
		this.Name = name;
		this.Value = value;
	}
	
	/**
	 * 获取绑定的值
	 */
	public String getValue() {
		return Value;
	}
	/**
	 * 设置绑定的值
	 */
	public void setValue(String value) {
		this.Value = value;
	}
	/**
	 * 获取显示的选项名称
	 */
	public String getName() {
		return Name;
	}
	/**
	 * 设置显示的选项名称
	 */
	public void setName(String name) {
		this.Name = name;
	}
	@Override
	public String toString() {
		return Name;
	}
	
	
}
