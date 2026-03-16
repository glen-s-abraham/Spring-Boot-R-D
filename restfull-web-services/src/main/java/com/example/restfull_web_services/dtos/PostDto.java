package com.example.restfull_web_services.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private Integer id;

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    private String createdAt;

    private String updatedAt;

    /**
     * Embed just enough user info so the response is self-contained
     * without risking circular serialization of the full User entity.
     * The userId links back to the owner; userDisplayName is a convenience field.
     */
    private Integer userId;
    private String userDisplayName;
}
