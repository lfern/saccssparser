package es.chipsolutions.saccssparser.sac.mincss;

import es.chipsolutions.saccssparser.sac.mincss.exceptions.MinCssInvalidPropertyValueException;
import org.w3c.css.sac.LexicalUnit;

public abstract class MinCssPropertyValue {

	public abstract String getPropertyName();
	protected abstract MinCssPropertyValue parse(LexicalUnit l) throws MinCssInvalidPropertyValueException;
	
	
}
