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
	    url: '/manage-photo/'+id,
	    type: 'DELETE',
	    success: function(result) {
	    	removePhoto(id);
	    }
	}).fail(function(response, status, xhr) {
		alertError(response.responseJSON);
	});
}

function removePhoto(id) {
	$("[data-photo-id='"+id+"']").remove();
}

$(document).on("click", "[data-delete-category]", function(e) {
	e.preventDefault();
	
	if (!confirm("Are you sure you want to delete this category?")) {
		return false;
	}

	$.ajax({url: "/category/"+$(this).attr("data-delete-category"),
		type: "DELETE",
		data: {},
		success: function() {
			openPage("categories");
		}}).fail(function(response, status, xhr) {
			alertError(response.responseJSON);
	})
});

$(document).on("click", "[data-delete-photo]", function(e) {
	e.preventDefault();

	var id = $(this).attr("data-delete-photo");
	
	var answer = confirm("Are you sure you want to delete this photo?");
	
	if (!answer) {
		return;
	}
	
	deletePhoto(id);
});

function showCategory(category, session) {
	$("#categoryWrapper #categoryName").text(category.name);
	$("#categoryWrapper #ownerName").text(category.owner);

	if (session.loggedIn && category.ownerId == session.account.id) {
		console.log("Adding edit button");
		addManageLinks(category, session);
	}

	if (category.subCategories.length == 0) {
		$("#categoryWrapper").append("<div class='category'>None</div>");
	} else {
		showSubCategories(category, null, session);
	}

	showThumbnails(category, "#categoryWrapper", session);
}

function addManageLinks(category) {
	$("#manageHolder").html("<a href='#' data-delete-category='"+category.id+"'>Delete</a> | <a href='#edit-category?id="+
			category.id+"'>Edit</a>");
}

function showSubCategories(category, parentId, session) {
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
		
		showThumbnails(subCategory, subCategory.id, session);
		showSubCategories(subCategory, wrapperId, session);
	}
}

function showThumbnails(category, containerId, session) {
	var thumbnails = category.thumbnails;
	
	for (i in thumbnails) {
		var thumbnail = thumbnails[i];

		var editHTML = getEditHTML(thumbnail, session);
		console.log("Showing thumbnail %o, in container "+containerId, thumbnail);
		$(containerId).append("<div data-photo-id='"+thumbnail.id+"'><span>"+escapeHtml(thumbnail.name)+"</span>"+editHTML+
				"<a href='#photo?id="+thumbnail.id+"'><img src='/thumbnail/"+thumbnail.id+"'/></a></div>")
	}
}

function getEditHTML(thumbnail, sessionInfo) {
	if (!sessionInfo.loggedIn || thumbnail.ownerID != sessionInfo.account.id) {
		return "";
	}
	
	return '<h4><a href="#edit-photo?id='+thumbnail.id+'">(edit)</a> | <a href="#" data-delete-photo="'
	+thumbnail.id+'">(delete)</a></h4>';
}