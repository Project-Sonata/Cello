package com.odeyalo.sonata.cello.core;

public enum ClientType {
    /**
     * A confidential client is an application that is capable of keeping a client password confidential to the world.
     * This client password is assigned to the client app by the authorization server.
     * This password is used to identify the client to the authorization server, to avoid fraud.
     * An example of a confidential client could be a web app, where no one but the administrator can get access to the server, and see the client password.
     */
    CONFIDENTIAL,
    /**
     * A public client is an application that is not capable of keeping a client password confidential.
     * For instance, a mobile phone application or a desktop application that has the client password embedded inside it.
     * Such an application could get cracked, and this could reveal the password.
     * The same is true for a JavaScript application running in the user's browser.
     * The user could use a JavaScript debugger to look into the application, and see the client password.
     */
    PUBLIC
}
