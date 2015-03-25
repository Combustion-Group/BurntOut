package com.burntout.burntout;

public class StringCheck {
	
	public String cleanseSpecialChars(String string) {
		
		String newString = "";
		
		for(int i=0; i<string.length(); i++) {
			
			char thisChar = string.charAt(i);
			
			if((thisChar > 47 && thisChar < 58) || (thisChar > 64 && thisChar < 91) || (thisChar > 96 && thisChar < 123) || thisChar == 32) {
				newString += thisChar;
			}
		}
		
		return newString;
	}

	
	public String keepSpacesPeriodCommaExcl(String string) {
		
		String newString = "";
		
		for(int i=0; i<string.length(); i++) {
			
			char thisChar = string.charAt(i);
			
			//Alphanumerics
			if((thisChar > 47 && thisChar < 58) || (thisChar > 64 && thisChar < 91) || (thisChar > 96 && thisChar < 123)) {
				
				
					newString += thisChar;
				
			}
			//Space, !, comma, period, ?
			else if(thisChar == 32 || thisChar == 33 || thisChar == 44 || thisChar == 46 || thisChar == 63) {
				newString += thisChar;
			}
			else {
				
			}
		}
		
		return newString;
	}
	
	@SuppressWarnings("unused")
	public boolean checkSpecialCharsName(String string) {
		
		String newString = "";
		
		for(int i=0; i<string.length(); i++) {
			
			char thisChar = string.charAt(i);
			
			if((thisChar > 47 && thisChar < 58) || (thisChar > 64 && thisChar < 91) || (thisChar > 96 && thisChar < 123) || thisChar == 32 || thisChar == 45) {
				newString += thisChar;
			}
			else {
				return false;
			}
		}
		
		return true;
	}
	
	@SuppressWarnings("unused")
	public boolean checkSpecialCharsMakeModel(String string) {
		
		String newString = "";
		
		for(int i=0; i<string.length(); i++) {
			
			char thisChar = string.charAt(i);
			
			if((thisChar > 47 && thisChar < 58) || (thisChar > 64 && thisChar < 91) || (thisChar > 96 && thisChar < 123) || thisChar == 32 || thisChar == 45 || thisChar == 46) {
				newString += thisChar;
			}
			else {
				return false;
			}
		}
		
		return true;
	}
	
	@SuppressWarnings("unused")
	public boolean checkSpecialCharsPlateNum(String string) {
		String newString = "";
		
		for(int i=0; i<string.length(); i++) {
			
			char thisChar = string.charAt(i);
			
			if((thisChar > 47 && thisChar < 58) || (thisChar > 64 && thisChar < 91) || (thisChar > 96 && thisChar < 123) || thisChar == 32 || thisChar == 45) {
				newString += thisChar;
			}
			else {
				return false;
			}
		}
		
		return true;
	}
	
	@SuppressWarnings("unused")
	public boolean checkSpecialCharsMessage(String string) {
		
		String newString = "";
		
		for(int i=0; i<string.length(); i++) {
			
			char thisChar = string.charAt(i);
			 
			//Alphanumerics
			if((thisChar > 47 && thisChar < 58) || (thisChar > 64 && thisChar < 91) || (thisChar > 96 && thisChar < 123)) {
				
				
					newString += thisChar;
				
			}
			//Space, !, comma, period, ?
			else if(thisChar == 32 || thisChar == 33 || thisChar == 44 || thisChar == 46 || thisChar == 63) {
				newString += thisChar;
			}
			else {
				return false;
			}
		}
		
		return true;
	}
}
