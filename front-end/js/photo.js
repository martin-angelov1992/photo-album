function populatePhoto() {
	var requestInfo = getRequestInfo(hash);
	var id = requestInfo.id;
	
	showPhoto(id);
}

function getPhoto(id) {
	$.getJSON( "photo", {
		id: id,
	}, function( response ) {
		var photo = response.photo;
		
		if (photo == null) {
			showPhotoNotFound();
			return;
		}
		
		showPhotoPath(photo);
		$("#photoName").text(photo.name);
		$("#owner").text(photo.owner);
		$("#description").text(photo.description);
		$("#dateAdded").text(photo.dateAdded);
		addImage(photo.id);
	});
}

function addImage(id) {
	var imageHtml = "<img src='image="+id+"'/>";
	
	$("#photoPlaceholder").html(imageHtml);
}

function showPhotoNotFound() {
	$("#photoError").text("Photo not found.");
}

function showPhotoPath(photo) {
	var pathHtml = "";
	
	var path = photo.path;
	
	for (i in path) {
		var category = path[i];
		
		var escapedName = escapeHtml(category.name);
		
		pathHtml += "<a id='"+getCategoryLink(category.id)+"'>"+escapedName+"</a>";
		
		if (i != path.length - 1) {
			pathHtml += " / ";
		}
	}
	
	$("#path").html(pathHtml);
}

function escapeHtml(text) {
    'use strict';
    return text.replace(/[\"&<>]/g, function (a) {
        return { '"': '&quot;', '&': '&amp;', '<': '&lt;', '>': '&gt;' }[a];
    });
}

function getCategoryLink(id) {
	return "#categories?id="+id;
}