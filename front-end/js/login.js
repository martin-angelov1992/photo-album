$(document).on("click", "#loginBtn", function(e) {
	e.preventDefault();
	tryLogin();
});

function tryLogin() {
	console.log("Trying to login.");
	var username = $("#usernameField").val();
	var password = $("#passwordField").val();
	
	tryLoginWithCredentials(username, password);
}

function tryLoginWithCredentials(username, password) {
	$("#loginError").html("");
	$.post( "/login", {
		username: username,
		password: password
	}, function( account) {
		handleSuccessfulLogin(account);
	}).fail(function(response, status, xhr) {
		reportFailedLogin();
	});
}

function reportFailedLogin() {
	$("#loginError").html("Login failed.");
}

function handleSuccessfulLogin(account) {
	populateLoggedInProfileNav(account);
	openPage("categories");
}