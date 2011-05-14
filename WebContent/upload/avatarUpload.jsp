<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.k99k.khunter.*,java.util.*,java.net.URLEncoder,java.io.*" session="false" %>
<%!//编辑页面中包含 camera.swf 的 HTML 代码
String sPrefix = KFilter.getStaticPrefix();
String prefix = KFilter.getPrefix();

/*
 *  注意在本页修改后，一定要根据目前图片的尾号重新定义picCC的值!!!!!!
 */
int picCC = 10;

	public String getFileExt(String fileName) {
		int dotindex = fileName.lastIndexOf(".");
		String extName = fileName.substring(dotindex, fileName.length());
		extName = extName.toLowerCase(); //置为小写
		return extName;
	}

	private byte[] getFlashDataDecode(String src) {
		char[] s = src.toCharArray();
		int len = s.length;
		byte[] r = new byte[len / 2];
		for (int i = 0; i < len; i = i + 2) {
			int k1 = s[i] - 48;
			k1 -= k1 > 9 ? 7 : 0;
			int k2 = s[i + 1] - 48;
			k2 -= k2 > 9 ? 7 : 0;
			r[i / 2] = (byte) (k1 << 4 | k2);
		}
		return r;
	}
	

	public boolean saveFile(String path, byte[] b) {
		try {
			FileOutputStream fs = new FileOutputStream(path);
			fs.write(b, 0, b.length);
			fs.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	%>
<%
	String action = request.getParameter("a");
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path;
			//System.out.println("basePath:"+basePath);
			//System.out.println("action:"+action);
	if (action == null) {

		} else if ("uploadavatar".equals(action)) {//上传图片,可以自己实现
		        FileOutputStream fos = null;
		        ServletInputStream  sis = null;
			try {
				//UUID.randomUUID().toString();
		    	String filename = UUID.randomUUID().toString()+".jpg";
		    	//System.out.println("filename:"+filename);
				 sis = request.getInputStream();
				 String toFile = this.getServletContext().getRealPath("/") + "/images/upload/temppic/"+filename;
				 
		            fos = new FileOutputStream(new File(toFile));
		            
		            byte[] bt = new byte[1024*2];  
		            int count;  
		            int i = 0;
		            int delayAppend;
		            while ((count = sis.readLine(bt,0,bt.length)) > 0) { 
		                if(i>7){
		                	if((bt[0]==45)&&(bt[1]==45)&&(bt[2]==45)&&(bt[3]==45)&&(bt[4]==45)) {
		                		break;
		                	}
		                	fos.write(bt, 0, count);  
		                }
		                i++;
		            }  
		        //System.out.println("toFile:"+toFile); 
		        //System.out.println("basePath+filename:"+basePath+filename); 
				out.clear();	
				out.print(basePath+"/images/upload/temppic/"+filename);
			} catch (Exception ex) {
				//ex.printStackTrace();
				System.out.println("exception:"+ex.getMessage()); 
				out.print("error");
			}
			finally {
	            try {
	                fos.close();
	                sis.close();
	            } catch (IOException ignored) {
	            }
	        }
		} else if ("rectavatar".equals(action)) {//缩略图
			String avatar1 = request.getParameter("avatar1");//大
			String avatar2 = request.getParameter("avatar2");//中
			String avatar3 = request.getParameter("avatar3");//小
			out.clear();
			//利用input参数传用户名
			String input = request.getParameter("input");
			//System.out.println("input:"+input); 
			String pa = this.getServletContext().getRealPath("/images/upload")+"/";
			//boolean re = saveFile(pa+picCC+"_1.jpg", getFlashDataDecode(avatar1));
			//re =  saveFile(pa+picCC+"_2.jpg", getFlashDataDecode(avatar2));
			//re =  saveFile(pa+picCC+"_3.jpg", getFlashDataDecode(avatar3));
			boolean re = saveFile(pa+input+"_1.jpg", getFlashDataDecode(avatar1));
			re =  saveFile(pa+input+"_2.jpg", getFlashDataDecode(avatar2));
			re =  saveFile(pa+input+"_3.jpg", getFlashDataDecode(avatar3));
			picCC++;
			if (re) {
				out.print("<?xml version=\"1.0\" ?><root><face success=\"1\"/></root>");
			} else {
				out.print("<?xml version=\"1.0\" ?><root><face success=\"0\"/></root>");
			}

		}
	%>