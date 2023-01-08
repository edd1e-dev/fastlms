package com.zerobase.fastlms.admin.service;

import com.zerobase.fastlms.admin.dto.CategoryDto;
import com.zerobase.fastlms.admin.model.CategoryInput;

import java.util.List;

public interface CategoryService {
    boolean add(String categoryName);
    boolean update(CategoryInput categoryInput);
    boolean delete(long id);
    List<CategoryDto> getList();
    List<CategoryDto> getFrontList(CategoryDto categoryDto);
}
