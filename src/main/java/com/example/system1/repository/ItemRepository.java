package com.example.system1.repository;

import com.example.system1.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    // This magic line lets you search by name OR description!
    List<Item> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);

    // Advanced Filter Query
    @Query("SELECT i FROM Item i WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR LOWER(i.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(i.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:category IS NULL OR :category = '' OR :category = 'All Categories' OR i.category = :category)")
    List<Item> searchItems(@Param("keyword") String keyword, @Param("category") String category);
}