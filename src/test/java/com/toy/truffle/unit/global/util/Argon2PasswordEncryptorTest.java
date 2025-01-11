package com.toy.truffle.unit.global.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.toy.truffle.global.util.Argon2PasswordEncryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class Argon2PasswordEncryptorTest {

	private Argon2PasswordEncryptor passwordEncryptor;

	@BeforeEach
	public void setUp() {
		passwordEncryptor = new Argon2PasswordEncryptor();
	}

	@Test
	@DisplayName("Argon2 비밀번호 암호화 테스트")
	public void testPasswordEncryption() {
		// given
		String rawPassword = "password123";

		// when
		String encryptedPassword = passwordEncryptor.encrypt(rawPassword);
		System.out.println("encryptedPassword : " + encryptedPassword); // 편의상 출력

		// then
		assertNotNull(encryptedPassword); // 암호화된 비밀번호가 null이 아닌지 확인
		assertNotEquals(rawPassword, encryptedPassword); // 암호화된 비밀번호가 원본 비밀번호와 다른지 확인
	}

	@Test
	@DisplayName("Argon2 비밀번호 검증 테스트")
	public void testPasswordVerification() {
		// given
		String rawPassword = "password123";
		String encryptedPassword = passwordEncryptor.encrypt(rawPassword);

		// when
		boolean isVerified = passwordEncryptor.verify(encryptedPassword, rawPassword);

		// then
		assertTrue(isVerified); // 암호화된 비밀번호와 원본 비밀번호가 일치하는지 확인
	}

	@Test
	@DisplayName("Argon2 비밀번호 검증 실패 테스트")
	public void testPasswordVerificationFail() {
		// given
		String rawPassword = "password123";
		String wrongPassword = "wrongPassword";
		String encryptedPassword = passwordEncryptor.encrypt(rawPassword);

		// when
		boolean isVerified = passwordEncryptor.verify(encryptedPassword, wrongPassword);

		// then
		assertFalse(isVerified); // 잘못된 비밀번호가 암호화된 비밀번호와 일치하지 않는지 확인
	}
}