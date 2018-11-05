package es.chipsolutions.saccssparser.sac.mincss;

import java.util.HashMap;
import java.util.Map;

public class MinCssSelectorList extends HashMap<String, MinCssConditions>{

	public MinCssSelectorList() {
		super();
	}

	public MinCssSelectorList(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public MinCssSelectorList(int initialCapacity) {
		super(initialCapacity);
	}

	public MinCssSelectorList(Map<? extends String, ? extends MinCssConditions> m) {
		super(m);
	}
	

}
