function populateCategories() {
	$.getJSON("/category-tree", {}, function(categories) {
		console.log("Found categories: %o", categories);
		var categoryTree = buildCategoryTrees(categories, $("#categories"));

		console.log("category tree: %o", categoryTree);
	});
}

function buildCategoryTrees(categories, wrapper) {
	console.log("Found categories: %o", categories);
	if (categories == null || categories.length == 0) {
		return;
	}
	
	for (i in categories) {
		var category = categories[i];
		var subWrapperId = "subWrapper_"+category.id;
		wrapper.append("<a href='#photos?categoryId="+category.id+"'>"+category.name+"</a>");
		wrapper.append("<div id='"+subWrapperId+"' class='categoryWrapper'></div>");
		wrapper.append(buildCategoryTrees(category.categories, $("#"+subWrapperId)));
	}
	
	return wrapper;
}