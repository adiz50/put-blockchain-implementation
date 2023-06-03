package put.poznan.backend.dto.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import put.poznan.backend.entities.User;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllUsersResponse {
    private String username;
    private UUID id;

    public AllUsersResponse( User user ) {
        this.username = user.getUsername();
        this.id = user.getId();
    }
}
