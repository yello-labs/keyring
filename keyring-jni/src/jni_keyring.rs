use std::os::raw::c_int;

use jni::JNIEnv;
use jni::objects::{JClass, JString};
use jni::sys::{jstring, jthrowable};
use keyring::KeyringError;

fn handle_exception(env: JNIEnv, e: KeyringError) {
    match e {
        KeyringError::SecretServiceError(_) => {
            env.throw_new("java/lang/Exception", e.to_string())
        }
        KeyringError::NoBackendFound => {
            env.throw_new("java/lang/Exception", e.to_string())
        }
        KeyringError::NoPasswordFound => {
            env.throw_new("java/lang/Exception", e.to_string())
        }
        KeyringError::Parse(_) => {
            env.throw_new("java/lang/Exception", e.to_string())
        }
        _ => {env.throw_new("java/lang/Exception", e.to_string())}
    };
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
            handle_exception(env, e);
            format!("")
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
            handle_exception(env, e);
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
            handle_exception(env, e);
        }
        _ => {}
    }

    let output = env
        .new_string(format!("{}:{}", domain, username))
        .expect("Error converting result to JVM String");

    output.into_inner()
}
