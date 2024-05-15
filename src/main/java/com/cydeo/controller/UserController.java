package com.cydeo.controller;

import com.cydeo.dto.UserDTO;
import com.cydeo.repository.RoleRepository;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.RoleService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping("/user")
public class UserController {

    private final RoleService roleService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserController(RoleService roleService, UserService userService, UserRepository userRepository, RoleRepository roleRepository) {
        this.roleService = roleService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/create")
    public String createUser(Model model){

        model.addAttribute("user", new UserDTO());
        model.addAttribute("roles", roleService.findAllRoles());
        model.addAttribute("users", userService.findAllUsers());

        return "/user/create";

    }

    @PostMapping("/create")
    public String insertUser(@ModelAttribute("user") UserDTO user, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("roles", roleService.findAllRoles());
            model.addAttribute("users", userService.findAllUsers());

            return "/user/create";

        }

        userService.save(user);

        return "redirect:/user/create";

    }

    @GetMapping("/update/{username}")
    public String editUser(@PathVariable("username") String username, Model model) {

        model.addAttribute("user", userService.findByUserName(username));
        model.addAttribute("roles", roleService.findAllRoles());
        model.addAttribute("users", userService.findAllUsers());

        return "/user/update";

    }

    @PostMapping("/update")
    public String updateUser( @ModelAttribute("user") UserDTO user, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("roles", roleService.findAllRoles());
            model.addAttribute("users", userService.findAllUsers());

            return "/user/update";

        }

        userService.update(user);

        return "redirect:/user/create";

    }

    @GetMapping("/delete/{username}")
    public String deleteUser(@PathVariable("username") String username) {
        userService.delete(username);
        return "redirect:/user/create";
    }

}

