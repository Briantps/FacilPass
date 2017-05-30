package constant;


public class ValidatePassword {

	
	public ValidatePassword(){
		
	}
	
	public boolean validateContainSeqNumPassword(String password){
		boolean res=false;
		int con=0;
		char x1, x2;
		try{
			for(int i=0;i<password.length()-1;i++){
				x1=password.charAt(i);
				x2=password.charAt(i+1);
				//System.out.println("x1 en validateSeqNumPassword" + x1);
				//System.out.println("x2 en validateSeqNumPassword" + x2);
				
				if(x1=='0'){
					if(x2=='1'){ con++; }
				}
				else if(x1=='1'){
					if(x2=='2'){ con++; }
				}
	            else if(x1=='2'){
	            	if(x2=='3'){ con++; }
				}
	            else if(x1=='3'){
	            	if(x2=='4'){ con++; }
				}
	            else if(x1=='4'){
	            	if(x2=='5'){ con++; }
				}
	            else if(x1=='5'){
	            	if(x2=='6'){ con++; }
				}
	            else if(x1=='6'){
	            	if(x2=='7'){ con++; }
				}
	            else if(x1=='7'){
	            	if(x2=='8'){ con++; }
				}
	            else if(x1=='8'){
	            	if(x2=='9'){ con++; }
				}	
				//System.out.println("con en validateSeqNumPassword"+ con);
			}
		    if(con>=2){
				res=true;
			}
		    else{
		    	res=false;
		    }
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return res;
		
	}
	
	public boolean validateContainRepeatNumPassword(String password){
		//System.out.println("password " + password);
		boolean res=false;
		int con=0;
		char x1=0, x2 = 0;
		try{
			for(int i=0;i<password.length()-1;i++){
				x1=password.charAt(i);
				//System.out.println("x1 en validateRepeatNumPassword" + x1);
				
				x2=password.charAt(i+1);
				//System.out.println("x2 en validateRepeatNumPassword" + x2);	
		
				if(x1==x2){
				    con++;
				}	
				//System.out.println("con en validateRepeatNumPassword" + con);
			}
			
		    if(con>=2){
				res=true;
			}
		    else{
		    	res=false;
		    }
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return res;
	}
	
	public boolean validateContainSeqPassword(String password){
		boolean res=false;
		int con=0;
		char x1, x2;
		try{
			for(int i=0;i<password.length()-1;i++){
				x1=password.charAt(i);
				x2=password.charAt(i+1);
				//System.out.println("x1 en validateSeqPassword" + x1);
				//System.out.println("x2 en validateSeqPassword" + x2);
				
				if(x1=='A'){
					if(x2=='B'){ con++; }
				}
				else if(x1=='B'){
					if(x2=='C'){ con++; }
				}
	            else if(x1=='C'){
	            	if(x2=='D'){ con++; }
				}
	            else if(x1=='D'){
	            	if(x2=='E'){ con++; }
				}
	            else if(x1=='E'){
	            	if(x2=='F'){ con++; }
				}
	            else if(x1=='F'){
	            	if(x2=='G'){ con++; }
				}
	            else if(x1=='G'){
	            	if(x2=='H'){ con++; }
				}
	            else if(x1=='H'){
	            	if(x2=='I'){ con++; }
				}
	            else if(x1=='I'){
	            	if(x2=='J'){ con++; }
				}	
	        	else if(x1=='J'){
					if(x2=='K'){ con++; }
				}
	            else if(x1=='K'){
	            	if(x2=='L'){ con++; }
				}
	            else if(x1=='L'){
	            	if(x2=='M'){ con++; }
				}
	            else if(x1=='M'){
	            	if(x2=='N'){ con++; }
				}
	            else if(x1=='N'){
	            	if(x2=='Ñ'){ con++; }
				}
	            else if(x1=='Ñ'){
	            	if(x2=='O'){ con++; }
				}
	            else if(x1=='O'){
	            	if(x2=='P'){ con++; }
				}
	            else if(x1=='P'){
	            	if(x2=='Q'){ con++; }
				}
	        	else if(x1=='Q'){
					if(x2=='R'){ con++; }
				}
	            else if(x1=='R'){
	            	if(x2=='S'){ con++; }
				}
	            else if(x1=='S'){
	            	if(x2=='T'){ con++; }
				}
	            else if(x1=='T'){
	            	if(x2=='U'){ con++; }
				}
	            else if(x1=='U'){
	            	if(x2=='V'){ con++; }
				}
	            else if(x1=='V'){
	            	if(x2=='X'){ con++; }
				}
	            else if(x1=='X'){
	            	if(x2=='Y'){ con++; }
				}
	            else if(x1=='Y'){
	            	if(x2=='Z'){ con++; }
				}
				//System.out.println("con en validateSeqPassword" + con);
			}
		    if(con>=2){
				res=true;
			}
		    else{
		    	res=false;
		    }
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return res;
		
	}
	
}
