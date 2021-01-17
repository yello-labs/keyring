extern crate keyring;

use jni::JNIEnv;

use jni::objects::{JClass, JString};

use jni::sys::jstring;

#[no_mangle]
pub extern "system" fn Java_org_yellolab_Keyring_getSecret(
    env: JNIEnv,
    _class: JClass,
    domain_string: JString,
    username_string: JString,
) -> jstring {
    let domain: String = env
        .get_string(domain_string)
        .expect("Couldn't get java string!")
        .into();
    let username: String = env
        .get_string(username_string)
        .expect("Couldn't get java string!")
        .into();

    let keyring = keyring::Keyring::new(&domain, &username);

    //TODO: This is not ideal.
    let new = match keyring.get_password() {
        Ok(value) => value,
        Err(_) => String::default(),
    };

    let output = env.new_string(new).expect("Couldn't create java string!");

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
        .expect("Couldn't get java string!")
        .into();
    let username: String = env
        .get_string(username_string)
        .expect("Couldn't get java string!")
        .into();
    let password: String = env
        .get_string(password_string)
        .expect("Couldn't get java string!")
        .into();

    let keyring = keyring::Keyring::new(&domain, &username);

    keyring.set_password(&password).unwrap();

    let output = env
        .new_string(password)
        .expect("Couldn't create java string!");

    output.into_inner()
}

#[cfg(test)]
mod tests {
    #[test]
    fn it_works() {}
}
