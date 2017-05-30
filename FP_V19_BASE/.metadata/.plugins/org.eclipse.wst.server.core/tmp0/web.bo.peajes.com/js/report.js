/**
 * REPORTS
 *
 */

function validate(noReport){
	var  array = new Array();
	if(noReport  == 0){	
		array = 
		    [     
		     [   // Validations for the Id TAG field
		         ["form:id_disp", "required", "El campo Id Dispositivo es requerido."] // Validation 1 : It is a required field
		     ],
		     [   // Validations for the begDate field
		         ["form:beg_dateInputDate", "required", "El campo Fecha Inicial es requerido."] // Validation 1 : It is a required field
		     ],
		     [   // Validations for the endDate field
		         ["form:end_dateInputDate", "required", "El campo Fecha Final es requerido."] // Validation 1 : It is a required field
		     ],
		     [   // Compare the two dates.
		         ["form:beg_dateInputDate", "compareTo=form:end_dateInputDate", "La Fecha Inicial debe ser menor que la Fecha Final."] // Validation 1 : End Date must be greater that Beg Date.
		     ]
		   ];
	} else if (noReport == 1) {
		array = 
		    [     
		     [   // Validations for the CÓDIGO USUARIO field
		         ["form:id_user", "required", "El campo Código Usuario es requerido."] // Validation 1 : It is a required field
		     ],
		     [   // Validations for the begDate field
		         ["form:beg_dateInputDate", "required", "El campo Fecha Inicial es requerido."] // Validation 1 : It is a required field
		     ],
		     [   // Validations for the endDate field
		         ["form:end_dateInputDate", "required", "El campo Fecha Final es requerido."] // Validation 1 : It is a required field
		     ],
		     [   // Compare the two dates.
		         ["form:beg_dateInputDate", "compareTo=form:end_dateInputDate", "La Fecha Inicial debe ser menor que la Fecha Final."] // Validation 1 : End Date must be greater that Beg Date.
		     ]
		   ];
	} else if (noReport == 3) {
		array = 
		    [     
		     [   // Validations for the begDate field
		         ["form:beg_dateInputDate", "required", "El campo Fecha Inicial es requerido."] // Validation 1 : It is a required field
		     ],
		     [   // Validations for the endDate field
		         ["form:end_dateInputDate", "required", "El campo Fecha Final es requerido."] // Validation 1 : It is a required field
		     ],
		     [   // Compare the two dates.
		         ["form:beg_dateInputDate", "compareTo=form:end_dateInputDate", "La Fecha Inicial debe ser menor que la Fecha Final."] // Validation 1 : End Date must be greater that Beg Date.
		     ]
		   ];
	}
	
	return validateForm(array);
}