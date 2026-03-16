package com.example.restfull_web_services.services;

import com.example.restfull_web_services.dtos.PostDto;

import java.util.List;

public interface PostService {

    /** Create a new post owned by the given user. */
    PostDto createPost(Integer userId, PostDto postDto);

    /** Fetch a single post by its own id. */
    PostDto getPostById(Integer postId);

    /** Fetch all posts in the system. */
    List<PostDto> getAllPosts();

    /** Fetch all posts belonging to a specific user. */
    List<PostDto> getPostsByUser(Integer userId);

    /** Update an existing post (only title & content are mutable). */
    PostDto updatePost(Integer postId, PostDto postDto);

    /** Delete a post by id. */
    void deletePost(Integer postId);
}
