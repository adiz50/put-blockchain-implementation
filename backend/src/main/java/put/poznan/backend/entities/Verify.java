package put.poznan.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import put.poznan.backend.types.ActionType;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "verify", schema = "public")
public class Verify {
    @Id
    private UUID id;
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_index")
    private User user;
    private ActionType actionType;
}
