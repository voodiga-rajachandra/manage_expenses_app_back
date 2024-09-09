package com.daurenassanbaev.firstpetproject.service;

import com.daurenassanbaev.firstpetproject.database.entity.Category;
import com.daurenassanbaev.firstpetproject.database.repository.CategoryRepository;
import com.daurenassanbaev.firstpetproject.dto.CategoryDto;
import com.daurenassanbaev.firstpetproject.validation.group.ExistsException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    public List<CategoryDto> findAllExpenses() {
        List<CategoryDto> list = categoryRepository.findAll().stream().filter(c -> c.getType().equals("expense")).map(c -> modelMapper.map(c, CategoryDto.class)).toList();
        return list;
    }
    public List<CategoryDto> findAllIncomes() {
        List<CategoryDto> list = categoryRepository.findAll().stream().filter(c -> c.getType().equals("income")).map(c -> modelMapper.map(c, CategoryDto.class)).toList();
        return list;
    }
    @Transactional
    public void save(CategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto, Category.class);
        var check = categoryRepository.findByCategoryName(categoryDto.getCategoryName());
        if (check.isPresent()) {
            throw new ExistsException("Category with this name is exists");
        }
        categoryRepository.save(category);
    }
    @Transactional
    public void delete(Integer id) {
        categoryRepository.deleteById(id);
    }
}
