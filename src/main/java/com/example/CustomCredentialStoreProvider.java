package com.example;

import org.wildfly.security.WildFlyElytronBaseProvider;

public class CustomCredentialStoreProvider extends WildFlyElytronBaseProvider {

    private static CustomCredentialStoreProvider INSTANCE = new CustomCredentialStoreProvider();

    public CustomCredentialStoreProvider() {
        super("CustomCredentialStoreProvider", "1.0", "Example Provider for CustomCredentialStore");

        putService(new Service(this, "CredentialStore", "CustomCredentialStore", "com.example.CustomCredentialStore", null, null));
    }

    public static CustomCredentialStoreProvider getInstance() {
        return INSTANCE;
    }
}
