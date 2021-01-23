package org.yellolab;

import java.util.Objects;

public class Keyring {
	static {
		System.load(Objects.requireNonNull(Keyring.class.getClassLoader().getResource("libkeyring_jni.so")).getFile());
	}
	
	public static native String getSecret(String domain, String userName) throws Exception;
	public static native String setSecret(String domain, String username, String secret) throws Exception;
	public static native String deleteSecret(String domaind, String username) throws Exception;
}
