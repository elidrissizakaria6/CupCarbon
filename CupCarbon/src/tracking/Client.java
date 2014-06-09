package tracking;

import java.io.IOException;

public class Client {

	public static void main(String[] args) throws IOException {
		TargetWS.clean();
		TargetWS.create("S1",2.3,4.45);
		TargetWS.create("S2",1.3,0.45);
		TargetWS.create("S3",0.54,-4.45);
		TargetWS.setCoords("S2", 0, 1);
	}

	
	
}
