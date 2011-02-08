/**
 * 
 */
package com.k99k.tools;

import java.util.HashMap;

import org.stringtree.json.JSONErrorListener;
import org.stringtree.json.JSONReader;
import org.stringtree.json.JSONValidatingReader;
import org.stringtree.json.JSONValidator;
import org.stringtree.json.JSONWriter;

/**
 * @author keel
 *
 */
public final class JSONTool {

	/**
	 * 不可实例化
	 */
	private JSONTool() {
	}
	
	/**
	 * 使用JSONValidatingReader读取json的String
	 * @param in String
	 * @return HashMap
	 */
	@SuppressWarnings("unchecked")
	public static final HashMap<String,Object> readJsonString(String in){
		JSONReader jsonReader = new JSONValidatingReader();
		return (HashMap<String,Object>)jsonReader.read(in);
	}
	
	/**
	 * 使用JSONWriter将HashMap转成String形式的json
	 * @param map
	 * @return json String
	 */
	public static final String writeJsonString(HashMap<String,Object> map){
		JSONWriter jsonWriter = new JSONWriter();
		return jsonWriter.write(map);
	}
	
	/**
	 * 验证json
	 * @param in String
	 * @return boolean 是否通过验证
	 */
	public static final boolean validateJsonString(String in){
		JSONValidator vali = new JSONValidator(new JSONErrorListener() {
			
			@Override
			public void start(String text) {
				
			}
			
			@Override
			public void error(String message, int column) {
				
			}
			
			@Override
			public void end() {
				
			}
		});
		return vali.validate(in);
	}
	
	public static void main(String[] args) {
		HashMap m = new HashMap();
		m.put("str", "sfsf");
		m.put("int", 2233);
		m.put("boolean", true);
		System.out.println(writeJsonString(m));
	}

}
