package com.sdtvnews.sdtvnews.servicesImp;

import com.sdtvnews.sdtvnews.config.CustomException;
import com.sdtvnews.sdtvnews.config.GetUserAccess;
import com.sdtvnews.sdtvnews.dto.request.SortedItem;
import com.sdtvnews.sdtvnews.dto.response.CategoryResponse;
import com.sdtvnews.sdtvnews.dto.request.CategoryRequest;
import com.sdtvnews.sdtvnews.entity.Category;
import com.sdtvnews.sdtvnews.repository.CategoryRepository;
import com.sdtvnews.sdtvnews.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CatgoryServiceImp implements CategoryService {

    private final CategoryRepository categoryRepository;
    @Autowired
    GetUserAccess getUserAccess;

    @Override
    public CategoryRequest createCategory(CategoryRequest request) {
        // Check if the category already exists
        List<Category> existingCategories = categoryRepository.findAll();
        for (Category category : existingCategories) {
            if (category.getName().equalsIgnoreCase(request.getName())) {
                // Return a message if the category already exists
                throw new CustomException("Category with the name '" + request.getName() + "' already exists.");
            }
        }
        int countIndex=existingCategories.size() + 1;
        // Create a new Category if it doesn't exist
        Category newCategory = new Category();
        newCategory.setName(request.getName());
        newCategory.setDescription(request.getDescription());
        newCategory.setStatus("1");//1=active;0=dis-active
        newCategory.setCreateBy(request.getCreateBy());
        newCategory.setCreateDate(LocalDateTime.now());
        newCategory.setIndexShow(countIndex);
        newCategory.setCreateBy(String.valueOf(getUserAccess.getUserAccess().getId()));
        // Save the new category
        categoryRepository.save(newCategory);
        return request; // or convert and return the saved entity as needed
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.lstBySortCategory(); // Retrieve all categories from the repository
        List<CategoryResponse> categoryResponses = new ArrayList<>();

        for (Category category : categories) {
            CategoryResponse response = new CategoryResponse();
            response.setId(category.getId());
            response.setName(category.getName());
            response.setDescription(category.getDescription());
            response.setStatus(category.getStatus());
            response.setCreateDate(category.getCreateDate());
            response.setIndexShow(category.getIndexShow());
            // Set other necessary fields
            categoryResponses.add(response);
        }

        return categoryResponses;
    }

    @Override
    public List<CategoryResponse> getByCategoriesASEC() {
        List<Category> categories = categoryRepository.lstBySortCategory(); // Retrieve all categories from the repository
        List<CategoryResponse> categoryResponses = new ArrayList<>();

        for (Category category : categories) {
            CategoryResponse response = new CategoryResponse();
            response.setId(category.getId());
            response.setName(category.getName());
            response.setDescription(category.getDescription());
            response.setStatus(category.getStatus());
            response.setCreateDate(category.getCreateDate());
            response.setIndexShow(category.getIndexShow());
            // Set other necessary fields

            categoryResponses.add(response);
        }

        return categoryResponses;
    }

    @Override
    public void handleSortedData( List<SortedItem> sortedItemList) {
        List<Category> listAllCategory = categoryRepository.lstBySortCategory();
        // Iterate through each SortedItem in the provided list
        for (SortedItem sortedItem : sortedItemList) {
            // Find the category with the same ID as the current sortedItem
            for (Category category : listAllCategory) {
                if (category.getId().equals(sortedItem.getId())) {
                    // Update the index of the category
                    category.setIndexShow(sortedItem.getIndex());
                    // Save the updated category back to the repository
                    categoryRepository.save(category);
                    // Optional: break once the category has been updated
                    break;
                }
            }
        }
    }

    @Override
    public List<CategoryResponse> getActiveCategories() {
        List<Category> categories = categoryRepository.lstActiveCategory(); // Retrieve all categories from the repository
        List<CategoryResponse> categoryResponses = new ArrayList<>();

        for (Category category : categories) {
            CategoryResponse response = new CategoryResponse();
            response.setId(category.getId());
            response.setName(category.getName());
            response.setDescription(category.getDescription());
            response.setStatus(category.getStatus());
            response.setCreateDate(category.getCreateDate());
            response.setIndexShow(category.getIndexShow());
            // Set other necessary fields

            categoryResponses.add(response);
        }

        return categoryResponses;
    }

    @Override
    public Optional<CategoryResponse> getCategoryById(Long id) {
        // Retrieve the category by its ID
        Optional<Category> categoryOptional = categoryRepository.findById(id);

        // Check if the category exists
        if (categoryOptional.isPresent()) {
            // Convert Category to CategoryResponse if it exists
            Category category = categoryOptional.get();
            CategoryResponse categoryResponse = new CategoryResponse();
            categoryResponse.setId(category.getId());
            categoryResponse.setName(category.getName());
            categoryResponse.setDescription(category.getDescription());
            categoryResponse.setStatus(category.getStatus());
            categoryResponse.setCreateDate(category.getCreateDate());
            categoryResponse.setIndexShow(category.getIndexShow());
            // Set other necessary fields

            return Optional.of(categoryResponse);
        } else {
            // Return an empty Optional if the category doesn't exist
            return Optional.empty();
        }
    }

    @Override
    public void updateCategory(Long id, CategoryRequest categoryRequest) {
        // Find the category by its ID
        Optional<Category> categoryOptional = categoryRepository.findById(id);

        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            // Update the category details
            category.setName(categoryRequest.getName());
            category.setDescription(categoryRequest.getDescription());
            category.setStatus(categoryRequest.getStatus());
            category.setIndexShow(categoryRequest.getIndexShow());
            category.setUpdateBy(String.valueOf(getUserAccess.getUserAccess().getId()));
            category.setUpdateDate(LocalDateTime.now());

            // Save the updated category
            categoryRepository.save(category);
        } else {
            throw new CustomException("Category not found with ID: " + id);
        }
    }

    @Override
    public void updateCategoryStatus(Long id, String status) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);

        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            category.setStatus(status); // Update the status (e.g., "0" for inactive)
            categoryRepository.save(category); // Save the updated category
        } else {
            throw new CustomException("Category not found with ID: " + id);
        }
    }

    public boolean isNameDuplicate(String name) {
        // Check if the title exists in the database
        return categoryRepository.existsByName(name);
    }

}
