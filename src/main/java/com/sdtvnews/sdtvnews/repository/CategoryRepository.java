package com.sdtvnews.sdtvnews.repository;

import com.sdtvnews.sdtvnews.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    @Query(value = "select count(*) from category c where status ='1'",nativeQuery = true)
    int countCategory();

    boolean existsByName(String name);

    @Query(value = "select * from category c where status ='1' order by index_show asc ",nativeQuery = true)
    List<Category>lstActiveCategory();

    @Query(value = "select * from category c order by index_show asc",nativeQuery = true)
    List<Category>lstBySortCategory();

    @Query(value = "select c.id from category c where name= :cateName ",nativeQuery = true)
    Long cateId(String cateName);

    @Modifying
    @Query(value = "UPDATE category i SET i.index_show = :orderIndex WHERE i.id = :id",nativeQuery = true)
    void updateOrderIndex(@Param("id") Long id, @Param("orderIndex") int orderIndex);
}
