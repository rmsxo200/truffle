package com.toy.truffle.user.service;

import com.toy.truffle.global.codeEnum.ResponseStatus;
import com.toy.truffle.global.config.problemDetails.CustomException;
import com.toy.truffle.global.config.problemDetails.ErrorCode;
import com.toy.truffle.global.dto.CommonResponseDTO;
import com.toy.truffle.global.util.Argon2PasswordEncryptor;
import com.toy.truffle.user.dto.SignUpDTO;
import com.toy.truffle.user.entity.User;
import com.toy.truffle.user.repository.UserRepository;
import groovy.util.logging.Slf4j;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final Argon2PasswordEncryptor passwordEncryptor;

	@Transactional
	public CommonResponseDTO signUpUser(SignUpDTO signUpDTO) {
		// 비밀번호 암호화 처리
		String encryptedPassword = passwordEncryptor.encrypt(signUpDTO.getPassword());

		User user = signUpDTO.toBuilder()
			.password(encryptedPassword) // 암호화된 비밀번호 설정
			.build()
			.toEntity();

		Long savedUserId = userRepository.save(user).getUserSeq();

		ResponseStatus responseStatus = (savedUserId != null)
			? ResponseStatus.USER_REGISTER_SUCCESS
			: ResponseStatus.USER_REGISTER_FAILURE;

		return CommonResponseDTO.builder()
			.status(responseStatus.isStatus())
			.message(responseStatus.getMessage())
			.build();
	}

	// 로그인
	@Transactional
	public User login(String email, String password) {
		// 사용자 정보 조회
		User user = this.findUserByEmail(email);
		// 비밀번호 비교
		if (!passwordEncryptor.verify(user.getPassword(), password)) {
			throw new CustomException(ErrorCode.INVALID_PASSWORD_ERROR);
		}
		// 리턴
		return user;
	}

	// 사용자 정보 조회
	@Transactional
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_ERROR));
	}

	public CommonResponseDTO createSession(User user, HttpServletRequest request){
		HttpSession session = request.getSession();
		session.setAttribute("userEmail", user.getEmail()); // 유저 이메일 저장
		session.setAttribute("userName", user.getUserName()); // 유저 이름 저장

		return CommonResponseDTO.builder()
			.status(true)
			.message("로그인 성공")
			.build();
	}
}
