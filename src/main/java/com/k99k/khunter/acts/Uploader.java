/**
 * 
 */
package com.k99k.khunter.acts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import com.k99k.khunter.Action;
import com.k99k.khunter.ActionMsg;

/**
 * 文件上传
 * @author keel
 *
 */
public class Uploader extends Action {

	/**
	 * @param name
	 */
	public Uploader(String name) {
		super(name);
	}
	
	
	private String savePath;
	
	

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#act(com.k99k.khunter.ActionMsg)
	 */
	@Override
	public ActionMsg act(ActionMsg msg) {
		
		
		
		return super.act(msg);
	}

	
	public String upload(HttpServletRequest request,String savePath,String clientPath){
		FileOutputStream fos = null;
        ServletInputStream  sis = null;
		try {
			//TODO 如果是上传任意文件,需要从form中取到上传文件的本地路径来确定文件名
			int po = clientPath.lastIndexOf("/");
			po = (po<0)?0:po;
	    	String filename = UUID.randomUUID().toString()+"__"+clientPath.substring(po); //UUID.randomUUID().toString()+".jpg";
	    	//System.out.println("filename:"+filename);
			 sis = request.getInputStream();
			 String toFile = savePath;//this.getServletContext().getRealPath("/") + "/images/upload/temppic/"+filename;
		 
            fos = new FileOutputStream(new File(toFile));
            
            byte[] bt = new byte[2048];  
            int count;  
            //因为按行读取最后一行会多读一个换行符,所以使用tmpb保存上一行尾部多余一个或两个byte,在新一行读取时根据情况写入
            byte[] tmpb = new byte[2];
            //尾部字节是否是一个,false表示有两个字节
            boolean one = false;
            //读第一行得到分隔标识
            sis.readLine(bt,0,bt.length);
            String firstLine = new String(bt).trim();
            int firstLineLen = firstLine.length();
          	//向后读6行,此为http协议中文件的head部分，
            for(int j = 0;j<7;j++){
            	sis.readLine(bt,0,bt.length);
            }
          	//文件从第二行开始接上一行尾部字节,appendLastTail为是否接上一行尾部byte
          	boolean appendLastTail = false;
            while ((count = sis.readLine(bt,0,bt.length)) > 0) { 
            	//第7行开始读,前几行均为文件头信息
                //文件中发现5个-(45的String就是-)时,判断是否是文件末尾
            	if((bt[0]==45)&&(bt[1]==45)&&(bt[2]==45)&&(bt[3]==45)&&(bt[4]==45)) {
            		String line = new String(bt);
            		if(line.length()>=firstLineLen){
            			line = line.substring(0,firstLineLen);
            			//当与firstLine分隔标识相同时，表示为文件末尾，跳出
            			if(line.equals(firstLine)){
            				break;
            			}
            		}
            	}
            	//从第8行开始写上一行的尾部字节
            	if(appendLastTail){
            		if(one){
                		fos.write(tmpb[0]);
                	}else{
                		fos.write(tmpb); 
                	}
            	}
            	if(count == 1){
            		//本行仅有一个字节,到下一行再作为尾部字节写入
            		tmpb[0] = bt[0];
            		one = true;
            	}else{
            		//写入除尾部字节外的本行字节
                	fos.write(bt, 0, count-2);
            		//保存尾部字节
                	tmpb[0] = bt[count-2];
                	tmpb[1] = bt[count-1];
                	one = false;
            	}
                appendLastTail = true;
            }  
	        //System.out.println("toFile:"+toFile); 
	        //System.out.println("basePath+filename:"+basePath+filename); 
			//out.clear();	
			//out.print(basePath+"/images/upload/temppic/"+filename);
			return filename;
		} catch (Exception ex) {
			ex.printStackTrace();
		}finally {
            try {
                fos.close();
                sis.close();
            } catch (IOException ignored) {
            }
        }
		return null;
	}


	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#reLoad()
	 */
	@Override
	public void reLoad() {
		super.reLoad();
	}


	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#exit()
	 */
	@Override
	public void exit() {

	}

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#getIniPath()
	 */
	@Override
	public String getIniPath() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.k99k.khunter.Action#init()
	 */
	@Override
	public void init() {

	}

}
