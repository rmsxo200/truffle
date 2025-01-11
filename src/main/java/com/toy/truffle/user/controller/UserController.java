package com.toy.truffle.user.controller;

import com.toy.truffle.global.dto.CommonResponseDTO;
import com.toy.truffle.user.dto.LoginDTO;
import com.toy.truffle.user.dto.SignUpDTO;
import com.toy.truffle.user.entity.User;
import com.toy.truffle.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	// 회원 가입 페이지 호출
	@GetMapping("/api/v1/signUp")
	public String signUp(Model model) {
		model.addAttribute("signUpDTO", new SignUpDTO());
		return "common/signUp";
	}

	// 로그인 페이지 호출
	@GetMapping("/api/v1/login")
	public String loginPage() {
		return "common/login";
	}

	// 회원가입
	@PostMapping("/api/v1/signUp")
	public String signUp(@ModelAttribute SignUpDTO signUpDTO,
		RedirectAttributes redirectAttributes) {

		CommonResponseDTO dto = userService.signUpUser(signUpDTO);

		if (dto.isStatus()) {
			log.debug("회원가입 성공");
			return "redirect:/api/v1/login";
		} else {
			log.debug("회원가입 실패");
			redirectAttributes.addFlashAttribute("errorMessage", dto.getMessage());
			return "redirect:/api/v1/signUp";
		}
	}

	@PostMapping("/api/v1/login")
	@ResponseBody
	public CommonResponseDTO loginProc(@Valid @RequestBody LoginDTO loginDTO, HttpServletRequest request) {
		User loginUserInfo = userService.login(loginDTO.email(), loginDTO.password());
		// 세션 생성 서비스
		return userService.createSession(loginUserInfo, request);
	}
}
