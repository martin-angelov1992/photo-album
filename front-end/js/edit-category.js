$.getScript( "js/category-operations.js", function() {
	$.getJSON("/category-with-path/"+requestInfo.id, function(category) {
		populateParentCategories(category.parentId);
		$("#newName").val(category.name)
		$("#pathHolder").text("Path: "+category.path);
	});
});

function populateEditCategory() {
}

$(document).on("click", "#submit", function() {
	tryEdit();
});

function tryEdit() {
	var name = $("#newName").val();
	var categoryId = requestInfo.id;
	var parent = $('input[name=parent]:checked', '#editCategoryForm').val();
	$("#errorHolder").text("");
	$.ajax({url: "/category/"+categoryId, data: {newName: name, parent: parent}, 
		method: "PUT",
		success: function() {
			openPage("photos", "categoryId="+categoryId);
	}}).fail(function(response, status, xhr) {
		showError(response.responseJSON);
	});
}