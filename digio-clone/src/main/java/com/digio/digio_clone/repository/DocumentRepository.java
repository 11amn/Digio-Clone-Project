package com.digio.digio_clone.repository;


import com.digio.digio_clone.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    @Query("SELECT d FROM Document d WHERE d.digioDocumentId =:id")
    Optional <Document> findByDocumentId(@Param("id") String id);

    Optional<Document> findByDigioDocumentId(String digioDocumentId);

}
