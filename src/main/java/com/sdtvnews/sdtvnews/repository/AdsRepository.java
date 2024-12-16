package com.sdtvnews.sdtvnews.repository;

import com.sdtvnews.sdtvnews.entity.Ads;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdsRepository extends JpaRepository<Ads,Long> {
    @Query(value = "select count(*) as count from ads a where status ='1'",nativeQuery = true)
    int countAds();

    @Query(value = "select * from ads a where status ='1'",nativeQuery = true)
    List<Ads>listActiveAds();
}
