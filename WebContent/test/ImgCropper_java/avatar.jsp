<%@ page language="java" import="java.util.*,java.net.URLEncoder,java.io.*" session="false"  pageEncoding="utf-8"%>
<%!//编辑页面中包含 camera.swf 的 HTML 代码
	public String renderHtml(String id, String basePath) {
		// 把需要回传的自定义参数都组装在 input 里
		//$input = urlencode( "uid={$uid}" );
		String u = basePath + "/ImgCropper_java/avatar.jsp";
		String uc_api = null;
		try{
		uc_api = URLEncoder.encode(u,"utf-8");
		}catch(Exception e){
		}
		String urlCameraFlash = "camera.swf?nt=1&inajax=1&appid=1&input=1&uploadSize=1000&ucapi="
				+ uc_api;
		urlCameraFlash = "<script src=\"common.js?B6k\" type=\"text/javascript\"></script><script type=\"text/javascript\">document.write(AC_FL_RunContent(\"width\",\"450\",\"height\",\"253\",\"scale\",\"exactfit\",\"src\",\""
				+ urlCameraFlash
				+ "\",\"id\",\"mycamera\",\"name\",\"mycamera\",\"quality\",\"high\",\"bgcolor\",\"#000000\",\"wmode\",\"transparent\",\"menu\",\"false\",\"swLiveConnect\",\"true\",\"allowScriptAccess\",\"always\"));</script>";

		return urlCameraFlash;
	}

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
	}%>

<%
	String action = request.getParameter("a");
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/test";
			System.out.println("basePath:"+basePath);
			System.out.println("action:"+action);
	if (action == null) {
%>
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
	<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<script type="text/javascript">
	function updateavatar(){
		alert("OK!");
	}
	</script>
	</head>
	<body>
	<%
		out.print(renderHtml("5", basePath));
	%>
	</body></html>
	<%
		} else if ("uploadavatar".equals(action)) {//上传图片,可以自己实现
			System.out.println("action:uploadavatar");
			//FileUpload fu = null;
			//fu = new FileUpload();
			//fu.doUpload(session.getServletContext(), request);// 上传文件
			//String res = fu.getErrMessage();

			/*
			if (!res.equals("Success.")) {
				out.print("error");
			}else{
				Iterator ir = fu.tmpFiles.iterator();
				while (ir.hasNext()) {// 将文件写入正式目录或数据库
					String fpath = (String) ir.next();
					fpath=fpath.substring(fpath.lastIndexOf("/"));
					out.clear();
					out.print(basePath+"/FileUploadTmp"+fpath);
				}
				
			}
			 */
			 
		        FileOutputStream fos = null;
		        ServletInputStream  sis = null;
			try {
				
		    	String filename = "/test/ImgCropper_java/testA.jpg";
		    	System.out.println("filename:"+filename);
				 sis = request.getInputStream();
				 String toFile = this.getServletContext().getRealPath("/") + filename;
				 
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
		            
		            
		            
		        System.out.println("toFile:"+toFile); 
		        
				out.clear();	
				out.print(basePath+"/ImgCropper_java/testA.jpg");
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
			String pa = this.getServletContext().getRealPath("/test/ImgCropper_java");
			boolean re = saveFile(pa+"/testA1.jpg", getFlashDataDecode(avatar1));
			re =  saveFile(pa+"/testA2.jpg", getFlashDataDecode(avatar2));
			re =  saveFile(pa+"/testA3.jpg", getFlashDataDecode(avatar3));
			if (re) {
				out.print("<?xml version=\"1.0\" ?><root><face success=\"1\"/></root>");
			} else {
				out.print("<?xml version=\"1.0\" ?><root><face success=\"0\"/></root>");
			}

		}
	%>
