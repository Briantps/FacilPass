/* 
    This is the source code for the validation function. 
    Add the following code just after the </HEAD> in the files where the 
    generalized validation functionality is required. 
    <SCRIPT language="JavaScript1.2" src="gen_validation.js"></SCRIPT> 
*/ 

    /* 
    *   File : gen_validation.js 
    *     
    *   Author : Prasanth M J 
    *   
    *   CreativeProgrammers.com - 
	*	Turn your Programming Expertise into Achievements!
    *   Visit http://www.creativeprogrammers.com 
    *     
    *   Email : prasanth@creativeprogrammers.com
    */ 
//---------------------------------EMail Check ------------------------------------ 

/*  checks the validity of an email address entered 
*   returns true or false 
*   
*/ 

function validateEmail(email)
{
// a very simple email validation checking. 
// you can add more complex email checking if it helps 
    var splitted = email.match("^(.+)@(.+)$");
    if(splitted == null) return false;
    if(splitted[1] != null )
    {
      var regexp_user=/^\"?[\w-_\.]*\"?$/;
      if(splitted[1].match(regexp_user) == null) return false;
    }
    if(splitted[2] != null)
    {
      var regexp_domain=/^[\w-\.]*\.[A-Za-z]{2,4}$/;
      if(splitted[2].match(regexp_domain) == null) 
      {
	    var regexp_ip =/^\[\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}\]$/;
	    if(splitted[2].match(regexp_ip) == null) return false;
      }// if
      return true;
    }
return false;
}

/* function validateData 
*  Checks each field in a form 
*  Called from validateForm function 
*/ 
function validateData(strValidateStr,objValue,strError) { 
    var epos = strValidateStr.search("="); 
    var  command  = ""; 
    var  cmdvalue = ""; 
    
    if (epos >= 0) {
		command = strValidateStr.substring(0, epos);
		cmdvalue = strValidateStr.substr(epos + 1);
	} else {
		command = strValidateStr;
	} 

    switch(command){ 
        case "required": 
        { 
           if(objValue.value.length == 0){ 
              if(!strError || strError.length ==0){ 
                strError = objValue.name + " : Required Field"; 
              } 
              alert(strError); 
              return false; 
           }
           break;             
         }//case required 
        case "compareTo":
        {
        	var dateToCompare = document.getElementById(cmdvalue);
        	
        	var startDate = new Date(objValue.value);
        	var endDate = new Date(dateToCompare.value);
        	
        	if(startDate.getTime() > endDate.getTime()){
        		alert(strError);
        		return false;
        	}
        	
        	break;
        }//case compare two dates
        case "email": 
          { 
               if(!validateEmail(objValue.value)) 
               { 
                 if(!strError || strError.length ==0) 
                 { 
                    strError = objValue.name+": Enter a valid Email address "; 
                 }//if                                               
                 alert(strError); 
                 return false; 
               }//if 
           break; 
          }//case email 
    }//switch 
    return true; 
} 

/* 
* function validateForm 
* the function that can be used to validate any form 
* returns false if the validation fails; true if success 
*/ 

function validateForm(array) {
	for ( var i = 0; i < array.length; i++) {
		for ( var idesc = 0; idesc < array[i].length; idesc++) {

			var element = document.getElementById(array[i][idesc][0]);
			
			if (validateData(array[i][idesc][1], element ,
					array[i][idesc][2]) == false) {
				element.focus();
				return false;
			}
		}
	}
	return true;
} 