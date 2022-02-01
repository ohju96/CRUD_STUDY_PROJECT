package poly.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import poly.dto.UserInfoDTO;
import poly.persistance.mapper.UserInfoMapper;
import poly.service.UserInfoService;
import poly.util.CmmUtil;

@Service("UserInfoServiceImpl")
public class UserInfoServiceImpl implements UserInfoService {
	
	@Resource(name = "UserInfoService")
	private UserInfoMapper userInfoMapper;
	
	@Override
	public int insertUserInfo(UserInfoDTO pDTO) throws Exception {
		
		//기타 에러 발생 : 0 | 회원가입 성공 : 1 | 아이디 중복으로 실패 : 2
		int res = 0;
		
		//controller에서 값이 정상적으로 못 넘어오는 경우를 대비하기 위해 사용
		if (pDTO == null) {
			pDTO = new UserInfoDTO();
		}
		
		//회원 가입 중복 방지를 위해 DB에서 데이터 조회
		UserInfoDTO rDTO = userInfoMapper.getUserExists(pDTO);
		
		//Mapper에서 값이 정상적으로 못 넘어오는 경우를 대비하기 위해 사용
		if (rDTO == null) {
			rDTO = new UserInfoDTO();
		}
		
		//중복된 회원정보가 있는 경우에 결과 값을 2로 변경하고 더 이상 작업을 진행하지 않는다.
		if (CmmUtil.nvl(rDTO.getExists_yn()).equals("Y")) {
			res = 2;
		}else {
			//회원 가입
			int success = userInfoMapper.InsertUserInfo(pDTO);
			
			if(success > 0) {
				res = 1;
			}else {
				res = 0;
			}
		}
		
		return 0;
	}

}
