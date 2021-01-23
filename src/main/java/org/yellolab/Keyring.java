package org.yellolab;

import java.io.File;
import java.util.Objects;

public class Keyring {
	static {
		String nativeLibrary = "libkeyring_jni";
		ClassLoader classLoader = Keyring.class.getClassLoader();
		File nativeLibraryFile = new File(Objects.requireNonNull(classLoader.getResource(nativeLibrary)).getFile());
		System.load(nativeLibraryFile.getAbsolutePath());
	}
	
	public static native String getSecret(String domain, String userName) throws Exception;
	public static native String setSecret(String domain, String username, String secret) throws Exception;
	public static native String deleteSecret(String domaind, String username) throws Exception;
}
