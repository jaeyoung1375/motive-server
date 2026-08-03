package kr.co.motive.fitness.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.motive.common.code.UserErrorCode;
import kr.co.motive.common.exception.CustomException;
import kr.co.motive.common.response.ApiResponse;
import kr.co.motive.fitness.dto.UserFitnessProfileInsertDto;
import kr.co.motive.fitness.dto.UserFitnessProfileResponseDto;
import kr.co.motive.fitness.dto.UserFitnessProfileUpdateDto;
import kr.co.motive.fitness.service.FitnessProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "운동프로필 Controller", description = "회원 운동프로필 컨트롤러")
public class FitnessProfileController {

	private final FitnessProfileService fitnessProfileService;

	@Operation(summary = "내 운동프로필 조회", description = "로그인한 회원의 운동프로필을 조회한다")
	@GetMapping("/fitness-profile")
	public ApiResponse<UserFitnessProfileResponseDto> getProfile() {

		return ApiResponse.ok(fitnessProfileService.getProfile(currentUserId()));
	}

	@Operation(summary = "내 운동프로필 등록", description = "로그인한 회원의 운동프로필을 등록한다")
	@PostMapping("/fitness-profile")
	public ApiResponse<Void> insertProfile(@Valid @RequestBody UserFitnessProfileInsertDto dto) {

		fitnessProfileService.insertProfile(currentUserId(), dto);
		return ApiResponse.ok();
	}

	@Operation(summary = "내 운동프로필 수정", description = "로그인한 회원의 운동프로필을 수정한다")
	@PutMapping("/fitness-profile")
	public ApiResponse<Void> updateProfile(@Valid @RequestBody UserFitnessProfileUpdateDto dto) {

		fitnessProfileService.updateProfile(currentUserId(), dto);
		return ApiResponse.ok();
	}

	private Long currentUserId() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !(authentication.getPrincipal() instanceof Long userId)) {
			throw new CustomException(UserErrorCode.UNAUTHORIZED);
		}

		return userId;
	}

}