package ETS.Login;

import java.io.Serializable;

public class User implements Serializable {
	/**
	 * Add this variable for serialization
	 */
	private static final long serialVersionUID = 1L;

	private String Name;
	private String userName;
	private String password;
	private String type;

	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
