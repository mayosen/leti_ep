package com.mayosen.letipractice.repos;

import com.mayosen.letipractice.models.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {
    List<Document> findAllByNameContainingIgnoreCase(String name);
}
