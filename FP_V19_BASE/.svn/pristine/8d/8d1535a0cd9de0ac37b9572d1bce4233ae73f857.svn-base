/*
Java Script: Validations.
 
VERSION 1.0.0
Last Modified: 23-09-2010
*/

// ------------------- ON KEY PRESS VALIDATION ------------------------//

/*
 * Alpha Numeric Only.
 */
function alphaNumOnly(evt){
	var charCode = (evt.which) ? evt.which : window.event.keyCode;
	if (charCode <= 13){
		return true;
	}else{
		var keyChar = String.fromCharCode(charCode);
		var re = /[a-zA-Z0-9_-]/
		return re.test(keyChar);
	}	
}

function alphaNumOnly2(evt){
	var charCode = (evt.which) ? evt.which : window.event.keyCode;
	if (charCode <= 13){
		return true;
	}else{
		var keyChar = String.fromCharCode(charCode);
		var re = /[a-zA-Z0-9]/
		return re.test(keyChar);
	}	
}


/*
 * Alpha Numeric and space only.ñ
 */
function alphaNumSpaceOnly(evt){	
	var charCode = (evt.which) ? evt.which : window.event.keyCode;
	if (charCode <= 13){
		return true;
	}else{
		var keyChar = String.fromCharCode(charCode);
		var re = /[\sa-zA-ZñÑ0-9_-]/
		return re.test(keyChar);			
	}	
}


/*
 * Alpha Numeric and space only.ñ
 */
function alphaNumSpaceOnly2(evt){	
	var charCode = (evt.which) ? evt.which : window.event.keyCode;
	if (charCode <= 13){
		return true;
	}else{
		var keyChar = String.fromCharCode(charCode);
		var re = /[\sA-ZÑÁÉÍÓÚa-zñáéíóú0-9]/;
		return re.test(keyChar);			
	}	
}

/*
 * No white space.
 */
function noWhiteSpace(evt){	
	var charCode = (evt.which) ? evt.which : window.event.keyCode;

	if (charCode <= 13){
		return true;
	}
	else{
		var keyChar = String.fromCharCode(charCode);
		var re = /\s/ 
		return !re.test(keyChar);
	}	
}

/*
 * Numeric Only.
 */
function numOnly(evt){	
	var charCode = (evt.which) ? evt.which : window.event.keyCode;
	if (charCode <= 13){
		return true;
	}else{
		var keyChar = String.fromCharCode(charCode);
		var re = /[0-9]/
		return re.test(keyChar);
	}	
}

function dateOnly(evt){	
	var charCode = (evt.which) ? evt.which : window.event.keyCode;
	if (charCode <= 13){
		return true;
	}else{
		var keyChar = String.fromCharCode(charCode);
		var re = /[0-9\/]/
		return re.test(keyChar);
	}	
}

/*
 * Alpha only.
 */
function alphaOnly(evt){	
	var charCode = (evt.which) ? evt.which : window.event.keyCode;

	if (charCode <= 13){
		return true;
	}else{
		var keyChar = String.fromCharCode(charCode);
		var re = /[a-zA-ZñÑ]/
		return re.test(keyChar);
	}	
}
 
/*
 * Alpha and Space only.
 */
function alphaSpaceOnly(evt){	
	var charCode = (evt.which) ? evt.which : window.event.keyCode;
 	if ((charCode <= 13) || (charCode == 46) ){
		return true;
	}else{
		var keyChar = String.fromCharCode(charCode);
		var re = /[\sA-ZÑÁÉÍÓÚa-zñáéíóú\\s]/
		return re.test(keyChar);
	}	
}



function especialBankName(evt){	
	var charCode = (evt.which) ? evt.which : window.event.keyCode;
	if (charCode <= 13){
		return true;
	}else{
		var keyChar = String.fromCharCode(charCode);
		var re = /[\sA-ZÑÁÉÍÓÚa-zñáéíóú0-9-]/
		return re.test(keyChar);			
	}	
}

/*
 * Alpha and Space only.
 */
function alphaSpacePoint(evt){	
	var charCode = (evt.which) ? evt.which : window.event.keyCode;
 	if ((charCode <= 13) || (charCode == 46) ){
		return true;
	}else{
		var keyChar = String.fromCharCode(charCode);
		var re = /[\sA-ZÑÁÉÍÓÚa-zñáéíóú.\\s]/
		return re.test(keyChar);
	}	
}

/*
 * Alpha only.
 */
function alphaNum2(evt){	
	var charCode = (evt.which) ? evt.which : window.event.keyCode;

	if (charCode == 13 || charCode==32){
		return false;
	}else{
		return true;
	}	
}

function borrarCero (campo){

   if(campo.value==0){
      campo.value="";
   } 
       
}

function solonumerosNoPrimerCero (evento,campo){
	   // e = e || window.event;
    if(evento.ctrlKey)   
    {
        // alert("Hizo CRTL")
    }
    else
    {
        // alert("ELSE");
        
        var key;
        
        if(window.event) // IE
        {
            key = evento.keyCode;
            if (key < 48 || key > 57 ) 
            {
                window.event.keyCode=0;
                return false;
            }else{
            	if(key == 48 && campo.value.length==0){
            		return false;
            	}
            	return true;
            }
        }
        else if(evento.which) // Netscape/Firefox/Opera
        {
            key = evento.which;
            
            if ((key >= 48 && key <=  57) || key == 8 || key == 9 || key == 0 ) 
            {
            	if(key == 48 && campo.value.length==0){
            		return false;
            	}
                return true;
            }else{
            	return false;
            }
        }
       
    }	
}


function solonumeros(evento){
	   // e = e || window.event;
	    if(evento.ctrlKey)   
	    {
	        // alert("Hizo CRTL")
	    }
	    else
	    {
	        // alert("ELSE");
	        
	        var key;
	        
	        if(window.event) // IE
	        {
	            key = evento.keyCode;
	            if (key < 48 || key > 57 ) 
	            {
	                window.event.keyCode=0;
	                return false;
	            }else{
	            	return true;
	            }
	        }
	        else if(evento.which) // Netscape/Firefox/Opera
	        {
	            key = evento.which;
	            
	            if ((key >= 48 && key <=  57) || key == 8 || key == 9 || key == 0 ) 
	            {
	                return true;
	            }else{
	            	return false;
	            }
	        }
	       
	    }
	
}

function noEspecial(evt){
	if(window.event){
	      keynum = evt.keyCode;
	    }else{
	      keynum = evt.which;
	    }
		
		if(((keynum >=33) && (keynum <=47)) || ((keynum >=58) && (keynum <=64)) || ((keynum >=91) && (keynum <=96)) || ((keynum >=123) && (keynum <=127))
				|| (keynum >= 166 && keynum<=180) || (keynum>=184 && keynum<=223) || (keynum ==238) || (keynum>=240) || (keynum>=161)){
			return false;
		} else {
			return true;
		}
	}

function noEspecialFacialTag(evt){
	if(window.event){
	      keynum = evt.keyCode;
	    }else{
	      keynum = evt.which;
	    }
		
		if(((keynum >=48) && (keynum <=57)) || ((keynum >=65) && (keynum <=90)) || ((keynum >=97) && (keynum <=122))){
			return true;
		} else {
			return false;
		}
	}

function method(evt){ 
    if(window.event && window.event.keyCode == 27){
     window.event.keyCode =  505; 
    }
    if(window.event && window.event.keyCode == 505){ 
     return false;    
    }
    else{
    	return true;
    }
   }

	
function separador(donde,caracter,evt){
	if(window.event){
		  keynum = evt.keyCode;
		}else{
		  keynum = evt.which;
		}
  if((keynum != 37) && (keynum !=39)){
	pat = /[\*.\+.\(.\).\?.\.$.\[.\].\^]/;
	valor = donde.value;
	largo = valor.length;		
	crtr = true;
	
	
	// alert(keynum);
	
	if(isNaN(caracter) || pat.test(caracter) == true){
		if (pat.test(caracter)==true){ 
			caracter = "\\" + caracter;
		}
		carcter = new RegExp(caracter,"g");
		valor = valor.replace(carcter,"");
		donde.value = valor.substring(0, 15);
		crtr = false;
	}
	else{
		var nums = new Array();
		cont = 0;
		for(m=0;m<largo;m++){
			if(valor.charAt(m) == "." || valor.charAt(m) == " ")
				{continue;}
			else{
				nums[cont] = valor.charAt(m);
				cont++;
			}
		}
	}
	var cad1="",cad2="",tres=0;
	if(largo > 3 && crtr == true){
		for (k=nums.length-1;k>=0;k--){
			cad1 = nums[k];
			cad2 = cad1 + cad2;
			tres++;
			if((tres%3) == 0){
				if(k!=0){
					cad2 = "." + cad2;
				}
			}
		}		
		donde.value = cad2.substring(0, 15);
	}
  }
}	

function separadorMiles(donde,caracter,evt){
	if(window.event){
		  keynum = evt.keyCode;
		}else{
		  keynum = evt.which;
		}
  if((keynum != 37) && (keynum !=39)){
	pat = /[\*.\+.\(.\).\?.\.$.\[.\].\^]/;
	valor = donde.value;
	largo = valor.length;		
	crtr = true;

	if(isNaN(caracter) || pat.test(caracter) == true){
		if (pat.test(caracter)==true){ 
			caracter = "\\" + caracter;
		}
		carcter = new RegExp(caracter,"g");
		valor = valor.replace(carcter,"");
		donde.value = valor.substring(0, 15);
		crtr = false;
	}
	else{
		var nums = new Array();
		cont = 0;
		for(m=0;m<largo;m++){
			if(valor.charAt(m) == "." || valor.charAt(m) == " " || valor.charAt(m) == "$")
				{continue;}
			else{
				nums[cont] = valor.charAt(m);
				cont++;
			}
		}
	}
	var cad1="",cad2="",tres=0;
	if(largo > 3 && crtr == true){
		for (k=nums.length-1;k>=0;k--){
			cad1 = nums[k];
			cad2 = cad1 + cad2;
			tres++;
			if((tres%3) == 0){
				if(k!=0){
					cad2 = "." + cad2;
				}
			}
		}		
		donde.value = "$"+cad2.substring(0, 15);
	}else if((largo > 0 && largo < 3) && crtr == true){
		for (k=nums.length-1;k>=0;k--){
			cad1 = nums[k];
			cad2 = cad1 + cad2;
			tres++;			
		}
		donde.value = "$"+cad2;
	}
  }
}
/**
 * @author ablasquez
 * @param evt
 * @returns {Boolean} funcion para el manejo de texto en el campo razon social
 *          de los formularios de registro de cliente, permitiendo en este campo
 *          numero y los caracteres especiales &,-,.,_ y / ademas de texto.
 */
function noEspecialRazSol(evt){
	var evento = evt || window.event;
	var propiedad = evento.charCode || evento.keyCode;
	
	if(window.event){
	      keynum = evt.keyCode;
	    }else{
	      keynum = propiedad;
	    }	
	    // alert ("keynum: "+keynum);
		if(((keynum >=33) && (keynum <=47)) || ((keynum >=58) && (keynum <=64)) || ((keynum >=91) && (keynum <=96)) || ((keynum >=123) && (keynum <=127)) || 
		(keynum >= 166 && keynum<=180) || (keynum>=184 && keynum<=223) || (keynum ==238) || (keynum>=240)){
		  if(keynum == 9 || keynum == 38 || keynum ==45 || (keynum==8) || keynum ==209 
				  || keynum ==241 || keynum ==46 || keynum ==95 || keynum ==47 || keynum ==243 || keynum ==250){		 
		    return true;
		  }else{	
			return false;
		  }	
		} else {		    
			if(keynum == 161){
				return false;
			}else{
				return true;
			}
		}
	}

function noEspecialOptionalNumber(evt){
	var evento = evt || window.event;
	var propiedad = evento.charCode || evento.keyCode;
	
	if(window.event){
	      keynum = evt.keyCode;
	    }else{
	      keynum = propiedad;
	    }	
	    // alert ("keynum: "+keynum);
		if(((keynum >=33) && (keynum <=47)) || ((keynum >=58) && (keynum <=64)) || ((keynum >=91) && (keynum <=96)) || ((keynum >=123) && (keynum <=127)) || 
		(keynum >= 166 && keynum<=180) || (keynum>=184 && keynum<=223) || (keynum ==238) || (keynum>=240)){
		  if(keynum == 35 || keynum == 36 || keynum == 9 || keynum == 38 || keynum ==45 || (keynum==8)){		 
		    return true;
		  }else{	
			return false;
		  }	
		} else {		    
			return true;
		}
	}

function especialCompany(evt){
	var evento = evt || window.event;
	var propiedad = evento.charCode || evento.keyCode;
	
	if(window.event){
	      keynum = evt.keyCode;
	    }else{
	      keynum = evt.which;
	    }	
	    
		// alert(keynum);
		if(((keynum >=33) && (keynum <=47)) || ((keynum >=58) && (keynum <=64)) || ((keynum >=91) && (keynum <=96)) || ((keynum >=123) && (keynum <=127))  || (keynum==161) || (keynum==168)){
		  if(keynum == 35 || keynum == 36 || keynum == 37 || keynum == 39 || keynum == 9 || keynum == 38 || keynum ==45 || keynum ==8 || keynum ==46 || keynum ==47 || keynum ==95
				  (keynum ==193) || (keynum ==201) || (keynum ==205) || (keynum ==208) || (keynum ==218)
				     ){		 
		    return true;
		  }else{	
			return false;
		  }	
		} else {		    
			return true;
		}
	}


function especialCompany2(evt){
	var evento = evt || window.event;
	var propiedad = evento.charCode || evento.keyCode;
	
	if(window.event){
	      keynum = evt.keyCode;
	    }else{
	      keynum = evt.which;
	    }	
	    
		// alert(keynum);
		if(((keynum >=33) && (keynum <=47)) || ((keynum >=58) && (keynum <=64)) || ((keynum >=91) && (keynum <=96)) || ((keynum >=123) && (keynum <=127)) || ((keynum>=172) && (keynum<=176)) || (keynum==161) || (keynum==168) || (keynum==180) ){
				 
			if( (keynum == 39) || (keynum == 9) || (keynum == 38) || (keynum ==45) || (keynum ==8) || (keynum ==46) || (keynum ==47) || (keynum ==95) ||
			  (keynum ==193) || (keynum ==201) || (keynum ==205) || (keynum ==208) || (keynum ==218) ||
			  (keynum ==38) || (keynum 	==95) || (keynum ==45) || (keynum ==46) || (keynum ==44) || (keynum ==47)){
				return true;
			}
			else{
				 return false;
			}
		   
		 
		} else {	
			return true;
		}
	}


function especialCompany3(evt){
	var evento = evt || window.event;
	var propiedad = evento.charCode || evento.keyCode;
	
	if(window.event){
	      keynum = evt.keyCode;
	    }else{
	      keynum = evt.which;
	    }	
	    
		// alert(keynum);
		if(((keynum >=33) && (keynum <=47)) || ((keynum >=58) && (keynum <=64)) || ((keynum >=91) && (keynum <=96)) || ((keynum >=123) && (keynum <=127))  || (keynum==161) || (keynum==168) || (keynum==180)
				 || (keynum==193) || (keynum==201) || (keynum==205) || (keynum==211) || (keynum==218) || (keynum==225) || (keynum==233) || (keynum==237) || (keynum==243) || (keynum==250) || (keynum==191)  
		     ){
				 
			if( (keynum == 9) || (keynum ==45) || (keynum ==8) || (keynum ==46) || (keynum ==47) || (keynum ==95) ||
			     (keynum ==208) ||
			  (keynum ==40) || (keynum ==41) || (keynum ==95) || (keynum ==45) || (keynum ==46)  || (keynum ==47)
			   ){
				return true;
			}
			else{
				 return false;
			}
		   
		 
		} else {		    
			return true;
		}
	}


/*
 * function especialCompany4(evt){ var evento = evt || window.event; var
 * propiedad = evento.charCode || evento.keyCode;
 * 
 * if(window.event){ keynum = evt.keyCode; }else{ keynum = evt.which; }
 * 
 * //alert(keynum); if(((keynum >=33) && (keynum <=47)) || ((keynum >=58) &&
 * (keynum <=64)) || ((keynum >=91) && (keynum <=96)) || ((keynum >=123) &&
 * (keynum <=127)) || (keynum==161) || (keynum==168) || (keynum==180) ||
 * (keynum==191)){
 * 
 * if( (keynum == 9) || (keynum ==45) || (keynum ==8) || (keynum ==46) ||
 * (keynum ==47) || (keynum ==95) || (keynum ==193) || (keynum ==201) || (keynum
 * ==205) || (keynum ==208) || (keynum ==218) || (keynum ==95) || (keynum ==45) ||
 * (keynum ==46) || (keynum ==44) || (keynum ==47) || (keynum ==40) || (keynum
 * ==41) || (keynum ==59) ){ return true; } else{ return false; } } else {
 * return true; } }
 */

	function especialCompany4(evt){	
		var charCode = (evt.which) ? evt.which : window.event.keyCode;
		if (charCode ='13'){
			return true;
		}else{
			var keyChar = String.fromCharCode(charCode);
			var re = /[\sA-ZÑÁÉÍÓÚa-zñáéíóú0-9 \/.,;_()-]/;
			return re.test(keyChar);			
		}	
	}
	
function especialCompany5(evt){
	var evento = evt || window.event;
	var propiedad = evento.charCode || evento.keyCode;
	
	if(window.event){
	      keynum = evt.keyCode;
	    }else{
	      keynum = evt.which;
	    }	
	    
		// alert(keynum);
		if(((keynum >=33) && (keynum <=39)) ||  ((keynum >=58) && (keynum <=64)) || ((keynum >=91) && (keynum <=96)) || ((keynum >=123) && (keynum <=127))  || (keynum==161) || (keynum==168) || (keynum==180) ||
				(keynum==42) || (keynum==43)){
				 
			if( (keynum == 9) || (keynum ==45) || (keynum ==8) || (keynum ==46) || (keynum ==47) || (keynum ==95) ||
			  (keynum ==193) || (keynum ==201) || (keynum ==205) || (keynum ==211) || (keynum ==218) ||
			   (keynum ==95) || (keynum ==44) || (keynum ==40) || (keynum ==41)  || (keynum ==59)
			   (keynum ==225) || (keynum ==233) || (keynum ==237) || (keynum ==243)  || (keynum ==250)
			     ){
				return true;
			}
			else{
				 return false;
			}
		   
		 
		} else {		    
			return true;
		}
	}
/**
 * @author arivera
 * @param evt
 * 
 * 
 * validacion para la furza de seguidad de la contraseña
 * 
 */

function validateFormLnsClientOut(){								 
	var quantityCapitalLetter = 0;
	var quantityChararterSpecial = 0;
	var quantityLowerCaseLetter = 0;
	var quantityNumberPassword = 0;	
    var newPassword=document.getElementById("Formulario:Contrasena").value;
    var sizePasword=newPassword.length;	
    var newPasswordC=document.getElementById("Formulario:Contrasena2").value;
	var dato;
	for (var i=0; i < newPassword.length; i++){
		dato = newPassword.charCodeAt(i); 
	
		if (dato >= 65 && dato <=90 || dato==209){
	         quantityCapitalLetter++;	        
	          document.getElementById("Formulario:imageCapital").src="/web.bo.peajes.com/img/ok.png";
	          document.getElementById("Formulario:capital").style.color = "#424242";
		}else if (dato >= 97 && dato <=122 || dato==241){
			 quantityLowerCaseLetter++;	
			 document.getElementById("Formulario:imageLower").src="/web.bo.peajes.com/img/ok.png";
			 document.getElementById("Formulario:lower").style.color = "#424242";
		}else if (dato >= 48 && dato <=57){
			quantityNumberPassword++;	
			document.getElementById("Formulario:imageNumber").src="/web.bo.peajes.com/img/ok.png";
			 document.getElementById("Formulario:number").style.color = "#424242";
			 
		} else if (dato >= 33 && dato <=47 || dato==59 || dato==61 || dato==63 || dato==191){
			quantityChararterSpecial++;
			document.getElementById("Formulario:imageCharacter").src="/web.bo.peajes.com/img/ok.png";
			document.getElementById("Formulario:character").style.color = "#424242";
			
		}
	}
	if((quantityNumberPassword==1&&quantityCapitalLetter>=0&&quantityLowerCaseLetter>=0&&quantityChararterSpecial>=0) ||
	   (quantityNumberPassword>=0&&quantityCapitalLetter==1&&quantityLowerCaseLetter>=0&&quantityChararterSpecial>=0) ||
	   (quantityNumberPassword>=0&&quantityCapitalLetter>=0&&quantityLowerCaseLetter==1&&quantityChararterSpecial>=0) ||
	   (quantityNumberPassword>=0&&quantityCapitalLetter>=0&&quantityLowerCaseLetter>=0&&quantityChararterSpecial==1)){
		 document.getElementById("Formulario:levelText1").style.backgroundColor = "#CC0000";
		 document.getElementById('Formulario:textLevel').innerHTML = "Baja";
	}
	else if(quantityNumberPassword>=2&&quantityCapitalLetter>=2&&quantityLowerCaseLetter>=2&&quantityChararterSpecial>=2&&(sizePasword<=11)||
		   (quantityNumberPassword==2&&quantityCapitalLetter>=2&&quantityLowerCaseLetter>=2&&quantityChararterSpecial>=2) ||
		   (quantityNumberPassword>=2&&quantityCapitalLetter==2&&quantityLowerCaseLetter>=2&&quantityChararterSpecial>=2) ||
		   (quantityNumberPassword>=2&&quantityCapitalLetter>=2&&quantityLowerCaseLetter==2&&quantityChararterSpecial>=2) ||
		   (quantityNumberPassword>=2&&quantityCapitalLetter>=2&&quantityLowerCaseLetter>=2&&quantityChararterSpecial==2)){
		document.getElementById("Formulario:levelText1").style.backgroundColor = "#FFCC00";	
        document.getElementById('Formulario:textLevel').innerHTML = "Media";	
	}
	else if(quantityNumberPassword>=3&&quantityCapitalLetter>=3&&quantityLowerCaseLetter>=3&&quantityChararterSpecial>=3&&(sizePasword>=12)){			              
        document.getElementById("Formulario:levelText1").style.backgroundColor = "#1EDF40";				        
        document.getElementById('Formulario:textLevel').innerHTML = "Alta";
	}


	if (quantityCapitalLetter == 0) {
	    document.getElementById("Formulario:levelText1").style.backgroundColor = "#CC0000";
	    document.getElementById("Formulario:imageCapital").src="/web.bo.peajes.com/img/err.png";
	    document.getElementById("Formulario:capital").style.color = "#FF0000";
	}
	if (quantityChararterSpecial == 0) {
		document.getElementById("Formulario:levelText1").style.backgroundColor = "#CC0000";
		document.getElementById("Formulario:imageCharacter").src="/web.bo.peajes.com/img/err.png";
		document.getElementById("Formulario:character").style.color = "#FF0000";
	}                                                                   
	if (quantityLowerCaseLetter == 0) {
		document.getElementById("Formulario:imageLower").src="/web.bo.peajes.com/img/err.png";
     	document.getElementById("Formulario:levelText1").style.backgroundColor = "#CC0000";
     	document.getElementById("Formulario:lower").style.color = "#FF0000";
	}
	if (quantityNumberPassword == 0) {
		 document.getElementById("Formulario:levelText1").style.backgroundColor = "#CC0000";
		 document.getElementById("Formulario:imageNumber").src="/web.bo.peajes.com/img/err.png";
		 document.getElementById("Formulario:number").style.color = "#FF0000";
	}
	if(sizePasword<=0){	
		  document.getElementById('Formulario:textLevel').style.visibility = "hidden";
		  document.getElementById("Formulario:levelText1").style.backgroundColor = "#D8D8D8";
		  document.getElementById('Formulario:textLevel').innerHTML = "";		       
	}else{
		 document.getElementById("Formulario:levelText1").style.visibility = "visible";
		 document.getElementById('Formulario:textLevel').style.visibility = "visible";	
		
	}
     if(newPasswordC==""){				
		document.getElementById('Formulario:msjError').innerHTML = "El campo confirmar contraseña no puede estar vacío";		
	}else if(newPassword==""){				
			  document.getElementById('Formulario:msjError').innerHTML = "El campo contraseña no puede estar vacío";		
	}else if(newPassword==newPasswordC){	
		document.getElementById('Formulario:msjError').innerHTML = "Estado correcto";	
	}else{
		document.getElementById('Formulario:msjError').innerHTML = "Las contraseñas ingresadas deben ser iguales";	
	}
     
     
}

function validateConionFormLnsClientOut(){								 
	var quantityCapitalLetterC = 0;
	var quantityChararterSpecialC = 0;
	var quantityLowerCaseLetterC = 0;
	var quantityNumberPasswordC = 0;	
    var newPasswordC=document.getElementById("Formulario:Contrasena2").value;
    var sizePaswordC=newPasswordC.length;	
    var newPassword=document.getElementById("Formulario:Contrasena").value;
	var dato;
	for (var i=0; i < newPasswordC.length; i++){
		dato = newPasswordC.charCodeAt(i); 
	
		if (dato >= 65 && dato <=90 || dato==209){
			quantityCapitalLetterC++;	        
	        
		}else if (dato >= 97 && dato <=122 || dato==241){
			 quantityLowerCaseLetterC++;	
						
		}else if (dato >= 48 && dato <=57){
			quantityNumberPasswordC++;	
			
		} else if (dato >= 33 && dato <=47){
			quantityChararterSpecialC++;
		
		}
	}	    
	if((quantityNumberPasswordC==1&&quantityCapitalLetterC>=0&&quantityLowerCaseLetterC>=0&&quantityChararterSpecialC>=0) ||
	   (quantityNumberPasswordC>=0&&quantityCapitalLetterC==1&&quantityLowerCaseLetterC>=0&&quantityChararterSpecialC>=0) ||
	   (quantityNumberPasswordC>=0&&quantityCapitalLetterC>=0&&quantityLowerCaseLetterC==1&&quantityChararterSpecialC>=0) ||
	   (quantityNumberPasswordC>=0&&quantityCapitalLetterC>=0&&quantityLowerCaseLetterC>=0&&quantityChararterSpecialC==1)){
		 document.getElementById("Formulario:levelText2").style.backgroundColor = "#CC0000";
		 document.getElementById('Formulario:textLevelC').innerHTML = "Baja";		           
        
	}
	else if(quantityNumberPasswordC>=2&&quantityCapitalLetterC>=2&&quantityLowerCaseLetterC>=2&&quantityChararterSpecialC>=2&&(sizePaswordC<=11)||
		   (quantityNumberPasswordC==2&&quantityCapitalLetterC>=2&&quantityLowerCaseLetterC>=2&&quantityChararterSpecialC>=2) ||
		   (quantityNumberPasswordC>=2&&quantityCapitalLetterC==2&&quantityLowerCaseLetterC>=2&&quantityChararterSpecialC>=2) ||
		   (quantityNumberPasswordC>=2&&quantityCapitalLetterC>=2&&quantityLowerCaseLetterC==2&&quantityChararterSpecialC>=2) ||
		   (quantityNumberPasswordC>=2&&quantityCapitalLetterC>=2&&quantityLowerCaseLetterC>=2&&quantityChararterSpecialC==2)){
		 document.getElementById("Formulario:levelText2").style.backgroundColor = "#FFCC00";	
         document.getElementById('Formulario:textLevelC').innerHTML = "Media";	
			       
	}
	else if(quantityNumberPasswordC>=3&&quantityCapitalLetterC>=3&&quantityLowerCaseLetterC>=3&&quantityChararterSpecialC>=3&&(sizePaswordC>=12)){			              
         document.getElementById("Formulario:levelText2").style.backgroundColor = "#1EDF40";				        
         document.getElementById('Formulario:textLevelC').innerHTML = "Alta";
	}
	
	if (quantityCapitalLetterC == 0) {
	     document.getElementById("Formulario:levelText2").style.backgroundColor = "#CC0000";		    
	}
	if (quantityChararterSpecialC == 0) {
		 document.getElementById("Formulario:levelText2").style.backgroundColor = "#CC0000";
	}
	if (quantityLowerCaseLetterC == 0) {		
     	 document.getElementById("Formulario:levelText2").style.backgroundColor = "#CC0000";
	}
	if (quantityNumberPasswordC== 0) {
		 document.getElementById("Formulario:levelText2").style.backgroundColor = "#CC0000";
	}
	if(sizePaswordC<=0){	
	   document.getElementById("Formulario:levelText2").style.backgroundColor = "#D8D8D8";
	   document.getElementById('Formulario:msjError').style.visibility = "hidden";	
	   document.getElementById('Formulario:textLevelC').innerHTML = "";
	}else{		
		document.getElementById("Formulario:levelText2").style.visibility = "visible";
		document.getElementById('Formulario:msjError').style.visibility = "visible";		
	}
	if(newPassword==""){				
		document.getElementById('Formulario:msjError').innerHTML = "El campo contraseña no puede estar vacío";		
	}
	else if(newPasswordC==""){				
		document.getElementById('Formulario:msjError').innerHTML = "El campo Confirmar contraseña no puede estar vacío";		
	}
	else if(newPassword==newPasswordC){	
		document.getElementById('Formulario:msjError').innerHTML = "Estado correcto";	
	}else{
		document.getElementById('Formulario:msjError').innerHTML = "Las contraseñas ingresadas deben ser iguales";	
	}
 }

function validate(){								 
	var quantityCapitalLetter = 0;
	var quantityChararterSpecial = 0;
	var quantityLowerCaseLetter = 0;
	var quantityNumberPassword = 0;	
    var newPassword=document.getElementById("panelGen:newPass").value;
    var sizePasword=newPassword.length;	
    var newPasswordC=document.getElementById("panelGen:new1").value;
	var dato;
	for (var i=0; i < newPassword.length; i++){
		dato = newPassword.charCodeAt(i); 
	
		if (dato >= 65 && dato <=90 || dato==209){
	         quantityCapitalLetter++;	        
	          document.getElementById("panelGen:imageCapital").src="/web.bo.peajes.com/img/ok.png";
	          document.getElementById("panelGen:capital").style.color = "#424242";
		}else if (dato >= 97 && dato <=122 || dato==241){
			 quantityLowerCaseLetter++;	
			 document.getElementById("panelGen:imageLower").src="/web.bo.peajes.com/img/ok.png";
			 document.getElementById("panelGen:lower").style.color = "#424242";
		}else if (dato >= 48 && dato <=57){
			quantityNumberPassword++;	
			document.getElementById("panelGen:imageNumber").src="/web.bo.peajes.com/img/ok.png";
			 document.getElementById("panelGen:number").style.color = "#424242";
			 
		} else if (dato >= 33 && dato <=47 || dato==59 || dato==61 || dato==63 || dato==191){
			quantityChararterSpecial++;
			document.getElementById("panelGen:imageCharacter").src="/web.bo.peajes.com/img/ok.png";
			document.getElementById("panelGen:character").style.color = "#424242";
			
		}
	}
	if((quantityNumberPassword==1&&quantityCapitalLetter>=0&&quantityLowerCaseLetter>=0&&quantityChararterSpecial>=0) ||
	   (quantityNumberPassword>=0&&quantityCapitalLetter==1&&quantityLowerCaseLetter>=0&&quantityChararterSpecial>=0) ||
	   (quantityNumberPassword>=0&&quantityCapitalLetter>=0&&quantityLowerCaseLetter==1&&quantityChararterSpecial>=0) ||
	   (quantityNumberPassword>=0&&quantityCapitalLetter>=0&&quantityLowerCaseLetter>=0&&quantityChararterSpecial==1)){
		 document.getElementById("panelGen:levelText1").style.backgroundColor = "#CC0000";
		 document.getElementById('panelGen:textLevel').innerHTML = "Baja";
	}
	else if(quantityNumberPassword>=2&&quantityCapitalLetter>=2&&quantityLowerCaseLetter>=2&&quantityChararterSpecial>=2&&(sizePasword<=11)||
		   (quantityNumberPassword==2&&quantityCapitalLetter>=2&&quantityLowerCaseLetter>=2&&quantityChararterSpecial>=2) ||
		   (quantityNumberPassword>=2&&quantityCapitalLetter==2&&quantityLowerCaseLetter>=2&&quantityChararterSpecial>=2) ||
		   (quantityNumberPassword>=2&&quantityCapitalLetter>=2&&quantityLowerCaseLetter==2&&quantityChararterSpecial>=2) ||
		   (quantityNumberPassword>=2&&quantityCapitalLetter>=2&&quantityLowerCaseLetter>=2&&quantityChararterSpecial==2)){
		document.getElementById("panelGen:levelText1").style.backgroundColor = "#FFCC00";	
        document.getElementById('panelGen:textLevel').innerHTML = "Media";	
	}
	else if(quantityNumberPassword>=3&&quantityCapitalLetter>=3&&quantityLowerCaseLetter>=3&&quantityChararterSpecial>=3&&(sizePasword>=12)){			              
        document.getElementById("panelGen:levelText1").style.backgroundColor = "#1EDF40";				        
        document.getElementById('panelGen:textLevel').innerHTML = "Alta";
	}


	if (quantityCapitalLetter == 0) {
	    document.getElementById("panelGen:levelText1").style.backgroundColor = "#CC0000";
	    document.getElementById("panelGen:imageCapital").src="/web.bo.peajes.com/img/err.png";
	    document.getElementById("panelGen:capital").style.color = "#FF0000";
	}
	if (quantityChararterSpecial == 0) {
		document.getElementById("panelGen:levelText1").style.backgroundColor = "#CC0000";
		document.getElementById("panelGen:imageCharacter").src="/web.bo.peajes.com/img/err.png";
		document.getElementById("panelGen:character").style.color = "#FF0000";
	}                                                                   
	if (quantityLowerCaseLetter == 0) {
		document.getElementById("panelGen:imageLower").src="/web.bo.peajes.com/img/err.png";
     	document.getElementById("panelGen:levelText1").style.backgroundColor = "#CC0000";
     	document.getElementById("panelGen:lower").style.color = "#FF0000";
	}
	if (quantityNumberPassword == 0) {
		 document.getElementById("panelGen:levelText1").style.backgroundColor = "#CC0000";
		 document.getElementById("panelGen:imageNumber").src="/web.bo.peajes.com/img/err.png";
		 document.getElementById("panelGen:number").style.color = "#FF0000";
	}
	if(sizePasword<=0){	
		  document.getElementById('panelGen:textLevel').style.visibility = "hidden";
		  document.getElementById("panelGen:levelText1").style.backgroundColor = "#D8D8D8";
		  document.getElementById('panelGen:textLevel').innerHTML = "";		       
	}else{
		 document.getElementById("panelGen:levelText1").style.visibility = "visible";
		 document.getElementById('panelGen:textLevel').style.visibility = "visible";	
		
	}
	if(newPassword==""){				
		document.getElementById('panelGen:msjError').innerHTML = "El campo nueva contraseña no puede estar vacío";		
	}
	else if(newPasswordC==""){				
		document.getElementById('panelGen:msjError').innerHTML = "El campo confirme nueva contraseña no puede estar vacío";		
	}
	else if(newPassword==newPasswordC){	
		document.getElementById('panelGen:msjError').innerHTML = "Estado correcto";	
	}else{
		document.getElementById('panelGen:msjError').innerHTML = "Las contraseñas ingresadas deben ser iguales";	
	}
     
     
}
     // Arivera ingreso de ojo para mostrar la contraseña
var al=true;
function onkeypreTec(){

	document.getElementById("panelGen:change").src="/web.bo.peajes.com/img/bg1-1.png"; 
}
function showImage2() {
	if(al==false){
		document.getElementById("panelGen:change").src="/web.bo.peajes.com/img/bg1-2.png";
		document.getElementById("panelGen:currentPloginClear").style.visibility = "visible";
		document.getElementById("panelGen:currentP").style.visibility = "hidden";
		document.getElementById("panelGen:currentPloginClear").value = document.getElementById("panelGen:currentP").value;	
	}else{
		document.getElementById("panelGen:change").src="/web.bo.peajes.com/img/bg1-3.png";
	}

}

function showImage1() {
	
	document.getElementById("panelGen:change").src="/web.bo.peajes.com/img/bg1-1.png";
	document.getElementById("panelGen:currentPloginClear").style.visibility = "hidden";
	document.getElementById("panelGen:currentP").style.visibility = "visible";
}
function mouseDown() {
	
	doStuff();
}

function mouseUp() {
	al=true;
	 document.getElementById("panelGen:change").src="/web.bo.peajes.com/img/bg1-1.png";    	 
		document.getElementById("panelGen:currentPloginClear").style.visibility = "hidden";
		document.getElementById("panelGen:currentP").style.visibility = "visible";
}

function doStuff() {
	   al=false;
	document.getElementById("panelGen:change").src="/web.bo.peajes.com/img/bg1-2.png";
	document.getElementById("panelGen:currentPloginClear").style.visibility = "visible";
	document.getElementById("panelGen:currentP").style.visibility = "hidden";
	document.getElementById("panelGen:currentPloginClear").value = document.getElementById("panelGen:currentP").value;	
}
     
/* Nuena contraseña */
function onkeypreTec1(){

		document.getElementById("panelGen:change1").src="/web.bo.peajes.com/img/bg1-1.png";     
	
}
function showImage21() {
	if(al==false){// esta presionado el el icono ojo
		document.getElementById("panelGen:change1").src="/web.bo.peajes.com/img/bg1-2.png";
		document.getElementById("panelGen:newPassClear").style.visibility = "visible";
		document.getElementById("panelGen:newPass").style.visibility = "hidden";
		document.getElementById("panelGen:newPassClear").value = document.getElementById("panelGen:newPass").value;	
	}else{
		document.getElementById("panelGen:change1").src="/web.bo.peajes.com/img/bg1-3.png"; 
	}
	
	
}

function showImage11() {
	
	document.getElementById("panelGen:change1").src="/web.bo.peajes.com/img/bg1-1.png";
	document.getElementById("panelGen:newPassClear").style.visibility = "hidden";
	document.getElementById("panelGen:newPass").style.visibility = "visible";
}

function mouseDown1() {
	
		doStuff1();
	
}

function mouseUp1() {
 	
	al=true;
	document.getElementById("panelGen:change1").src="/web.bo.peajes.com/img/bg1-1.png";    	 
	document.getElementById("panelGen:newPassClear").style.visibility = "hidden";
	document.getElementById("panelGen:newPass").style.visibility = "visible";
}

function doStuff1() {

	al=false;
	document.getElementById("panelGen:change1").src="/web.bo.peajes.com/img/bg1-2.png";
	document.getElementById("panelGen:newPassClear").style.visibility = "visible";
	document.getElementById("panelGen:newPass").style.visibility = "hidden";
	document.getElementById("panelGen:newPassClear").value = document.getElementById("panelGen:newPass").value;	
}


/* confirmar contraseña */

function onkeypreTec2(){

	document.getElementById("panelGen:change2").src="/web.bo.peajes.com/img/bg1-1.png";
}
function showImage12() {
	if(al==false){

		document.getElementById("panelGen:change2").src="/web.bo.peajes.com/img/bg1-2.png";
		document.getElementById("panelGen:new1").style.visibility = "hidden";
		document.getElementById("panelGen:new1Clear").style.visibility = "visible";
		document.getElementById("panelGen:new1Clear").value = document.getElementById("panelGen:new1").value;
	}else{
		document.getElementById("panelGen:change2").src="/web.bo.peajes.com/img/bg1-3.png"; 
	}
	
}

function showImage22() {    

	document.getElementById("panelGen:change2").src="/web.bo.peajes.com/img/bg1-1.png";
	document.getElementById("panelGen:new1Clear").style.visibility = "hidden";
	document.getElementById("panelGen:new1").style.visibility = "visible";
}

function mouseDown2() {

	doStuff2();
}

function mouseUp2() {
	al=true;
	
	document.getElementById("panelGen:change2").src="/web.bo.peajes.com/img/bg1-1.png";    	 
	document.getElementById("panelGen:new1Clear").style.visibility = "hidden";
	document.getElementById("panelGen:new1").style.visibility = "visible";

}

function doStuff2() {
    al=false;
	document.getElementById("panelGen:change2").src="/web.bo.peajes.com/img/bg1-2.png";
	document.getElementById("panelGen:new1").style.visibility = "hidden";
	document.getElementById("panelGen:new1Clear").style.visibility = "visible";
	document.getElementById("panelGen:new1Clear").value = document.getElementById("panelGen:new1").value;	

}

/* ojito para modulo de ingreso */

function onkeypreTecL(){
	
	document.getElementById("formName:notlogged:changeL").src="/web.bo.peajes.com/img/bg1-1.png";   
}
function showImageL() {
	if(al==false){
		document.getElementById("formName:notlogged:changeL").src="/web.bo.peajes.com/img/bg1-2.png";
		
		document.getElementById("formName:notlogged:login").style.visibility = "hidden";
		document.getElementById("formName:notlogged:loginClear").style.visibility = "visible";
		document.getElementById("formName:notlogged:loginClear").value = document.getElementById("formName:notlogged:login").value;
		}else{
			document.getElementById("formName:notlogged:changeL").src="/web.bo.peajes.com/img/bg1-3.png";
		}
	
}

function showImageL1() {    	
	document.getElementById("formName:notlogged:changeL").src="/web.bo.peajes.com/img/bg1-1.png";
	
	document.getElementById("formName:notlogged:loginClear").style.visibility = "hidden";
	document.getElementById("formName:notlogged:login").style.visibility = "visible";	
}

function mouseDownL() {
	
	doStuffL();
}

function mouseUpBody(){
	
	al=true;
	
}

function mouseUpL() {
	al=true;
	document.getElementById("formName:notlogged:changeL").src="/web.bo.peajes.com/img/bg1-1.png";    	 
	document.getElementById("formName:notlogged:loginClear").style.visibility = "hidden";
	document.getElementById("formName:notlogged:login").style.visibility = "visible";
}

function doStuffL() {
    al=false;
	document.getElementById("formName:notlogged:changeL").src="/web.bo.peajes.com/img/bg1-2.png";
	
	document.getElementById("formName:notlogged:login").style.visibility = "hidden";
	document.getElementById("formName:notlogged:loginClear").style.visibility = "visible";
	document.getElementById("formName:notlogged:loginClear").value = document.getElementById("formName:notlogged:login").value;
}

/*
 * validacion ojito de FormLnsClientOutside
 */

function onkeypreTecF1(){

		document.getElementById("Formulario:change1").src="/web.bo.peajes.com/img/bg1-1.png";     
	
}
function showImageF1() {
	if(al==false){// esta presionado el el icono ojo
		document.getElementById("Formulario:change1").src="/web.bo.peajes.com/img/bg1-2.png";
		document.getElementById("Formulario:newPassClear").style.visibility = "visible";
		document.getElementById("Formulario:Contrasena").style.visibility = "hidden";
		document.getElementById("Formulario:newPassClear").value = document.getElementById("Formulario:Contrasena").value;	
	}else{
		document.getElementById("Formulario:change1").src="/web.bo.peajes.com/img/bg1-3.png"; 
	}
	
	
}

function showImageF11() {
	
	document.getElementById("Formulario:change1").src="/web.bo.peajes.com/img/bg1-1.png";
	document.getElementById("Formulario:newPassClear").style.visibility = "hidden";
	document.getElementById("Formulario:Contrasena").style.visibility = "visible";
}

function mouseDownF1() {
	
		doStuffF1();
	
}

function mouseUpF1() {
 	
	al=true;
	document.getElementById("Formulario:change1").src="/web.bo.peajes.com/img/bg1-1.png";    	 
	document.getElementById("Formulario:newPassClear").style.visibility = "hidden";
	document.getElementById("Formulario:Contrasena").style.visibility = "visible";
}

function doStuffF1() {

	al=false;
	document.getElementById("Formulario:change1").src="/web.bo.peajes.com/img/bg1-2.png";
	document.getElementById("Formulario:newPassClear").style.visibility = "visible";
	document.getElementById("Formulario:Contrasena").style.visibility = "hidden";
	document.getElementById("Formulario:newPassClear").value = document.getElementById("Formulario:Contrasena").value;	
}


/* confirmar contraseña */

function onkeypreTecF2(){

	document.getElementById("Formulario:change2").src="/web.bo.peajes.com/img/bg1-1.png";
}
function showImageF2() {
	if(al==false){

		document.getElementById("Formulario:change2").src="/web.bo.peajes.com/img/bg1-2.png";
		document.getElementById("Formulario:new1").style.visibility = "hidden";
		document.getElementById("Formulario:new1Clear").style.visibility = "visible";
		document.getElementById("Formulario:new1Clear").value = document.getElementById("panelGen:new1").value;
	}else{
		document.getElementById("Formulario:change2").src="/web.bo.peajes.com/img/bg1-3.png"; 
	}
	
}

function showImageF21() {    

	document.getElementById("Formulario:change2").src="/web.bo.peajes.com/img/bg1-1.png";
	document.getElementById("Formulario:new1Clear").style.visibility = "hidden";
	document.getElementById("Formulario:Contrasena2").style.visibility = "visible";
}

function mouseDownF2() {

	doStuffF2();
}

function mouseUpF2() {
	al=true;
	
	document.getElementById("Formulario:change2").src="/web.bo.peajes.com/img/bg1-1.png";    	 
	document.getElementById("Formulario:new1Clear").style.visibility = "hidden";
	document.getElementById("Formulario:Contrasena2").style.visibility = "visible";

}

function doStuffF2() {
    al=false;
	document.getElementById("Formulario:change2").src="/web.bo.peajes.com/img/bg1-2.png";
	document.getElementById("Formulario:Contrasena2").style.visibility = "hidden";
	document.getElementById("Formulario:new1Clear").style.visibility = "visible";
	document.getElementById("Formulario:new1Clear").value = document.getElementById("Formulario:Contrasena2").value;	

}


     
     

function validateConfirmation(){								 
	var quantityCapitalLetterC = 0;
	var quantityChararterSpecialC = 0;
	var quantityLowerCaseLetterC = 0;
	var quantityNumberPasswordC = 0;	
    var newPasswordC=document.getElementById("panelGen:new1").value;
    var sizePaswordC=newPasswordC.length;	
    var newPassword=document.getElementById("panelGen:newPass").value;
	var dato;
	for (var i=0; i < newPasswordC.length; i++){
		dato = newPasswordC.charCodeAt(i); 
	
		if (dato >= 65 && dato <=90 || dato==209){
			quantityCapitalLetterC++;	        
	        
		}else if (dato >= 97 && dato <=122 || dato==241){
			 quantityLowerCaseLetterC++;	
						
		}else if (dato >= 48 && dato <=57){
			quantityNumberPasswordC++;	
			
		} else if (dato >= 33 && dato <=47){
			quantityChararterSpecialC++;
		
		}
	}	    
	if((quantityNumberPasswordC==1&&quantityCapitalLetterC>=0&&quantityLowerCaseLetterC>=0&&quantityChararterSpecialC>=0) ||
	   (quantityNumberPasswordC>=0&&quantityCapitalLetterC==1&&quantityLowerCaseLetterC>=0&&quantityChararterSpecialC>=0) ||
	   (quantityNumberPasswordC>=0&&quantityCapitalLetterC>=0&&quantityLowerCaseLetterC==1&&quantityChararterSpecialC>=0) ||
	   (quantityNumberPasswordC>=0&&quantityCapitalLetterC>=0&&quantityLowerCaseLetterC>=0&&quantityChararterSpecialC==1)){
		 document.getElementById("panelGen:levelText2").style.backgroundColor = "#CC0000";
		 document.getElementById('panelGen:textLevelC').innerHTML = "Baja";		           
        
	}
	else if(quantityNumberPasswordC>=2&&quantityCapitalLetterC>=2&&quantityLowerCaseLetterC>=2&&quantityChararterSpecialC>=2&&(sizePaswordC<=11)||
		   (quantityNumberPasswordC==2&&quantityCapitalLetterC>=2&&quantityLowerCaseLetterC>=2&&quantityChararterSpecialC>=2) ||
		   (quantityNumberPasswordC>=2&&quantityCapitalLetterC==2&&quantityLowerCaseLetterC>=2&&quantityChararterSpecialC>=2) ||
		   (quantityNumberPasswordC>=2&&quantityCapitalLetterC>=2&&quantityLowerCaseLetterC==2&&quantityChararterSpecialC>=2) ||
		   (quantityNumberPasswordC>=2&&quantityCapitalLetterC>=2&&quantityLowerCaseLetterC>=2&&quantityChararterSpecialC==2)){
		 document.getElementById("panelGen:levelText2").style.backgroundColor = "#FFCC00";	
         document.getElementById('panelGen:textLevelC').innerHTML = "Media";	
			       
	}
	else if(quantityNumberPasswordC>=3&&quantityCapitalLetterC>=3&&quantityLowerCaseLetterC>=3&&quantityChararterSpecialC>=3&&(sizePaswordC>=12)){			              
         document.getElementById("panelGen:levelText2").style.backgroundColor = "#1EDF40";				        
         document.getElementById('panelGen:textLevelC').innerHTML = "Alta";
	}
	
	if (quantityCapitalLetterC == 0) {
	     document.getElementById("panelGen:levelText2").style.backgroundColor = "#CC0000";		    
	}
	if (quantityChararterSpecialC == 0) {
		 document.getElementById("panelGen:levelText2").style.backgroundColor = "#CC0000";
	}
	if (quantityLowerCaseLetterC == 0) {		
     	 document.getElementById("panelGen:levelText2").style.backgroundColor = "#CC0000";
	}
	if (quantityNumberPasswordC== 0) {
		 document.getElementById("panelGen:levelText2").style.backgroundColor = "#CC0000";
	}
	if(sizePaswordC<=0){	
	   document.getElementById("panelGen:levelText2").style.backgroundColor = "#D8D8D8";
	   document.getElementById('panelGen:msjError').style.visibility = "hidden";	
	   document.getElementById('panelGen:textLevelC').innerHTML = "";
	}else{		
		document.getElementById("panelGen:levelText2").style.visibility = "visible";
		document.getElementById('panelGen:msjError').style.visibility = "visible";		
	}
	if(newPassword==""){				
		document.getElementById('panelGen:msjError').innerHTML = "El campo nueva contraseña no puede estar vacío";		
	}
	else if(newPasswordC==""){				
		document.getElementById('panelGen:msjError').innerHTML = "El campo confirme nueva contraseña no puede estar vacío";		
	}
	else if(newPassword==newPasswordC){	
		document.getElementById('panelGen:msjError').innerHTML = "Estado correcto";	
	}else{
		document.getElementById('panelGen:msjError').innerHTML = "Las contraseñas ingresadas deben ser iguales";	
	}
 }


/**
 * @author ablasquez
 * @param evt
 * @returns {Boolean} funcion que permite registrar en un campo de texto
 *          unicamente valores hexadecimales.
 */
function onlyHexa(evt){
	var evento = evt || window.event;
	var propiedad = evento.charCode || evento.keyCode;
	
	if(window.event){
	      keynum = evt.keyCode;
	    }else{
	      keynum = evt.which;
	    }
		// alert(keynum);
		if(((keynum >=65) && (keynum <=70)) || ((keynum >=97) && (keynum <=102)) || ((keynum >=48) && (keynum <=57)) || (keynum==0) || (keynum==8)){
		  	return true;
		} else {		    
			return false;
		}
}

/**
 * @author ablasquez
 * @param box
 * @returns {String} Funcion que convierte el texto en mayusculas
 */
function mayus(box){
	text = box.value;
	textM = text.toUpperCase();
	box.value = textM;
}

/**
 * @author ablasquez
 * @param box
 * @returns {String} Elimina los espacios en blanco al inicio y al final del
 *          texto
 */
function trimFunc(box){
    // alert("box: "+box.value.trim());
	text = box.value;
	textM = text.replace(/^\s+|\s+$/g, ''); 
	box.value = textM;
}

function noEspecialobjection(evt){
	var evento = evt || window.event;
	var propiedad = evento.charCode || evento.keyCode;
	
	if(window.event){
	      keynum = evt.keyCode;
	    }else{
	      keynum = evt.which;
	    }	
	    // alert ("keynum: "+keynum);
		if(((keynum >=33) && (keynum <=47)) || ((keynum >=58) && (keynum <=64)) || ((keynum >=91) && (keynum <=96)) || ((keynum >=123) && (keynum <=127)) || 
		(keynum >= 166 && keynum<=180) || (keynum>=184 && keynum<=223) || (keynum ==238) || (keynum>=240)){
		  if(keynum == 35 || keynum == 36 || keynum == 9 || keynum == 38 || keynum ==45 || (keynum==8) || keynum ==44 || (keynum==46) || (keynum==36) || (keynum==35) || (keynum==39) || (keynum==37)
				  || keynum == 243 || keynum == 250 || keynum == 193 || keynum == 201 || keynum == 205 || keynum == 211  || keynum == 218  || keynum == 241 || keynum == 209
				  || keynum == 40 || keynum == 41 || keynum == 34 || keynum == 191 || keynum == 63 || keynum == 33  || keynum == 161  || keynum == 58 || keynum == 64 || keynum == 47 ){		 
		    return true;
		  }else{	
			return false;
		  }	
		} else {		    
			return true;
		}
	}


function especialParameter(evt){
	var evento = evt || window.event;
	var propiedad = evento.charCode || evento.keyCode;
	
	if(window.event){
	      keynum = evt.keyCode;
	    }else{
	      keynum = propiedad;
	    }
		 // alert(keynum);
		if(((keynum >=166) && (keynum <=180)) || ((keynum >=184) && (keynum <=223)) || (keynum >=238) ){
		  	return false;
		} else {		    
			return true;
		}
}


function replaceAll( text, busca, reemplaza ){
  while (text.toString().indexOf(busca) != -1)
      text = text.toString().replace(busca,reemplaza);
  return text;
}
	
	
function numNoZero(evt,box){
    boxV = replaceAll(box.value,".","");   
	   if(window.event){
	      keynum = evt.keyCode;
	    }else{
	      keynum = evt.which;
	    }
		
		if(((keynum >=48) && (keynum <=57)) || (keynum==8) || (keynum==9)){
		   if( keynum == 48) {
			if(boxV > 0){
				return true;
				} else{
				  return false;
				}  
			} else{
				return true;
			}
	 	  
		} else {
			return false;
		}
}

function clearOnFocus(box){
box.value ="";
}

function cuentaLineas(string) {
 	  	var count=1;
 	  	for(var i=0;i<string.length;i++)
 	  	 { 	  	 	
 	  	 	if(string.substr(i,1).charCodeAt(0)==10)
 	  	 	 {
 	  	 	 	count++;
 	  	 	 }
 	  	 }
 	  	return count;
 }

function checkMaxLength(evt, box, maxLength){    
    if(window.event){
	      keynum = evt.keyCode;
	    }else{
	      keynum = evt.which;
	    }
	// alert("keynum: "+keynum);
	
    text = box.value;
	length = text.length+cuentaLineas(text);	
	// alert(length+":"+maxLength+":"+text.length);
	if(length>(maxLength)) {
	   box.value=text.substring(0,(maxLength-(cuentaLineas(text))));
	}		
}

function onlyText(box){
	var texto = box.value;
	var textoFinal = "";
	textoFinal= texto.replace( /[^-A-ZÑÁÉÍÓÚa-zñáéíóú.\s]+/g, '' );
	box.value=textoFinal;
}

function onlyNumber(box){
	var texto = box.value;
	var textoFinal = "";
	textoFinal= texto.replace( /[^0-9]+/g, '' );
	box.value=textoFinal;
}


function textWithoutPoint(box){
	var texto = box.value;
	var textoFinal = "";
	textoFinal= texto.replace( /[^-A-ZÑÁÉÍÓÚa-zñáéíóú\s]+/g, '' );
	box.value=textoFinal;
}

function textWithPoint(box){
	var texto = box.value;
	var textoFinal = "";
	textoFinal= texto.replace( /[^-A-ZÑÁÉÍÓÚa-zñáéíóú.\s]+/g, '' );
	box.value=textoFinal;
}


function textAndNumber(box){
	var texto = box.value;
	var textoFinal = "";
	textoFinal= texto.replace( /[^-A-Za-z0-9\s]+/g, '' );
	box.value=textoFinal;
}



function soloLetras(e) {
    key = e.keyCode || e.which;
    tecla = String.fromCharCode(key).toLowerCase();
    letras = " áéíóúabcdefghijklmnñopqrstuvwxyz";
    especiales = [8, 37, 39, 46];

    tecla_especial = false
    for(var i in especiales) {
        if(key == especiales[i]) {
            tecla_especial = true;
            break;
        }
    }
    if(letras.indexOf(tecla) == -1 && !tecla_especial)
        return false;
    else
    	return true;
}

function limpia() {
    var val = document.getElementById("miInput").value;
    var tam = val.length;
    for(i = 0; i < tam; i++) {
        if(!isNaN(val[i]))
            document.getElementById("miInput").value = '';
    }
}
//
//
// function disableOtherChar(evt) {
// var charCode;
// charCode = (evt.which) ? evt.which : evt.keyCode;
// var ctrl;
// ctrl = (document.all) ? event.ctrlKey : evt.modifiers & Event.CONTROL_MASK;
// if ((charCode > 47 && charCode < 58) || (charCode > 64 && charCode < 91) ||
// (charCode > 96 && charCode < 123) || charCode == 8 || charCode == 9 ||
// charCode == 45 || (ctrl && charCode == 86) || ctrl && charCode == 67) {
// return true;
// } else {
// $(":text").live("cut copy paste", function (e) {
// e.preventDefault();
// });
// return false;
// }
// }


function abc(value)
{
var count=0;
var iChars = "!@#$%^&*()+=-[]\\\';,./{}|\":<>?";
for (var i = 0; i < value.length; i++) {
    if (iChars.indexOf(value.charAt(i)) != -1) {
count++;

        }
    }
if(count>0)
{document.getElementById("btn").disabled = true;
}
else{
document.getElementById("btn").disabled = false;
}

}

/* Restringe digitación a caracteres alfabéticos, espacio sin punto */

function alphaSpaceWithoutPoint(evt){	
	var charCode = (evt.which) ? evt.which : window.event.keyCode;
 	if ((charCode <= 13)){
		return true;
	}else{
		var keyChar = String.fromCharCode(charCode);
		var re = /[\sA-ZÑÁÉÍÓÚa-zñáéíóú\\s]/;
		return re.test(keyChar);
	}	
}

function optionalPhone(evt){	
	var charCode = (evt.which) ? evt.which : window.event.keyCode;
	if (charCode <= 13){
		return true;
	}else{
		var keyChar = String.fromCharCode(charCode);
		var re = /[\sa-zA-ZñÑ0-9]/;
		return re.test(keyChar);			
	}	
}


function emailValidation(evt){	
	var charCode = (evt.which) ? evt.which : window.event.keyCode;
	if (charCode <= 13){
		return true;
	}else{
		var keyChar = String.fromCharCode(charCode);
		var re = /[a-zA-Z@.+_0-9-]/;
		return re.test(keyChar);			
	}	
}


function disclaimer(evt){
	var evento = evt || window.event;
	var propiedad = evento.charCode || evento.keyCode;
	
	if(window.event){
	      keynum = evt.keyCode;
	    }else{
	      keynum = evt.which;
	    }	
	    
	// alert(keynum);
	if(((keynum >=160) && (keynum <=163))|| (keynum==130) ||
	  ((keynum >=65) && (keynum <=90)) ||
	  ((keynum >=97) && (keynum <=122))  || (keynum >=48) && (keynum <=57)){
			 
		if( (keynum == 47) || (keynum ==46) || (keynum ==95) || (keynum ==45) || (keynum ==40) || (keynum ==41) ||
		     (keynum ==44) || (keynum ==59) ){
			return true;
		}
		else{
			 return false;
		}
	} else {		    
		return true;
	}
}

function alphaNumSpaceSimple(evt){	
	var charCode = (evt.which) ? evt.which : window.event.keyCode;
	if (charCode <= 13){
		return true;
	}else{
		var keyChar = String.fromCharCode(charCode);
		var re = /[\sa-zA-ZñÑ0-9_.)(\/-]/;
		return re.test(keyChar);			
	}	
}

function onlyEmail(evt){
	var charCode = (evt.which) ? evt.which : window.event.keyCode;
	if (charCode <= 13){
		return true;
	}else{
		var keyChar = String.fromCharCode(charCode);
		var re = /[a-zA-Z@.;_0-9-]/;
		return re.test(keyChar);			
	}
}

function isContrato(evt){	
	var charCode = (evt.which) ? evt.which : window.event.keyCode;
	// alert(charCode);
	if (charCode <= 13){
		return true;
	}else{
		var keyChar = String.fromCharCode(charCode);
		var re = /[\sa-zA-ZñÑ0-9_.)(\/-]/;
		return re.test(keyChar);			
	}	
}

function noEspecialobjection2(evt){
	var evento = evt || window.event;
	var propiedad = evento.charCode || evento.keyCode;
	
	if(window.event){
	      keynum = evt.keyCode;
	    }else{
	      keynum = evt.which;
	    }	
	    // alert ("keynum: "+keynum);
		if(((keynum >=33) && (keynum <=47)) || ((keynum >=58) && (keynum <=64)) || ((keynum >=91) && (keynum <=96)) || ((keynum >=123) && (keynum <=159)) || 
		(keynum >= 166 && keynum<=180) || (keynum>=184 && keynum<=223) || (keynum ==238) || (keynum ==162) || (keynum ==165) || (keynum ==227) || (keynum ==164) || (keynum ==181) || (keynum ==182) || (keynum>=166)){
		  
			if(keynum == 35 || keynum == 36 || keynum == 9 || keynum == 38 || keynum ==45 || (keynum==8) || keynum ==44 || (keynum==46) || (keynum==36) || (keynum==35) || (keynum==39) || (keynum==37)
				  || keynum == 243 || keynum == 250 || keynum == 193 || keynum == 201 || keynum == 205 || keynum == 211  || keynum == 218  || keynum == 241 || keynum == 209
				  || keynum == 40 || keynum == 41 || keynum == 34 || keynum == 191 || keynum == 63 || keynum == 33  || keynum == 161  || keynum == 58 || keynum == 64 || keynum == 47 
				  || keynum == 225 || keynum == 233 || keynum == 237 || keynum == 95 || keynum == 59 || keynum == 43  || keynum == 42 || keynum == 61){		 
		    return true;
		  }else{	
			return false;
		  }	
		} else {		    
			return true;
		}
	}



function Observation(evt){
	var evento = evt || window.event;
	var propiedad = evento.charCode || evento.keyCode;
	
	if(window.event){
	      keynum = evt.keyCode;
	    }else{
	      keynum = evt.which;
	    }	
	   // alert ("keynum: "+keynum);
		if(((keynum >=33) && (keynum <=47)) || ((keynum >=58) && (keynum <=64)) || ((keynum >=91) && (keynum <=96)) || ((keynum >=123) && (keynum <=159)) || 
		(keynum >= 166 && keynum<=180) || (keynum>=184 && keynum<=223) || (keynum ==238) || (keynum ==162) || (keynum ==165) || (keynum ==227) || (keynum ==164) || (keynum ==181) || (keynum ==182) || (keynum>=166)){
		  
			if(keynum == 35 || keynum == 36 || keynum == 37 || keynum == 9 || keynum == 38 || keynum ==45 || (keynum==8) || keynum ==44 || (keynum==46) || (keynum==36) || (keynum==35) || (keynum==39) || (keynum==37)
				  || keynum == 243 || keynum == 250 || keynum == 193 || keynum == 201 || keynum == 205 || keynum == 211  || keynum == 218  || keynum == 241 || keynum == 209
				  || keynum == 40 || keynum == 41 || keynum == 34 || keynum == 191 || keynum == 63 || keynum == 33 || keynum == 161 || keynum == 58 || keynum == 64 || keynum == 47 
				  || keynum == 225 || keynum == 233 || keynum == 237 || keynum == 95 || keynum == 59 || keynum == 43 || keynum == 42 || keynum == 61 || keynum == 60 || keynum == 62
				  || keynum == 252 || keynum == 91 || keynum == 92 || keynum == 93 || keynum == 94 || keynum == 96 || keynum == 123 || keynum == 124 || keynum == 125 || keynum == 126){		 
		    return true;
		  }else{	
			return false;
		  }	
		} else {		    
			return true;
		}
    }


function CompanyCourierRoll(evt){
	var evento = evt || window.event;
	var propiedad = evento.charCode || evento.keyCode;

	if(window.event){
	      keynum = evt.keyCode;
	    }else{
	      keynum = evt.which;
	    }	
	 
	 // alert(keynum);
	 
	if(((keynum >=65) && (keynum <=90)) || ((keynum >=97) && (keynum <=122)) || // A-Z
																				// a-z
	   (keynum==193 || keynum==201 || keynum==205 || keynum==211 || keynum==218 || keynum==225 || keynum==233 || keynum==237 || keynum==243 || keynum==250 || keynum==241 || keynum==209) || // áéíóú
																																																// ñÑ
	   ((keynum>=33 && keynum<=47) || keynum==59  || keynum==61 || keynum==63 || keynum==92 || keynum==161 || keynum==191|| keynum==8220 || keynum==8221 || keynum==32 ) ||// ! #
																																								// espacio
       (keynum >=48) && (keynum <=57)){ // 0-9
			return true;
		}
		else{
			 return false;
		}
}
var tiempoInicial=3;
var t=0;
function enablebutton(){
	 tiempoInicial=3;
	 t=0;
	for ( var int = 0; int < 10; int++) {
		
		if(document.getElementById("recharge:dtable:"+int+":clinkpdf")){	
		document.getElementById("recharge:dtable:"+int+":clinkpdf").className="disabledlink";	
		document.getElementById("recharge:dtable:"+int+":clinkpdf").disabled=true;
		}
	}
	 t = setInterval(enabl,1000);	
}
	function enabl(){
		
		
		tiempoInicial--;
	
		if(tiempoInicial==0){
			clearTimeout(t);
			for ( var int = 0; int < 10; int++) {
				if(document.getElementById("recharge:dtable:"+int+":clinkpdf")){
		     	document.getElementById("recharge:dtable:"+int+":clinkpdf").className="enabledlink";
		     	document.getElementById("recharge:dtable:"+int+":clinkpdf").disabled=false;
				}
			}
		
		}
		
	}

	
	function nameAgreement(evt){	
		var evento = evt || window.event;
		var propiedad = evento.charCode || evento.keyCode;

		if(window.event){
		      keynum = evt.keyCode;
		    }else{
		      keynum = evt.which;
		    }	
		if(((keynum >=65) && (keynum <=90)) || ((keynum >=97) && (keynum <=122)) // A-Z a-z
			|| ((keynum >=160) && (keynum <=165)) || keynum==130 // áéíóú Ññ
			|| keynum==38 || keynum==47 || keynum==45 || keynum==95 || keynum==32){ //&/_- espacio
				return true;
			}
			else{
				 return false;
			}
	}


	function descriptionAgreement(evt){	
		var evento = evt || window.event;
		var propiedad = evento.charCode || evento.keyCode;

		if(window.event){
		      keynum = evt.keyCode;
		    }else{
		      keynum = evt.which;
		    }	
		if(((keynum >=65) && (keynum <=90)) || ((keynum >=97) && (keynum <=122)) // A-Z a-z
			|| ((keynum >=160) && (keynum <=165)) || keynum==130 // áéíóú Ññ
			|| keynum==38 || keynum==47 || keynum==45 || keynum==95 || keynum==46 || keynum==44 || keynum==59 || keynum==40 || keynum==41 || keynum==32 //&/_-.,;() espacio
			|| ((keynum >=48) && (keynum <=57))){ // 0-9
				return true;
			}
			else{
				 return false;
			}
	}
	
	function nameAgreement(evt){	
		var charCode = (evt.which) ? evt.which : window.event.keyCode;
		if (charCode ='13'){
			return true;
		}else{
			var keyChar = String.fromCharCode(charCode);
			var re = /[\sA-ZÑÁÉÍÓÚa-zñáéíóú \/&_-]/;
			return re.test(keyChar);			
		}
	}


	function descriptionAgreement(evt){	
		var charCode = (evt.which) ? evt.which : window.event.keyCode;
		if (charCode ='13'){
			return true;
		}else{
			var keyChar = String.fromCharCode(charCode);
			var re = /[\sA-ZÑÁÉÍÓÚa-zñáéíóú0-9 \/&.,;_()-]/;
			return re.test(keyChar);			
		}
	}

