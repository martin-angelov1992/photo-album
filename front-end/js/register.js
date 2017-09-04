var errorMap = {
	invalidUsername: "Invalid username. Please use only alphanumeric letters.",
	invalidPassword: "Invalid password. Password should be atleast 6 characters long.",
	invalidEmail: "This e-mail is not valid.",
	invalidName: "Pleace specify a name.",
	usernameAlreadyExists: "This username already exists in our system."
};

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
	$.post("/register", {
		username: username,
		password: password,
		email: email,
		name: name
		
	}, function(account) {
		populateLoggedInProfileNav(account);
		openPage("categories");
	}).fail(function(response, status, xhr) {
		reportRegistrationError(response.responseJSON);
	});
}

function reportRegistrationError(error) {
	var key = convertConstantToKey(error);

	error = errorMap[key];

	$("#registrationError").html(error);
}