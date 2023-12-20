package put.poznan.backend.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "comment", schema = "public")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_index")
    @ToString.Exclude
    @JsonBackReference
    private User user;
    @Column(length = 2000)
    private String text;
    private LocalDateTime date;
    @ManyToOne(targetEntity = Post.class)
    @JoinColumn(name = "post_index")
    @ToString.Exclude
    @JsonBackReference
    private Post post;
}
