package com.toy.truffle.unit.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.toy.truffle.global.dto.CommonResponseDTO;
import com.toy.truffle.global.util.Argon2PasswordEncryptor;
import com.toy.truffle.user.dto.SignUpDTO;
import com.toy.truffle.user.entity.User;
import com.toy.truffle.user.repository.UserRepository;
import com.toy.truffle.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private Argon2PasswordEncryptor passwordEncryptor;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpSession session;

	@InjectMocks
	private UserService userService;

	private String email;
	private String userName;
	private String rawPassword;
	private String encryptedPassword;
	private SignUpDTO signUpDTO;

	@BeforeEach
	public void setUp() {
		// 공통 데이터 초기화
		email = "testuser@test.com";
		userName = "테스트사용자";
		rawPassword = "password123";
		encryptedPassword = "argon2EncryptedPassword";

		// SignUpDTO 초기화
		signUpDTO = SignUpDTO.builder()
			.email(email)
			.userName(userName)
			.password(rawPassword)
			.build();
	}

	@Test
	@DisplayName("회원가입 실패")
	public void testFailedUserSave() {

	}

	@Test
	@DisplayName("[SERVICE]회원가입 성공")
	public void testSuccessUserSave() {
		// when
		when(passwordEncryptor.encrypt(rawPassword)).thenReturn(encryptedPassword);
		when(userRepository.save(any(User.class))).thenReturn(User.builder()
			.userSeq(1L)
			.email(email)
			.userName(userName)
			.password(encryptedPassword)
			.build());

		CommonResponseDTO commonResponseDTO = userService.signUpUser(signUpDTO);

		// then
		assertEquals(true, commonResponseDTO.isStatus());
		assertEquals("회원가입 성공", commonResponseDTO.getMessage());
		verify(passwordEncryptor, times(1)).encrypt(rawPassword);
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	@DisplayName("email 중복 체크 실패")
	@Disabled
	public void testDuplicateUserEmail() {

	}

	@Test
	@DisplayName("[SERVICE] 사용자 1건 조회")
	public void testFindByUser() {

		User mockUser = User.builder()
			.userSeq(1L)
			.email(email)
			.userName(userName)
			.password(encryptedPassword)
			.build();

		when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

		User result = userService.findUserByEmail(email);

		assertNotNull(result);
		assertEquals(mockUser.getUserName(), result.getUserName());
		assertEquals(mockUser.getEmail(), result.getEmail());
		assertEquals(mockUser.getUserSeq(), result.getUserSeq());

		verify(userRepository, times(1)).findByEmail(email);
	}

	@Test
	@DisplayName("[SERVICE] 로그인")
	public void testLogin() {

		// given
		String email = this.email;
		String rawPassword = this.rawPassword;
		String encryptedPassword = this.encryptedPassword;

		// User Mock data
		User mockUser = User.builder()
			.userSeq(1L)
			.email(email)
			.userName(userName)
			.password(encryptedPassword)
			.build();

		// when
		// 사용자 조회
		when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
		// 비밀번호 검증
		when(passwordEncryptor.verify(encryptedPassword, rawPassword)).thenReturn(true);

		// 로그인 로직 호출
		User loggedInUser = userService.login(email, rawPassword);

		// then
		// 로그인 성공 시 반환된 사용자 정보 확인
		assertNotNull(loggedInUser);
		assertEquals(email, loggedInUser.getEmail());
		assertEquals(userName, loggedInUser.getUserName());

		// 메서드 호출 횟수 검증
		verify(passwordEncryptor, times(1)).verify(encryptedPassword, rawPassword);
		verify(userRepository, times(1)).findByEmail(email);
	}

	@Test
	@DisplayName("[SERVICE] 세션 생성 테스트")
	public void testCreateSession() {
		// given
		User mockUser = User.builder()
			.email("test_user@test.com")
			.userName("홍길동")
			.build();

		when(request.getSession()).thenReturn(session);

		// when
		CommonResponseDTO response = userService.createSession(mockUser, request);

		// then
		verify(request, times(1)).getSession(); // 세션 요청 확인
		verify(session, times(1)).setAttribute("userEmail", "test_user@test.com");
		verify(session, times(1)).setAttribute("userName", "홍길동");

		assertThat(response.isStatus()).isTrue();
		assertThat(response.getMessage()).isEqualTo("로그인 성공");
	}
}
