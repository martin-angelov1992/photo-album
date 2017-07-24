var errorMap = {
		notLoggedIn: "You must login, in order to delete photos.",
		notOwner: "Photo not found."
	};

function populatePhotos() {
	var requestInfo = getRequestInfo(hash);
	var id = requestInfo.id;
	
	showCategory(id);
}

function deletePhoto(id) {
	$.ajax({
	    url: '/photo',
	    data: {id: id},
	    type: 'DELETE',
	    success: function(result) {
	    	var status = result.status;
	    	
	    	if (status == "OK" || status == "PHOTO_NOT_FOUND") {
	    		removePhoto(id);
	    	} else {
	    		showError(status)
	    	}
	    }
	});
}

function removePhoto(id) {
	$("[data-photo-id='"+id+"']").remove();
}

function getCategory(id) {
	
}

$(document).on("click", "[data-delete-photo]", function(e) {
	e.preventDefault();

	var id = $(this).attr("data-delete-photo");
	
	var confirm = alert("Are you sure you want to delete this photo?");
	
	if (!confirm) {
		return;
	}
	
	deletePhoto(id);
});

function showCategory(id) {
	var category = getCategory(id);
	
	showSubCategories(category);
	showThumbnails(category);
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

		var editHTML = getEditHTML(thumbnail);
		$("#"+categoryId).append("<div data-photo-id='"+thumbnail.id+"'><h4>"+escapeHtml(thumbnail.name)+"</h4>"+editHTML+"<img src='thumbnail/'"+thumbnail.id+"/></div>")
	}
}

function getEditHTML(thumbnail) {
	if (!sessionInfo.loggedIn || thumbnail.ownerID != sessionInfo.account.id) {
		return "";
	}
	
	return '<h4><a href="#editPhoto?id="'+thumbnail.id+'">(edit)</a> | <a href="#" data-delete-photo="'
	+thumbail.id+'">(delete)</a></h4>';
}