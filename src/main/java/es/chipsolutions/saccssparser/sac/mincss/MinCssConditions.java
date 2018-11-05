package es.chipsolutions.saccssparser.sac.mincss;

import java.util.HashMap;
import java.util.Map;

public class MinCssConditions extends HashMap<String,MinCssPropertyList>{

	public MinCssConditions() {
		super();
	}

	public MinCssConditions(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public MinCssConditions(int initialCapacity) {
		super(initialCapacity);
	}

	public MinCssConditions(
			Map<? extends String, ? extends MinCssPropertyList> m) {
		super(m);
	}

}
