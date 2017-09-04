$.getScript( "js\category-operations.js" );

function populateEditCategory() {
	populateParentCategories();
}

$(document).on("click", "#submit", function() {
	tryEdit();
});

function tryEdit() {
	var name = $("#name");
	var categoryId = $("#categoryId");
	$("#errorHolder").text("");
	$.post("category", {id: categoryId, name: name}, function(response) {
		var errorCode = response.errorCode;
		
		if (errorCode) {
			showError(errorCode);
			return;
		}
		
		var newId = response.newId;
		openPage("photos/"+newId);
	});
}