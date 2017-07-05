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
		
		
	}
}

function showThumbnails(category, containerId) {
	var thumbnails = category.thumbnails;
	
	for (i in thumbnails) {
		var thumbnail = thumbnails[i];
		
		
	}
}