package org.liftoff.recipebook.controllers;

import org.liftoff.recipebook.models.Recipe;
import org.liftoff.recipebook.models.User;
import org.liftoff.recipebook.models.data.RecipeCategoryRepository;
import org.liftoff.recipebook.models.data.RecipeRepository;
import org.liftoff.recipebook.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping(value = "view")
public class ViewController {

    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private RecipeCategoryRepository recipeCategoryRepository;
    @Autowired
    AuthenticationController authenticationController;
    @Autowired
    private UserRepository userRepository;


    @RequestMapping(value = "recipe")
    public String view(Model model,HttpServletRequest request,@RequestParam("id")String id) {
        HttpSession session = request.getSession();
        User sessionUser = authenticationController.getUserFromSession(session);
        int userId = sessionUser.getId();

        int i = Integer.parseInt(id);
        Recipe viewThisRecipe = recipeRepository.findById(i);
        String[] myIngredients = viewThisRecipe.getIngredients().split("\\$\\$");
        model.addAttribute("profile", userRepository.findById(userId).get());
        model.addAttribute("recipes", recipeRepository.findAll());
        model.addAttribute("categories", recipeCategoryRepository.findAll());
        model.addAttribute("myIngredients",myIngredients);
        model.addAttribute("recipe",viewThisRecipe);

        return "view";
    }

}