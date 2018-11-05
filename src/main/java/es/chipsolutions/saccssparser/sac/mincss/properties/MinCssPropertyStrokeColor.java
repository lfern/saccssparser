package es.chipsolutions.saccssparser.sac.mincss.properties;

import es.chipsolutions.saccssparser.sac.mincss.MinCssPropertyValue;
import es.chipsolutions.saccssparser.sac.mincss.exceptions.MinCssInvalidPropertyValueException;
import org.w3c.css.sac.LexicalUnit;

/**
 * stroke-color #color
 * 
 * @author luis
 *
 */
public class MinCssPropertyStrokeColor extends MinCssPropertyValue{

	int red,green,blue;
	public MinCssPropertyStrokeColor(){
		super();
	}
	public MinCssPropertyStrokeColor(int red, int green, int blue) {
		super();
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	@Override
	public String getPropertyName() {
		return "stroke-color";
	}

	@Override
	protected MinCssPropertyValue parse(LexicalUnit l) throws MinCssInvalidPropertyValueException{
		if (l.getLexicalUnitType() == LexicalUnit.SAC_RGBCOLOR){
			LexicalUnit params = l.getParameters();
			int red = params.getIntegerValue();
			int green = params.getNextLexicalUnit().getNextLexicalUnit().getIntegerValue();
			int blue = params.getNextLexicalUnit().getNextLexicalUnit().getNextLexicalUnit().getNextLexicalUnit().getIntegerValue();
			return new MinCssPropertyStrokeColor(red, green, blue);
		}
		throw new MinCssInvalidPropertyValueException();
	}
	@Override
	public String toString() {
		
		return "rgb("+red+","+green+","+blue+")"; 
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
	

}
