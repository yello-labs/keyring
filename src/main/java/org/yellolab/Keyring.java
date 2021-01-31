package org.yellolab;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Keyring {
  static {
    String nativeLibrary = "libkeyring_jni";
    File temp;
    try (InputStream in = Keyring.class.getClassLoader().getResourceAsStream(nativeLibrary)) {
      byte[] buffer = new byte[1024];
      int read = -1;
      temp = File.createTempFile(nativeLibrary, "");
      try (FileOutputStream fos = new FileOutputStream(temp)) {

        while ((read = in.read(buffer)) != -1) {
          fos.write(buffer, 0, read);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException("Could not generate DLL for the Keyring class", e);
    }
    System.load(temp.getAbsolutePath());
  }

  public static native String getSecret(String domain, String userName) throws Exception;

  public static native String setSecret(String domain, String username, String secret)
      throws Exception;

  public static native String deleteSecret(String domaind, String username) throws Exception;
}
