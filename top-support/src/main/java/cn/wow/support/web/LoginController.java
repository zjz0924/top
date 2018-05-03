package cn.wow.support.web;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.SecureRandom;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.wow.common.service.OperationLogService;
import cn.wow.common.utils.VerifyCodeUtils;
import cn.wow.common.utils.operationlog.OperationType;
import cn.wow.common.utils.operationlog.ServiceType;

/**
 * 登录控制器
 * @author zhenjunzhuo
 */
@Controller
@RequestMapping(value = "")
public class LoginController {
	
	@Autowired
	private OperationLogService operationLogService;
	
	// mac地址
 	@Value("${legal.switch}")
 	protected String macAddress;
	
	Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	private Random rand = new SecureRandom();
	
	@RequestMapping(value = "/checkLogin")
	public String checkLogin(HttpServletRequest httpServletRequest, Model model) {
		String username = (String) SecurityUtils.getSubject().getPrincipal();
		if (username != null) {
			return "redirect:/index";
		}
		return "redirect:/login";
	}
	
	
	@RequestMapping(value = "/login")
	public String login(HttpServletRequest httpServletRequest, Model model) {
		String username = (String)SecurityUtils.getSubject().getPrincipal();
		if(username!=null) {//如果已经登陆，则跳转到首页
			return "redirect:/index";
		}
		
		String exceptionClassName = (String)httpServletRequest.getAttribute("shiroLoginFailure");
		if(exceptionClassName!=null) {
				String error = null;
			if (UnknownAccountException.class.getName().equals(exceptionClassName)) {
				error = "用户名或密码错误，请核对后重新登录！";
			} else if (IncorrectCredentialsException.class.getName().equals(exceptionClassName)) {
				error = "用户名或密码错误，请核对后重新登录！";
			} else if (LockedAccountException.class.getName().equals(exceptionClassName)) {
				error = "账号已被锁定，请与管理员联系";
			} else if (exceptionClassName != null) {
				error = "登录错误";
			}
			model.addAttribute("error", error);
		}
		return "common/login";
	}
	
	
	/**
	 * 生成验证码 
	 */
	@RequestMapping(value = "/verifycode")
	public void getVerifyCodeImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("image/jpeg");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		// 预防出现 Caused by: java.lang.NoClassDefFoundError: Could not initialize
		// class sun.awt.X11GraphicsEnvironment
		System.setProperty("java.awt.headless", "true");

		// 图像宽高
		int width = 90, height = 20;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// 创建画图
		Graphics g = image.getGraphics();

		// 背景色
		// g.setColor(getRandColor(200,250));
		g.setColor(new Color(255, 255, 255));
		g.fillRect(0, 0, width, height);

		// 字体
		g.setFont(new Font("Arial", Font.TRUETYPE_FONT, 25));

		// 获取随机颜色
		Color fontColor = getRandColor(0, 220);
		StringBuilder vcode = new StringBuilder();
		String aCode = null;

		for (int i = 0; i < 4; i++) {
			aCode = String.valueOf(rand.nextInt(10));
			vcode.append(aCode);
			// 设置绘制文字的颜色
			g.setColor(fontColor);
			// g.setColor(new
			// Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110)));//���ú����4����ɫ��ͬ����������Ϊ����̫�ӽ�����ֻ��ֱ�����
			g.drawString(aCode, 20 * i, 20);
		}

		VerifyCodeUtils.setVerifyCode(vcode.toString(), request, response);
		// 销毁绘图graphics
		g.dispose();
		// 获取输出流
		OutputStream out = response.getOutputStream();
		try {
			ImageIO.write(image, "JPEG", out);
			out.flush();
		} finally {
			out.close();
		}
	}
	
	/**
	 * 获取随机颜色
	 */
	private Color getRandColor(int fcc, int bcc) { 
		int fc = fcc;
		int bc = bcc;
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + rand.nextInt(bc - fc);
		int g = fc + rand.nextInt(bc - fc);
		int b = fc + rand.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	 
	/**
	 * 检查验证码 
	 */
	@RequestMapping(value = "/checkverify", method = RequestMethod.GET)
	@ResponseBody
	public boolean checkVerify(HttpServletRequest request, HttpServletResponse response) {
		String vcode = (String) request.getParameter("vcode");

		if (VerifyCodeUtils.checkVerifyCode(vcode, request, response)) {
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * 退出
	 */
	@RequestMapping(value = "/loginout")
	public String loginout(HttpServletRequest request, HttpServletResponse response){
		Subject subject = SecurityUtils.getSubject();
		String username = (String) SecurityUtils.getSubject().getPrincipal();
		
		if (subject.isAuthenticated()) {
			subject.logout(); // session 会销毁，在SessionListener监听session销毁，清理权限缓存
		}
		
		// 添加日志
		operationLogService.save(username, OperationType.LOGOUT, ServiceType.ACCOUNT, "");
		return "redirect:/login";
	}
	
	private String getLocalMac(InetAddress ia) throws SocketException {
		// 获取网卡，获取地址
		byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < mac.length; i++) {
			if (i != 0) {
				sb.append("-");
			}
			// 字节转换为整数
			int temp = mac[i] & 0xff;
			String str = Integer.toHexString(temp);
			if (str.length() == 1) {
				sb.append("0" + str);
			} else {
				sb.append(str);
			}
		}
		return sb.toString().toUpperCase();
	}
	
}
