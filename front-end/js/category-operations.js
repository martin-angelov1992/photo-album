var errorMap = {
	emptyName: "You cannot create a category with an empty name.",
	illegalCharacter: "You have illegal character in your category name.",
	notLoggedIn: "You need to login, in order to edit categories.",
	notOwner: "You are not owner of this category."
};

function populateParentCategories(parent) {
	populateParents(parent);
}

function populateParents(parent) {
	addNoParentOption(!parent);
	console.log("Populating parents. Selected parent: "+parent);
	$.getJSON( "/category", {}, 
		function(categories) {
			for (i in categories) {
				var category = categories[i];
				var checked = category.id == parent;
				
				populateParent(category, checked);
			}
		}
	);
}

function addNoParentOption(checked) {
	$("#parentsHolder").append("<input type='radio' name='parent' value='none'" + 
			(checked ? " checked='checked'" : "") + "/> "
			+ "<i>No Parent</i><br>");
}

function populateParent(category, checked) {
	var path = category.path;
	var name = category.name;
	var id = category.id;
	
	$("#parentsHolder").append("<input type='radio' name='parent' value='"+id+"'" + 
			(checked ? " checked='checked'" : "") + "/> "
			+name+" <span style='color:gray'>"+escapeHtml(path)+"</span><br>");
}