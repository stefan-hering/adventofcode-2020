
data class Recipe(
    val ingredients: Set<String>,
    val allergens: Set<String>
)

fun readLines(): List<String> =
    Unit.javaClass.getResource("/input")
        .readText()
        .split("\n")

fun readRecipes(): List<Recipe> = readLines().asSequence()
    .map { it.replace(")", "").split("(contains ") }
    .map {
        Recipe(
            ingredients = it[0]
                .split(" ")
                .filter { it.isNotBlank() }
                .toSet(),
            allergens = if (it.size > 1)
                it[1].split(", ")
                    .filter { it.isNotBlank() }
                    .toSet() else emptySet()
        )
    }
    .toList()

fun getPossibleIngredientsForAllergens(recipes: List<Recipe>): Map<String, Set<String>> {
    val possibleAllergents = mutableMapOf<String, Set<String>>()
    recipes.forEach { recipe ->
        recipe.allergens.forEach { allergen ->
            possibleAllergents[allergen]?.let {
                possibleAllergents[allergen] = it.intersect(recipe.ingredients)
            } ?: possibleAllergents.put(allergen, recipe.ingredients)
        }
    }
    return possibleAllergents
}

fun getAllergensToIngredients(allergensToPossibleIngredients: Map<String, Set<String>>): MutableMap<String, String> {
    val allergensToIngredients = mutableMapOf<String, String>()
    var possibleAllergens = allergensToPossibleIngredients

    while(possibleAllergens.isNotEmpty()) {
        for (allergen in possibleAllergens.entries) {
            val ingredients = allergen.value.minus(allergensToIngredients.keys)

            if(ingredients.size == 1) {
                allergensToIngredients[ingredients.first()] = allergen.key
                possibleAllergens = possibleAllergens - allergen.key
            }
        }
    }

    return allergensToIngredients
}

fun main() {
    val recipes = readRecipes()

    val allergensToIngredients =
        getAllergensToIngredients(getPossibleIngredientsForAllergens(recipes))

    println(recipes
        .flatMap { it.ingredients }
        .filter { allergensToIngredients.contains(it).not() }
        .count())

    println(allergensToIngredients.asSequence()
        .sortedBy { it.value }
        .map { it.key }
        .joinToString(","))
}
