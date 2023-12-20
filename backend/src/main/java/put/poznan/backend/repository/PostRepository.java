package put.poznan.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import put.poznan.backend.entities.Post;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {

    @Query(value = "select count(p) from Post p")
    int getCountOfPosts();

    @Query(value = "select p from Post p left join p.comments c where p.body like ?1 or p.title like " +
            "?1 or c.text like ?1")
    List<Post> getPostsContaining( String query );
}
