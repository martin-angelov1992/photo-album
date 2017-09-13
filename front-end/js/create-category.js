$.getScript( "js/category-operations.js", function() {
	populateParentCategories();
});

function populateCreateCategory() {
}

$("#createCategoryForm").submit(function(e) {
	e.preventDefault();
	tryCreate();
});

function tryCreate() {
	var name = $("#name").val();
	var parent = $('input[name=parent]:checked', '#createCategoryForm').val();
	$("#errorHolder").text("");
	$.post("/category", {name: name, parent: parent}, function(newId) {
		openPage("photos", "categoryId="+newId);
	}).fail(function(errorCode, status, xhr) {
		showError(errorCode.responseJSON);
	});
}

