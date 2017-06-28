function populateCategories() {
	var categories = getCategoryTree();
	
	buildCategoryTrees(categories);
}

function buildCategoryTrees(categories) {
	if (categories.length == 0) {
		return;
	}
	
	for (i in categories) {
		var category = categories[i];
		
		buildCategoryTrees(category.categories)
	}
}