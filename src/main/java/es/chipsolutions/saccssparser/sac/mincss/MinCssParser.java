package es.chipsolutions.saccssparser.sac.mincss;

import es.chipsolutions.saccssparser.sac.mincss.exceptions.MinCssInvalidPropertyException;
import es.chipsolutions.saccssparser.sac.mincss.exceptions.MinCssInvalidPropertyValueException;
import es.chipsolutions.saccssparser.sac.mincss.exceptions.MinCssParseException;
import es.chipsolutions.saccssparser.sac.mincss.exceptions.MinCssSoftException;
import org.w3c.css.sac.*;
import org.w3c.css.sac.helpers.ParserFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.*; 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This example count the number of property in the style rules (outside a
 * media rule).
 * @author  Philippe Le Hegaret
 */
public class MinCssParser implements DocumentHandler {
	boolean inMedia         = false;
	boolean inStyleRule     = false;
	int     propertyCounter = 0;
	
	MinCssSelectorList selectorsParsed = new MinCssSelectorList();
	ArrayList<MinCssPropertyList>currentPropSelList;

        protected final MinCssPropertyParser propertyParser;
        public MinCssParser(MinCssPropertyParser propertyParser) {
            this.propertyParser = propertyParser;
        }

	public MinCssSelectorList getSelectorsParsed(){
		return this.selectorsParsed;
	}
	public void startMedia(SACMediaList media) throws CSSException {
		inMedia = true;
	}

	public void endMedia(SACMediaList media) throws CSSException {
		inMedia = false;
	}
	
	private ArrayList<String> parseConditions(Condition c){
		ArrayList<String> condList = new ArrayList<String>();
		switch(c.getConditionType()){
			case Condition.SAC_ID_CONDITION:
				AttributeCondition ac = (AttributeCondition)c;
				condList.add("#"+ac.getValue());
				break;
			case Condition.SAC_CLASS_CONDITION:
				AttributeCondition ac2 = (AttributeCondition)c;
				condList.add("."+ac2.getValue());
				break;
			case Condition.SAC_PSEUDO_CLASS_CONDITION:
				condList.add("");
				AttributeCondition ac3 = (AttributeCondition)c;
				condList.add(ac3.getValue());
				break;
			case Condition.SAC_AND_CONDITION:
				CombinatorCondition cc = (CombinatorCondition)c;
				Condition c1 = null;
				condList.add("");
				do {
					c1 = cc.getFirstCondition();
					Condition c2 = cc.getSecondCondition();
					
					switch(c2.getConditionType()){
						case Condition.SAC_PSEUDO_CLASS_CONDITION:
							AttributeCondition ccac = (AttributeCondition)c2;
							condList.add(ccac.getValue());
							break;
						default:
							return null;
					}
					if (c1.getConditionType() != Condition.SAC_AND_CONDITION){
						break;
					}
					cc = (CombinatorCondition)c1;
				} while(true);
				switch(c1.getConditionType()){
					case Condition.SAC_ID_CONDITION:
						AttributeCondition ccac1 = (AttributeCondition)c1;
						condList.set(0,"#"+ccac1.getValue());
						break;
					case Condition.SAC_CLASS_CONDITION:
						AttributeCondition ccac2 = (AttributeCondition)c1;
						condList.set(0,"."+ccac2.getValue());
						break;
					case Condition.SAC_PSEUDO_CLASS_CONDITION:
						AttributeCondition ccac3 = (AttributeCondition)c1;
						condList.add(ccac3.getValue());
						break;

					default:
						return null;
				}
				break;
			default:
				System.out.println("condition->"+c.getConditionType());
				return null;
		}
		return condList;
	}
	private ArrayList<MinCssPropertyList> parseSelector(Selector s){
		ArrayList<MinCssPropertyList> list = new ArrayList<MinCssPropertyList>();
		switch (s.getSelectorType()){
			case Selector.SAC_CONDITIONAL_SELECTOR:
				ConditionalSelector cs = (ConditionalSelector)s;
				Condition c = cs.getCondition();
				ArrayList<String> selectors = this.parseConditions(c);
				if (selectors != null){
					String main = selectors.get(0);
					if (!this.selectorsParsed.containsKey(main)){
						this.selectorsParsed.put(main,new MinCssConditions());
						this.selectorsParsed.get(main).put("",new MinCssPropertyList());
					}
					if (selectors.size() == 1){
						list.add(this.selectorsParsed.get(main).get(""));
					} else {
						String normalizedParams = MinCssSelector.normalizeParams(selectors.subList(1, selectors.size()).toArray(new String[0]));
						if (!this.selectorsParsed.get(main).containsKey(normalizedParams)){
							this.selectorsParsed.get(main).put(normalizedParams, new MinCssPropertyList());
						}
						list.add(this.selectorsParsed.get(main).get(normalizedParams));
						
/*						
						for(int i=1;i<selectors.size();i++){
							String sName = selectors.get(i);
							if (!this.selectorsParsed.get(main).containsKey(sName)){
								this.selectorsParsed.get(main).put(sName, new MinCssPropertyList());
							}
							list.add(this.selectorsParsed.get(main).get(sName));
						}
*/						
					}
				}
				break;
			case Selector.SAC_ELEMENT_NODE_SELECTOR:
				ElementSelector es = (ElementSelector)s;
				String main = "";
				if (es.getLocalName() != null){
					main = es.getLocalName();
				}
				if (!this.selectorsParsed.containsKey(main)){
					this.selectorsParsed.put(main,new MinCssConditions());
					this.selectorsParsed.get(main).put("",new MinCssPropertyList());
				}
				list.add(this.selectorsParsed.get(main).get(""));
				break;
			default:
				System.out.println(s.getSelectorType());
				return null;
		}
		return list;
	}

	public void startSelector(SelectorList patterns) throws CSSException {
		if (!inMedia) {
			inStyleRule = true;
			propertyCounter = 0;
			
			this.currentPropSelList = new ArrayList<MinCssPropertyList>();
			for(int i=0;i<patterns.getLength();i++){
				Selector s = patterns.item(i);
				ArrayList<MinCssPropertyList> list = this.parseSelector(s);
				if (list != null){
					currentPropSelList.addAll(list);
				}
			}
		}
	}

	public void endSelector(SelectorList patterns) throws CSSException {
		if (!inMedia) {
			//System.out.println( "Found " + propertyCounter + " properties.");
		}
		inStyleRule = false;
		currentPropSelList.clear();
		currentPropSelList = null;
	}

	public void property(String name, LexicalUnit value, boolean important)
			throws CSSException {
		if (inStyleRule) {
			propertyCounter++;
			try {
				MinCssProperty p = new MinCssProperty(name,propertyParser.parse(name, value));
				for(int i=0;i<currentPropSelList.size();i++){
					currentPropSelList.get(i).put(p.getName(),p);
				}
			} catch (MinCssInvalidPropertyException ex){
				System.out.println("-->"+name);
				System.out.println("---->Lexical unit type:"+value.getLexicalUnitType());
			} catch (MinCssInvalidPropertyValueException ex){
				System.out.println("-->"+name);
				System.out.println("---->Lexical unit type:"+value.toString());
			}
		}
	}
        public static MinCssSelectorList parse(File filename) throws FileNotFoundException, IOException,MinCssParseException,
				MinCssSoftException{
            MinCssPropertyParser propParser = new MinCssPropertyParser();
            propParser.initDefault();
            return parse(filename, propParser);
        }
	public static MinCssSelectorList parse(File filename, String resourcePropertyParserClases) 
			throws FileNotFoundException, IOException,MinCssParseException,
				MinCssSoftException{
            MinCssPropertyParser propParser = new MinCssPropertyParser();
            propParser.initFromResource(resourcePropertyParserClases);
            return parse(filename, propParser);
            
	}
        
        public static MinCssSelectorList parse(File filename, MinCssPropertyParser propParser) 
			throws FileNotFoundException, IOException,MinCssParseException,
				MinCssSoftException{
		System.setProperty("org.w3c.css.sac.parser", "org.w3c.flute.parser.Parser");
		InputSource source;
		try {
			source = new InputSource(new FileReader(filename));
			Parser parser = new ParserFactory().makeParser();

			MinCssParser demoSac = new MinCssParser(propParser);
			parser.setDocumentHandler(demoSac);
			parser.parseStyleSheet(source);
			
			return demoSac.getSelectorsParsed();
		} catch (ClassCastException e) {
			throw new MinCssSoftException(e);
		} catch (ClassNotFoundException e) {
			throw new MinCssSoftException(e);
		} catch (IllegalAccessException e) {
			throw new MinCssSoftException(e);
		} catch (InstantiationException e) {
			throw new MinCssSoftException(e);
		} catch (CSSException e) {
			throw new MinCssParseException(e);
		}
		
	}
        
	public static void main(String[] args) throws Exception {
		/*
		System.setProperty("org.w3c.css.sac.parser", "org.w3c.flute.parser.Parser");
		InputSource source = new InputSource();
		URL uri = new URL("file", null, -1, args[0]);
		InputStream stream = uri.openStream();

		source.setByteStream(stream);
		source.setURI(uri.toString());
		Parser parser = new ParserFactory().makeParser();

		DemoSac demoSac = new DemoSac();
		parser.setDocumentHandler(demoSac);
		parser.parseStyleSheet(source);
		stream.close();
		
		System.out.println(demoSac.getSelectorsParsed().toString());
		*/
		System.out.println(MinCssParser.parse(new File(args[0])).toString());
	}

	@Override
	public void comment(String arg0) throws CSSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endDocument(InputSource arg0) throws CSSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endFontFace() throws CSSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void endPage(String arg0, String arg1) throws CSSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void ignorableAtRule(String arg0) throws CSSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void importStyle(String arg0, SACMediaList arg1, String arg2)
			throws CSSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void namespaceDeclaration(String arg0, String arg1)
			throws CSSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startDocument(InputSource arg0) throws CSSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startFontFace() throws CSSException {
		// TODO Auto-generated method stub

	}

	@Override
	public void startPage(String arg0, String arg1) throws CSSException {
		// TODO Auto-generated method stub

	}    
}