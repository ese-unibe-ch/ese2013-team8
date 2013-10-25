package ch.unibe.ese.core;

public class Friend {
	private int phoneNr;
	private String name;
	
	
	//TODO: add Image of Friend
	public Friend(int phoneNr, String name){
		this.phoneNr = phoneNr;
		this.name = name;
	}
	
	public String toString(){
		return name + ", Phone nr: " + phoneNr;
	}
	
	
	public boolean equals(Object friend){
		if(this == friend) 
			return true;
		if (friend == null) 
			return false;
		if (getClass() != friend.getClass())
			return false;
		
		Friend other = (Friend) friend;
		if(other.phoneNr == this.phoneNr)
			return true;
		return false;
	}

	public int getPhoneNr() {
		return phoneNr;
	}

	public void setPhoneNr(int phoneNr) {
		this.phoneNr = phoneNr;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
}
