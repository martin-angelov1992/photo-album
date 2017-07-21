function populatePhotos() {
	var requestInfo = getRequestInfo(hash);
	var id = requestInfo.id;
	
	showCategory(id);
}

function showCategory(id) {
	var category = getCategory(id);
	
	showSubCategories(category);
	showThumbnails(category);
}

function getCategory(id) {
	
}

function showSubCategories(category, containerId) {
	var subCategories = category.subCategories;

	for (i in subCategories) {
		var subCategory = subCategories[i];
		var newContainerId = containerId+"_"+subCategory.id;
		
		$("#"+categoryId).append("<div id='"+newCategoryId+"' class='category'><h3>"+escapeHtml(category)+"</h3></div>");
		
		showThumbnails(subCategory, newCategoryId)
		showSubCategories(subCategory, newContainerId);
	}
}

function showThumbnails(category, containerId) {
	var thumbnails = category.thumbnails;
	
	for (i in thumbnails) {
		var thumbnail = thumbnails[i];
		
		$("#"+categoryId).append("<div><h4>"+escapeHtml(thumbnail.name)+"</h4><img src='thumbnail/'"+thumbnail.id+"/></div>")
	}
}