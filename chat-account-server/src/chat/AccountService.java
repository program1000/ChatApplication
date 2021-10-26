package chat;

public class AccountService {

	public static void main(String[] args) {
		AccountService service = new AccountService();

		service.initZeroConfig();
	}
	
	private void initZeroConfig() {
		ZeroConfig zc = new ZeroConfig();
		if( !zc.isInited() ) {
			throw new RuntimeException("Not inited");
		}
	}

}




