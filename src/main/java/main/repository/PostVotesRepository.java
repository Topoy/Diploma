package main.repository;

import main.model.PostVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostVotesRepository extends JpaRepository<PostVote, Integer>
{
    @Query(value = "SELECT * FROM post_votes pv WHERE pv.value = 1", nativeQuery = true)
    List<PostVote> getAllLikes();

    @Query(value = "SELECT * FROM post_votes pv WHERE pv.value = -1", nativeQuery = true)
    List<PostVote> getAllDislikes();
}
