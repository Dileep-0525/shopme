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
		
		$("input[name='extraImage']").each(function(index){
			$(this).change(function(){
				showExtraImageThumbnail(this,index);
			});
		});
	});
	
	
	function showExtraImageThumbnail(fileInput,index){
		var file = fileInput.files[0];
			var reader = new FileReader();
			reader.onload = function(e) {
				$("#extrathumbnail" +index).attr("src", e.target.result);
			};
			reader.readAsDataURL(file);
			
			addNextExtraImageSection(index + 1);
	}

	function addNextExtraImageSection(index){
		html = `
				<div class="col border m-3 p-2">
					<div><label>Extra Image #${index + 1 }:</label></div>
					<div class="m-2">
						<img id="extrathumbnail${index}" alt="Extra Image #${index + 1} preview" class="img-fluid" src="${defaultImageThumbnailSrc}">
					</div>
					<div>
						<input type="file" name="extraImage"
							onchange="showExtraImageThumbnail(this,${index})"
						 accept="image/png,image.jpeg"/>
					</div>
				</div>
		`;
		
		$("#divProductImages").append(html);
	}
		
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