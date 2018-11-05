package es.chipsolutions.saccssparser.sac.mincss.properties;

import es.chipsolutions.saccssparser.sac.mincss.MinCssPropertyValue;
import es.chipsolutions.saccssparser.sac.mincss.exceptions.MinCssInvalidPropertyValueException;
import org.w3c.css.sac.LexicalUnit;

public class MinCssPropertyBackgroundColor extends MinCssPropertyValue{
	
	int red,green,blue;
	float alpha;
	public MinCssPropertyBackgroundColor() {
		super();
	}
	public MinCssPropertyBackgroundColor(int red, int green, int blue, float alpha){
		super();
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}
	@Override
	public String getPropertyName() {
		return "background";
	}

	@Override
	protected MinCssPropertyValue parse(LexicalUnit l)
			throws MinCssInvalidPropertyValueException {
		if (l.getLexicalUnitType() == LexicalUnit.SAC_RGBCOLOR){
			LexicalUnit params = l.getParameters();
			int red = params.getIntegerValue();
			int green = params.getNextLexicalUnit().getNextLexicalUnit().getIntegerValue();
			int blue = params.getNextLexicalUnit().getNextLexicalUnit().getNextLexicalUnit().getNextLexicalUnit().getIntegerValue();
			
			l = l.getNextLexicalUnit();
			if ((l != null) && (l.getLexicalUnitType() == LexicalUnit.SAC_PERCENTAGE)){
				float alfa = l.getFloatValue();
				return new MinCssPropertyBackgroundColor(red, green, blue, alfa);
			}
		}
		throw new MinCssInvalidPropertyValueException();
	}

	@Override
	public String toString() {
		
		return "rgb("+red+","+green+","+blue+") alpha="+alpha; 
	}
	public int getRed() {
		return red;
	}
	public int getGreen() {
		return green;
	}
	public int getBlue() {
		return blue;
	}
	public float getAlpha() {
		return alpha;
	}
	
}
