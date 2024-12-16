package com.sdtvnews.sdtvnews.services;

import com.sdtvnews.sdtvnews.dto.ListArticleDTO;
import com.sdtvnews.sdtvnews.dto.request.NewsArticleRequest;
import com.sdtvnews.sdtvnews.entity.NewsArticle;
import java.io.IOException;
import java.util.List;

public interface NewsArticleService {

    NewsArticle createArticle(NewsArticleRequest request)throws IOException;

    List<ListArticleDTO> getAllArticles();

    List<ListArticleDTO> getAllArticlesSearch(String keyWord);

    List<ListArticleDTO> getSearchArticles(String userId, String fromDate, String toDate,String categoryId,String marquee);

    NewsArticle getArticleById(Long id);

    void updateSections(Long id, NewsArticleRequest request)throws IOException;

    void deleteArticle(Long id);

    void updateArticleStatus(Long id, String status);

    boolean isTitleDuplicate(String title);

}

