$(document).on("submit", "#changePasswordForm", function() {
	tryChangePassword();
});

function tryChangePassword() {
	$("#passwordError").text("");
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
	$.ajax({url: "/change-password",
		type: "PUT",
		data: {password: password},
		success: function() {
			openPage("edit-profile");
		}}).fail(function(response, status, xhr) {
			showError("Password should be atleast 6 characters long.");
	});
}