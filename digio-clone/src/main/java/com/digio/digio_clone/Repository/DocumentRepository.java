package com.digio.digio_clone.Repository;


import com.digio.digio_clone.Entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
