package com.sdtvnews.sdtvnews.repository;

import com.sdtvnews.sdtvnews.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    boolean existsByName(String name);

    @Query(value = "select * from `role` r where status ='1'",nativeQuery = true)
    List<Role>lstActiveRole();

}
