package com.example.restfull_web_services.controllers;

import com.example.restfull_web_services.dtos.UserDto;
import com.example.restfull_web_services.services.UserService;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET /users → CollectionModel<EntityModel<UserDto>>
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<UserDto>>> getAllUsers() {
        List<EntityModel<UserDto>> users = userService.getAllUsers().stream()
                .map(this::toEntityModel)
                .toList();

        CollectionModel<EntityModel<UserDto>> collection = CollectionModel.of(
                users,
                linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    // GET /users/{id} → EntityModel<UserDto>
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserDto>> getUserById(@PathVariable Integer id) {
        UserDto userDto = userService.getUserById(id);
        return ResponseEntity.ok(toEntityModel(userDto));
    }

    // POST /users → EntityModel<UserDto> + Location header
    @PostMapping
    public ResponseEntity<EntityModel<UserDto>> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto createdUser = userService.createUser(userDto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdUser.getId())
                .toUri();

        return ResponseEntity.created(location).body(toEntityModel(createdUser));
    }

    // PUT /users/{id} → EntityModel<UserDto>
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserDto>> updateUser(
            @PathVariable Integer id,
            @Valid @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(toEntityModel(updatedUser));
    }

    // DELETE /users/{id} → 204 No Content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // ─── Helper ───────────────────────────────────────────────────────────────

    /**
     * Wraps a UserDto in an EntityModel with:
     * - self link → GET /users/{id}
     * - all-users → GET /users
     * - update → PUT /users/{id}
     * - delete → DELETE /users/{id}
     */
    private EntityModel<UserDto> toEntityModel(UserDto dto) {
        Link selfLink = linkTo(methodOn(UserController.class).getUserById(dto.getId())).withSelfRel();
        Link allUsers = linkTo(methodOn(UserController.class).getAllUsers()).withRel("all-users");
        Link updateLink = linkTo(methodOn(UserController.class).updateUser(dto.getId(), dto)).withRel("update");
        Link deleteLink = linkTo(methodOn(UserController.class).deleteUser(dto.getId())).withRel("delete");

        return EntityModel.of(dto, selfLink, allUsers, updateLink, deleteLink);
    }
}
