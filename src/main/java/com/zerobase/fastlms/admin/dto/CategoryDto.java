package com.zerobase.fastlms.admin.dto;

import com.zerobase.fastlms.admin.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    private Long id;
    private String categoryName;
    private int sortValue;
    private boolean usingYn;

    private int courseCount;

    public static List<CategoryDto> of(List<Category> categories) {
        List<CategoryDto> categoryDtos = new ArrayList<>();

        if (!CollectionUtils.isEmpty(categories)) {
            categories.forEach(e -> {
                categoryDtos.add(of(e));
            });
            return categoryDtos;
        }

        return null;
    }

    private static CategoryDto of(Category e) {
        return CategoryDto.builder()
                .id(e.getId())
                .categoryName(e.getCategoryName())
                .sortValue(e.getSortValue())
                .usingYn(e.isUsingYn())
                .build();
    }
}
