package com.aacode.bietdiet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.aacode.bietdiet.models.SearchHistory;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {

    List<SearchHistory> findByEmailOrderByIdDesc(String email);

    @Transactional
    @Modifying
    void deleteByEmail(String email);
}