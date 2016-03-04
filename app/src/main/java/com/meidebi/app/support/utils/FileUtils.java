package com.meidebi.app.support.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;

import com.meidebi.app.XApplication;

public class FileUtils {
	
//	private static   String getSdCardPath() = "/sdcard/";
	private static   String path = "meidebi/";
	
    private static String getSdCardPath() {
        if (isExternalStorageMounted()) {
            return XApplication.getInstance().getExternalCacheDir().getAbsolutePath();
        } else {
            return "";
        }
    }


    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }
	  public static String getFilePathFromUrl(String url) {

	        if (!isExternalStorageMounted())
	            return  "";

	        if (TextUtils.isEmpty(url))
	            return "";

	        int index = url.lastIndexOf("/");

	        String s = url.substring(index);

	        String filename = s.substring(s.indexOf("/"));

	        return  getSdCardPath()+path + filename;
	    }
	  
	  public static String getFileNameFromUrl(String url) {
	       String fileName = "";
	        if (url == null) {
	            return null;
	        }
	        int start = url.lastIndexOf("/");
	        int end = url.lastIndexOf("?");
	        if (start >= 0) {
	            fileName = url.substring(start + 1);
	            if (end >= 0) {
	                fileName = url.substring(start , end);
	            }
	        }
	        return fileName;
	    }
	  
	  
	    public static boolean isExternalStorageMounted() {

	        boolean canRead = Environment.getExternalStorageDirectory().canRead();
	        boolean onlyRead = Environment.getExternalStorageState().equals(
	                Environment.MEDIA_MOUNTED_READ_ONLY);
	        boolean unMounted = Environment.getExternalStorageState().equals(
	                Environment.MEDIA_UNMOUNTED);

	        return !(!canRead || onlyRead || unMounted);
	    }
	    
	    public static File createNewFileInSDCard(String absolutePath) {
	        if (!isExternalStorageMounted()) {
	            return null;
	        }

	        File file = new File(absolutePath);
	        if (file.exists()) {
	            return file;
	        } else {
	            File dir = file.getParentFile();
	            if (!dir.exists()) {
	                dir.mkdirs();
	            }

	            try {
	                if (file.createNewFile()) {
	                    return file;
	                }
	            } catch (IOException e) {
	                return null;
	            }
	        }
	        return null;
	    }

	    public static Bitmap decodeFile(File f) {
            try {
                    // decode image size
                    BitmapFactory.Options o = new BitmapFactory.Options();
                    o.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(new FileInputStream(f), null, o);

                    // Find the correct scale value. It should be the power of 2.
                    final int REQUIRED_SIZE = 100;
                    int width_tmp = o.outWidth, height_tmp = o.outHeight;
                    int scale = 1;
                    while (true) {
                            if (width_tmp / 2 < REQUIRED_SIZE
                                            || height_tmp / 2 < REQUIRED_SIZE)
                                    break;
                            width_tmp /= 2;
                            height_tmp /= 2;
                            scale *= 2;
                    }

                    // decode with inSampleSize
                    BitmapFactory.Options o2 = new BitmapFactory.Options();
                    o2.inSampleSize = scale;
                    return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
            } catch (FileNotFoundException e) {
            }
            return null;
    	}
	    
		/**
		 * ����ͼƬ��SD��
		 * 
		 * @param URL
		 * @param data
		 * @throws java.io.IOException
		 */
		public static void saveImage(String filePath, byte[] data) throws IOException {
			saveData(filePath, data);
		}

		/**
		 * ��ȡͼƬ
		 * 
		 * @param filename
		 * @return
		 * @throws java.io.IOException
		 */
		public static byte[] readImage(String filePath) throws IOException {
 			byte[] tmp = readData( filePath);
			return tmp;
		}

		/**
		 * ��ȡͼƬ����
		 * 
		 * @param path
		 * @param name
		 * @return
		 * @throws java.io.IOException
		 */
		private static byte[] readData(String paths) throws IOException {
			// String name = MyHash.mixHashStr(url);
			ByteArrayBuffer buffer = null;
 
			File file = new File(paths);
			if (!file.exists()) {
				return null;
			}
			InputStream inputstream = new FileInputStream(file);
			buffer = new ByteArrayBuffer(1024);
			byte[] tmp = new byte[1024];
			int len;
			while (((len = inputstream.read(tmp)) != -1)) {
				buffer.append(tmp, 0, len);
			}
			inputstream.close();
			return buffer.toByteArray();
		}

		/**
		 * ͼƬ���湤����
		 * 
		 * @param path
		 * @param fileName
		 * @param data
		 * @throws java.io.IOException
		 */
		public static void saveData( String fileName, byte[] data)
				throws IOException {
			// String name = MyHash.mixHashStr(AdName);
			createNewFileInSDCard(fileName);
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream outStream = new FileOutputStream(file);
			outStream.write(data);
			outStream.close();
		}

		/**
		 * �ж��ļ��Ƿ���� true���� false������
		 * 
		 * @param url
		 * @return
		 */
		public static boolean compare(String filePath) {
			
			File file = new File(filePath);
			if (!file.exists()) {
				return false;
			}
			return true;
		}

	  public static void saveToSD(String imageUrl) throws IOException{
		  byte[] data = readImage(imageUrl);
 
			saveData(getFilePathFromUrl(imageUrl), data);
	}
	  public static void delFolder(String folderPath) {
	      try {
	              delAllFile(folderPath); //ɾ����������������
	              String filePath = folderPath;
	              filePath = filePath.toString();
	              File myFilePath = new File(filePath);
	              myFilePath.delete(); //ɾ����ļ���

	      }
	      catch (Exception e) {
	              e.printStackTrace();

	      }
	}

	  public static void delAllFile(String path) {
	      File file = new File(path);
	      if (!file.exists()) {
	              return;
	      }
	      if (!file.isDirectory()) {
	     return;
	      }
	      String[] tempList = file.list();
	      File temp = null;
	      for (int i = 0; i < tempList.length; i++) {
	              if (path.endsWith(File.separator)) {
	                      temp = new File(path + tempList[i]);
	              }
	              else {
	                      temp = new File(path + File.separator + tempList[i]);
	              }
	              if (temp.isFile()) {
	                      temp.delete();
	              }
	              if (temp.isDirectory()) {
	                      delAllFile(path+"/"+ tempList[i]);//��ɾ���ļ���������ļ�
	                      delFolder(path+"/"+ tempList[i]);//��ɾ����ļ���
	              }
	      }
	}
	  /** 
	   * �����ļ� 
	   * @param bm 
	   * @param fileName 
	 * @return 
	   * @throws java.io.IOException
	   */  
	  public static Bitmap bmsaveFile(Bitmap bm, String fileName) throws IOException { 
	     

	      File myCaptureFile = new File(fileName);  
	 
	      BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));  
	      bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);  
	      bos.flush();  
	      bos.close();  
	      return bm;
	  }
	  public static byte[] readStream(InputStream inStream) throws Exception { 
	      byte[] buffer = new byte[1024]; 
	      int len = -1; 
	      ByteArrayOutputStream outStream = new ByteArrayOutputStream(); 
	      while ((len = inStream.read(buffer)) != -1) { 
	               outStream.write(buffer, 0, len); 
	      } 
	      byte[] data = outStream.toByteArray(); 
	      outStream.close(); 
	      inStream.close(); 
	      return data; 

	 } 
	  public static Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) { 
	      if (bytes != null) 
	          if (opts != null) 
	              return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,opts); 
	          else 
	              return BitmapFactory.decodeByteArray(bytes, 0, bytes.length); 
	      return null; 
	  } 
	  
	  public static int CopySdcardFile(String fromFile, String toFile)
	  {

	  try 
	  {
	  InputStream fosfrom = new FileInputStream(fromFile);
	  OutputStream fosto = new FileOutputStream(toFile);
	  byte bt[] = new byte[1024];
	  int c;
	  while ((c = fosfrom.read(bt)) > 0) 
	  {
	  fosto.write(bt, 0, c);
	  }
	  fosfrom.close();
	  fosto.close();
	  return 0;

	  } catch (Exception ex) 
	  {
	  return -1;
	  }
	  }
	  
		public static Bitmap loadImageFromUrl(Context context, String imageUrl) {
			Bitmap bitmap = null;
			try {
				String filepath=getFilePathFromUrl(imageUrl);
				boolean isExists = compare(filepath);
				if (isExists == false) {
					Net net = new Net();
					byte[] data = net.downloadResource(context, imageUrl);
					bitmap = BitmapFactory
							.decodeByteArray(data, 0, data.length);
					 saveImage(filepath, data);
				} else {
					byte[] data =  readImage(filepath);
					bitmap = BitmapFactory
							.decodeByteArray(data, 0, data.length);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			return bitmap;
		}
	 
}
