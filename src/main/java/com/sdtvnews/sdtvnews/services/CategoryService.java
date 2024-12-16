package com.sdtvnews.sdtvnews.services;

import com.sdtvnews.sdtvnews.dto.request.SortedItem;
import com.sdtvnews.sdtvnews.dto.response.CategoryResponse;
import com.sdtvnews.sdtvnews.dto.request.CategoryRequest;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    CategoryRequest createCategory(CategoryRequest request);

    List<CategoryResponse>getAllCategories();

    List<CategoryResponse>getActiveCategories();

    Optional<CategoryResponse>getCategoryById(Long id);

    void updateCategory(Long id, CategoryRequest categoryRequest);

    void updateCategoryStatus(Long id, String status);

    boolean isNameDuplicate(String name);

    List<CategoryResponse> getByCategoriesASEC();

    void handleSortedData(List<SortedItem> sortedItemList);
}
