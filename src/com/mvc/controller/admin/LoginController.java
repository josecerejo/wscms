package com.mvc.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mvc.basemvc.annotation.WithoutAuthorize;

// @WithoutAuthorize 自定义注解 无需验证
@Controller
@RequestMapping("/admin")
@WithoutAuthorize
public class LoginController{
	
	/*@Autowired
	 * 
	private IAdminUserService userService;
	
	@Autowired
	private ILoginLogService logService;*/
    
	@RequestMapping(value = "")
	public String loginPage(){
		return "admin/login";
	}
	
	/*@RequestMapping(value = "/logincheck")
	public @ResponseBody JSONObject loginCheck(HttpServletRequest request,
			HttpServletResponse response,AdminUser user) throws Exception{
		
		JSONObject json = new JSONObject();
		json.put("success", false) ;
		json.put("pwd",false);
		json.put("loginname", false);
		json.put("status", false);
		
		String loginName = user.getLoginName();
		String password = user.getPassword();
		
		AdminUser adminUser = userService.queryUserByNameAndPass(loginName, password,Status.STATUS_VALID.getCode());
		AdminUser adminUserByOnlyName = userService.findUserByLoginName(loginName,-1);
		AdminUser adminUserByNameAndStatus = userService.findUserByLoginName(loginName,Status.STATUS_VALID.getCode());
		
		if(adminUser != null){
			json.put("success", true);
			
			request.getSession().setAttribute("host", adminUser);
			
			//更新用户最近登陆的IP和时间
			adminUser.setLastLoginIp(request.getLocalAddr());
			adminUser.setLastLoginTime(new Date());
			userService.updateUser(adminUser);
			
			//记录登陆日志
			LoginLog log = new LoginLog();
			log.setIp(request.getLocalAddr());
			log.setLoginDate(new Date());
			log.setUserId(adminUser.getId());
			logService.insertLoginLog(log);
			
		}else if(adminUserByNameAndStatus != null){
			json.put("pwd", true);
		}else if(adminUserByOnlyName != null){
			json.put("status", true) ;
		}else{
			json.put("loginname", true);
		}
			
		return json;
	}
	
	*//***
	 *退出系统
	 * @param user
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 *//*
	@RequestMapping("/loginout")
	public String loginout(AdminUser user,HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		if(user.getId()!=0){
				AdminLog log = new AdminLog();

				log.setAdminUserId(user.getId());
				log.setDetail(user.getUserName()+"退出系统");
				log.setLogDate(new Date());
				log.setOperateType(LogTyp.loginOut);
				adminLogService.addLog(log);

				user.setLastLoginTime(new Date());
				user.setLastLoginIp(IpUtil.getIpAddr(request));
				adminUserService.updateUserLoginOut(user);

				GlobalUserOnLine.userOnlineMap.remove(""+user.getId());

		}
		request.getSession().invalidate();
		return "admin/login";
	}*/
}
