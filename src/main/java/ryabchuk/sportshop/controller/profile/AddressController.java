package ryabchuk.sportshop.controller.profile;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ryabchuk.sportshop.config.user.CustomUserDetails;
import ryabchuk.sportshop.dto.AddressDto;
import ryabchuk.sportshop.service.user.AddressService;

@Controller
@RequestMapping("/profile/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping("/create")
    public String createAddressView(@RequestParam(value = "required", required = false) Boolean required,
                                    @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                    Model model) {
        model.addAttribute("addressDto", new AddressDto());
        model.addAttribute("required", Boolean.TRUE.equals(required));
        if (Boolean.TRUE.equals(required)) {
            model.addAttribute("userId", customUserDetails.getId());
            model.addAttribute("requiredMessage", "Чтобы сделать заказ, укажите адрес.");
        }
        return "address/create";
    }

    @PostMapping("/create")
    public String createAddress(@ModelAttribute @Valid AddressDto addressDto,
                                BindingResult result,
                                @RequestParam(value = "required", required = false) Boolean required,
                                @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "address/create";
        }
        addressService.addAddress(addressDto, customUserDetails.getId());
        return Boolean.TRUE.equals(required) ? "redirect:/cart" : "redirect:/profile";
    }

    @DeleteMapping("/delete")
    public String deleteAddress(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        addressService.deleteAddressByUserId(customUserDetails.getId());
        return "redirect:/profile";
    }

    @GetMapping("/edit")
    public String editAddressPage(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        model.addAttribute("addressDto", addressService.getAddress(customUserDetails.getId()));
        return "address/edit";
    }

    @PatchMapping("/edit")
    public String editAddress(@ModelAttribute @Valid AddressDto addressDto,
                              BindingResult result,
                              @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (result.hasErrors()) {
            return "address/edit";
        }

        addressService.editAddress(addressDto, customUserDetails.getId());
        return "redirect:/profile";
    }
}
