var errorMap = {
	notLoggedIn: "You must login, in order to edit your profile.",
	invalidEmail: "This e-mail is invalid.",
	invalidName: "Invalid name. Please, do not leave blank and use only alphanumeric characters."
}

$(document).ready(function(e) {
	$.getJSON( "/profile", {}, function( response ) {
		var name = response.name;
		var email = response.mail;
		
		$("#name").val(name);
		$("#email").val(email);
		
		return response;
	});

	$("#editProfileForm").submit(function(e) {
		e.preventDefault();
		e.stopPropagation();
		
		var name = $("#name").val();
		var email = $("#email").val();

		$.ajax({url: "/profile",
			type: "PUT",
			data: {name: name, email: email},
			success: function() {
				alert("Edit Successful!")
				openPage("categories");
			}}).fail(function(response, status, xhr) {
				reportEditError(response.responseJSON);
		})
	})
});

function reportEditError(error) {
	var key = convertConstantToKey(error);

	error = errorMap[key];

	$("#editProfileError").html(error);
}