/**
 * 
 */
package com.k99k.tools;

import java.util.HashMap;

import org.stringtree.json.JSONReader;
import org.stringtree.json.JSONValidatingReader;

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
	 * 使用JSONReader读取json的String
	 * @param in String
	 * @return HashMap
	 */
	@SuppressWarnings("unchecked")
	public static final HashMap<String,Object> readJsonString(String in){
		JSONReader jsonReader = new JSONValidatingReader();
		return (HashMap<String,Object>)jsonReader.read(in);
	}

}
