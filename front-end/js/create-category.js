$.getScript( "category-operations.js" );

function populateCreateCategory() {
	populateParentCategories();
}

$(document).on("click", "#submit", function() {
	tryCreate();
});

function tryCreate() {
	var name = $("#name");
	var parent = $("#parent");
	$("#errorHolder").text("");
	$.post("category", {name: name, parent: parent}, function(response) {
		var errorCode = response.errorCode;
		
		if (errorCode) {
			showError(errorCode);
			return;
		}
		
		var newId = response.newId;
		openPage("photos/"+newId);
	});
}


function populateParentCategories() {
	var categories = getAllCategories();
	populateParents();
}

function populateParents() {
	$.getJSON( "category/all", {}, 
		function( response ) {
			var categories = response.categories;
			
			for (i in categories) {
				var category = categories[i];
				
				populateParent(category);
			}
		}
	);
}

function populateParent(category) {
	var path = category.path;
	var name = category.name;
	var id = category.id;
	
	$("#parentsHolder").append("<input type='radio' name='parent' value='"+id+"'/> "
			+name+" <span style='color:gray'>"+escapeHtml(path)+"</span><br>");
}