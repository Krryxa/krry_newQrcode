package qrcode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import com.swetake.util.Qrcode;

/**
 * QrcodeText 二维码测试类
 * @author krry
 * @version 1.0
 *
 */
public class QrcodeText{

		public static String qrcode(String message,String type,HttpServletRequest request){
			String pathName = null;
			FileOutputStream outputStream = null;
			
			try{
				//创建二维码对象
				Qrcode qrcode = new Qrcode();
				//设置二维码的纠错级别
				//L(7%) M(15%) Q(25%) H(30%)
				qrcode.setQrcodeErrorCorrect('L'); //一般纠错级别小一点
				//设置二维码的编码模式 Binary(按照字节编码模式)
				qrcode.setQrcodeEncodeMode('B');
				//设置二维码的版本号 1-40  1:20*21    2:25*25   40:177*177
				qrcode.setQrcodeVersion(7);
				
				//获取图片缓存流对象
				int width = 235;
				int height = 235;
				BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				
				//获取画笔
				Graphics2D gs = image.createGraphics();
				//设置背景颜色为白色
				gs.setBackground(Color.WHITE);
				//设置画笔的颜色红色
				gs.setColor(Color.BLACK);
				//绘制矩形
				gs.clearRect(0, 0, width, height);
				
				//设置内容
				String content = message;
				byte[] contentsBytes = content.getBytes("utf-8");
				//绘制方法
				//设置偏移量，不设置可能导致解析出错
				int pixoff = 5;
				boolean[][] code = qrcode.calQrcode(contentsBytes);
				
				if(type.equals("1")){
					for(int i=0;i<code.length;i++){
						for(int j=0;j<code.length;j++){
							if(code[j][i]){
								gs.fillRect(j*5+pixoff, i*5+pixoff, 5, 5);
							}
						}
					}
				}else{
					//取素材
					Toolkit tool = Toolkit.getDefaultToolkit();
					String aspath = request.getServletContext().getRealPath("/ImageResource");
					
					//该方法第一次无法加载完全图片
//					Image image_eye = tool.getImage(aspath+"\\"+type+"\\eye.png");
//					Image image1 = tool.getImage(aspath+"\\"+type+"\\1.png");
//					Image image2 = tool.getImage(aspath+"\\"+type+"\\2.png");
//					Image image3 = tool.getImage(aspath+"\\"+type+"\\3.png");
					
					//可以第一次加载完全图片
					BufferedImage image_eye = ImageIO.read(new FileInputStream(aspath+"\\"+type+"\\eye.png"));
					BufferedImage image1 = ImageIO.read(new FileInputStream(aspath+"\\"+type+"\\1.png"));
					BufferedImage image2 = ImageIO.read(new FileInputStream(aspath+"\\"+type+"\\2.png"));
					BufferedImage image3 = ImageIO.read(new FileInputStream(aspath+"\\"+type+"\\3.png"));
					
					
					//绘制码眼
					gs.drawImage(image_eye, 5, 5, 35, 35, null);
					gs.drawImage(image_eye, (code.length-7)*5+5, 5, 35, 35, null);
					gs.drawImage(image_eye, 5, (code.length-7)*5+5, 35, 35, null);
					
					for(int i=0;i<7;i++){
						//码眼部分全部设置为零
						for(int j=0;j<7;j++){
							code[i][j]=false;
						}
						for(int j=code.length-7;j<code.length;j++){
							code[i][j]=false;
							code[j][i]=false;
						}
					}
					
					//绘制
					for(int i=0;i<code.length;i++){
						for(int j=0;j<code.length;j++){
							//1*3
							if(code[i][j]){
								if(i+2<code.length&&code[i+1][j]&&code[i+2][j]){
									gs.drawImage(image3, j*5+5, i*5+5, 5, 15, null);
									//gs.drawImage(image13, i, j, null);
									code[i+2][j]=false;
									code[i+1][j]=false;
									
								}else if(i+1<code.length&&code[i+1][j]){
									//1*2
									gs.drawImage(image2, j*5+5, i*5+5, 5, 10, null);
									code[i+1][j]=false;
								}else{
									gs.drawImage(image1, j*5+5, i*5+5, 5, 5, null);
								}
							}
						}
					}
				}
				
				
				//释放画笔
				gs.dispose();
				//生成二维码图片
				String realPath = request.getRealPath("/");
				//String realPath = "D:\\apache-tomcat-8.0.33\\webapps\\krry_Qrcode\\upload\\";
				pathName = new Date().getTime()+".png";
				outputStream = new FileOutputStream(new File(realPath+"upload\\", pathName));
				ImageIO.write(image, "png", outputStream);
				
				System.out.println("二维码图片生成成功!");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try {
					//关闭流
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return pathName;
		}
}
