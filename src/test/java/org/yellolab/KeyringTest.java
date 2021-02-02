package org.yellolab;

import java.io.File;
import java.util.Objects;
import java.util.UUID;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.yellolab.exceptions.KeyringException;

public class KeyringTest {

  @Test
  public void testForNativeLibrary() {
    String nativeLibrary = "libkeyring_jni";
    ClassLoader classLoader = Keyring.class.getClassLoader();
    File nativeLibraryFile =
        new File(Objects.requireNonNull(classLoader.getResource(nativeLibrary)).getFile());

    Assert.assertNotNull(nativeLibrary);
    Assert.assertNotNull(classLoader);
    Assert.assertNotNull(nativeLibraryFile);
    try {
      Keyring.getSecret("", "");
    } catch (org.yellolab.exceptions.KeyringException ignored) {
    } catch (Exception e) {
      Assert.fail();
    }
  }

  @Test(dependsOnMethods = {"testForNativeLibrary"})
  public void setSecretBlindly() throws Exception {
    Keyring.setSecret("no", "not", "working");
  }

  @Test(dependsOnMethods = {"testForNativeLibrary", "setSecretBlindly"})
  public void getSecret() throws Exception {
    Keyring.getSecret("no", "not");
  }

  @Test(
      dependsOnMethods = {"testForNativeLibrary"},
      expectedExceptions = {KeyringException.class})
  public void getSecretExpectException() throws Exception {
    // Likelihood of collisions with real entries are astronomically low
    Keyring.getSecret(UUID.randomUUID().toString(), UUID.randomUUID().toString());
  }

  @Test(dependsOnMethods = {"testForNativeLibrary"})
  public void setSecret() throws Exception {
    Keyring.setSecret("secret", "secret", "S3(r37");
  }

  @Test(dependsOnMethods = {"testForNativeLibrary", "setSecret"})
  public void deleteSecret() throws Exception {
    Keyring.deleteSecret("secret", "secret");
  }
}
