package com.sdtvnews.sdtvnews.repository;

import com.sdtvnews.sdtvnews.dto.ListUserDTO;
import com.sdtvnews.sdtvnews.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query(value = "select count(*) from `user` u  where status ='1'",nativeQuery = true)
    int countUser();

    @Query(value = "select * from `user` u where u.status='1' and user_name=:username",nativeQuery = true)
    User findByUserNameLogin(String username);

    boolean existsByUserName(String username);

    @Query(value = "select\n" +
            "\tu.id,\n" +
            "\tu.last_name,\n" +
            "\tu.first_name ,\n" +
            "\tr.name,\n" +
            "\tu.create_date ,\n" +
            "\tu.status,\n" +
            "\tu.user_name,\n" +
            "\tu.pass_word\n" +
            "from `user` u \n" +
            "inner join `role` r on u.role_id=r.id \n" +
            "where u.status ='1' and u.user_name=:username",nativeQuery = true)
    ListUserDTO findByUserName(String username);

    @Query(value ="select\n" +
            "\tu.id,\n" +
            "\tu.last_name,\n" +
            "\tu.first_name ,\n" +
            "\tr.name,\n" +
            "\tu.create_date ,\n" +
            "\tu.status,\n" +
            "\tu.user_name,\n" +
            "\tu.pass_word\n" +
            "from `user` u \n" +
            "inner join `role` r on u.role_id=r.id " ,nativeQuery = true)
    List<ListUserDTO>lstAllUser();

    @Query(value = "select * from `user` u where u.status ='1'",nativeQuery = true)
    List<User>lstActiveUser();
}
