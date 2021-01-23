package org.yellolab;

import org.junit.Test;
import org.yellolab.exceptions.KeyringException;

import java.util.UUID;

public class KeyringTest {
	@Test
	public void setSecretBlindly() throws Exception {
		Keyring.setSecret("no", "not", "working");
	}
	
	@Test
	public void getSecret() throws Exception {
		Keyring.getSecret("no", "not");
	}
	
	@Test(expected = KeyringException.class)
	public void getSecretExpectException() throws Exception {
		//Likelihood of collisions with real entries are astronomically low
		Keyring.getSecret(UUID.randomUUID().toString(), UUID.randomUUID().toString());
	}
	
	@Test
	public void setSecret() throws Exception {
		Keyring.setSecret("", "", "");
		Keyring.getSecret("","");
		Keyring.deleteSecret("","");
	}
}