package it.unimore.sia;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import it.unimore.sia.ExpirationAuthenticationHandler;
import edu.vt.middleware.ldap.auth.AuthenticatorConfig;
import edu.vt.middleware.ldap.handler.DefaultConnectionHandler;
import edu.vt.middleware.ldap.LdapConfig;
import edu.vt.middleware.ldap.auth.handler.AuthenticationCriteria;
import java.io.FileInputStream;

/**
 * Unit test for simple App.
 */
public class ExpirationAuthenticationHandlerTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ExpirationAuthenticationHandlerTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( ExpirationAuthenticationHandlerTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testAuthenticate()
    {
	AuthenticationCriteria ac = new AuthenticationCriteria("uid=162938,ou=people,dc=unimore,dc=it");
	ac.setCredential("zh4je379");
	try {
	    
	    LdapConfig lc = LdapConfig.createFromProperties(new FileInputStream("ldap.properties"));	
	    DefaultConnectionHandler ch = new DefaultConnectionHandler(lc);

	    System.out.println("in test: " + ch.getLdapConfig().toString());
		
	    AuthenticatorConfig config = AuthenticatorConfig.createFromProperties(new FileInputStream("ldap.properties"));	

	    ExpirationAuthenticationHandler expirationHandler = new ExpirationAuthenticationHandler();
	    expirationHandler.setAuthenticatorConfig(config);

	    expirationHandler.authenticate(ch, ac); 
	    }
	catch (javax.naming.NamingException ne)
	    {
		System.out.println("Eccezione: " + ne.toString());
	    }
	catch (java.io.FileNotFoundException fnfe)
	    {
		System.out.println("Eccezione: " + fnfe.toString());
	    }
	assertTrue( true );
    }
}
