package ryabchuk.sportshop.controller.moderator;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ryabchuk.sportshop.config.user.CustomUserDetails;
import ryabchuk.sportshop.model.product.Product;
import ryabchuk.sportshop.service.product.CategoryService;
import ryabchuk.sportshop.service.product.ProductService;

import java.io.IOException;

@Controller
@RequestMapping("/moderator/products")
@AllArgsConstructor
public class ModeratorProductController {

    private final ProductService productService;
    private final CategoryService categoryService;


    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "moderator/products/list";
    }

    @GetMapping("/create")
    public String createProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "moderator/products/form";
    }

    @PostMapping
    public String createProduct(@ModelAttribute Product product,
                                @RequestParam Long categoryId,
                                @RequestParam("imageFile") MultipartFile imageFile,
                                @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {
        productService.createProduct(product, categoryId, userDetails.getId(), imageFile);
        return "redirect:/moderator/products";
    }

    @GetMapping("/{id}/edit")
    public String editProductForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "moderator/products/form";
    }

    @PostMapping("/{id}")
    public String updateProduct(@PathVariable Long id,
                                @ModelAttribute Product product,
                                @RequestParam Long categoryId,
                                @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        productService.updateProduct(id, product, categoryId, imageFile);
        return "redirect:/moderator/products";
    }


    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/moderator/products";
    }
}
