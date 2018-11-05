package es.chipsolutions.saccssparser.sac.mincss;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MinCssPropertyList extends HashMap<String, MinCssProperty>{


	public MinCssPropertyList() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MinCssPropertyList(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
		// TODO Auto-generated constructor stub
	}
	public MinCssPropertyList(int initialCapacity) {
		super(initialCapacity);
		// TODO Auto-generated constructor stub
	}
	public MinCssPropertyList(Map<? extends String, ? extends MinCssProperty> m) {
		super(m);
		// TODO Auto-generated constructor stub
	}
	public MinCssPropertyList cloneProperties() {
		return cloneProperties(new MinCssPropertyList());
	}
	public MinCssPropertyList cloneProperties(MinCssPropertyList oldPropertyList){
		if (oldPropertyList == null){
			oldPropertyList = new MinCssPropertyList();
		}
		Iterator<String> it = this.keySet().iterator();
		while(it.hasNext()){
			String propName = it.next();
			oldPropertyList.put(propName, this.get(propName));
		}
		return oldPropertyList;
	}

}
