package es.chipsolutions.saccssparser.sac.mincss;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;


public class MinCssSelector {
	String selector;
	String[] params;
	String normalizedParams;
	String normalized;
	public MinCssSelector(String selector, String ... params){
		this.selector = selector;
		this.params = params;
		doNormalize();
	}
	
	public static String normalizeParams(String ... params){
		String[] ordered = new String[params.length];
		System.arraycopy(params, 0, ordered, 0, params.length);
		Arrays.sort(ordered);
		return StringUtils.join(ordered,":");
	}
	
	private void doNormalize(){
		this.normalizedParams = normalizeParams(this.params);
		this.normalized = selector + ((normalizedParams.length() == 0)?"":":"+ normalizedParams);
	}
	
	public String getSelector() {
		return selector;
	}

	public String[] getParams() {
		return params;
	}

	public String getNormalized(){
		return normalized;
	}
	
	public String getNormalizedParams(){
		return normalizedParams;
	}
	
	public MinCssSelector(MinCssSelector selector, String ... params){
		this.selector = selector.selector;
		this.params = Arrays.copyOf(selector.params, selector.params.length+params.length);
		System.arraycopy(params, 0, this.params, selector.params.length, params.length);
		doNormalize();
	}

}
