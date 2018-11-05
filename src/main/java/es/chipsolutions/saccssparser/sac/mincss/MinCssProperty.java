package es.chipsolutions.saccssparser.sac.mincss;

public class MinCssProperty {
	String name;
	MinCssPropertyValue value;
	public MinCssProperty(String name, MinCssPropertyValue value){
		this.name = name;
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public MinCssPropertyValue getValue() {
		return value;
	}
	public void setValue(MinCssPropertyValue value) {
		this.value = value;
	}
	@Override
	public String toString(){
		return name + ":"+value.toString();
	}
	
}
