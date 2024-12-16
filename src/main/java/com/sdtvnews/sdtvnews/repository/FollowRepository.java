package com.sdtvnews.sdtvnews.repository;

import com.sdtvnews.sdtvnews.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow,Long> {

}
