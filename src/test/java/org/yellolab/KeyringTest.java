package org.yellolab;

import org.junit.Test;

public class KeyringTest {
	@Test
	public void getSecret() {
		Keyring.getSecret("", "");
	}
	
	@Test
	public void setSecret() {
		Keyring.setSecret("", "", "");
	}
}