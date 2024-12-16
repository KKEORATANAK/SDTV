package com.sdtvnews.sdtvnews.controller_frontend;
import com.sdtvnews.sdtvnews.dto.ListArticleDTO;
import com.sdtvnews.sdtvnews.dto.response.News;
import com.sdtvnews.sdtvnews.entity.Ads;
import com.sdtvnews.sdtvnews.entity.Category;
import com.sdtvnews.sdtvnews.entity.NewsArticle;
import com.sdtvnews.sdtvnews.repository.AdsRepository;
import com.sdtvnews.sdtvnews.repository.CategoryRepository;
import com.sdtvnews.sdtvnews.repository.NewsArticleRepository;
import com.sdtvnews.sdtvnews.services.NewsArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/home")
public class IndexNewsController {

    @Value("${facebook.link}")
    private String facebookLink;

    @Value("${youtube.link}")
    private String youtubeLink;

    @Value("${telegram.link}")
    private String telegramLink;

    @Value("${youtube-video.link}")
    private String youtubeVideolink;

    @Value("${youtube-video.format}")
    private String youtubeFormat;

    @Value("${page-size}")
    private int pageSize;

    @Autowired
    NewsArticleRepository newsArticleRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    AdsRepository adsRepository;
    @Autowired
    NewsArticleService newsArticleService;

    @GetMapping("/")
    public String dashboard(Model model,@RequestParam(defaultValue = "0") int page) {

        List<ListArticleDTO>listMarquee=newsArticleRepository.listMarquee();
        // Concatenate the titles of the articles, separated by "*"
        String concatenatedMarquee = listMarquee.stream()
                .map(ListArticleDTO::getTitle) // or any other property you want to extract
                .collect(Collectors.joining(" * "));
        // Add the concatenated string as an attribute to the model
        model.addAttribute("concatenatedMarquee", concatenatedMarquee);
        //list active category
        List<Category>lstActiveCategory=categoryRepository.lstActiveCategory();
        model.addAttribute("lstActiveCategory",lstActiveCategory);
        //link for youtube video on dashboard
        model.addAttribute("youtubeVideolink",youtubeVideolink+"?autoplay=1&mute=1&");
        //list active ads
        List<Ads>adsList=adsRepository.listActiveAds();
        model.addAttribute("adsList",adsList);
        // Fetch paginated news articles
        Page<NewsArticle> newsArticlesPage = newsArticleRepository.listNewsArticlesActive(PageRequest.of(page, pageSize));
        List<News> newsList = new ArrayList<>();
        for (NewsArticle newsArticle : newsArticlesPage.getContent()) {
            News news = new News();
            String content = newsArticle.getContent();
            String firstImage = extractFirstImage(content);
            news.setFirstImage(firstImage);
            news.setTitle(newsArticle.getTitle());
            news.setId(String.valueOf(newsArticle.getId()));
            news.setCreateDate(newsArticle.getCreateDate());
            newsList.add(news);
        }
        model.addAttribute("newsList", newsList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", newsArticlesPage.getTotalPages());
        model.addAttribute("size", pageSize);
        //model map social
        model.addAttribute("facebookLink",facebookLink);
        model.addAttribute("youtubeLink",youtubeLink);
        model.addAttribute("telegramLink",telegramLink);
        return "fronted/index";
    }

    @GetMapping("/category/{cateName}")
    public String category(Model model, @PathVariable String cateName,@RequestParam(defaultValue = "0") int page) {
        // Check index to show on home page
        if ("ទំព័រដើម".equalsIgnoreCase(cateName.trim())) {
            return "redirect:/home/";
        }

        List<ListArticleDTO>listMarquee=newsArticleRepository.listMarquee();
        // Concatenate the titles of the articles, separated by "*"
        String concatenatedMarquee = listMarquee.stream()
                .map(ListArticleDTO::getTitle) // or any other property you want to extract
                .collect(Collectors.joining(" * "));
        // Add the concatenated string as an attribute to the model
        model.addAttribute("concatenatedMarquee", concatenatedMarquee);
        //list active category
        List<Category>lstActiveCategory=categoryRepository.lstActiveCategory();
        model.addAttribute("lstActiveCategory",lstActiveCategory);
        //link for youtube video on dashboard
        model.addAttribute("youtubeVideolink",youtubeVideolink+"?autoplay=1&mute=1&");
        //list active ads
        List<Ads>adsList=adsRepository.listActiveAds();
        model.addAttribute("adsList",adsList);
        //find category id
        Long cateId= categoryRepository.cateId(cateName);
        //list news data
        Page<NewsArticle> newsArticles = newsArticles=newsArticleRepository.listNewsArticlesActiveByCategory(cateId,PageRequest.of(page, pageSize));
        List<News> newsList = new ArrayList<>();
        for (NewsArticle newsArticle : newsArticles) {
            News news = new News();
            String content = newsArticle.getContent();  // Assuming NewsArticle has a getContent() method
            String firstImage = extractFirstImage(content);  // Assuming extractFirstImage() is your custom method to get the first image URL
            news.setFirstImage(firstImage);  // Setting the extracted first image URL
            news.setTitle(newsArticle.getTitle());  // Setting title if needed
            news.setId(String.valueOf(newsArticle.getId()));
            news.setCreateDate(newsArticle.getCreateDate());
            // Set other fields like title, id, etc., here as well
            newsList.add(news);
        }
        model.addAttribute("newsList",newsList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", newsArticles.getTotalPages());
        model.addAttribute("size", pageSize);
        model.addAttribute("cateName", cateName);
        //model map social
        model.addAttribute("facebookLink",facebookLink);
        model.addAttribute("youtubeLink",youtubeLink);
        model.addAttribute("telegramLink",telegramLink);
        return "fronted/category-page";
    }

    @GetMapping("/category-search")
    public String categorySearch(@RequestParam("keyWord") String keyWord,Model model,@RequestParam(defaultValue = "0") int page) {
        List<ListArticleDTO>listMarquee=newsArticleRepository.listMarquee();
        // Concatenate the titles of the articles, separated by "*"
        String concatenatedMarquee = listMarquee.stream()
                .map(ListArticleDTO::getTitle) // or any other property you want to extract
                .collect(Collectors.joining(" * "));
        // Add the concatenated string as an attribute to the model
        model.addAttribute("concatenatedMarquee", concatenatedMarquee);
        //list active category
        List<Category>lstActiveCategory=categoryRepository.lstActiveCategory();
        model.addAttribute("lstActiveCategory",lstActiveCategory);
        //link for youtube video on dashboard
        model.addAttribute("youtubeVideolink",youtubeVideolink+"?autoplay=1&mute=1&");
        //list active ads
        List<Ads>adsList=adsRepository.listActiveAds();
        model.addAttribute("adsList",adsList);
        //list news data
        String keyWordConcate = "%" + keyWord + "%";
        Page<ListArticleDTO> newsArticles = newsArticleRepository.listArticleBySearchFronted(keyWordConcate,PageRequest.of(page, pageSize));
        List<News> newsList = new ArrayList<>();
        for (ListArticleDTO newsArticle : newsArticles) {
            News news = new News();
            String content = newsArticle.getContent();  // Assuming NewsArticle has a getContent() method
            String firstImage = extractFirstImage(content);  // Assuming extractFirstImage() is your custom method to get the first image URL
            news.setFirstImage(firstImage);  // Setting the extracted first image URL
            news.setTitle(newsArticle.getTitle());  // Setting title if needed
            news.setId(String.valueOf(newsArticle.getId()));
            news.setCreateDate(newsArticle.getCreateDate());
            // Set other fields like title, id, etc., here as well
            newsList.add(news);
        }

        model.addAttribute("newsList",newsList);
        int count = (int) newsArticles.getTotalElements();
        model.addAttribute("keyWord",keyWord);
        model.addAttribute("count",count);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", newsArticles.getTotalPages());
        model.addAttribute("size", pageSize);
        //model map social
        model.addAttribute("facebookLink",facebookLink);
        model.addAttribute("youtubeLink",youtubeLink);
        model.addAttribute("telegramLink",telegramLink);
        return "fronted/category-page-search";
    }

    @GetMapping("/category-detail/{id}")
    public String categoryDetail(Model model, @PathVariable String id) {
        List<ListArticleDTO>listMarquee=newsArticleRepository.listMarquee();
        // Concatenate the titles of the articles, separated by "*"
        String concatenatedMarquee = listMarquee.stream()
                .map(ListArticleDTO::getTitle) // or any other property you want to extract
                .collect(Collectors.joining(" * "));
        // Add the concatenated string as an attribute to the model
        model.addAttribute("concatenatedMarquee", concatenatedMarquee);
        //list active category
        List<Category>lstActiveCategory=categoryRepository.lstActiveCategory();
        model.addAttribute("lstActiveCategory",lstActiveCategory);
        //link for youtube video on dashboard
        model.addAttribute("youtubeVideolink",youtubeVideolink+youtubeFormat);
        //list active ads
        List<Ads>adsList=adsRepository.listActiveAds();
        model.addAttribute("adsList",adsList);
        //find category id

        //get news Articles by id
        NewsArticle newsArticles = newsArticleService.getArticleById(Long.valueOf(id));
        model.addAttribute("newsArticles",newsArticles);
        //model map social
        model.addAttribute("facebookLink",facebookLink);
        model.addAttribute("youtubeLink",youtubeLink);
        model.addAttribute("telegramLink",telegramLink);
        return "fronted/category-detail";
    }

    public String extractFirstImage(String content) {
        // Regular expression to match both relative and absolute image URLs
        String firstImageUrl = null;
        String regex = "<img[^>]+src=['\"](http[^'\"]+|/[^'\"]+)['\"]";  // Match both absolute and relative URLs
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            firstImageUrl = matcher.group(1);  // Get the first matched URL
        }
        // If no image URL is found, set a default image
        if (firstImageUrl == null) {
            firstImageUrl = "/images/default-image.jpg";  // Default image if none found
        }

        return firstImageUrl;
    }

    @GetMapping("/contact")
    public String contact(Model model) {

        List<ListArticleDTO>listMarquee=newsArticleRepository.listMarquee();
        // Concatenate the titles of the articles, separated by "*"
        String concatenatedMarquee = listMarquee.stream()
                .map(ListArticleDTO::getTitle) // or any other property you want to extract
                .collect(Collectors.joining(" * "));

        // Add the concatenated string as an attribute to the model
        model.addAttribute("concatenatedMarquee", concatenatedMarquee);
        //list active category
        List<Category>lstActiveCategory=categoryRepository.lstActiveCategory();
        model.addAttribute("lstActiveCategory",lstActiveCategory);

        //list active ads
        List<Ads>adsList=adsRepository.listActiveAds();
        model.addAttribute("adsList",adsList);

        //model map social
        model.addAttribute("facebookLink",facebookLink);
        model.addAttribute("youtubeLink",youtubeLink);
        model.addAttribute("telegramLink",telegramLink);

        return "fronted/contact";
    }

}
