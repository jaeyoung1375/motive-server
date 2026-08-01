package kr.co.motive.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.motive.auth.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserProfileDto {

    @Schema(description = "회원아이디")
    private Long userId;

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "계정상태 (ACTIVE, DEACTIVATE)")
    private String status;

    @Schema(description = "권한")
    private Role role;

    public static UserProfileDto from(User user) {
        return UserProfileDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .status(user.getStatus())
                .role(user.getRole())
                .build();
    }


}
