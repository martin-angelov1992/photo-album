var errorMap = {
		notLoggedIn: "You need to login in order to upload photos.", 
		categoryDoesNotExist: "This category no longer exists."}
}

$(document).ready(function() {
	$.getJSON( "/category", {}, function(categories) {
		var i;
		for (i in categories) {
			var category = categories[i];

			appendCategoryOption(category);
		}
	});

	$("#uploadPhotoForm").submit(function(e) {
		e.preventDefault();
		
		var formData = new FormData();

		var name = $("#name").val();
		var description = $("#description").val();
		var category = $("#category").val();
		var file = $("#file").val();

		formData.append('file', file, file.name);
		formData.append('name', name);
		formData.append('description', description);
		formData.append('categoryID', category);

		var xhr = new XMLHttpRequest();
		xhr.onload = function () {
		  if (xhr.status === 200) {
			  $("#submit").html('Submit');
		  } else {
			  showError("");
		  }
		};
		
		xhr.open('POST', '/manage-photo', true);
		xhr.send(formData);
	});
})

function appendCategoryOption(category) {
	$('#category').append($('<option>', {
	    value: category.id,
	    text: category.name+" ("+category.path+")"
	}));
}