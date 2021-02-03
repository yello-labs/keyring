package org.yello.labs.exceptions;

/**
 * * Exceptions used in the rust jni library. Mapped from the rust keyring package enum
 * keyring::KeyringError
 */
public class KeyringException extends RuntimeException {
  public KeyringException(String exception) {
    super(exception);
  }
}

class NoBackendFoundException extends KeyringException {
  NoBackendFoundException(String exception) {
    super(exception);
  }
}

class NoPasswordFoundException extends KeyringException {
  NoPasswordFoundException(String exception) {
    super(exception);
  }
}

class ParseException extends KeyringException {
  ParseException(String exception) {
    super(exception);
  }
}

class MacOsKeychainException extends KeyringException {
  MacOsKeychainException(String exception) {
    super(exception);
  }
}

class WindowsVaultException extends KeyringException {
  WindowsVaultException(String exception) {
    super(exception);
  }
}

class SecretServiceException extends KeyringException {
  SecretServiceException(String exception) {
    super(exception);
  }
}
