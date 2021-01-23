use jni::errors::Error;
use jni::objects::{JClass, JString, JThrowable};
use jni::sys::{jstring, jthrowable};
use jni::JNIEnv;
use keyring::KeyringError;

fn handle_exception(env: JNIEnv, e: KeyringError) -> Option<JThrowable> {
    match e {
        KeyringError::NoBackendFound => env.throw_new("org/yellolab/exceptions/NoBackendFoundException", e.to_string()),
        KeyringError::NoPasswordFound => env.throw_new("org/yellolab/exceptions/NoPasswordFoundException", e.to_string()),
        KeyringError::Parse(_) => env.throw_new("org/yellolab/exceptions/ParseException", e.to_string()),
        #[cfg(target_os = "macos")]
        KeyringError::MacOsKeychainError(_) => env.throw_new("org/yellolab/exceptions/MacOsKeychainException", e.to_string()),
        #[cfg(target_os = "windows")]
        KeyringError::WindowsVaultError => env.throw_new("org/yellolab/exceptions/WindowsVaultException", e.to_string()),
        #[cfg(target_os = "linux")]
        KeyringError::SecretServiceError(_) => env.throw_new("org/yellolab/exceptions/SecretServiceException", e.to_string()),
    }
    .unwrap();
    match env.exception_occurred() {
        Ok(e) => Some(e),
        _ => None,
    }
}

#[no_mangle]
pub extern "system" fn Java_org_yellolab_Keyring_getSecret(
    env: JNIEnv,
    _class: JClass,
    domain_string: JString,
    username_string: JString,
) -> jstring {
    let domain: String = env
        .get_string(domain_string)
        .expect("Error reading 'domain' JVM argument")
        .into();
    let username: String = env
        .get_string(username_string)
        .expect("Error reading 'username' JVM argument")
        .into();

    let keyring = keyring::Keyring::new(&domain, &username);

    let password: String = match keyring.get_password() {
        Ok(value) => value,
        Err(e) => {
            return handle_exception(env, e).unwrap().into_inner();
        }
    };

    let output = env
        .new_string(password)
        .expect("Error converting result to JVM String");

    output.into_inner()
}

#[no_mangle]
pub extern "system" fn Java_org_yellolab_Keyring_setSecret(
    env: JNIEnv,
    _class: JClass,
    domain_string: JString,
    username_string: JString,
    password_string: JString,
) -> jstring {
    let domain: String = env
        .get_string(domain_string)
        .expect("Error reading 'domain' JVM argument")
        .into();
    let username: String = env
        .get_string(username_string)
        .expect("Error reading 'username' JVM argument")
        .into();
    let password: String = env
        .get_string(password_string)
        .expect("Error reading 'password' JVM argument")
        .into();

    let keyring = keyring::Keyring::new(&domain, &username);

    match keyring.set_password(&password) {
        Err(e) => {
            return handle_exception(env, e).unwrap().into_inner();
        }
        _ => {}
    }

    let output = env
        .new_string(password)
        .expect("Error converting result to JVM String");

    output.into_inner()
}

#[no_mangle]
pub extern "system" fn Java_org_yellolab_Keyring_deleteSecret(
    env: JNIEnv,
    _class: JClass,
    domain_string: JString,
    username_string: JString,
) -> jstring {
    let domain: String = env
        .get_string(domain_string)
        .expect("Error reading 'domain' JVM argument")
        .into();
    let username: String = env
        .get_string(username_string)
        .expect("Error reading 'username' JVM argument")
        .into();

    let keyring = keyring::Keyring::new(&domain, &username);

    match keyring.delete_password() {
        Err(e) => {
            return handle_exception(env, e).unwrap().into_inner();
        }
        _ => {}
    }

    let output = env
        .new_string(format!("{}:{}", domain, username))
        .expect("Error converting result to JVM String");

    output.into_inner()
}
