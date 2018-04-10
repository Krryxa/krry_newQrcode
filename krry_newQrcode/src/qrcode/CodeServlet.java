package qrcode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DataBindingException;

import com.swetake.util.Qrcode;

public class CodeServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		
		
		
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
			int width = 140;
			int height = 140;
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			
			//获取画笔
			Graphics2D gs = image.createGraphics();
			//设置背景颜色为白色
			gs.setBackground(Color.WHITE);
			//设置画笔的颜色黑色
			gs.setColor(Color.BLACK);
			//绘制矩形
			gs.clearRect(0, 0, width, height);
			
			//设置内容
			String content = request.getParameter("message");
			//获取的内容变成字节数组
			byte[] contentsBytes = content.getBytes("utf-8");
			//绘制方法
			//设置偏移量，不设置可能导致解析出错
			int pixoff = 2;
			boolean[][] code = qrcode.calQrcode(contentsBytes);
			
			for(int i=0;i<code.length;i++){
				for(int j=0;j<code.length;j++){
					if(code[j][i]){
						gs.fillRect(j*3+pixoff, i*3+pixoff, 3, 3);
					}
				}
			}
			
			
			//生成二维码图片
//			File imgFile = new File("D:\\krry.png"); //设置路径
//			ImageIO.write(image, "png", imgFile);  //保存图片
			String realPath = request.getServletContext().getRealPath("/upload");
			String pathName = new Date().getTime()+".png";
			FileOutputStream outputStream = new FileOutputStream(new File(realPath, pathName));
			ImageIO.write(image, "png", outputStream);
			
			//放在request作用域里
			request.setAttribute("imgPath", pathName);
		//	request.getRequestDispatcher("index.jsp").forward(request, response); //转发
			
			outputStream.close();
			System.out.println("二维码图片生成成功!");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
