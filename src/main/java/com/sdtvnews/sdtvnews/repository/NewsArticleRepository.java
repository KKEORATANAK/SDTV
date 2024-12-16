package com.sdtvnews.sdtvnews.repository;

import com.sdtvnews.sdtvnews.dto.ListArticleDTO;
import com.sdtvnews.sdtvnews.entity.NewsArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NewsArticleRepository extends JpaRepository<NewsArticle,Long> {

    @Query(value = "SELECT \n" +
            "    na.id,\n" +
            "    na.title,\n" +
            "    na.content,\n" +
            "    na.create_date,\n" +
            "    c.name,\n" +
            "    na.status,\n" +
            "    CONCAT(u.first_name, ' ', u.last_name) AS full_name\n" +
            "FROM \n" +
            "    news_article na\n" +
            "INNER JOIN \n" +
            "    category c \n" +
            "    ON na.cate_id = c.id\n" +
            "INNER JOIN \n" +
            "    `user` u \n" +
            "    ON na.create_by = u.id\n" +
            "WHERE na.title LIKE :keyWord \n" +
            "ORDER BY \n" +
            "    na.create_date DESC;" ,nativeQuery = true)
    List<ListArticleDTO>listArticleBySearch(String keyWord);

    @Query(value = "select count(*) from news_article na where status ='1'",nativeQuery = true)
    int countArticle();

    @Query(value = "select \n" +
            "\tna.id,\n" +
            "    na.title,\n" +
            "    na.create_date,\n" +
            "    c.name,\n" +
            "    na.status,\n" +
            "    CONCAT(u.first_name, ' ', u.last_name) AS full_name\n" +
            "from news_article na\n" +
            "inner join category c on na.cate_id = c.id \n" +
            "inner join `user` u on na.create_by=u.id \n" +
            "order by na.create_date desc ",nativeQuery = true)
    List<ListArticleDTO>lstNewsArticle();

    @Query(value = "select\n" +
            "\t\tna.id,\n" +
            "\t\tna.title,\n" +
            "\t\tna.create_date,\n" +
            "\t\tc.name,\n" +
            "\t\tna.status,\n" +
            "\t\tCONCAT(u.first_name, ' ', u.last_name) AS full_name\n" +
            "    from news_article na\n" +
            "    inner join category c on na.cate_id = c.id\n" +
            "    inner join `user` u on na.create_by=u.id\n" +
            "    where DATE(na.create_date) BETWEEN :fromDate AND :toDate \n" +
            "    order by na.create_date desc",nativeQuery = true)
    List<ListArticleDTO>lstNewsArticleByCreateDate(String fromDate,String toDate);

    @Query(value = "select\n" +
            "\t\tna.id,\n" +
            "\t\tna.title,\n" +
            "\t\tna.create_date,\n" +
            "\t\tc.name,\n" +
            "\t\tna.status,\n" +
            "\t\tCONCAT(u.first_name, ' ', u.last_name) AS full_name\n" +
            "    from news_article na\n" +
            "    inner join category c on na.cate_id = c.id\n" +
            "    inner join `user` u on na.create_by=u.id\n" +
            "    where 1=1\n" +
            "    and c.id = :categoryId \n" +
            "    and DATE(na.create_date) BETWEEN :fromDate AND :toDate\n" +
            "    order by na.create_date desc",nativeQuery = true)
    List<ListArticleDTO>lstNewsArticleByCategory(String categoryId, String fromDate,String toDate);

    @Query(value = "select\n" +
            "\t\tna.id,\n" +
            "\t\tna.title,\n" +
            "\t\tna.create_date,\n" +
            "\t\tc.name,\n" +
            "\t\tna.status,\n" +
            "\t\tCONCAT(u.first_name, ' ', u.last_name) AS full_name\n" +
            "    from news_article na\n" +
            "    inner join category c on na.cate_id = c.id\n" +
            "    inner join `user` u on na.create_by=u.id\n" +
            "    where 1=1\n" +
            "    and na.status_marquee = :marquee \n" +
            "    and DATE(na.create_date) BETWEEN :fromDate AND :toDate\n" +
            "    order by na.create_date desc",nativeQuery = true)
    List<ListArticleDTO>lstNewsArticleByMarquee(String fromDate,String toDate,String marquee);


    @Query(value = "select\n" +
            "\t\tna.id,\n" +
            "\t\tna.title,\n" +
            "\t\tna.create_date,\n" +
            "\t\tc.name,\n" +
            "\t\tna.status,\n" +
            "\t\tCONCAT(u.first_name, ' ', u.last_name) AS full_name\n" +
            "    from news_article na\n" +
            "    inner join category c on na.cate_id = c.id\n" +
            "    inner join `user` u on na.create_by=u.id\n" +
            "    where 1=1\n" +
            "    and u.id =:userId  \n" +
            "    and DATE(na.create_date) BETWEEN :fromDate AND :toDate\n" +
            "    order by na.create_date desc",nativeQuery = true)
    List<ListArticleDTO>lstNewsArticleByUser(String userId, String fromDate,String toDate);

    @Query(value = " select\n" +
            "\t\tna.id,\n" +
            "\t\tna.title,\n" +
            "\t\tna.create_date,\n" +
            "\t\tc.name,\n" +
            "\t\tna.status,\n" +
            "\t\tCONCAT(u.first_name, ' ', u.last_name) AS full_name\n" +
            "    from news_article na\n" +
            "    inner join category c on na.cate_id = c.id\n" +
            "    inner join `user` u on na.create_by=u.id\n" +
            "    where 1=1\n" +
            "    and c.id = :categoryId \n" +
            "    and u.id = :userId \n" +
            "    and DATE(na.create_date) BETWEEN :fromDate AND :toDate \n" +
            "    order by na.create_date desc",nativeQuery = true)
    List<ListArticleDTO>lstNewsArticleByUserAndCategory(String userId,String categoryId,String fromDate,String toDate);

    @Query(value = " select\n" +
            "\t\tna.id,\n" +
            "\t\tna.title,\n" +
            "\t\tna.create_date,\n" +
            "\t\tc.name,\n" +
            "\t\tna.status,\n" +
            "\t\tCONCAT(u.first_name, ' ', u.last_name) AS full_name\n" +
            "    from news_article na\n" +
            "    inner join category c on na.cate_id = c.id\n" +
            "    inner join `user` u on na.create_by=u.id\n" +
            "    where 1=1\n" +
            "    and na.status_marquee = :marquee \n" +
            "    and u.id = :userId \n" +
            "    and DATE(na.create_date) BETWEEN :fromDate AND :toDate \n" +
            "    order by na.create_date desc",nativeQuery = true)
    List<ListArticleDTO>lstNewsArticleByUserAndMarquee(String userId,String fromDate,String toDate,String marquee);

    @Query(value = " select\n" +
            "\t\tna.id,\n" +
            "\t\tna.title,\n" +
            "\t\tna.create_date,\n" +
            "\t\tc.name,\n" +
            "\t\tna.status,\n" +
            "\t\tCONCAT(u.first_name, ' ', u.last_name) AS full_name\n" +
            "    from news_article na\n" +
            "    inner join category c on na.cate_id = c.id\n" +
            "    inner join `user` u on na.create_by=u.id\n" +
            "    where 1=1\n" +
            "    and na.status_marquee = :marquee \n" +
            "    and c.id = :categoryId \n" +
            "    and DATE(na.create_date) BETWEEN :fromDate AND :toDate \n" +
            "    order by na.create_date desc",nativeQuery = true)
    List<ListArticleDTO>lstNewsArticleByCategoryAndMarquee(String categoryId,String fromDate,String toDate,String marquee);

    @Query(value = " select\n" +
            "\t\tna.id,\n" +
            "\t\tna.title,\n" +
            "\t\tna.create_date,\n" +
            "\t\tc.name,\n" +
            "\t\tna.status,\n" +
            "\t\tCONCAT(u.first_name, ' ', u.last_name) AS full_name\n" +
            "    from news_article na\n" +
            "    inner join category c on na.cate_id = c.id\n" +
            "    inner join `user` u on na.create_by=u.id\n" +
            "    where 1=1\n" +
            "    and c.id = :categoryId \n" +
            "    and u.id = :userId \n" +
            "    and na.status_marquee = :marquee \n" +
            "    and DATE(na.create_date) BETWEEN :fromDate AND :toDate \n" +
            "    order by na.create_date desc",nativeQuery = true)
    List<ListArticleDTO>lstNewsArticleByUserAndCategoryAndMarquee(String userId,String categoryId,String fromDate,String toDate,String marquee);

    @Query(value = "select\n" +
            "\t\tna.id,\n" +
            "\t\tna.title,\n" +
            "\t\tna.create_date,\n" +
            "\t\tc.name,\n" +
            "\t\tCONCAT(u.first_name, ' ', u.last_name) AS full_name\n" +
            "    from news_article na\n" +
            "    inner join category c on na.cate_id = c.id\n" +
            "    inner join `user` u on na.create_by=u.id\n" +
            "    where DATE(na.create_date) = :today \n" +
            "    order by na.create_date desc",nativeQuery = true)
    List<ListArticleDTO> todayPost(String today);

    @Query(value = "select\n" +
            "\t\tna.id,\n" +
            "\t\tna.title,\n" +
            "\t\tna.create_date,\n" +
            "\t\tc.name,\n" +
            "\t\tCONCAT(u.first_name, ' ', u.last_name) AS full_name\n" +
            "    from news_article na\n" +
            "    inner join category c on na.cate_id = c.id\n" +
            "    inner join `user` u on na.create_by=u.id\n" +
            "    where na.status_marquee ='1' \n" +
            "    order by na.create_date desc",nativeQuery = true)
    List<ListArticleDTO>listMarquee();

    boolean existsByTitle(String title);

    @Query(value = "select * from news_article na where status ='1' order by na.create_date desc",nativeQuery = true)
    Page<NewsArticle> listNewsArticlesActive(Pageable pageable);

    @Query(value = "select * from news_article na where status ='1' and cate_id = :cateId order by na.create_date desc ",nativeQuery = true)
    Page<NewsArticle>listNewsArticlesActiveByCategory(Long cateId,Pageable pageable);

    @Query(value = "SELECT \n" +
            "    na.id,\n" +
            "    na.title,\n" +
            "    na.content,\n" +
            "    na.create_date,\n" +
            "    c.name,\n" +
            "    na.status,\n" +
            "    CONCAT(u.first_name, ' ', u.last_name) AS full_name\n" +
            "FROM \n" +
            "    news_article na\n" +
            "INNER JOIN \n" +
            "    category c \n" +
            "    ON na.cate_id = c.id\n" +
            "INNER JOIN \n" +
            "    `user` u \n" +
            "    ON na.create_by = u.id\n" +
            "WHERE na.title LIKE :keyWord \n" +
            "ORDER BY \n" +
            "    na.create_date DESC;" ,nativeQuery = true)
    Page<ListArticleDTO>listArticleBySearchFronted(String keyWord,Pageable pageable);

}
