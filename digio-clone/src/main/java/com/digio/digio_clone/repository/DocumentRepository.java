package com.digio.digio_clone.repository;


import com.digio.digio_clone.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
