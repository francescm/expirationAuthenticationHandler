package it.unimore.sia;

import javax.naming.NamingException;
import edu.vt.middleware.ldap.auth.*;
import edu.vt.middleware.ldap.auth.handler.*;
//import edu.vt.middleware.ldap.auth.AuthenticatorConfig;
import edu.vt.middleware.ldap.handler.ConnectionHandler;
import edu.vt.middleware.ldap.auth.handler.AbstractAuthenticationHandler;
import edu.vt.middleware.ldap.Ldap;
import javax.naming.directory.Attributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.util.Locale;
import java.util.GregorianCalendar;
import java.util.Calendar;


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
	try {	    
	    ch.connect(ac.getDn(), ac.getCredential());
	} catch (NamingException e) {
	    this.logger.debug("NamingException: " + e);
	    Ldap ldap = null;
	    try {
		ldap = new Ldap(this.config);
		Attributes a = ldap.getAttributes(
						  ac.getDn(),
						  new String[]{"pwdChangedTime", "pwdPolicySubentry"});
		String pct = a.get("pwdChangedTime") != null ?
		    (String) a.get("pwdChangedTime").get() : "";
		String policy = a.get("pwdPolicySubentry") != null ?
		    (String) a.get("pwdPolicySubentry").get() : "";

		if ((pct == "") || (policy == ""))
		    {
			throw new NamingException("Wrong username/password");
		    }
		else
		    {
			//			String policy = a.get("pwdPolicySubentry") != null ?
			//			    (String) a.get("pwdPolicySubentry").get() : "cn=default,ou=policies,dc=unimore,dc=it";
			this.logger.info("Check policy for " + ac.toString() + ": pwdChangedTime: " + pct + "; pwdPolicySubentry: " + policy);
			String maxAge = "";

			Attributes b = ldap.getAttributes(
							  policy,
							  new String[]{"pwdMaxAge"});
			maxAge = b.get("pwdMaxAge") != null ?
			    (String) b.get("pwdMaxAge").get() : ""; 
			
			Date d = null;
			try
			    {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.ITALY);
				d = dateFormat.parse(pct.substring(0, (pct.length() - 1)));
			    }
			catch (ParseException pe)
			    {
				this.logger.error("Parsing failed: " + pe.toString());
				//				System.out.println("Parsing failed: " + pe.toString());
				//			throw new ParseException(pe.toString(), pe.getErrorOffset());
			    }
			GregorianCalendar expirationTime = (GregorianCalendar)Calendar.getInstance();
			expirationTime.setTime(d);
			expirationTime.add(Calendar.SECOND, Integer.parseInt(maxAge));			
			GregorianCalendar now = new GregorianCalendar();
			if (expirationTime.before(now))
			    {
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy hh:mm:ss");
				this.logger.info("Password expired for " + ac.toString() + "(" + formatter.format(expirationTime.getTime()) + ")");
				System.out.println(expirationTime.toString());
				throw new NamingException("Password expired");
			    }
			else
			    {
				throw new NamingException("Wrong username/password");
			    }
		    }
	    } finally {
		if (ldap != null) {
		    ldap.close();
		}   
	    }   
	}   
    }
}

