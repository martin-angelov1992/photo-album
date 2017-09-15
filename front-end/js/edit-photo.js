var photoId = null;

var errorMap = {
		notLoggedIn: "You must login in order to edit photos.",
		photoNotFound: "This photo does not exist."
	};

function populateEditPhoto() {
	var photoId = requestInfo.id;
	$.getJSON( "/manage-photo/"+photoId, {}, function( photo ) {
		console.log("Populating photo: %o", photo);
		populatePhotoDetails(photo);
	});
}

$(document).ready(function() {
	$("#editPhotoForm").submit(function(e){
		e.preventDefault();

		var name = $("#name").val();
		var description = $("#description").val();

		var photoId = requestInfo.id;


		$.ajax({url: "/manage-photo/"+photoId,
				type: "PUT",
				data: {name: name, description: description},
				success: function (response) {
					openPage("photo", "id="+photoId);
				}}).fail(function(response, status, xhr) {
					showError(response.responseJSON);
				});
	});
})

function populatePhotoDetails(photo) {
	$("#name").val(photo.name);
	$("#description").val(photo.description);
}