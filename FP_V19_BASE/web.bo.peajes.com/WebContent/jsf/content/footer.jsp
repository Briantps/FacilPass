<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns     = "http://www.w3.org/1999/xhtml" 
	  xmlns:jsp = "http://java.sun.com/JSP/Page"
	  xmlns:f   = "http://java.sun.com/jsf/core"
	  xmlns:h   = "http://java.sun.com/jsf/html" 
	  xmlns:ui  = "http://java.sun.com/jsf/facelets"
	  xmlns:rich= "http://richfaces.org/rich"
	  xmlns:a4j = "http://richfaces.org/a4j">
	<head></head>
	<body>
		<f:view>
			<div class="footer">	 
				<div class="span-24">
					<div class="bottom-message2"> 
						<h:form>
						    <h:outputText value="Versión: ${version.versionMsg}"/><br/>		
							<h:outputText value="Peajes Electrónicos S.A.S"/><br/>				
							<h:outputText value="Cra 13 No.26-45 Of.1302 Bogotá D.C Colombia"/><br/>
							<h:outputText value="Líneas de atención 01 8000 180 440 - (1) 7466060 facilpass@facilpass.com" /><br/>
							<h:outputText value="Copyright © 2013. All Rights Reserved." /><br/>
							<h:outputText value="Para una mejor experiencia con la web, recomendamos" />
						</h:form>
						<a href="http://www.mozilla.org/es-ES/firefox/fx/" target="_blank"><img alt="Firefox"
						height="32" width="32" src="/web.bo.peajes.com/img/logo-mozilla-mini2.png"/></a>						
						<a href="https://www.google.com/intl/es/chrome/browser/?hl=es" target="_blank"><img alt="Chrome"
						height="32" width="32" src="/web.bo.peajes.com/img/google-chrome-logo-mini2.png"/></a>
						<a href="http://windows.microsoft.com/es-xl/internet-explorer/ie-10-worldwide-languages" target="_blank"><img alt="IE10" 
						height="32" width="32" src="/web.bo.peajes.com/img/ie10-logo-mini.png"/></a>
					</div>
			  	</div>
			</div>
		</f:view>
	</body>
</html>