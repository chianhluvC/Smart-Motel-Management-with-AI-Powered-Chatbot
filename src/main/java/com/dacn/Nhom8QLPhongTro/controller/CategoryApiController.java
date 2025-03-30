package com.dacn.Nhom8QLPhongTro.controller;


import com.dacn.Nhom8QLPhongTro.dto.CategoryDto;
import com.dacn.Nhom8QLPhongTro.entity.Category;
import com.dacn.Nhom8QLPhongTro.services.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

@Controller
@Transactional
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryApiController {

    private final CategoryService categoryService;


    private CategoryDto convertToCategoryDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setPrice(category.getPrice());
        return categoryDto;
    }




    @GetMapping
    @ResponseBody
    public List<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories().stream().map(this::convertToCategoryDto).toList();
    }





}
