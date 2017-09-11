var errorMap = {
		notLoggedIn: "You must login, in order to delete photos.",
		notOwner: "Photo not found."
	};

function populatePhotos(session) {
	var requestInfo = getRequestInfo(window.location.hash);
	var categoryId = requestInfo.categoryId;
	$.getJSON("/category/"+categoryId, function(category) {
		showCategory(category, session);
	});
}

function deletePhoto(id) {
	$.ajax({
	    url: '/photo/'+id,
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

$(document).on("click", "[data-delete-photo]", function(e) {
	e.preventDefault();

	var id = $(this).attr("data-delete-photo");
	
	var confirm = alert("Are you sure you want to delete this photo?");
	
	if (!confirm) {
		return;
	}
	
	deletePhoto(id);
});

function showCategory(category, session) {
	$("#categoryWrapper #categoryName").text(category.name);
	$("#categoryWrapper #ownerName").text(category.owner);

	if (session.loggedIn && category.ownerId == session.account.id) {
		console.log("Adding edit button");
		addEditLink(category);
	}

	if (category.subCategories.length == 0) {
		$("#categoryWrapper").append("<div class='category'>None</div>");
	} else {
		showSubCategories(category);
	}

	showThumbnails(category);
}

function addEditLink(category) {
	$("#editPlaceholder").html("<a href='#edit-category?id="+
			category.id+"'>Edit</a>");
}

function showSubCategories(category, parentId) {
	console.log("Showing category: %o", category);
	var subCategories = category.subCategories;

	var wrapperId = null;
	
	if (!parentId) {
		wrapperId = "#categoryWrapper";
	} else {
		wrapperId = "#subCategory_"+parentId;
	}
	
	for (i in subCategories) {
		var subCategory = subCategories[i];
		
		$(wrapperId).append("<div id='subCategory_"+subCategory.id+"' class='category'><a href='#photos?categoryId="+
				subCategory.id+"'>"+escapeHtml(subCategory.name)+"</div>");
		
		showThumbnails(subCategory, subCategory.id)
		showSubCategories(subCategory, subCategory.id);
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
	
	return '<h4><a href="#edit-photo?id="'+thumbnail.id+'">(edit)</a> | <a href="#" data-delete-photo="'
	+thumbail.id+'">(delete)</a></h4>';
}