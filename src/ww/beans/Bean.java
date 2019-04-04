package ww.beans;

public class Bean {

	private String name;

	/**
	 * @param name
	 */
	public Bean(String name) {
		super();
		this.name = name;
	}

	
	/**
	 * 
	 */
	public Bean() {
		super();
		// TODO Auto-generated constructor stub
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Bean [name=");
		builder.append(name);
		builder.append("]");
		return builder.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
