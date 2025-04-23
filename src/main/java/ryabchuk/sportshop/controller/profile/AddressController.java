package ryabchuk.sportshop.controller.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ryabchuk.sportshop.config.user.CustomUserDetails;
import ryabchuk.sportshop.dto.AddressDto;
import ryabchuk.sportshop.service.user.AddressService;

@Controller
@RequestMapping("/profile/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping("/create")
    public String createAddressView(Model model) {
        model.addAttribute("address", new AddressDto());
        return "address/create";
    }

    @PostMapping("/create")
    public String createAddress(@ModelAttribute AddressDto addressDto,
                                @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        addressService.addAddress(addressDto, customUserDetails.getId());
        return "redirect:/profile";
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
    public String editAddress(@ModelAttribute AddressDto addressDto,
                              @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        addressService.editAddress(addressDto, customUserDetails.getId());
        return "redirect:/profile";
    }
}
