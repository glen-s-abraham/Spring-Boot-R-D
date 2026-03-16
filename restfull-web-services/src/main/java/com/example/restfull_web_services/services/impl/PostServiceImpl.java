package com.example.restfull_web_services.services.impl;

import com.example.restfull_web_services.dtos.PostDto;
import com.example.restfull_web_services.exceptions.PostNotFoundException;
import com.example.restfull_web_services.exceptions.UserNotFoundException;
import com.example.restfull_web_services.models.Post;
import com.example.restfull_web_services.models.User;
import com.example.restfull_web_services.repositories.PostRepository;
import com.example.restfull_web_services.repositories.UserRepository;
import com.example.restfull_web_services.services.PostService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // ─── Mapping Helpers ──────────────────────────────────────────────────────

    private PostDto mapToDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .userId(post.getUser().getId())
                .userDisplayName(post.getUser().getName())
                .build();
    }

    private User resolveUser(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));
    }

    private Post resolvePost(Integer postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id " + postId));
    }

    // ─── Service Methods ──────────────────────────────────────────────────────

    @Override
    public PostDto createPost(Integer userId, PostDto postDto) {
        User user = resolveUser(userId);
        Post post = Post.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .user(user)
                .build();
        return mapToDto(postRepository.save(post));
    }

    @Override
    public PostDto getPostById(Integer postId) {
        return mapToDto(resolvePost(postId));
    }

    @Override
    public List<PostDto> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PostDto> getPostsByUser(Integer userId) {
        // Verify the user exists before querying their posts
        resolveUser(userId);
        return postRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PostDto updatePost(Integer postId, PostDto postDto) {
        Post existing = resolvePost(postId);
        if (postDto.getTitle() != null)
            existing.setTitle(postDto.getTitle());
        if (postDto.getContent() != null)
            existing.setContent(postDto.getContent());
        return mapToDto(postRepository.save(existing));
    }

    @Override
    public void deletePost(Integer postId) {
        if (!postRepository.existsById(postId)) {
            throw new PostNotFoundException("Post not found with id " + postId);
        }
        postRepository.deleteById(postId);
    }
}
