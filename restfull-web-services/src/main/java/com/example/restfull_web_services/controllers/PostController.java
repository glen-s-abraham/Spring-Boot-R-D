package com.example.restfull_web_services.controllers;

import com.example.restfull_web_services.dtos.PostDto;
import com.example.restfull_web_services.services.PostService;
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

/**
 * PostController — REST endpoints for Post CRUD.
 *
 * URL structure:
 * /posts - global post collection
 * /users/{userId}/posts - all posts belonging to a specific user (nested
 * resource)
 *
 * All responses use HATEOAS EntityModel / CollectionModel to embed _links.
 */
@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // ─── Global Post Endpoints ────────────────────────────────────────────────

    /** GET /posts → all posts in the system */
    @GetMapping("/posts")
    public ResponseEntity<CollectionModel<EntityModel<PostDto>>> getAllPosts() {
        List<EntityModel<PostDto>> posts = postService.getAllPosts()
                .stream()
                .map(this::toEntityModel)
                .toList();

        return ResponseEntity.ok(CollectionModel.of(
                posts,
                linkTo(methodOn(PostController.class).getAllPosts()).withSelfRel()));
    }

    /** GET /posts/{id} → single post */
    @GetMapping("/posts/{id}")
    public ResponseEntity<EntityModel<PostDto>> getPostById(@PathVariable Integer id) {
        return ResponseEntity.ok(toEntityModel(postService.getPostById(id)));
    }

    /** PUT /posts/{id} → update a post */
    @PutMapping("/posts/{id}")
    public ResponseEntity<EntityModel<PostDto>> updatePost(
            @PathVariable Integer id,
            @Valid @RequestBody PostDto postDto) {
        return ResponseEntity.ok(toEntityModel(postService.updatePost(id, postDto)));
    }

    /** DELETE /posts/{id} → delete a post */
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    // ─── Nested User ↔ Post Endpoints ────────────────────────────────────────

    /**
     * GET /users/{userId}/posts
     * Returns all posts by a specific user.
     * Responds with 404 if the user does not exist.
     */
    @GetMapping("/users/{userId}/posts")
    public ResponseEntity<CollectionModel<EntityModel<PostDto>>> getPostsByUser(
            @PathVariable Integer userId) {

        List<EntityModel<PostDto>> posts = postService.getPostsByUser(userId)
                .stream()
                .map(this::toEntityModel)
                .toList();

        return ResponseEntity.ok(CollectionModel.of(
                posts,
                linkTo(methodOn(PostController.class).getPostsByUser(userId)).withSelfRel(),
                linkTo(methodOn(PostController.class).getAllPosts()).withRel("all-posts")));
    }

    /**
     * POST /users/{userId}/posts
     * Creates a new post owned by the given user.
     */
    @PostMapping("/users/{userId}/posts")
    public ResponseEntity<EntityModel<PostDto>> createPost(
            @PathVariable Integer userId,
            @Valid @RequestBody PostDto postDto) {

        PostDto created = postService.createPost(userId, postDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/posts/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(toEntityModel(created));
    }

    // ─── HATEOAS Helper ───────────────────────────────────────────────────────

    /**
     * Wraps a PostDto in an EntityModel with:
     * - self → GET /posts/{id}
     * - all-posts → GET /posts
     * - user-posts → GET /users/{userId}/posts
     * - update → PUT /posts/{id}
     * - delete → DELETE /posts/{id}
     */
    private EntityModel<PostDto> toEntityModel(PostDto dto) {
        Link self = linkTo(methodOn(PostController.class).getPostById(dto.getId())).withSelfRel();
        Link allPosts = linkTo(methodOn(PostController.class).getAllPosts()).withRel("all-posts");
        Link userPosts = linkTo(methodOn(PostController.class).getPostsByUser(dto.getUserId())).withRel("user-posts");
        Link update = linkTo(methodOn(PostController.class).updatePost(dto.getId(), dto)).withRel("update");
        Link delete = linkTo(methodOn(PostController.class).deletePost(dto.getId())).withRel("delete");

        return EntityModel.of(dto, self, allPosts, userPosts, update, delete);
    }
}
