$.getScript( "js\category-operations.js" );

function populateCreateCategory() {
	populateParentCategories();
}

$("#createCategoryForm").submit(function(e) {
	e.preventDefault();
	tryCreate();
});

function tryCreate() {
	var name = $("#name").val();
	var parent = $("#parent").val();
	$("#errorHolder").text("");
	$.post("/category", {name: name, parent: parent}, function(newId) {
		openPage("photos", "categoryId="+newId);
	}).fail(function(errorCode, status, xhr) {
		showError(errorCode);
	});
}

function populateParentCategories() {
	populateParents();
}

function populateParents() {
	addNoParentOption();
	$.getJSON( "/category", {}, 
		function(categories) {
			for (i in categories) {
				var category = categories[i];
				
				populateParent(category);
			}
		}
	);
}

function addNoParentOption() {
	$("#parentsHolder").append("<input type='radio' name='parent' value='none' checked='checked'/> "
			+ "<i>No Parent</i><br>");
}

function populateParent(category) {
	var path = category.path;
	var name = category.name;
	var id = category.id;
	
	$("#parentsHolder").append("<input type='radio' name='parent' value='"+id+"'/> "
			+name+" <span style='color:gray'>"+escapeHtml(path)+"</span><br>");
}