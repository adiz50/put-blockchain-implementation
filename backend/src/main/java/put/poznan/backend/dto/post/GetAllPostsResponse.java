package put.poznan.backend.dto.post;

import lombok.Data;
import put.poznan.backend.entities.Post;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetAllPostsResponse {
    public List<PostDto> posts;

    public GetAllPostsResponse( List<Post> posts ) {
        this.posts = new ArrayList<>();
        for ( Post post : posts ) {
            this.posts.add( new PostDto( post ) );
        }
    }
}
