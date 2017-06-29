function populateCategories() {
	var categories = getCategoryTree();

	var categoryTree = buildCategoryTrees(categories);
	$("#categories").html(categoryTree);
}

function buildCategoryTrees(categories) {
	var wrapper = $("#categoryTemplate").clone();
	if (categories.length == 0) {
		return;
	}
	
	for (i in categories) {
		var category = categories[i];
		wrapper.append("<a href='photos?categoryId="+category.id+"'>"+category.name+"</a>");
		wrapper.append(buildCategoryTrees(category.categories));
	}
	
	return wrapper;
}

function getCategoryTree() {
	$.getJSON( "categoryTree", {}, function( response ) {
		return response.categories;
	});
}