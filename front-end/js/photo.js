function populatePhoto() {
	console.log("Starting to populate photo.");
	var id = requestInfo.id;
	
	showPhoto(id);
}

function showPhoto(id) {
	$.getJSON( "/manage-photo/"+id, function( photo ) {
		console.log("Showing photo: %o", photo);
		if (photo == null) {
			showPhotoNotFound();
			return;
		}
		
		showPhotoPath(photo);
		$("#photoName").html("Name: <i>"+photo.name+"</i>");
		$("#owner").html("Owner: <i>"+photo.owner+"</i>");
		$("#description").html("Description: <i>"+photo.description+"</i>");
		$("#dateAdded").html("Date Added: <i>"+photo.dateAdded+"</i>");
		addImage(photo.id);
	});
}

function addImage(id) {
	var imageHtml = "<img src='/photo/"+id+"'/>";
	
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

function getCategoryLink(id) {
	return "#categories?id="+id;
}