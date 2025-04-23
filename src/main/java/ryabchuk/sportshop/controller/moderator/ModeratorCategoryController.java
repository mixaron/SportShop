package ryabchuk.sportshop.controller.moderator;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ryabchuk.sportshop.model.product.Category;
import ryabchuk.sportshop.service.product.CategoryService;

@Controller
@RequestMapping("/moderator/categories")
@AllArgsConstructor
public class ModeratorCategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "moderator/categories/list";
    }

    @GetMapping("/create")
    public String createCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "moderator/categories/form";
    }

    @PostMapping
    public String createCategory(@ModelAttribute @Valid Category category, BindingResult result) {
        if (result.hasErrors()) {
            return "moderator/categories/form";
        }

        try {
            categoryService.createCategory(category);
        } catch (IllegalArgumentException e) {
            result.rejectValue("name", "error.category", e.getMessage());
            return "moderator/categories/form";
        }
        return "redirect:/moderator/categories";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/moderator/categories";
    }
}
