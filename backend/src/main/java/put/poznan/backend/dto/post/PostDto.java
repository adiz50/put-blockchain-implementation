package put.poznan.backend.dto.post;

import lombok.Data;
import put.poznan.backend.dto.comment.CommentDto;
import put.poznan.backend.entities.Comment;
import put.poznan.backend.entities.Post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class PostDto {
    private UUID id;
    private String body;
    private String title;
    private LocalDateTime date;
    private List<CommentDto> comments;

    public PostDto( Post post ) {
        this.id = post.getId();
        this.body = post.getBody();
        this.title = post.getTitle();
        this.date = post.getDate();
        this.comments = new ArrayList<>();
        for ( Comment comment : post.getComments() ) {
            comments.add( new CommentDto( comment ) );
        }
    }
}
