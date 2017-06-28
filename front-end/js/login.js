$(document).on("click", "#loginBtn", function() {
	tryLogin();
});

function tryLogin() {
	var username = $("#usernameField").val();
	var password = $("#passwordField").val();
	
	tryLoginWithCredentials(username, password);
}

function tryLoginWithCredentials(username, password) {
	$("#loginError").html("");
	$.getJSON( "login", {
		username: username,
		password: password
	}, function( response ) {
		var account = response.account;
		
		if (account == null) {
			reportFailedLogin();
		} else {
			handleSuccessfulLogin(account);
		}
	});
}

function reportFailedLogin() {
	$("#loginError").html("Login failed.");
}

function handleSuccessfulLogin(account) {
	populateLoggedInProfileNav(account);
	openPage("categories");
}