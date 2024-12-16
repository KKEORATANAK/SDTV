package com.sdtvnews.sdtvnews.repository;

import com.sdtvnews.sdtvnews.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {

    @Query(value ="select count(*) from client c where status ='1'",nativeQuery = true)
    int countClient();

    boolean existsByName(String name);

}
