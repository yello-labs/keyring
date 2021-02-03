package org.yellolab;

import java.net.URL;
import java.util.UUID;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.yellolab.exceptions.KeyringException;

public class KeyringTest {

  @Test
  public void testForNativeLibraries() {
    // Make sure this thing has all of the so included
    String[] libraries =
        new String[] {"libkeyring_jni.so", "libkeyring_jni.dylib", "libkeyring_jni.dll"};
    ClassLoader classLoader = Keyring.class.getClassLoader();
    for (String nativeLibrary : libraries) {
      URL resourceLocation = classLoader.getResource(nativeLibrary);

      if (resourceLocation == null) {
        throw new SkipException("We are missing a native library, this is not ready for release");
      }
    }
  }

  @Test
  public void setSecretBlindly() throws Exception {
    try {
      Keyring.setSecret("no", "not", "working");
    } catch (ExceptionInInitializerError e) {
      Assert.fail("There was an error initializing the Keyring", e);
    }
  }

  @Test(dependsOnMethods = {"setSecretBlindly"})
  public void getSecret() throws Exception {
    Keyring.getSecret("no", "not");
  }

  @Test(
      dependsOnMethods = {"setSecretBlindly"},
      expectedExceptions = {KeyringException.class})
  public void getSecretExpectException() throws Exception {
    // Likelihood of collisions with real entries are astronomically low
    Keyring.getSecret(UUID.randomUUID().toString(), UUID.randomUUID().toString());
  }

  @Test(dependsOnMethods = {"setSecretBlindly"})
  public void setSecret() throws Exception {
    Keyring.setSecret("secret", "secret", "S3(r37");
  }

  @Test(dependsOnMethods = {"setSecretBlindly", "setSecret"})
  public void deleteSecret() throws Exception {
    Keyring.deleteSecret("secret", "secret");
  }
}
