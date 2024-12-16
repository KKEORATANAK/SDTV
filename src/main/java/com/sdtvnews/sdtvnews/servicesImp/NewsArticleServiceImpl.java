package com.sdtvnews.sdtvnews.servicesImp;


import com.sdtvnews.sdtvnews.config.*;
import com.sdtvnews.sdtvnews.dto.ListArticleDTO;
import com.sdtvnews.sdtvnews.dto.request.NewsArticleRequest;
import com.sdtvnews.sdtvnews.entity.NewsArticle;
import com.sdtvnews.sdtvnews.repository.NewsArticleRepository;
import com.sdtvnews.sdtvnews.services.NewsArticleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class NewsArticleServiceImpl implements NewsArticleService {

    @Value("${file.upload-image}")
    String imageDirectory ;

    private final NewsArticleRepository newsArticleRepository;
    @Autowired
    GetUserAccess getUserAccess;

    @Override
    public NewsArticle createArticle(NewsArticleRequest request) throws IOException {
        String updatedContent = ImageUtil.processBase64Images(request.getContent(), request.getTitle(),imageDirectory);
        // Create a new NewsArticle object
        NewsArticle article = new NewsArticle();
        article.setTitle(request.getTitle());
        article.setContent(updatedContent);
        article.setCateId(request.getCateId());
        article.setCreateBy(getUserAccess.getUserAccess().getId());
        article.setCreateDate(LocalDateTime.now());
        article.setStatus("1");
        article.setStatusMarquee(request.getStatusMarquee());

        // Finally, save the article
        return newsArticleRepository.save(article);
    }

    @Override
    public void updateSections(Long id, NewsArticleRequest request) throws IOException {
        String updateContent = ImageUtil.processBase64Images(request.getContent(), request.getTitle(),imageDirectory);
        Optional<NewsArticle> newsArticle = newsArticleRepository.findById(id);
        if (newsArticle.isPresent()) {
            NewsArticle article = newsArticle.get();
            // Update the article details
            article.setTitle(request.getTitle());
            article.setContent(updateContent);
            article.setCateId(request.getCateId());
            article.setStatusMarquee(request.getStatusMarquee());
            // Update the modification date if needed
            article.setUpdateDate(LocalDateTime.now());
            article.setUpdateBy(getUserAccess.getUserAccess().getId());
            // Save the updated article
            newsArticleRepository.save(article);
        } else {
            throw new CustomException("Sections not found with ID: " + id);
        }
    }

    @Override
    public void updateArticleStatus(Long id, String status) {
        Optional<NewsArticle> newsArticle = newsArticleRepository.findById(id);
        if (newsArticle.isPresent()) {
            NewsArticle article = newsArticle.get();
            article.setStatus(status); // Update the status (e.g., "0" for inactive)
            article.setUpdateStatusBy(getUserAccess.getUserAccess().getId());
            article.setUpdateStatusDate(LocalDateTime.now());
            newsArticleRepository.save(article); // Save the updated advertiseWithUs
        } else {
            throw new CustomException("Ads not found with ID: " + id);
        }
    }

    @Override
    public List<ListArticleDTO> getAllArticles() {
        return newsArticleRepository.lstNewsArticle();
    }

    @Override
    public List<ListArticleDTO> getAllArticlesSearch(String keyWord) {
        String keyWordConcate = "%" + keyWord + "%";
        return newsArticleRepository.listArticleBySearch(keyWordConcate);
    }

    @Override
    public List<ListArticleDTO> getSearchArticles(String userId, String fromDate, String toDate,String categoryId,String marquee) {
        List<ListArticleDTO> listSearchArticleDTOS = new ArrayList<>();
        if (userId != null && categoryId != null && marquee != null) {
            // Case 1: All three parameters are provided
            System.out.println("userId, categoryId, and marquee are provided");
            listSearchArticleDTOS = newsArticleRepository.lstNewsArticleByUserAndCategoryAndMarquee(userId, categoryId, fromDate, toDate, marquee);
        } else if (userId != null && categoryId != null) {
            // Case 2: userId and categoryId are provided
            System.out.println("userId and categoryId are provided");
            listSearchArticleDTOS = newsArticleRepository.lstNewsArticleByUserAndCategory(userId, categoryId, fromDate, toDate);
        } else if (userId != null && marquee != null) {
            // Case 3: userId and marquee are provided
            System.out.println("userId and marquee are provided");
            listSearchArticleDTOS = newsArticleRepository.lstNewsArticleByUserAndMarquee(userId, fromDate, toDate, marquee);
        } else if (categoryId != null && marquee != null) {
            // Case 4: categoryId and marquee are provided
            System.out.println("categoryId and marquee are provided");
            listSearchArticleDTOS = newsArticleRepository.lstNewsArticleByCategoryAndMarquee(categoryId, fromDate, toDate, marquee);
        } else if (userId != null) {
            // Case 5: Only userId is provided
            System.out.println("Only userId is provided");
            listSearchArticleDTOS = newsArticleRepository.lstNewsArticleByUser(userId, fromDate, toDate);
        } else if (categoryId != null) {
            // Case 6: Only categoryId is provided
            System.out.println("Only categoryId is provided");
            listSearchArticleDTOS = newsArticleRepository.lstNewsArticleByCategory(categoryId, fromDate, toDate);
        } else if (marquee != null) {
            // Case 7: Only marquee is provided
            System.out.println("Only marquee is provided");
            listSearchArticleDTOS = newsArticleRepository.lstNewsArticleByMarquee(fromDate, toDate, marquee);
        } else {
            // Case 8: Only date range is provided
            System.out.println("Only date range is provided");
            listSearchArticleDTOS = newsArticleRepository.lstNewsArticleByCreateDate(fromDate, toDate);
        }
        return listSearchArticleDTOS;
    }

    @Override
    public NewsArticle getArticleById(Long id) {
        return newsArticleRepository.findById(id).orElseThrow(() -> new RuntimeException("Article not found"));
    }

    public boolean isTitleDuplicate(String title) {
        // Check if the title exists in the database
        return newsArticleRepository.existsByTitle(title);
    }

    @Override
    public void deleteArticle(Long id) {
        newsArticleRepository.deleteById(id);
    }

}
