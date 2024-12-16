package com.sdtvnews.sdtvnews.repository;

import com.sdtvnews.sdtvnews.entity.Sections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionsRepository extends JpaRepository<Sections,Long> {

}
