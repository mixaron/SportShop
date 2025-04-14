package ryabchuk.sportshop.controller.admin;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ryabchuk.sportshop.config.user.CustomUserDetails;
import ryabchuk.sportshop.model.Product;
import ryabchuk.sportshop.service.CategoryService;
import ryabchuk.sportshop.service.ProductService;

import java.io.IOException;

@Controller
@RequestMapping("/admin/products")
@AllArgsConstructor
public class AdminProductController {

    private final ProductService productService;
    private final CategoryService categoryService;


    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/products/list";
    }

    @GetMapping("/create")
    public String createProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/products/form";
    }

    @PostMapping
    public String createProduct(@ModelAttribute Product product,
                                @RequestParam Long categoryId,
                                @RequestParam("imageFile") MultipartFile imageFile,
                                @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {
        productService.createProduct(product, categoryId, userDetails.getId(), imageFile);
        return "redirect:/admin/products";
    }

    @GetMapping("/{id}/edit")
    public String editProductForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "admin/products/form";
    }

    @PostMapping("/{id}")
    public String updateProduct(@PathVariable Long id,
                                @ModelAttribute Product product,
                                @RequestParam Long categoryId,
                                @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        productService.updateProduct(id, product, categoryId, imageFile);
        return "redirect:/admin/products";
    }


    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
}
