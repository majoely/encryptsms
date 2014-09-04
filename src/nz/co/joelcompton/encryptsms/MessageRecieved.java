package nz.co.joelcompton.encryptsms;

/**
 * 
 * Class to hold the recieved messages as a temporary storages while
 * the users hasn't read it yet.
 * 
 * @author Joel Compton
 *
 */
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
