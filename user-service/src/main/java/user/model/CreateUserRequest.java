package user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Request payload for creating a new user")
public class CreateUserRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Schema(description = "Desired username", example = "newuser", required = true)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Schema(description = "User password", example = "Str0ngP@ssw0rd", required = true)
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email format")
    @Size(max = 100)
    @Schema(description = "User email address", example = "new.user@example.com", required = true)
    private String email;

    @NotBlank(message = "User first name is required")
    @Size(max = 50)
    @Schema(description = "User first name", example = "user", required = true)
    private String firstName;

    @NotBlank(message = "User last name is required")
    @Size(max = 50)
    @Schema(description = "User last name", example = "user", required = true)
    private String lastName;

}
