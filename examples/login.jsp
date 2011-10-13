<%@ page import="edu.internet2.middleware.shibboleth.idp.authn.LoginContext" %>
<%@ page import="edu.internet2.middleware.shibboleth.idp.authn.LoginHandler" %>
<%@ page import="edu.internet2.middleware.shibboleth.idp.session.*" %>
<%@ page import="edu.internet2.middleware.shibboleth.idp.util.HttpServletHelper"
 %>
<%@ page import="org.opensaml.saml2.metadata.*" %>
<%@ page import="java.util.StringTokenizer" %>

<%
    LoginContext loginContext = 
 HttpServletHelper.getLoginContext(HttpServletHelper.getStorageService(application), application, request);
    Session userSession = HttpServletHelper.getUserSession(request);
%>
<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html lang="en" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml">
	<head>
	<title>UniMORE WebLogin</title>
	<style type="text/css" title="styled">
		@import url("<%= request.getContextPath() %>/login.css");
	</style>
	</head>
	<body>
	   <div id="principale">
		  <div id="login_testa">
		  </div>
		  <div id="login_corpo">
			<div id="login_form">
			<% if (loginContext == null) { %>
			<p><font color="red">Error:</font> Direct access to this page is not supported.
Please ensure that you did not bookmark this page or reach it by using the back
  button or selecting it from your browser history.  To log in to a particular
  service, please visit that service first.
  	   	  	</p>
			<p><font color="red">Errore:</font> Non &egrave; possibile accedere direttamente a questa pagina. Controllare di non aver inserito questa pagina nei preferiti ed evitare di raggiungerla con il tasto back del browser o con la cronologia di navigazione. Per fare il login su un certo servizio, prima visitare la pagina del servizio.
  	   	  	</p>

			<% } else { %>
			  <% if (request.getAttribute(LoginHandler.AUTHENTICATION_EXCEPTION_KEY) != null) { %>
<p><% 
String ex = request.getAttribute(LoginHandler.AUTHENTICATION_EXCEPTION_KEY).toString();
StringTokenizer st = new StringTokenizer(ex, ":");
String msg = "";
while (st.hasMoreElements()) {
msg = st.nextToken();
}
out.println(msg);
%></p>
			  <p>Authentication Failed</p>
			  <% } %>
			  
			  <% if(request.getAttribute("actionUrl") != null){ %>
			  <form action="<%=request.getAttribute("actionUrl")%>" method="post" class="cmxform">
			    <% }else{ %>
			    <form action="j_security_check" method="post">
			      <% } %>

						<fieldset>
							<legend>Log-in</legend>
							<ul>
								<li>
									<label for="j_username"><b>Nome utente</b></label>
									<input name="j_username" type="text" id="j_username" size="16" />
								</li>
								<li>
									<label for="j_password"><b>Password</b></label>
									<input name="j_password" type= "password" id="j_password" size= "16" />
								</li>
<li><input type="checkbox" name="resetuserconsent" value="true" />
        Reset my attribute release approvals</li>
								<li>
									<input name="Login" type="submit" id="Login" value="Entra (Login)" />									
								</li>
							</ul>
						</fieldset>
					</form>
			  <p>The resource that you have attempted to access requires that you log in with your UniMORE UID.<br />
			  Il servizio a cui si sta accedendo richiede l'inserimento delle proprie credenziali UniMORE:
			  </p>
				<ul>
					<li><strong>per gli studenti:</strong> le credenziali (numero tessera e PIN) le sono state rilasciate dalla segreteria studenti al momento dell'immatricolazione;<br />
						  lei pu&ograve; <a href="https://iam.unimore.it/cambia_password/login.php">cambiare la password</a>		  inserendo l'ultima password<br />
						  ulteriori informazioni nella sezione delle <a href="http://start.studenti.unimore.it/on-line/Homepage/artCatFAQ.1596.1.99.2.all.html">domande ricorrenti</a>.
					</li>

					<li><strong>per tutti gli altri:</strong> le credenziali sono state scelte da lei al momento dell'identificazione o alla presa di servizio; nel caso non le ricordi pu&ograve; rivolgersi all'incaricato dell'identificazione presso la sua Struttura di afferenza. Consultare <a href="https://iam.unimore.it/incaricati/lista.php">l'elenco degli incaricati</a></li>
				</ul>
				<p>Contattare il <a href="mailto:supporto.identity@unimore.it">supporto tecnico</a>, specificando il servizio cui state accedendo, lo username usato e il tipo di errore ricevuto.</p>
				<p>Dettagli su <a href="http://www.unimore.it/idem.html">IDEM e sull'adesione dell'Universit&agrave; di Modena e Reggio nell'Emilia</a>.</p>
			<%}%>
			</div>
		  </div>
		  <div id="login_base">
		  </div>
	   </div>
  </body>
</html>
