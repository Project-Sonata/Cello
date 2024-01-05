## DRAFT "How Cello processes the Oauth2 Requests internally"

Cello Framework provide easy way to customize the handling of the authorization request.

Steps how the Oauth2 request is processed:

1) The client redirect the user to /oauth2/authorize endpoint
2) Request is validated and if it is valid, then the resource-owner is redirected to /login page
3) User enters the credentials and if valid, then the resource owner is redirected to /oauth2/consent page.
4) The html view represent the page with requested scopes and resource owner can deny or allow the access to resource owner's data.
5) If resource owner approved, then redirect to provided redirect uri with query parameters based on Oauth2 Specification.
6) If resource owner denied, then redirect to provided redirect uri with query parameters based on Oauth2 Specification.


Cello Authorization request persisting mechanisms.

Authorization request is stored in Oauth2AuthorizationRequestRepository that provide the methods to associate the current authorization request with current authentication.

Default implementation is WebSessionOauth2AuthorizationRequestRepository, that stores the authorization requests in Spring WebSession.

The drawback of this mechanism that resource owner can't be authenticated multiple times even if the different client is requesting authorization. 
Additionally, new authorization request ALWAYS override the existing one, so it leads to some inconsistency. 
The same behaviour exists in Spring Authorization Server.



What can we do to fix?
Add the flow ID to log in process and implement another Oauth2AuthorizationRequestRepositorySupport, 
that will save the Oauth2AuthorizationRequest and return ID, we can write the custom ResourceOwnerAuthenticationSuccessHandler
that will save the Oauth2AuthorizationRequest to Oauth2AuthorizationRequestRepositorySupport and redirect to log in uri with flow parameters,
as well, the form WILL send the current flow ID, consent page will be aware about flow ID, 
so every request will contain flow ID. 

/oauth2/login?flow_id=123 -> redirect -> /oauth2/consent?flow_id=123 -> 