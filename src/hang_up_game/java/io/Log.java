package hang_up_game.java.io;

public class Log {
	
	private static final String Debug = "D>";
	private static final String Info = "I>";
	private static final String Warning = "W>";
	private static final String Error = "E>";
	
	public static final char DEBUG = 0;
	public static final char INFO = 1;
	public static final char WARNING = 2;
	public static final char ERROR = 3;
	
	private static char logType = 1;
	
	private static void log(String type, String title, String msg) {
		if(type.equals(Error)) {
			System.err.println(type + title + ": " + msg);
		}
		else {
			switch(logType) {
				case 1:
					if(type.equals(Debug)) return;
					break;
				case 2:
					if(type.equals(Info)) return;
					break;
				case 3:
					if(type.equals(Warning)) return;
			}
			System.out.println(type + "[" + title + "]: " + msg);
		}
	}
	
	public static void d(String title, String msg) {
		log(Debug, title, msg);
	}
	
	public static void i(String title, String msg) {
		log(Info, title, msg);
	}
	
	public static void w(String title, String msg) {
		log(Warning, title, msg);
	}
	
	public static void e(String title, String msg) {
		log(Error, title, msg);
	}
	
	public static void setLogType(int type) {
		if(type < 0 || type > 3) throw new IllegalArgumentException();
		logType = (char)type;
	}
	
}
