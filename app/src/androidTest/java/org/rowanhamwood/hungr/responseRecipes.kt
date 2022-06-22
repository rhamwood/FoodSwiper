package org.rowanhamwood.hungr

import org.rowanhamwood.hungr.remote.network.RecipeModel

val recipeModel1 = RecipeModel(
    uri = "http://www.edamam.com/ontologies/edamam.owl#recipe_76af243a27a2c3f938003d551238833e",
    url = "http://www.goodhousekeeping.com/food-recipes/a15642/chess-pie-recipe-clv0314/",
    source = "Good Housekeeping",
    label = "Chess Pie",
    largeImage = "large",
    smallImage = "small"
)

val recipeModel2 = RecipeModel(
    uri = "http://www.edamam.com/ontologies/edamam.owl#recipe_985748e59463ec8cd0a4182f22ad0e42",
    url = "http://www.rachaelray.com/recipe/limesicle-summer-pie-sicles/",
    source = "Rachael Ray",
    label = "Limesicle Summer Pie-Sicles",
    largeImage = "large",
    smallImage = "small"
)



val responseRecipeList = listOf<RecipeModel>(recipeModel1, recipeModel2)