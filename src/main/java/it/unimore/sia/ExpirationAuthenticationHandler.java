package it.unimore.sia;

import javax.naming.NamingException;
import edu.vt.middleware.ldap.auth.*;
import edu.vt.middleware.ldap.auth.handler.*;
//import edu.vt.middleware.ldap.auth.AuthenticatorConfig;
import edu.vt.middleware.ldap.handler.ConnectionHandler;
import edu.vt.middleware.ldap.auth.handler.AbstractAuthenticationHandler;
import edu.vt.middleware.ldap.Ldap;
import javax.naming.directory.Attributes;


public class ExpirationAuthenticationHandler extends AbstractAuthenticationHandler
{


  /** Default constructor. */
  public ExpirationAuthenticationHandler() {}


  /**
   * Creates a new <code>BindAuthenticationHandler</code> with the supplied
   * authenticator config.
   *
   * @param  ac  authenticator config
   */
  public ExpirationAuthenticationHandler(final AuthenticatorConfig ac)
  {
    this.setAuthenticatorConfig(ac);
  }



  /** {@inheritDoc} */
  public AuthenticationHandler newInstance()
  {
    return new ExpirationAuthenticationHandler(this.config);
  }


  /** {@inheritDoc} */
  public void authenticate(
    final ConnectionHandler ch,
    final AuthenticationCriteria ac)
    throws NamingException
    {
	this.logger.debug("Starting authentication for " + ac.toString());
	try {
	    System.out.println(ac.getDn());
	    System.out.println(ch.getLdapConfig().getLdapUrl());
	    System.out.println(ch.getLdapConfig().getBindDn());
	    System.out.println(ch.getLdapConfig().getBindCredential());
	    
	    ch.connect(ac.getDn(), ac.getCredential());
	} catch (NamingException e) {
	    this.logger.debug("NamingException: " + e);
	    Ldap ldap = null;
	    try {
		ldap = new Ldap(this.config);
		Attributes a = ldap.getAttributes(
						  ac.getDn(),
						  new String[]{"passwordExpirationTime"});
		String pet = a.get("passwordExpirationTime") != null ?
		    (String) a.get("passwordExpirationTime").get() : ""; 
		throw new NamingException("Password Expiration Time: " + pet + ". Login Grace Remaining");
	    } finally {
		if (ldap != null) {
		    ldap.close();
		}   
	    }   
	}   
    }

}

