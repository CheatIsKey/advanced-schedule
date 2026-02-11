package jpa.basic.advancedschedule.schedule.repository;

import jpa.basic.advancedschedule.schedule.dto.ReadSchedulesResponse;
import jpa.basic.advancedschedule.schedule.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByUserId(Long userId);

    @Query(value = "SELECT new jpa.basic.advancedschedule.schedule.dto.ReadSchedulesResponse(" +
            "s.id, s.title, s.content, s.author, COUNT(c), s.createdAt, s.modifiedAt) " +
            "FROM Schedule s " +
            "LEFT JOIN Comment c ON s.id = c.schedule.id " +
            "GROUP BY s.id",
            countQuery = "SELECT COUNT(s) FROM Schedule s"
    )
    Page<ReadSchedulesResponse> findAllWithCommentsCount(Pageable pageable);
}


