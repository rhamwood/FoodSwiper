package org.rowanhamwood.hungr

import org.rowanhamwood.hungr.local.database.DatabaseRecipe
import org.rowanhamwood.hungr.remote.network.RecipeModel

val recipeModel1 = RecipeModel(
    uri = "http://www.edamam.com/ontologies/edamam.owl#recipe_76af243a27a2c3f938003d551238833e",
    url = "http://www.goodhousekeeping.com/food-recipes/a15642/chess-pie-recipe-clv0314/",
    source = "Good Housekeeping",
    label = "Chess Pie",
    largeImage = "https://upload.wikimedia.org/wikipedia/commons/7/7e/Cherry-Pie-Slice.jpg",
    smallImage = "https://upload.wikimedia.org/wikipedia/commons/8/84/Coconut_cream_pie.jpg"
)

val recipeModel2 = RecipeModel(
    uri = "http://www.edamam.com/ontologies/edamam.owl#recipe_985748e59463ec8cd0a4182f22ad0e42",
    url = "http://www.rachaelray.com/recipe/limesicle-summer-pie-sicles/",
    source = "Rachael Ray",
    label = "Limesicle Summer Pie-Sicles",
    largeImage = "https://upload.wikimedia.org/wikipedia/commons/7/7e/Cherry-Pie-Slice.jpg",
    smallImage = "https://upload.wikimedia.org/wikipedia/commons/8/84/Coconut_cream_pie.jpg"
)

val databaseRecipe1 = DatabaseRecipe(
    uri = "http://www.edamam.com/ontologies/edamam.owl#recipe_76af243a27a2c3f938003d551238833e",
    url = "http://www.goodhousekeeping.com/food-recipes/a15642/chess-pie-recipe-clv0314/",
    source = "Good Housekeeping",
    label = "Chess Pie",
    image = "https://upload.wikimedia.org/wikipedia/commons/8/84/Coconut_cream_pie.jpg"
)

val databaseRecipe2 = DatabaseRecipe(
    uri = "http://www.edamam.com/ontologies/edamam.owl#recipe_985748e59463ec8cd0a4182f22ad0e42",
    url = "http://www.rachaelray.com/recipe/limesicle-summer-pie-sicles/",
    source = "Rachael Ray",
    label = "Limesicle Summer Pie-Sicles",
    image = "https://upload.wikimedia.org/wikipedia/commons/8/84/Coconut_cream_pie.jpg"
)



val responseDatabaseRecipeList = listOf<DatabaseRecipe>(databaseRecipe1, databaseRecipe2)

val responseRecipeList = listOf<RecipeModel>(recipeModel1, recipeModel2)