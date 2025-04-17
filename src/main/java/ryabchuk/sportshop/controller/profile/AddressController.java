package ryabchuk.sportshop.controller.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ryabchuk.sportshop.config.user.CustomUserDetails;
import ryabchuk.sportshop.dto.AddressDto;
import ryabchuk.sportshop.service.AddressService;

@Controller
@RequestMapping("/profile/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping
    public String viewAddress(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        model.addAttribute("address", addressService.getAddress(customUserDetails.getId()));
        return "address/view";
    }

    @GetMapping("/create")
    public String createAddressView(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        model.addAttribute("address", addressService.getAddress(customUserDetails.getId()));
        return "address/edit";
    }

    @PostMapping("/create")
    public String createAddress(@ModelAttribute AddressDto addressDto,
                                @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        addressService.addAddress(addressDto, customUserDetails.getId());
        return "redirect:/profile/address";
    }

    @PostMapping("/{id}/delete")
    public String deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return "redirect:/admin/categories";
    }
}
