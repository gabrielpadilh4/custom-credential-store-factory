# Custom WildFly Elytron Credential Store (Example Only)

This repository contains an example implementation of a custom WildFly/Elytron Credential Store designed for educational and demonstration purposes. It illustrates the necessary steps to register a custom security provider and implement the CredentialStoreSpi interface for integration with the Elytron subsystem.

## ⚙️ Project Structure and Components

This project focuses on the two core classes required for Elytron integration:

1. `CustomCredentialStore.java`: Implements `CredentialStoreSpi`. This is the core logic that handles credential storage and retrieval. In this example, it hardcodes the alias `DB_PASSWORD_ALIAS` to a cleartext `PasswordCredential`.

2. `CustomCredentialStoreProvider.java`: Extends `WildFlyElytronBaseProvider`. This class acts as the Java Security Provider, registering the custom credential store implementation (`CustomCredentialStore`) with the unique service name `CustomCredentialStore`.

## Prerequisites

- Java 21 or higher
- Apache Maven
- WildFly or JBoss EAP server running
- Access to the JBoss CLI (./bin/jboss-cli.sh)

## Installation and Deployment (JBoss CLI Steps)

Use the following JBoss CLI commands to install the module, register the provider, and configure a data source to use it.

NOTE: Ensure your CLI session is connected (`./bin/jboss-cli.sh --connect`).

### 1. Build and Add Module

Build the project and install the resulting JAR as a WildFly module. Replace the path with your actual build path.
Bash

Build the project
~~~
mvn clean install
~~~

Add the module to EAP
~~~
module add --name=com.example.demo-cred-store \
    --resources=~/path/to/target/demo-credential-store-factory-1.0-SNAPSHOT.jar \
    --dependencies=org.wildfly.security.elytron
~~~

### 2. Configure and Register the Provider

Define how to load your security provider and then register it in the Elytron subsystem.
Bash
~~~
/subsystem=elytron/provider-loader=CustomProvider:add(class-names=[com.example.CustomCredentialStoreProvider], module=com.example.demo-cred-store)
/subsystem=elytron:write-attribute(name=initial-providers, value=CustomProvider)
reload
~~~

### 3. Define the Credential Store Instance

Create an instance of the CredentialStore named CustomCredentialStore, using the provider you just defined.
Bash
~~~
/subsystem=elytron/credential-store=CustomCredentialStore:add(providers=CustomProvider, type=CustomCredentialStore,credential-reference={clear-text=''}
~~~
Note that `clear-text` will not be used. I'm lefting it empty because it is mandatory.

### 4. Link to a Data Source

Configure an existing data source (replace PostgresDS with your actual data source name) to fetch its password from your custom store using the hardcoded alias `DB_PASSWORD_ALIAS`.
~~~
/subsystem=datasources/data-source=PostgresDS:write-attribute(name=credential-reference, value={store=CustomCredentialStore, alias=DB_PASSWORD_ALIAS})

reload

~~~

After the final reload, the "PostgresDS" data source will successfully retrieve "mydbpassword" from the custom in-memory store.