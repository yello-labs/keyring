package org.yellolab;

import java.util.Objects;

public class Keyring {
	static {
		//TODO: pull from classloader
		System.load(Objects.requireNonNull(Keyring.class.getClassLoader().getResource("libkeyring_jni.so")).getFile());
	}
	
	public static native String getSecret(String domain, String userName);
	public static native String setSecret(String domain, String username, String secret);
	
	public static void main(String[] args) {
		String output = getSecret("", "");
	}
}
