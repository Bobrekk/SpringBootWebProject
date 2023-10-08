package org.example.Controllers;

import org.example.Entities.Post;
import org.example.Requests.PostCreateRequest;
import org.example.Requests.PostUpdateRequest;
import org.example.Responses.PostResponse;
import org.example.Services.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<PostResponse> getAllPosts(@RequestParam Optional<Long> userId) {return postService.getAll(userId);
    }

    @PostMapping
    public Post createOnePost(@RequestBody PostCreateRequest newPostRequest) {
        return postService.createOnePost(newPostRequest);
    }

    @GetMapping("/{postId}")
    public PostResponse getOnePost(@PathVariable Long postId) {
        return postService.getOnePostByIdWithLikes(postId);
    }

    @PutMapping("/{postId}")
    public Post updateOnePost(@PathVariable Long postId, @RequestBody PostUpdateRequest updatePostRequest) {
        return postService.updateOnePostById(postId, updatePostRequest);
    }

    @DeleteMapping("/{postid}")
    public void deleteOnePost(@PathVariable Long postId) {
        postService.deleteOnePostById(postId);
    }
}
