package poly.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sun.istack.internal.logging.Logger;

import poly.dto.UserInfoDTO;
import poly.service.UserInfoService;
import poly.util.CmmUtil;
import poly.util.EncryptUtil;

@Controller("UserInfoController")
public class UserInfoController {

	private Logger log = Logger.getLogger(this.getClass());
	
	@Resource(name = "UserInfoService")
	private UserInfoService userInfoService;
	
	@RequestMapping(value = "user/UserRegForm")
	public String userRegForm() {
		log.info(this.getClass().getName() + ".user/userRegForm ok !");
		
		return "/user/UserRegForm";
	}
		
	@RequestMapping(value = "user/insertUserInfo")
	public String insertUserInfo(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws Exception {
		
		log.info(this.getClass().getName() + "insertUserInfo start !");
		
		// 회원가입 결과에 대한 메시지를 전달할 변수
		String msg = "";
		
		//웹(회원정보 입력화면)에서 받는 정보를 저장할 변수
		UserInfoDTO pDTO = null;
		
		try {
			String user_id = CmmUtil.nvl(request.getParameter("user_id"));
			String user_name = CmmUtil.nvl(request.getParameter("user_name"));
			String password = CmmUtil.nvl(request.getParameter("password"));
			String email = CmmUtil.nvl(request.getParameter("email"));
			String addr1 = CmmUtil.nvl(request.getParameter("addr1"));
			String addr2 = CmmUtil.nvl(request.getParameter("addr2"));

			log.info("user_id : " + user_id);
			log.info("user_name : " + user_name);
			log.info("password : " + password);
			log.info("email : " + email);
			log.info("addr1 : " + addr1);
			log.info("addr2 : " + addr2);

			pDTO = new UserInfoDTO();

			pDTO.setUser_id(user_id);
			pDTO.setUser_name(user_name);

			pDTO.setPassword(EncryptUtil.encHashSHA256(password));
			pDTO.setEmail(EncryptUtil.encAES128CBC(email));
			pDTO.setAddr1(addr1);
			pDTO.setAddr2(addr2);

			// 회원가입
			int res = userInfoService.insertUserInfo(pDTO);

			if (res == 1) {
				msg = "회원가입되었습니다.";
			} else if (res == 2) {
				msg = "이미 가입된 이메일 주소입니다.";
			} else {
				msg = "오류로 인해 회원가입이 실패하였습니다.";
			}

		} catch (Exception e) {
			msg = "실패하였습니다. : " + e.toString();
			log.info(e.toString());
			e.printStackTrace();
		} finally {
			log.info(this.getClass().getName() + ".insetUserInfo end ! ");

			model.addAttribute("msg", msg);

			model.addAttribute("pDTO", pDTO);

			pDTO = null;

		}

		return "/user/Msg";
	}
	
}
