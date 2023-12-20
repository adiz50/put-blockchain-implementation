package put.poznan.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import put.poznan.backend.dto.comment.request.PostCommentRequest;
import put.poznan.backend.dto.post.GetAllPostsResponse;
import put.poznan.backend.entities.Comment;
import put.poznan.backend.service.PostService;

@RestController
@CrossOrigin
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    public GetAllPostsResponse getPosts( @RequestParam(required = false) String query ) {
        return postService.getPosts( query );
    }

    @PostMapping("/comment")
    public Comment postComment( @RequestBody PostCommentRequest req ) {
        return postService.addComment( req );
    }
}
