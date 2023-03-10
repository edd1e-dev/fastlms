package com.zerobase.fastlms.admin.service;

import com.zerobase.fastlms.admin.dto.CategoryDto;
import com.zerobase.fastlms.admin.entity.Category;
import com.zerobase.fastlms.admin.mapper.CategoryMapper;
import com.zerobase.fastlms.admin.model.CategoryInput;
import com.zerobase.fastlms.admin.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    private Sort getSortBySortValue() {
        return Sort.by(Sort.Direction.DESC, "sortValue");
    }

    @Override
    public boolean add(String categoryName) {
        // 중복체크 필요

        Optional<Category> optionalCategory =
                categoryRepository.findByCategoryName(categoryName);

        if (optionalCategory.isPresent()) {
            return false;
        }

        Category category = Category.builder()
                .categoryName(categoryName)
                .usingYn(true)
                .sortValue(0)
                .build();

        categoryRepository.save(category);

        return true;
    }

    @Override
    public boolean update(CategoryInput categoryInput) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryInput.getId());
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            category.setCategoryName(categoryInput.getCategoryName());
            category.setSortValue(categoryInput.getSortValue());
            category.setUsingYn(categoryInput.isUsingYn());
            categoryRepository.save(category);
        }

        return true;
    }

    @Override
    public boolean delete(long id) {
        categoryRepository.deleteById(id);
        return true;
    }

    @Override
    public List<CategoryDto> getList() {
        List<Category> categories = categoryRepository.findAll(getSortBySortValue());
        return CategoryDto.of(categories);
    }

    @Override
    public List<CategoryDto> getFrontList(CategoryDto categoryDto) {
        return categoryMapper.select(categoryDto);
    }
}
