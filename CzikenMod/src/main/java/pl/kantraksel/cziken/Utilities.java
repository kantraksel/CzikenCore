package pl.kantraksel.cziken;

import java.io.Closeable;

public class Utilities {
	public static boolean closeStream(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}
}
