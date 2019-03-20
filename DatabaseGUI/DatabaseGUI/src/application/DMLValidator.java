/****************************************************
 * Yishai Seela 305373706 89-281-03
 * Ori Hirshfeld 201085776 89-281-02
 * Ido Klein 203392964 89-281-03
 * 
 ***************************************************/

package application;

/*
 * DMLValidator - check if DML is valid
 */
public class DMLValidator {
	
	private String query;

	/*
	 * constructor
	 */
	public DMLValidator(String query) {
		this.query= query;
	}

	/*
	 * ValidateStructure - check if quary is valid
	 * return "VALID" if command is valid or error message if not
	 */
	public String ValidateStructure() {
		
		String badStructure= "WRONG QUERY STRUCTURE";
		
		
		if(!query.contains("SELECT") || !query.contains("FROM") ) {
			return badStructure;
		}
		
		String[] seperatedQuery = query.split(" ");
		switch(seperatedQuery[0]) {
		
		case "SELECT":
			if(!query.contains("FROM")) {
				return badStructure;
			}
			break;
		
		case "INSERT":
			if(!query.contains("INTO") && (!query.contains("VALUES"))) {
				return badStructure;
			}
			break;
		case "UPDATE":
			if(!query.contains("SET")) {
				return badStructure;
			}
			break;
		case "DELETE":
			if(!query.contains("FROM")) {
				return badStructure;
			}
			break;
	}
		return "VALID";

	}

}
