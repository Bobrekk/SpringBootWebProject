package org.example.Services;

import org.example.Entities.Like;
import org.example.Entities.Post;
import org.example.Entities.User;
import org.example.Repos.PostRepository;
import org.example.Requests.PostCreateRequest;
import org.example.Requests.PostUpdateRequest;
import org.example.Responses.LikeResponse;
import org.example.Responses.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private PostRepository postRepository;
    @Autowired
    private LikeService likeService;
    private UserService userService;

    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    public List<PostResponse> getAll(Optional<Long> userId) {
        List<Post> list;
        if(userId.isPresent()) {
            list = postRepository.findByUserId(userId.get());
        }
        else{
            list = postRepository.findAll();
        }
        return list.stream().map(p -> {
            List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.of(p.getUser().getId()), Optional.of(p.getId()));
            return new PostResponse(p, likes);}).collect(Collectors.toList());
    }

    public Post getOnePostById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public PostResponse getOnePostByIdWithLikes(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.ofNullable(null), Optional.of(postId));
        return new PostResponse(post, likes);
    }

    public Post createOnePost(PostCreateRequest newPostRequest) {

        User user = userService.getOneUserById(newPostRequest.getUserId());

        if(user == null)
            return null;
        Post toSave = new Post();
        toSave.setId(newPostRequest.getId());
        toSave.setText(newPostRequest.getText());
        toSave.setTitle(newPostRequest.getTitle());
        toSave.setUser(user);
        toSave.setCreateDate(new Date());
        return postRepository.save(toSave);
    }

    public Post updateOnePostById(Long postId, PostUpdateRequest updatePostRequest) {
        Optional<Post> post = postRepository.findById(postId);

        if(post.isPresent()) {
            Post toUpdate = post.get();
            toUpdate.setText(updatePostRequest.getText());
            toUpdate.setTitle(updatePostRequest.getTitle());
            postRepository.save(toUpdate);
            return toUpdate;
        }

        return null;
    }

    public void deleteOnePostById(Long postId) {
        postRepository.deleteById(postId);
    }
}
