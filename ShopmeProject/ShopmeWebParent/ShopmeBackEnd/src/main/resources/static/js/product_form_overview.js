dropdownBrands = $("#brand");
	dropdownCategories = $("#category");
	
	$(document).ready(function(){
		    
		$("#shortDescription").richText();
		$("#fullDescription").richText();
		dropdownBrands.change(function(){
			dropdownCategories.empty(); 
			getCategories();
		});
		
		getCategories();
	});
	
	
	
		
	function checkUnique(form){
        // Check if the product name is unique
        productId = $("#id").val();
        productName = $("#name").val();
      
        csrfValue = $("input[name='_csrf']").val();
        
        params = {id: productId, name: productName, _csrf: csrfValue};
        
        $.post(checkUniqueUrl, params, function(response){
            if(response == "OK"){
               form.submit();
            } else if(response == "Duplicate"){
                showWarningModal("There is another product having the name " + productName);
            }else{
	            showErrorModal("Unkonwn response from the server: ");
            }		
        }).fail(function(){
            showErrorModal("Could not connect to the server");
        });
        
        return false;
    }
	
	function getCategories(){
		brandId = dropdownBrands.val();
		url = brandModuleURL + "/" + brandId + "/categories";
		
		$.get(url, function(responseJson){
			$.each(responseJson, function(index,category){
				$("<option>").val(category.id).text(category.name).appendTo(dropdownCategories);
				console.log(dropdownCategories);
				console.log(category.id, category.name);
			});
		});
	}