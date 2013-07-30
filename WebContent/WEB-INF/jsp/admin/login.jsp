<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>管理系统后台登录</title>
<script src="${pageContext.request.contextPath}/js/jquery-1.7.1.js" type="text/javascript" ></script>
<script src="${ pageContext.request.contextPath}/js/json.js" type="text/javascript" ></script>
<link href="${pageContext.request.contextPath}/css/login.css" type="text/css" rel="stylesheet" />

<script type="text/javascript">

	$(function() {
		$("#loginName").focus();
		$("#subBtn").bind("click",beforeSubmitFn );
	});
	
	//回车提交
	document.onkeydown = function(e) {
		e = e || window.event;
		var key = e ? (e.charCode || e.keyCode) : 0;
		if (key == 13) {
			beforeSubmitFn();
		}
	};

	function submitFn() {
		var uname = $("#loginName").val();   // 登录名
		var password = $("#password").val();  // 密码
		if (uname == null || $.trim(uname).length == 0) {
			alert("请输入用户名");
			return;
		}
		if (password == null || $.trim(password).length == 0) {
			alert("请输入密码");
			return;
		}
		if (document.getElementById("subBtn").disabled == true) {
			alert("正在的登录,请等待....");
			return;
		}
		var option = {
			type : "POST",
			beforeSend : function() {
				document.getElementById("subBtn").disabled = true;
			},
			url : "${pageContext.request.contextPath }/admin/logincheck",
			data : {loginName : uname,password : password,_json : -1},
			success : function(json) {
				document.getElementById("subBtn").disabled = false;
				if (json['success']) {
					//登陆成功，跳转到主页面
					window.location = "${pageContext.request.contextPath }/admin/frame";
				} else {
					if (json["pwd"]) {
						alert("您的密码错误");
					} else if (json["loginname"]) {
						alert("用户名错误");
					} else if (json["status"]) {
						alert("您账号不是有效状态,请与系统管理员联系");
					}
				}
			}
		};
		loginAjaxFn(option);
	}
	//Ajax提交
	function loginAjaxFn(option) {
		$.ajax(option);
	}

	function beforeSubmitFn(){
		submitFn();
	}
</script>

</head>
<body>
	<div id="head"></div>
	<div id="main">
		<p><span>用户名：</span><input type="text" class="CMS_txt" id="loginName" name="loginName" forcuse /></p>
		<p><span>密&nbsp;&nbsp;码：</span><input type="password" class="CMS_txt" name="password" id="password" /></p>
		<div id="center">
			<input type="button" class="CMS_button" onclick="javascript:submitFn();" id="subBtn" />
		</div>
	</div>
	<div id="head2"></div>
	<div id="head2"></div>
	<div id="footer">
		<p>版权所有 Copyright 20013-2023 www.wjz.cn All Rights Reserved
			京ICP备11029827号-2</p>
		<p>技术支持：Administrator</p>
		<p>
			<a href="#">关于我们</a>|<a href="#">广告服务</a>|<a href="#">联系我们</a>|<a
				href="#">诚聘英才</a>|<a href="#">网站地图</a>|<a href="#">版权隐私</a>|<a
				href="#">使用协议</a>|<a href="#">帮助中心</a>
		</p>
	</div>
</body>
</html>
