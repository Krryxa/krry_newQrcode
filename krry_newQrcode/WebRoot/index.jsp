<%@ page language="java" import="java.util.*,qrcode.CodeServlet" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html>
 <head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <meta name="Keywords" content="关键词,关键词">
  <meta name="Description" content="">
  <link rel="stylesheet" href="css/animate.css">
  <link rel="stylesheet" type="text/css" href="css/sg.css" />
  <title>Java开发微信二维码系统 --krry</title>
  <style type="text/css">
    *{margin:0;padding:0}
	body{background:url("images/1.jpg");background-size:cover;font-family:"微软雅黑";}
	.leftcontainer{width:400px;height:200px;float:left;}
	.qrcode{width:780px;margin:50px auto;}
	.qrcode h2{font-size:22px;margin:28px;text-shadow:1px 1px 1px #FF3434;padding-left:10px;color:#F36161;font-weight:500;}
	.left{float:left;}
	.right{float:right;}
	.span{color:#fb3a19;font-size: 18px;}
	.qrcode .area{width:400px;height:162px;padding:8px;outline:none;font-size:16px;font-family:"微软雅黑";}
	.qrcode .type{margin:19px 13px 27px 0px;}
	.qrcode .inp{display:block;width:300px;height:45px;margin:10px auto;color:#fff;background:#f90;font-size:18px;font-family:"微软雅黑";border:0;outline:none;border-radius:9px;cursor:pointer;transition:.6s;}
	.qrcode .inp:hover{background:red;transition:.6s;}
	#typeQrcode{width:90px;height: 32px;border-radius: 10px;border: none;}
	.png{width:235;height:235;margin-top:40px;}
	.ping{color: #FF5722;font-size: 20px;}
	.png2{width:235px;height:235px;margin-top:20px;}
	.uploadbutton2{	margin-left:36px;
				    width: 104px;
				    height: 32px;
				    border-radius: 10px;
				    border: 0;
				    outline: none;
				    background: #ff7bbd;
				    color: #fff;
				    cursor: pointer;}
	.kongjian{width:418px;}
	/*解析二维码*/
	.scanqr{display:none; margin-left: 100px;}
	#uploadQrcode{display:none;}
	.uploadbutton{	
				    width: 52px;
				    height: 32px;
				    border-radius: 10px;
				    border: 0;
				    outline: none;
				    background: #ff5353;
				    color: #fff;
				    cursor: pointer;}
	#qrfile{  
	    width:200px;  
	    height:200px;  
	}  
	#qr-canvas{  
	    display:none;  
	}  
	#outdiv  
	{  text-align:center;  
	    width:200px;  
	    height:200px;  
	    border:1px solid red;
	}  
	#result{  
	    border: solid;  
	    border-width: 1px 1px 1px 1px;  
	    padding:20px;  
	    width:37.3%;  
	}  
	#imghelp{  
	    position: relative;
	    left: 0px;
	    top: -122px;
	    z-index: 100; 
	}  
  </style>
 </head>
 <body onload="load();setimg();">
		<div class="qrcode">
			<h2>Java开发微信二维码扫一扫系统</h2>
			<div class="leftcontainer">
				<div class="left inputqr">
					<textarea class="area" name="message"></textarea>
				</div>
				
				<div class="left scanqr">
					<div id="outdiv">
						<div id="qrfile">
							<canvas id="out-canvas" width="200" height="200"></canvas>
							<div id="imghelp">
								<input type="button" class="uploadbutton" id="uploadfile" value="上传"/>
								<input type="file" id="uploadQrcode" accept="image/*" onchange="handleFiles(this.files)"/>
							</div>
							<div id="result" style="display:none;"></div>  
						</div>
					</div>
					<canvas id="qr-canvas" width="800" height="600"></canvas> <!--Canvas to draw image -->  
				</div>
			</div>
			
			<div class="right">
				<p class="ping">示例二维码</p>
				<div class="png2" width="235">
					<img alt="示例" src="images/exp/1.png">
				</div>
			</div>
			
			<div class="left kongjian">
				<div class="type">
					<span class="span">二维码类型：</span>
					<select id="typeQrcode" onchange="selectType()">
						<option value="1">默认类型</option>
						<option value="2">1</option>
						<option value="3">2</option>
					</select>
					<input type="button" class="uploadbutton2" id="changebutton" value="上传二维码"/>
				</div>
				<input type="button" class="inp" value="生成二维码" />
				<div class="png" width="235"></div>
			</div>
			
		</div>
	<script src="js/jquery-1.11.3.min.js"></script>
	<script type="text/javascript" src="js/llqrcode.js"></script>  
	<script type="text/javascript" src="js/webqr.js"></script>
	<script src="js/sg.js"></script>
	<script src="js/sgutil.js"></script>
	<script>
		function selectType(val){
			if($("#typeQrcode").val()==1){
				$(".right .png2 img").attr("src","images/exp/1.png");
			}
			if($("#typeQrcode").val()==2){
				$(".right .png2 img").attr("src","images/exp/2.png");
			}
			if($("#typeQrcode").val()==3){
				$(".right .png2 img").attr("src","images/exp/3.png");
			}
		}
		$(function(){
			var canvas = document.getElementById("out-canvas");
		    var ctx = canvas.getContext("2d");//获取权限
			//点击上传
			$("#uploadfile").click(function(){
				$("#uploadQrcode").click();
			});
			
			//点击转换
			$("#changebutton").click(function(){
				//清除画布
				ctx.clearRect(0, 0, canvas.width, canvas.height);
				//清除文本
				$(".inputqr .area").val("");
				
				if($(".inputqr").is(":hidden")){ //是隐藏
					$(this).val("上传二维码");
					$(".inputqr").show();
					$(".scanqr").hide();
					
				}else{
					$(this).val("输入文本");
					$(".inputqr").hide();
					$(".scanqr").show();
				}
			});
			
			$(".inp").click(function(){
				var value = "";
				//如果是上传二维码
				if($(".inputqr").is(":hidden")){
					value = $("#result").text();
					if(isEmpty(value)){ //如果没有上传
						$.tmDialog.alert({open:"top",content:"请先上传二维码图片",title:"Krry的温馨提醒"});
						return;
					}
					if(value == "errorxiaoqi"){
						$.tmDialog.alert({open:"top",content:"解析错误",title:"Krry的温馨提醒"});
						return;
					}
				}//如果是文本输入
				else{
					value = $(".area").val();
					if(isEmpty(value)){ //如果没有输入
						$.tmDialog.alert({open:"top",content:"请先输入信息",title:"Krry的温馨提醒"});
						return;
					}
				}
				
				var type = $("#typeQrcode").val();
				$(".png").removeClass("animated rotateInUpLeft");
				$.ajax({
					url:"image.jsp",
					type:"post",
					data:{info:value,type:type},
					success:function(data){
						$(".png").html("<img src='upload/"+data+"' />");
						$(".png").addClass("animated rotateInUpLeft");
					}
				});
			});
			
		});
		
		function isEmpty(val) {
			val = $.trim(val);
			if (val == null)
				return true;
			if (val == undefined || val == 'undefined')
				return true;
			if (val == "")
				return true;
			if (val.length == 0)
				return true;
			if (!/[^(^\s*)|(\s*$)]/.test(val))
				return true;
			return false;
		}
	</script>
 </body>
</html>
