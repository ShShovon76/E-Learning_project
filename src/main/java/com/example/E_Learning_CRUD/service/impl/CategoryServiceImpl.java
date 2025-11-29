package com.example.E_Learning_CRUD.service.impl;

import com.example.E_Learning_CRUD.dto.course.CategoryRequestDTO;
import com.example.E_Learning_CRUD.dto.course.CategoryResponseDTO;
import com.example.E_Learning_CRUD.entity.Category;
import com.example.E_Learning_CRUD.exception.ResourceNotFoundException;
import com.example.E_Learning_CRUD.repository.CategoryRepository;
import com.example.E_Learning_CRUD.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponseDTO create(CategoryRequestDTO dto) {
        Category c = new Category();
        c.setName(dto.getName());
        c.setDescription(dto.getDescription());
        if(dto.getParentCategoryId()!=null) {
            Category parent = categoryRepository.findById(dto.getParentCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", dto.getParentCategoryId()));
            c.setParentCategory(parent);
        }
        categoryRepository.save(c);
        return toResponse(c);
    }

    @Override
    public CategoryResponseDTO update(Long id, CategoryRequestDTO dto) {
        Category c = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", id));
        c.setName(dto.getName());
        c.setDescription(dto.getDescription());
        categoryRepository.save(c);
        return toResponse(c);
    }

    @Override
    public CategoryResponseDTO getById(Long id) {
        return toResponse(categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", id)));
    }

    @Override
    public List<CategoryResponseDTO> getAll() {
        return categoryRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if(!categoryRepository.existsById(id)) throw new ResourceNotFoundException("Category", id);
        categoryRepository.deleteById(id);
    }

    private CategoryResponseDTO toResponse(Category c) {
        CategoryResponseDTO r = new CategoryResponseDTO();
        r.setId(c.getId());
        r.setName(c.getName());
        r.setDescription(c.getDescription());
        r.setParentCategoryId(c.getParentCategory() != null ? c.getParentCategory().getId() : null);
        return r;
    }
}
