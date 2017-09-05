$(document).on("submit", "#changePasswordForm", function() {
	tryChangePassword();
});

function tryChangePassword() {
	var password = $("#password").val();
	var passwordAgain = $("#passwordAgain").val();
	
	if (password == "") {
		showError("Password can not be empty.");
		return;
	}
	
	if (password != passwordAgain) {
		showError("The two passwords do not match.");
		return;
	}
	
	doChange(password);
}

function showError(error) {
	$("#passwordError").text(error);
}

function doChange(password) {
	sendChange(password);
}

function sendChange(password) {
	$.getJSON( "changePassword", {
		password: password
	}, function( response ) {
		showPage("edit-profile");
	});
}