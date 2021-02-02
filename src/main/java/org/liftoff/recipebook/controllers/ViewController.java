package org.liftoff.recipebook.controllers;

import org.liftoff.recipebook.models.Recipe;
import org.liftoff.recipebook.models.data.RecipeCategoryRepository;
import org.liftoff.recipebook.models.data.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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


    @RequestMapping(value = "recipe")
    public String view(Model model) {
        model.addAttribute("recipes", recipeRepository.findAll());
        model.addAttribute("categories", recipeCategoryRepository.findAll());
//        List<String> ingredientList = Arrays.asList(recipe.ingredients.split("$$"));

        return "view/{id}";
    }

}