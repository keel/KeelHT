/**
 * 
 */
package com.k99k.khunter;

import org.apache.log4j.Logger;
import com.k99k.tools.StringUtil;

/**
 * 错误码集合,erroCode前两位为1级下标,后三位为2级下标
 * @author keel
 *
 */
public final class ErrorCode {
	
	private ErrorCode(){
	}
	
	/**
	 * 二维数组,第一维为分类，第二维定位到具体的错误码
	 */
	private static final String[][] errorArr = createErr();
	
	
	/**
	 * 实始化所有错误码,二维数组,第一维为分类，第二维定位到具体的错误码
	 * @return String[100][1000]
	 */
	private static final String[][] createErr(){
		
		String[][] errArr = new String[100][1000];
		
		//所有的0位表示ok
		for (int i = 0; i < errArr.length; i++) {
			errArr[i][0] = "ok";
		}
		
		errArr[4][4] = "page not found.";
		
		//KIoc
		errArr[9][10] = "KIoc-saveJsonIni ini not found.";
		errArr[9][12] = "KIoc-saveJsonIni json para error.";
		errArr[9][13] = "KIoc-saveJsonIni writeTxtInUTF8 failed.";
		errArr[9][14] = "KIoc-saveJsonIni json validate fail";
		errArr[9][15] = "KIoc-loadClassInstance Exception throwed.className:";
		errArr[9][16] = "KIoc-setProp Method can't be found:";
		errArr[9][17] = "KIoc-setProp Exception throwed.propName:";
		errArr[9][18] = "KIoc-setProps one of the props can't be found,para position:";
		errArr[9][19] = "KIoc-setProps Exception throwed.para position:";
		errArr[9][20] = "KIoc-readTxt UnsupportedEncodingException filePath:";
		errArr[9][21] = "KIoc-readTxt IOException filePath:";
		errArr[9][22] = "KIoc-writeTxtInUTF8 IOException throwed.file:";
		errArr[9][23] = "KIoc-updateIniFileNode Exception throwed. ini:";
		errArr[9][24] = "KIoc-updateIniFileNode(ArrayList) Exception throwed. ini:";
		
		
		//KObjAction
		errArr[15][1] = "KObjAction-checkKObjJson failed.";
		errArr[15][2] = "KObjAction-save failed - save bak file failed:";
		errArr[15][3] = "KObjAction-init kobjs node not exist! KObjAction init failed.";
		errArr[15][4] = "KObjAction-init Exception throwed.";
		errArr[15][5] = "KObjAction-createKObjTable dao not found:";
		errArr[15][6] = "KObjAction-createKObjTable JSON error";
		errArr[15][7] = "KObjAction-createKObjTable update kobj.json failed.";
		errArr[15][8] = "KObjAction- ";
		
		
		
		
		
		
		
		
		
		return errArr;
	}
	
	
	
	
	
	
	
	
//	static{
////		error.put("404", "page not found.");
//		
//		errorArr[1][0] = "000.";
//		errorArr[4][4] = "page not found.";
//	}
	
	
	/**
	 * 获 取错误信息,erroCode前两位为1级下标,后三位为2级下标
	 * @param errorCode 共4-5位int,前两位为1级下标,后三位为2级下标
	 * @return errMsg 若无定义则返回""
	 */
	public static final String getErrorInfo(int errCode){
		//先保证位数正确
		if (errCode>99999 || errCode<1000) {
			return "";
		}
		String str = String.valueOf(errCode);
		//如果是4位则1级下标仅有1位
		int foreLen = (errCode > 9999) ? 2 : 1;
		int errCode1 = Integer.parseInt(str.substring(0,foreLen));
		int errCode2 = Integer.parseInt(str.substring(foreLen));
		return StringUtil.objToStrNotNull(errorArr[errCode1][errCode2]);
	}
	
	/**
	 * 日志记录错误
	 * @param log
	 * @param errorCode
	 * @param e Exception
	 */
	public static final void logError(Logger log,int errorCode,Exception e,String plusInfo){
		log.error(errorCode+" : "+getErrorInfo(errorCode)+plusInfo,e);
	}
	
	/**
	 * 日志记录错误
	 * @param log
	 * @param errorCode
	 */
	public static final void logError(Logger log,int errorCode,String plusInfo){
		log.error(errorCode+" : "+getErrorInfo(errorCode)+plusInfo);
	}
	
	
	/**
	 * 获 取错误信息,erroCode前两位为1级下标,后三位为2级下标
	 * @param errCode1 1-2位int
	 * @param errCode2 3位int 
	 * @return errMsg 若无定义则返回""
	 */
	public static final String getErrorInfo(int errCode1,int errCode2){
		//先保证位数正确
		if (errCode1>100 || errCode1<=0 || errCode2>1000 || errCode2<0) {
			return "";
		}
		return StringUtil.objToStrNotNull(errorArr[errCode1][errCode2]);
	}
	
	/**
	 * 日志记录错误
	 * @param log
	 * @param errCode1 1-2位int
	 * @param errCode2 3位int 
	 * @param e Exception
	 */
	public static final void logError(Logger log,int errCode1,int errCode2,Exception e,String plusInfo){
		log.error(errCode1+"-"+errCode2+" : "+getErrorInfo(errCode1,errCode2)+plusInfo,e);
	}
	
	/**
	 * 日志记录错误
	 * @param log
	 * @param errCode1 1-2位int
	 * @param errCode2 3位int 
	 */
	public static final void logError(Logger log,int errCode1,int errCode2,String plusInfo){
		log.error(errCode1+"-"+errCode2+" : "+getErrorInfo(errCode1,errCode2));
	}
	
	/**
	 * 获取所有error的String[][]
	 * @return
	 */
	public static final String[][] getErrorArr(){
		return errorArr;
	}
	
	/**
	 * 新增或更新一个error code,errCode共4-5位int,前两位为1级下标,后三位为2级下标
	 * @param errCode1 1-2位int
	 * @param errCode2 3位int 
	 * @param errMsg String
	 */
	public static final void saveErrCode(int errCode1,int errCode2,String errMsg){
		errorArr[errCode1][errCode2] = errMsg;
	}
	
	
	
//	private final static HashMap<String,String> error = new HashMap<String, String>(200);
//
//	/**
//	 * 获 取错误信息
//	 * @param errorCode
//	 * @return errMsg
//	 */
//	public static final String getErrorInfo(String errorCode){
//		return error.get(errorCode);
//	}
//	
//	/**
//	 * 日志记录错误
//	 * @param log
//	 * @param errorCode
//	 * @param e Exception
//	 */
//	public static final void logError(Logger log,String errorCode,Exception e){
//		log.error(errorCode+" : "+error.get(errorCode),e);
//	}
//	
//	/**
//	 * 日志记录错误
//	 * @param log
//	 * @param errorCode
//	 */
//	public static final void logError(Logger log,String errorCode){
//		log.error(errorCode+" : "+error.get(errorCode));
//	}
//	
//	/**
//	 * 获取所有error的Map
//	 * @return
//	 */
//	public static final HashMap<String,String> getErrMap(){
//		return error;
//	}
//	
//	/**
//	 * 新增或更新一个error code
//	 * @param errCode
//	 * @param errMsg
//	 */
//	public static final void saveErrCode(String errCode,String errMsg){
//		error.put(errCode, errMsg);
//	}
}
