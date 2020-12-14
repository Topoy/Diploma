package main.repository;

import main.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>
{

    @Query(value = "SELECT * FROM posts p WHERE p.is_active = 1 AND p.moderation_status = 'ACCEPTED' AND p.`time` < NOW()" +
            "ORDER BY `time` DESC", nativeQuery = true)
    List<Post> getRecentPosts(Pageable pageable);

    @Query(value = "SELECT * FROM posts p2 WHERE p2.is_active = 1 AND p2.moderation_status = 'ACCEPTED'" +
            "ORDER BY (SELECT COUNT(*) FROM post_comments pc WHERE pc.post_id = p2.id) DESC", nativeQuery = true)
    List<Post> getMostPopularPosts(Pageable pageable);

    @Query(value = "SELECT * FROM posts p3 WHERE p3.is_active = 1 AND p3.moderation_status = 'ACCEPTED' " +
            "AND p3.`time` < NOW() ORDER BY (SELECT COUNT(*) FROM post_votes pv WHERE pv.post_id = p3.id) DESC",
            nativeQuery = true)
    List<Post> getBestPosts(Pageable pageable);

    @Query(value = "SELECT * FROM posts p WHERE p.is_active = 1 AND p.moderation_status = 'ACCEPTED' AND " +
            "p.`time` < NOW() ORDER BY `time`", nativeQuery = true)
    List<Post> getEarlyPosts(Pageable pageable);

    @Query(value = "SELECT * FROM posts p WHERE p.is_active = 1 AND p.moderation_status = 'ACCEPTED'", nativeQuery = true)
    List<Post> getAllPosts();

}
