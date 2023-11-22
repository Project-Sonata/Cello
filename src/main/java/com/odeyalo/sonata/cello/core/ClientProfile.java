package com.odeyalo.sonata.cello.core;

/**
 * Describe client profile based on Oauth2 Specification
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc6749#section-2.1">Client Types</a>
 */
public enum ClientProfile {
    /**
     * A web application is a confidential client running on a web
     * server.  Resource owners access the client via an HTML user
     * interface rendered in a user-agent on the device used by the
     * resource owner.  The client credentials as well as any access
     * token issued to the client are stored on the web server and are
     * not exposed to or accessible by the resource owner.
     */
    WEB_APPLICATION,
    /**
     * A user-agent-based application is a public client in which the
     * client code is downloaded from a web server and executes within a
     * user-agent (e.g., web browser) on the device used by the resource
     * owner.  Protocol data and credentials are easily accessible (and
     * often visible) to the resource owner.  Since such applications
     * reside within the user-agent, they can make seamless use of the
     * user-agent capabilities when requesting authorization.
     */
    USER_AGENT_BASED,
    /**
     * A native application is a public client installed and executed on
     * the device used by the resource owner.  Protocol data and
     * credentials are accessible to the resource owner.  It is assumed
     * that any client authentication credentials included in the
     * application can be extracted.  On the other hand, dynamically
     * issued credentials such as access tokens or refresh tokens can
     * receive an acceptable level of protection.  At a minimum, these
     * credentials are protected from hostile servers with which the
     * application may interact.  On some platforms, these credentials
     * might be protected from other applications residing on the same
     * device.
     */
    NATIVE_APPLICATION
}
