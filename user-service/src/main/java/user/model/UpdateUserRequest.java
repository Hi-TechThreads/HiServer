package user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Request payload for updating an existing user information")
public class UpdateUserRequest {

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "must be a valid email address")
    @Size(max = 100)
    @Schema(description = "Update user email address (optional)", example = "update@user.com")
    private String email;

    @NotBlank(message = "First name cannot be blank")
    @Size(max = 50)
    @Schema(description = "Update user first name (optional)", example = "user")
    private String firstname;

    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 50)
    @Schema(description = "Update user last name (optional)", example = "user")
    private String lastname;
}
