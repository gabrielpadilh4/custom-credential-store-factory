package com.example;

import java.security.Provider;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashMap;
import java.util.Map;

import org.wildfly.security.credential.Credential;
import org.wildfly.security.credential.PasswordCredential;
import org.wildfly.security.credential.store.CredentialStore;
import org.wildfly.security.credential.store.CredentialStoreException;
import org.wildfly.security.credential.store.CredentialStoreSpi;
import org.wildfly.security.credential.store.UnsupportedCredentialTypeException;
import org.wildfly.security.password.Password;
import org.wildfly.security.password.interfaces.ClearPassword;

public class CustomCredentialStore extends CredentialStoreSpi {
	
	private static String DB_PASSWORD_ALIAS = "DB_PASSWORD_ALIAS";
	private static String MY_CLEAR_DB_PASSWORD = "mydbpassword";

    Map<String, Credential> credentials = new HashMap<>();

    @Override
    public void initialize(Map<String, String> attributes, CredentialStore.ProtectionParameter protectionParameter,
            Provider[] providers)
            throws CredentialStoreException {
    	// Initialize the credentials
        credentials.put(DB_PASSWORD_ALIAS,
                new PasswordCredential((Password) ClearPassword.createRaw("clear", MY_CLEAR_DB_PASSWORD.toCharArray())));
        this.initialized = true;
    }

    @Override
    public boolean isModifiable() {
        return initialized;
    }

    @Override
    public void remove(String credentialAlias, Class<? extends Credential> credentialType, String credentialAlgorithm,
            AlgorithmParameterSpec parameterSpec)
            throws CredentialStoreException {
    }

    @Override
    public <C extends Credential> C retrieve(String credentialAlias, Class<C> credentialType,
            String credentialAlgorithm, AlgorithmParameterSpec parameterSpec,
            CredentialStore.ProtectionParameter protectionParameter) throws CredentialStoreException {

        Credential credential = credentials.get(credentialAlias);

        if (credential != null && credentialType.isInstance(credential)) {
            return credentialType.cast(credential);
        }

        throw new CredentialStoreException("Credential not found or type mismatch");
    }

    @Override
    public void store(String credentialAlias, Credential credential,
            CredentialStore.ProtectionParameter protectionParameter)
            throws CredentialStoreException, UnsupportedCredentialTypeException {
    }

}