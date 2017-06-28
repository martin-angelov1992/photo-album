$(document).on("click", "#registerBtn", function() {
	tryRegister();
});

function tryRegister() {
	var username = $("#usernameField").val();
	var password = $("#passwordField").val();
	var retypePassword = $("#retypePasswordField").val();
	var email = $("#emailField").val();
	var name = $("#nameField").val();
	
	tryRegisterWithDetails(username, password, retypePassword, email, name);
}

function tryRegisterWithDetails(username, password, retypePassword, email, name) {
	if (password != retypePassword) {
		reportRegistrationError("The two password fields do not match");
		return;
	}
	
	$("#registrationError").html("");
	$.getJSON( "login", {
		username: username,
		password: password,
		email: email,
		name: name
		
	}, function(response) {
		if (response.error != null) {
			reportRegistrationError(response.error);
		} else {
			populateLoggedInProfileNav(response.account);
			openPage("categories");
		}
	});
}

function reportRegistrationError(error) {
	$("#registrationError").html(error);
}