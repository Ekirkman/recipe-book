package org.liftoff.recipebook.controllers;

import org.liftoff.recipebook.models.Recipe;
import org.liftoff.recipebook.models.RecipeData;
import org.liftoff.recipebook.models.User;
import org.liftoff.recipebook.models.UserData;
import org.liftoff.recipebook.models.data.RecipeRepository;
import org.liftoff.recipebook.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    AuthenticationController authenticationController;

    @PostMapping("search")
    public String displaySearchResults(Model model, @RequestParam String searchTerm, HttpServletRequest request){

        List<User> results = new ArrayList<>();
        results = UserData.findUser(searchTerm, userRepository.findAll());

        List<Recipe> recipeResults = new ArrayList<>();
        recipeResults = RecipeData.findRecipes(searchTerm, recipeRepository.findAll());


        HttpSession session = request.getSession();
        User sessionUser = authenticationController.getUserFromSession(session);
        int userId = sessionUser.getId();

        model.addAttribute("profile", userRepository.findById(userId).get());
        model.addAttribute("results", results);
        model.addAttribute("searchTerm", searchTerm);
        model.addAttribute("recipeNameResults",recipeResults);
        return "search";
    }
}
