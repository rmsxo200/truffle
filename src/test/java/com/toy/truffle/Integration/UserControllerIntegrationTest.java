package com.toy.truffle.Integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toy.truffle.user.dto.LoginDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("[INTEGRATION] 로그인 실패 - 벨리데이션 에러")
	public void testLoginValidationErrors() throws Exception {
		// given
		LoginDTO loginDTO = new LoginDTO("", "");
		String requestBody = objectMapper.writeValueAsString(loginDTO);

		// when & then
		mockMvc.perform(post("/api/v1/login")
				.contentType("application/json; charset=UTF-8")
				.content(requestBody))
			//.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.type").value("https://example.com/errors/validation-error"))
			.andExpect(jsonPath("$.detail.email").value("이메일을 입력해주세요."))
			.andExpect(jsonPath("$.detail.password").value("비밀번호를 입력해주세요."));
	}
}
