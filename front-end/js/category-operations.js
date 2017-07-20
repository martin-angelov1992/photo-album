var errorMap = {
	emptyName: "You cannot create a category with an empty name.",
	illegalCharacter: "You have illegal character in your category name."
};

function capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}

function convertConstantToKey(constant) {
	var key = "";
	var parts = constant.split("_");
  
  for (i in parts) {
  	var part = parts[i];
    var lowerCasePart = part.toLowerCase();
    
  	if (i == 0) {
      key += lowerCasePart;
    	continue;
    }
    
    key += capitalizeFirstLetter(lowerCasePart);
  }
  
  return key;
}

function showError(errorCode) {
	var errorKey = convertConstantToKey(constant);
	
	var errorText = errorMap[errorCode];

	$("#errorHolder").text(errorText);
}