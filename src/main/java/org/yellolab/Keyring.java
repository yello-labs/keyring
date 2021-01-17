package org.yellolab;

import java.util.Objects;

public class Keyring {
	static {
		System.load("/home/dakota/IdeaProjects/keyring/keyring-jni/target/release/libkeyring_jni.so");
	}
	
	public static native String getSecret(String domain, String userName);
	public static native String setSecret(String domain, String username, String secret);
	
	public static void main(String[] args) {
		String output = getSecret("", "");
	}
}
