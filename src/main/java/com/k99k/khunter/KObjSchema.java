/**
 * 
 */
package com.k99k.khunter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.k99k.tools.JSONTool;

/**
 * KObj与Dao结合使用的schema，用于产生空的KObj和填充及设置KObj属性
 * @author keel
 *
 */
public class KObjSchema {
	
	static final Logger log = Logger.getLogger(KObjSchema.class);
	/**
	 * 保存schema的ArrayList,用于验证字段
	 */
	private final ArrayList<KObjColumn> schemaList = new ArrayList<KObjColumn>();
	
	/**
	 * 保存schema的HashMap,用于获取某一字段
	 */
	private final HashMap<String,KObjColumn> schemaMap = new HashMap<String, KObjColumn>();
	
	/**
	 * 初始化Schema,注意父Schema定义必须先于子Schema
	 * @param schemaDefineList
	 * @return 是否初始化成功
	 */
	public boolean initSchema(ArrayList<HashMap<String,Object>> schemaDefineList){
		int i = 0;
		for (Iterator<HashMap<String, Object>> iterator = schemaDefineList.iterator(); iterator.hasNext();) {
			HashMap<String, Object> map = iterator.next();
			//验证Column的key和value类型
			if(!JSONTool.checkMapTypes(map,new String[]{"col","def","type","intro","len"},new Class[]{String.class,Object.class,Integer.class,String.class,Integer.class})){
				ErrorCode.logError(log, 8,1, ""+i);
				return false;
			}
			
			String col = (String) map.get("col");
			Object def = map.get("def");
			int type = Integer.parseInt(map.get("type").toString());
			String intro = (String) map.get("intro");
			int len = Integer.parseInt(map.get("len").toString());
			
			//验证type和def
			if (!KObjColumn.checkType(type) || !KObjColumn.checkColType(def, type)) {
				ErrorCode.logError(log, 8,2, "index-"+i+" type:"+type+" default:"+def);
				return false;
			}
			KObjColumn  k = new KObjColumn(col,def,type,intro,len);	
			//设置验证器
			if (map.containsKey("validator")) {
				Object o = map.get("validator");
				if (o instanceof String) {
				}else{
					ErrorCode.logError(log, 8,5, "index-"+i+" validator is not String:"+o);
					return false;
				}
				//"com.k99k.khunter.StringValidator,0,5"为class+type+paras
				
				String[] vaArr = o.toString().split(",");
				String vClass = vaArr[0];
				int vType = Integer.parseInt(vaArr[1]);
				String[] vParas = new String[vaArr.length-2];
				for (int j = 0; j < vaArr.length-2; j++) {
					vParas[j] = vaArr[j+2];
				}
				
				Object v = KIoc.loadClassInstance(HTManager.getClassPath(), vClass);
				if (v instanceof KObjColumnValidate) {
					KObjColumnValidate validator = (KObjColumnValidate)o;
					//设置参数并初始化
					validator.initType(vType, vParas);
					k.setValidator(validator);
				}else{
					ErrorCode.logError(log, 8,5, "index-"+i+" validator is not KObjColumnValidate:"+o);
					return false;
				}
			}
			//父Schema必须先定义
			this.schemaMap.put(col, k);
			//处理子Column的情况
			if (col.indexOf('.') > 0) {
				//获取父Schema
				int lastDotPosi = col.lastIndexOf('.');
				String pre = col.substring(0,lastDotPosi);
				String subKey = col.substring(lastDotPosi+1);
				KObjColumn preK = this.schemaMap.get(pre);
				if (preK == null) {
					ErrorCode.logError(log, 8,3, "index-"+i+" col:"+col);
					return false;
				}
				if (preK.getType() == 2) {
					//父Schema为HashMap
					preK.setSubColumn(subKey, k);
				}else if(preK.getType() == 3){
					//父Schema为ArrayList
					preK.setSubColumn(k);
				}else{
					ErrorCode.logError(log, 8,4, "index-"+i+" col:"+col);
					return false;
				}
				
			}else{
				schemaList.add(k);
			}
			
			i++;
		}
		
		return true;
	}
	

	/**
	 * 验证某一字段值
	 * @param kobjPath 字段的路径,如:tags.tagName
	 * @param value
	 * @return 字段不存在或验证失败时返回false
	 */
	public  boolean validate(String kobjPath,Object value){
		KObjColumn k = this.schemaMap.get(kobjPath);
		if (k == null) {
			return false;
		}
		return k.validateColumn(value);
	}
	
	/**
	 * 验证整个KObj的HashMap
	 * @param kobjMap
	 * @return
	 */
	public  boolean validate(HashMap<String,Object> kobjMap){
		for (Iterator<KObjColumn> iterator = this.schemaList.iterator(); iterator.hasNext();) {
			KObjColumn kc = iterator.next();
			String col = kc.getCol();
			Object o = kobjMap.get(col);
			if (o == null) {
				return false;
			}
			if(!kc.validateColumn(o)){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 设置KObject属性,注意在kobjPath中不包含List，否则返回false
	 * @param kobj
	 * @param kobjPath
	 * @param prop
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean setProp(KObject kobj,String kobjPath,Object prop){
		if (!this.validate(kobjPath, prop)) {
			return false;
		}
		int dotPosi = kobjPath.indexOf('.');
		if (dotPosi >= 0) {
//			String preKey = kobjPath.substring(0,dotPosi);
//			KObjColumn k = this.schemaMap.get(preKey);
			String[] paths = kobjPath.split("\\.");
			HashMap<String,Object> target = kobj.getPropMap();
			for (int i = 0; i < paths.length-1; i++) {
				if (paths[i].equals("*")) {
					//ArrayList
					return false;
				}else{
					target = (HashMap<String, Object>) target.get(paths[i]);
				}
			}
			target.put(paths[paths.length-1], prop);
		}else{
			kobj.setProp(kobjPath, prop);
		}
		return true;
	}
	
	

	/**
	 * 创建一个空的KObject,注意只涉及一级KObjColumn设置，子KObjColumn不会再处理,均按父KObjColumn中的默认值设置
	 * @return KObject
	 */
	public KObject createEmptyKObj(){
		KObject kobj = new KObject();
		for (Iterator<KObjColumn> iterator = this.schemaList.iterator(); iterator.hasNext();) {
			KObjColumn kc = iterator.next();
			String col = kc.getCol();
			kobj.setProp(col, kc.getDef());
		}
		return kobj;
	}
	
	public KObject createKObj(KObject kobj){
		
		return null;
		
		
		
	}
	
	public KObject setKObjProp(KObject kobj,String propPath,Object prop){
		
		
		return null;
	}
	
}
