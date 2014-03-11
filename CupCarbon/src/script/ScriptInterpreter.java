package script;

public class ScriptInterpreter {

	// ------------------------------------------------------------
	// Display script
	// ------------------------------------------------------------
	public static void displayScript(int[][][] script) {
		for (int k = 0; k < script.length; k++) {
			for (int i = 0; i < script[0].length; i++) {
				System.out.println(command(script[k][i][0]) + " "
						+ script[k][i][1]);
			}
			System.out.println("---------------");
		}
	}

	// ------------------------------------------------------------
	// Transform the command value to command name
	// ------------------------------------------------------------
	public static String command(int i) {
		if (i == 0)
			return "DELAY";
		if (i == 1)
			return "SEND";
		if (i == 2)
			return "BREAK";
		return "";
	}

}
