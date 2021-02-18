package org.liftoff.recipebook.models;

import java.util.ArrayList;

public class RecipeData {

    public static ArrayList<Recipe> findRecipes(String termCheck, Iterable<Recipe> allRecipes) {

        ArrayList<Recipe> currentRecipes = new ArrayList<>();

        String searchTerm = termCheck.toLowerCase();

        for (Recipe recipe : allRecipes) {

            if (recipe.getName().toLowerCase().contains(searchTerm)) {
                currentRecipes.add(recipe);

            } else if (recipe.getDescription().toLowerCase().contains(searchTerm)) {
                currentRecipes.add(recipe);

            }else if (recipe.getIngredients().toLowerCase().contains(searchTerm)) {
                currentRecipes.add(recipe);
            }
        }
        return currentRecipes;
    }
}