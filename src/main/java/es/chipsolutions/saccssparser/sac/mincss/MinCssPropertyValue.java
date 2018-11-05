package es.chipsolutions.saccssparser.sac.mincss;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;

import es.chipsolutions.saccssparser.sac.mincss.exceptions.MinCssInvalidPropertyException;
import es.chipsolutions.saccssparser.sac.mincss.exceptions.MinCssInvalidPropertyValueException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.css.sac.LexicalUnit;

public abstract class MinCssPropertyValue {

	private static HashMap<String, MinCssPropertyValue> properties = new HashMap<String, MinCssPropertyValue>();
	
	public abstract String getPropertyName();
	
	
	public static MinCssPropertyValue parse(String property, LexicalUnit l) throws MinCssInvalidPropertyException, MinCssInvalidPropertyValueException{
		if (!properties.containsKey(property)){
			throw new MinCssInvalidPropertyException();
		}
		MinCssPropertyValue p = properties.get(property);		
		return p.parse(l);
	}
	protected abstract MinCssPropertyValue parse(LexicalUnit l) throws MinCssInvalidPropertyValueException;
	
	static {
		String fileContents = getResource("es/chipsolutions/saccssparser/sac/mincss/cssproperties.json");
		JSONObject obj = new JSONObject(fileContents);
		JSONArray arr = obj.getJSONArray("props");
		for (int i = 0; i < arr.length(); i++){
		    String className = arr.getString(i);
		    Class<MinCssPropertyValue> cl;
			try {
				cl = (Class<MinCssPropertyValue>) Class.forName(className);
				MinCssPropertyValue p = cl.newInstance();
			    properties.put(p.getPropertyName(),p);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		}
		/*
		Reflections reflections = new Reflections("org.i3dat.sac.mincss");    
		Set<Class<? extends MinCssPropertyValue>> classes = reflections.getSubTypesOf(MinCssPropertyValue.class);
		Iterator <Class<? extends MinCssPropertyValue>> it = classes.iterator();
		while(it.hasNext()){
			Class<? extends MinCssPropertyValue> cl = it.next();
			try {
				Constructor<? extends MinCssPropertyValue> cons = cl.getConstructor();
				MinCssPropertyValue p = cons.newInstance();		
				properties.put(p.getPropertyName(),p);
			}catch (NoSuchMethodException e){
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		*/
	}
	public static String getResource(String resourceName){
		ClassLoader classLoader = MinCssPropertyValue.class.getClassLoader();
		
		InputStream inputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		int ctr;
		try {
			inputStream = classLoader.getResourceAsStream(resourceName);
		    ctr = inputStream.read();
		    while (ctr != -1) {
		        byteArrayOutputStream.write(ctr);
		        ctr = inputStream.read();
		    }
		    inputStream.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		return byteArrayOutputStream.toString();

	}
	public static String getFile(String fileName) {

		StringBuilder result = new StringBuilder("");

		//Get file from resources folder
		ClassLoader classLoader = MinCssPropertyValue.class.getClassLoader();
		File file = new File(classLoader.getResource(fileName).getFile());
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				result.append(line).append("\n");
			}

			scanner.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if (scanner != null) scanner.close();
		}
			
		return result.toString();

	  }
}
