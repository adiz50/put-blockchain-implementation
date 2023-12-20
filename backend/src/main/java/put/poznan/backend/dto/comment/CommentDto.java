package put.poznan.backend.dto.comment;

import lombok.Data;
import put.poznan.backend.entities.Comment;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CommentDto {
    private UUID id;
    private UUID userId;
    private String username;
    private String text;
    private LocalDateTime date;

    public CommentDto( Comment comment ) {
        this.id = comment.getId();
        this.userId = comment.getUser().getId();
        this.username = comment.getUser().getUsername();
        this.text = comment.getText();
        this.date = comment.getDate();
    }
}
