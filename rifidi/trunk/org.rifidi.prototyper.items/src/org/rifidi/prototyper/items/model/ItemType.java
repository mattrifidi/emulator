/**
 * 
 */
package org.rifidi.prototyper.items.model;

/**
 * @author kyle
 * 
 */
public enum ItemType {

	CARGO {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return "Cargo";
		}

	},
	FORKLIFT {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return "Forklift";
		}

	};
	
	public static ItemType getType(String stringValue){
		if(stringValue.equalsIgnoreCase("Cargo")){
			return CARGO;
		}else{
			return FORKLIFT;
		}
	}

}
