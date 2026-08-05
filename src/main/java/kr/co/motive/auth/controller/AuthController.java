package kr.co.motive.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.motive.auth.dto.TokenResponseDto;
import kr.co.motive.auth.dto.User;
import kr.co.motive.auth.dto.UserProfileDto;
import kr.co.motive.auth.dto.UserResponseDto;
import kr.co.motive.auth.service.AuthService;
import kr.co.motive.auth.util.JwtTokenUtil;
import kr.co.motive.common.code.UserErrorCode;
import kr.co.motive.common.exception.CustomException;
import kr.co.motive.common.response.ApiResponse;
import kr.co.motive.common.util.SecurityUtil;
import kr.co.motive.fitness.mapper.FitnessProfileMapper;
import kr.co.motive.fitness.service.FitnessProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final FitnessProfileMapper fitnessProfileMapper;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping("/refresh")
    public ApiResponse<TokenResponseDto> refresh(@CookieValue(name = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {
        
        // 회원아이디 조회
        Long userId = jwtTokenUtil.getUserId(refreshToken);

        // 온보딩 완료 여부
        boolean onboardingCompleted = fitnessProfileMapper.existsProfile(userId);

        if(refreshToken == null){
            throw new CustomException(UserErrorCode.INVALID_REFRESH_TOKEN);
        }

        UserResponseDto result = authService.refresh(refreshToken);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", result.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofDays(14))
                .sameSite("Lax")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        TokenResponseDto responseDto = TokenResponseDto
                .builder()
                .accessToken(result.getAccessToken())
                .isNew(result.isNew())
                .onboardingCompleted(onboardingCompleted)
                .build();



        return ApiResponse.ok(responseDto);

    }

    @GetMapping("/me")
    public ApiResponse<UserProfileDto> getMe(HttpServletRequest request){

        Long userId = SecurityUtil.getUserId();

        User user = authService.findUser(userId);

        if(user == null){
            throw new CustomException(UserErrorCode.USER_NOT_FOUND);
        }

        return ApiResponse.ok(UserProfileDto.from(user));
    }
}
