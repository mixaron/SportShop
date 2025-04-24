package ryabchuk.sportshop.service.product;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ryabchuk.sportshop.model.product.Category;
import ryabchuk.sportshop.repository.product.CategoryRepository;
import ryabchuk.sportshop.repository.product.ProductRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
    }

    public void createCategory(Category category) {
        if (categoryRepository.findByName(category.getName()).isPresent()) {
            throw new IllegalArgumentException("Категория с таким названием уже существует");
        }
        if (category.getParent() != null) {
            category.setParent(getCategoryById(category.getParent().getId()));
        }
        categoryRepository.save(category);
    }

    public void updateCategory(Long id, Category updated) {
        Category category = getCategoryById(id);

        if (categoryRepository.existsByName(updated.getName())
                && updated.getDescription().equals(category.getDescription())) {
            throw new IllegalArgumentException("Категория с таким именем уже существует");
        }

        category.setName(updated.getName());
        category.setDescription(updated.getDescription());

        if (updated.getParent() != null) {
            category.setParent(getCategoryById(updated.getParent().getId()));
        } else {
            category.setParent(null);
        }

        categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        if (!productRepository.findByCategoryId(id).isEmpty()) {
            throw new IllegalStateException("Нельзя удалить категорию, в которой есть товары.");
        }
        categoryRepository.deleteById(id);
    }
}