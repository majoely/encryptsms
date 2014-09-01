package nz.co.joelcompton.encryptsms;

public class MessageRecieved {
	
	private String phoneNumber;
	private String message;
	
	public MessageRecieved(String pn, String mes) {
		this.phoneNumber = pn;
		this.message = mes;
	}
	
	public String getPhoneNumber() {
		return this.phoneNumber;
	}
	
	public String getMessage() {
		return this.message;
	}

}
