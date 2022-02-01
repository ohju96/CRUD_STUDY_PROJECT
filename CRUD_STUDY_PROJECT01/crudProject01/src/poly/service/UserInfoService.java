package poly.service;

import poly.dto.UserInfoDTO;

public interface UserInfoService {

	//회원 가입하기 | 회원 정보 등록
	int insertUserInfo(UserInfoDTO pDTO) throws Exception;
}
