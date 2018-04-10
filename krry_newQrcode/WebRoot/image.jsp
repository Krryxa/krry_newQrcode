<%@page import="qrcode.QrcodeText"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String info = request.getParameter("info");
	String type = request.getParameter("type");
	String Name = QrcodeText.qrcode(info,type,request);  //调用方法，绘制二维码图片并保存,并获得图片名称
	out.print(Name);
%>