package es.chipsolutions.saccssparser.sac.mincss.properties;

import es.chipsolutions.saccssparser.sac.mincss.MinCssPropertyValue;
import es.chipsolutions.saccssparser.sac.mincss.exceptions.MinCssInvalidPropertyValueException;
import org.w3c.css.sac.LexicalUnit;

public class MinCssPropertyShape extends MinCssPropertyValue{
	public static final int SHAPE_CIRCLE = 0;
	public static final int SHAPE_SQUARE = 1;

	int shape;
	
	public MinCssPropertyShape() {
		super();
	}

	public MinCssPropertyShape(int shape) {
		super();
		this.shape = shape;
	}

	@Override
	public String getPropertyName() {
		return "shape";
	}

	@Override
	protected MinCssPropertyValue parse(LexicalUnit l) throws MinCssInvalidPropertyValueException{
		if (l.getLexicalUnitType() == LexicalUnit.SAC_IDENT){
			String token = l.getStringValue();
			if (token.equals("circle")){
				return new MinCssPropertyShape(SHAPE_CIRCLE);
			} else if (token.equals("square")){
				return new MinCssPropertyShape(SHAPE_SQUARE);
			}
		}
		throw new MinCssInvalidPropertyValueException();
	}
	@Override
	public String toString() {
		switch (shape){
			case SHAPE_CIRCLE:
				return "circle";
			case SHAPE_SQUARE:
				return "square";
		}
		return "undefined";
	}

	public int getShape() {
		return this.shape;
	}

}
