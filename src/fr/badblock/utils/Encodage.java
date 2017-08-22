package fr.badblock.utils;

public enum Encodage {
	 
	// UTF-8 encodage
	UTF8("UTF-8");
	
	// Field of name
	private String name;
	
	/**
	 * Constructor of the Encodage enum
	 * @param name
	 */
	Encodage (String name) {
		// Set the name on the field
		this.name = name;
	}
	
	/**
	 * Set the name of this encodage
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Getting the name of this encodage
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
}
