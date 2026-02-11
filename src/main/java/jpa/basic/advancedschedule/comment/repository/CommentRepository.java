package jpa.basic.advancedschedule.comment.repository;

import jpa.basic.advancedschedule.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * fetch join을 써서 LAZY 전략의 N + 1 문제를 해결하기.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c " +
            "JOIN FETCH c.schedule s " +
            "JOIN FETCH c.user u " +
            "WHERE s.id = :scheduleId")
    List<Comment> findAllByScheduleId(@Param("scheduleId") Long scheduleId);

    @Query("SELECT c FROM Comment c " +
            "JOIN FETCH c.schedule s " +
            "JOIN FETCH c.user u " +
            "WHERE c.id = :commentId")
    Optional<Comment> findByIdWithScheduleAndUser(@Param("commentId") Long commentId);
}
