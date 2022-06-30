package com.general.template.repository;

import com.general.template.domain.Score;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Long> {

    @Query(value = "select * from scores s ORDER By s.created_at desc limit 20", nativeQuery = true)
    List<Score> findLatestScores();


    Page<Score> findAllByTitleContainingOrDescriptionContaining(String searchParam, String keyword, Pageable page);

    @Query(value = "SELECT * FROM scores s where match(s.title, s.description) against(:keyword in boolean mode)", nativeQuery = true)
    Page<Score> searchKeyword(@Param("keyword") String keyword,  Pageable page);

    Score findFirstByTitle(String title);

    @Query(value = "SELECT * FROM scores s where match(s.title, s.description) against(?1) limit 1", nativeQuery = true)
    Score findByTitle(String title);
}
