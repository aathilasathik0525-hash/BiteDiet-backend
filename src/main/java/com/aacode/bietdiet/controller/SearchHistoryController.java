package com.aacode.bietdiet.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aacode.bietdiet.models.SearchHistory;
import com.aacode.bietdiet.repository.SearchHistoryRepository;

@RestController
@RequestMapping("/api/search-history")
@CrossOrigin(origins = "*")
public class SearchHistoryController {

    private final SearchHistoryRepository searchHistoryRepository;

    public SearchHistoryController(
            SearchHistoryRepository searchHistoryRepository) {

        this.searchHistoryRepository =
                searchHistoryRepository;
    }

    // ==========================================
    // SAVE SEARCH
    // ==========================================

    @PostMapping
    public ResponseEntity<SearchHistory> saveSearch(
            @RequestBody SearchHistory searchHistory) {

        if (searchHistory.getEmail() == null ||
            searchHistory.getEmail().trim().isEmpty()) {

            return ResponseEntity.badRequest().build();
        }

        if (searchHistory.getSearchQuery() == null ||
            searchHistory.getSearchQuery().trim().isEmpty()) {

            return ResponseEntity.badRequest().build();
        }

        searchHistory.setSearchedAt(
                LocalDateTime.now().toString()
        );

        SearchHistory saved =
                searchHistoryRepository.save(searchHistory);

        return ResponseEntity.ok(saved);
    }

    // ==========================================
    // GET USER SEARCH HISTORY
    // ==========================================

    @GetMapping
    public ResponseEntity<List<SearchHistory>> getSearchHistory(
            @RequestParam String email) {

        List<SearchHistory> history =
                searchHistoryRepository
                        .findByEmailOrderByIdDesc(email);

        return ResponseEntity.ok(history);
    }

    // ==========================================
    // DELETE USER SEARCH HISTORY
    // ==========================================

    @DeleteMapping
    public ResponseEntity<?> deleteSearchHistory(
            @RequestParam String email) {

        searchHistoryRepository.deleteByEmail(email);

        return ResponseEntity.ok(
                "Search history deleted successfully"
        );
    }
}