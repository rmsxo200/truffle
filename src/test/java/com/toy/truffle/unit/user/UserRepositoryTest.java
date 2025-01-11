package com.toy.truffle.unit.user;

import static org.assertj.core.api.Assertions.assertThat;

import com.toy.truffle.user.entity.User;
import com.toy.truffle.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	private User saveUser;

	@BeforeEach
	public void setUp() {
		// 공통 테스트 데이터 저장
		saveUser = User.builder()
			.userName("테스트유저")
			.email("test@test.com")
			.password("Asdf1234!@")
			.build();

		userRepository.save(saveUser);
	}

	@AfterEach
	public void clearTestData() {
		userRepository.deleteAll();
	}

	@Test
	@DisplayName("회원정보 저장")
	public void testSaveUserSignUp() {
		// when
		final User result = userRepository.findByEmail(saveUser.getEmail()).orElse(null);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getUserName()).isEqualTo("테스트유저");
		assertThat(result.getEmail()).isEqualTo("test@test.com");
		assertThat(result.getPassword()).isEqualTo("Asdf1234!@");
	}

	@Test
	@DisplayName("사용자 정보 1건 조회")
	public void testFindUserByEmail() {
		// when
		Optional<User> result = userRepository.findByEmail("test@test.com");
		// then
		assertThat(result).isPresent();
		assertThat(result.get().getUserName()).isEqualTo("테스트유저");
		assertThat(result.get().getPassword()).isEqualTo("Asdf1234!@");
	}
}
