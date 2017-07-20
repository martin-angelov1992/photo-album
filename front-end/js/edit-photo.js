var photoId = null;

function populateEditPhoto() {
	photoId = requestInfo.id;
	$.getJSON( "photo/"+photoId, {}, function( response ) {
		var photo = response.photo;
		pupulatePhotoDetails(photo);
	});
}

function populatePhotoDetails(photo) {
	var name = $("#name");
	var description = $("#description");
	
	$.post("photo/"+photoId, 
			{name: name, description: description},
			function (response) {
				
			});
}