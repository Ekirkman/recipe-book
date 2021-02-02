package org.liftoff.recipebook.controllers;

import org.liftoff.recipebook.models.User;
import org.liftoff.recipebook.models.data.RecipeCategoryRepository;
import org.liftoff.recipebook.models.data.RecipeRepository;
import org.liftoff.recipebook.models.Recipe;
import org.liftoff.recipebook.models.RecipeCategory;
import org.liftoff.recipebook.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller

public class RecipeController {
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeCategoryRepository recipeCategoryRepository;

    @Autowired
    AuthenticationController authenticationController;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("CreateRecipe")
    public String renderCreateRecipeForm(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        User sessionUser = authenticationController.getUserFromSession(session);
        int userId = sessionUser.getId();

        model.addAttribute("recipe",new Recipe());
        model.addAttribute("categories", recipeCategoryRepository.findAll());
        model.addAttribute("profile", userRepository.findById(userId).get());
        return "CreateRecipe";
    }

    @PostMapping("CreateRecipe")
    public String createRecipe(@RequestParam String name, Recipe recipe, @RequestParam String description,
                               @RequestParam String hiddenIngredients, @RequestParam RecipeCategory category,
                               @RequestParam String imageUrl, HttpSession session,Model model){


        //Get the userId from the session
        int currentUserId = (int) session.getAttribute("user");


        //save the recipe to the database
        recipe.setUserId(currentUserId);
        recipe.setImageUrl(imageUrl);
        recipe.setName(name);
        recipe.setDescription(description.trim());//added .trim() to get rid of unnecessary white space
        recipe.setIngredients(hiddenIngredients);
        recipe.setCategory(category);
        recipeRepository.save(recipe);
        User user = userRepository.findById(currentUserId).get();
        model.addAttribute("profile", userRepository.findById(currentUserId).get());
        model.addAttribute("user", user);
        String[] myIngredients = recipe.getIngredients().split("\\$\\$");
        model.addAttribute("myIngredients",myIngredients);

    return "view";

    }


    @GetMapping("delete")
    public String displayRemoveRecipe(Model model, HttpServletRequest request) {
    HttpSession session = request.getSession();
    User sessionUser = authenticationController.getUserFromSession(session);
    int userId = sessionUser.getId();
    model.addAttribute("profile", userRepository.findById(userId).get());
    model.addAttribute("recipesToDelete",recipeRepository.getAllRecipesByUserId(userId));
    return "delete";
    }

    @PostMapping("delete")
    public String removeRecipe(@RequestParam int deleteThis, HttpServletRequest request,
                               Model model){
        recipeRepository.deleteById(deleteThis);
        HttpSession session = request.getSession();
        User sessionUser = authenticationController.getUserFromSession(session);
        User user = userRepository.findById(sessionUser.getId()).get();
        int userId = sessionUser.getId();
        model.addAttribute("user", user);
        model.addAttribute("profile", userRepository.findById(userId).get());
        return "profile";
    }


    //Edit code still in progress still gives an error when a empty recipe is submitted


    @GetMapping("editRecipe")
    public String displayChooseARecipeToEdit(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User sessionUser = authenticationController.getUserFromSession(session);
        int userId = sessionUser.getId();
        model.addAttribute("profile", userRepository.findById(userId).get());
        model.addAttribute("recipesToEdit",recipeRepository.getAllRecipesByUserId(userId));
        return "editRecipe";
    }
    @PostMapping("editRecipe")
    public String displayEditRecipeForm(@RequestParam int editThis, HttpServletRequest request,
                               Model model){
        HttpSession session = request.getSession();
        User sessionUser = authenticationController.getUserFromSession(session);
        User user = userRepository.findById(sessionUser.getId()).get();
        int userId = sessionUser.getId();
        Recipe needToSplit = recipeRepository.findById(editThis);
        String[] currentIngredients = needToSplit.getIngredients().split("\\$\\$");
        model.addAttribute("user", user);
        model.addAttribute("profile", userRepository.findById(userId).get());
        model.addAttribute("editThisRecipe",recipeRepository.findById(editThis));
        model.addAttribute("categories", recipeCategoryRepository.findAll());
        model.addAttribute("currentIngredients",currentIngredients);
        return "editRecipeForm";
    }
    @PostMapping("saveEditedRecipe")
    public String saveEditedRecipe(@RequestParam String name, Recipe recipe, @RequestParam String description,
                               @RequestParam(required = false) String hiddenIngredients, @RequestParam RecipeCategory category,
                               @RequestParam String imageUrl, HttpSession session,@RequestParam String oldRecipeId,
                                   @RequestParam(required = false) String originalIngredients) {

        String p = hiddenIngredients;
        String v = originalIngredients;
        String x ="";
        //Get the userId from the session
//        int currentUserId = (int) session.getAttribute("user");
        int i = Integer.parseInt(oldRecipeId);
        Recipe recipeBeingEdited = recipeRepository.findById(i);
        //save the recipe to th database
        if(v == null && p == null){
            return "redirect";
        }else{
            if(p.trim()!= null){x = v.replace(",","$$");}
            //cant use commas in ingredients yet
            String newIngredients = x.concat("$$"+p);
            if(newIngredients.trim() !=""){recipeBeingEdited.setIngredients(newIngredients);}
        }

       if(imageUrl.trim()!=""){recipeBeingEdited.setImageUrl(imageUrl);}
        recipeBeingEdited.setName(name);
        recipeBeingEdited.setDescription(description.trim());//added .trim() to get rid of unnecessary white space
        recipeBeingEdited.setCategory(category);
        recipeRepository.save(recipeBeingEdited);
        return "redirect:";
    }
}










