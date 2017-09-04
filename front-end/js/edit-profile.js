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
});

$(document).on("click", "#editProfileForm", function(e) {
	var name = $("#name").val();
	var email = $("#email").val();

	$.post({url: "/profile", 
		email: email,
		type: "PUT",
		name: name,	
		success: function() {
			alert("Edit Successful!")
			openPage("categories");
		}}).fail(function(response, status, xhr) {
			reportEditError(response.responseJSON);
	})
});

function reportEditError(error) {
	var key = convertConstantToKey(error);

	error = errorMap[key];

	$("#editProfileError").html(error);
}