var photoId = null;

var errorMap = {
		notLoggedIn: "You must login in order to edit photos.",
		photoNotFound: "This photo does not exist."
	};

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
				var errorCode = response.error;
				
				if (errorCode != null) {
					showError(errorCode);
					return;
				}
				
				showPage("photo/id="+photoId);
			});
}

