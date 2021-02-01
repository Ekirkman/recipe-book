package org.liftoff.recipebook.controllers;

import org.liftoff.recipebook.models.RecipeCategory;
import org.liftoff.recipebook.models.User;
import org.liftoff.recipebook.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.liftoff.recipebook.models.data.RecipeCategoryRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller

public class RecipeCategoryController {

    @Autowired
    private RecipeCategoryRepository recipeCategoryRepository;

    @Autowired
    AuthenticationController authenticationController;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("createCategory")
    public String displayCreateCategory(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = authenticationController.getUserFromSession(session);
        int userId = sessionUser.getId();

        model.addAttribute("profile", userRepository.findById(userId).get());
        return "createCategory";
    }

    @PostMapping("createCategory")
    public String createCreateCategory(@RequestParam String name, Model model, RecipeCategory recipeCategory) {
        recipeCategory.setName(name);
        recipeCategoryRepository.save(recipeCategory);
        return "index";
    }
}
