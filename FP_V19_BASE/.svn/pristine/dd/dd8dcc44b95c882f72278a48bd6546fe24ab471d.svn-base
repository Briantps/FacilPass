<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns     = "http://www.w3.org/1999/xhtml" 
	  xmlns:jsp = "http://java.sun.com/JSP/Page"
	  xmlns:f   = "http://java.sun.com/jsf/core"
	  xmlns:h   = "http://java.sun.com/jsf/html" 
	  xmlns:ui  = "http://java.sun.com/jsf/facelets"
	  xmlns:rich= "http://richfaces.org/rich"
	  xmlns:a4j = "http://richfaces.org/a4j">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=8" />
		<script charset="utf-8" src="http://widgets.twimg.com/j/2/widget.js"></script>
		<link rel="stylesheet" href="/web.bo.peajes.com/css/styles.css" type="text/css" media="screen, projection" />
		<script language="javascript" type="text/javascript" src="/web.bo.peajes.com/js/validation.js"></script>
	</head>
	<style>
		.rich-mpnl-header
		{
		  background-color: #A8A8DC;
		  border-top-color: #A8A8DC;
		  border-right-color: #A8A8DC;
		  border-bottom-color: #A8A8DC;
		  border-left-color: #A8A8DC;
		  background-image: none;
		  color: #000000;
		}	
		
	</style>
	<script>
	function getFocus() {		
		setTimeout(function(){document.getElementById("formfocus:alertAcepts").focus()},1000);		
	}
	function getFocust() {		
		setTimeout(function(){document.getElementById("formfocust:alertAceptst").focus()},1000);		
	}
	</script>
	<body>
		<f:view>
			<div class="container">	  
		  		<div class="span-24">
		  			<f:loadBundle basename="staticText.messages" var="msg" />  
			  		<rich:messages styleClass="bold" style="color: red;"/>
					<h:form id="formName" styleClass="white" align="center">
						<div align="center">
							<h:outputText value="#{loginMBean.txtLogin}"
							escape="false"
							style="width: 680; height: auto;resize: none;display: compact;word-wrap: break-word;"  />
						</div>
						<rich:spacer height="60"/>
						<br/><br/>
						<div class="login-img" align="center" style="margin-top:2px;">	
							<h:form id="notlogged">
								<table height="150px">	
					 				<tr>
					 					<td colspan="2"><br/></td>
					 				</tr>			 
					 				<tr>
					  					<td width="41%">
					  						<div style="margin-left:80%">
					  							<h:outputText styleClass="login-text" value="Usuario" />
					  						</div>
					  					</td>
					  					<td>
										  <h:inputText styleClass="login-box" value="#{loginMBean.mail}" style="width: 250px"/>
										</td>
					 				</tr>
					 				<tr>
					 					<td colspan="2"><br/></td>
					 				</tr>			 
					 				<tr>
					  					<td>
					  						<div style="margin-left:80%"><h:outputText styleClass="login-text" value="Contraseña" /></div>
					  					</td>
					  					<td>
					  						<h:inputSecret styleClass="login-box" value="#{loginMBean.password}" style="width: 250px"/>
					  					</td>
					 				</tr>
					 				<tr>
					 					<td colspan="2"><br/></td>
					 				</tr>	
					 				<tr>
					 					<td colspan="2">
					 						<div align="center">
					 							<h:commandButton value="Ingresar" action="#{loginMBean.validateUser}" />
					 						</div>
					 					</td>
					 				</tr>			 
								</table>
							</h:form>
						</div>
						<rich:spacer height="50"/>	
					</h:form>
		  		</div>
			</div>		
			<rich:modalPanel id="panel" width="360" height="115" showWhenRendered="#{loginMBean.exist}" onresize="return false;" moveable="false" onbeforeshow="getFocus()">
				<f:facet name="header">
					<h:panelGroup>
						<h:outputText value="Mensaje - Login"/>
					</h:panelGroup>
				</f:facet>
				<f:facet name="controls">
					<h:panelGroup>
						<a4j:form>
							<h:commandButton action="#{loginMBean.hideModal}" image="/img/close.png" />	
						</a4j:form>
					</h:panelGroup>
				</f:facet>
				<h:outputText value="#{loginMBean.modalMessage}" styleClass="messageLabel"/>
				<rich:spacer height="10"/>
				<h:form id="formfocus">
					<h:panelGrid columns="3" styleClass="tableBO">
						<h:panelGroup>
							<div class="span-1"></div>
						</h:panelGroup>
			     		<h:panelGroup>
				     		<h:commandButton id="alertAcepts" value="Aceptar" action="#{loginMBean.hideModal}"  style="width: 80px;"/>
				     	</h:panelGroup>	     	
			    	</h:panelGrid>
			    </h:form>	
				<br />				
			</rich:modalPanel>	
		
		<!--Modal que permite el ingreso del código OTP para usuarios Nuevos -->
			<rich:modalPanel id="pOtp" width="630" height="120" showWhenRendered="#{loginMBean.showOtp}" onresize="return false;" moveable="false">
				<f:facet name="header">
					<h:panelGroup>
						<h:outputText value="Ingreso OTP"/>
					</h:panelGroup>
				</f:facet>
				<f:facet name="controls">
					<h:panelGroup>
						<a4j:form>
							<h:commandButton action="#{loginMBean.hideModalOtpPSE}" image="/img/close.png" />	
						</a4j:form> 
					</h:panelGroup>
				</f:facet>	
				<h:form id="frmOtp">			
					<h:outputText value="Señor usuario ingrese el código que le fue enviado a su cuenta de correo electrónico registrada en el sistema" styleClass="messageLabel"/>
					<h:inputText id="otp" value="#{loginMBean.otp}" style="width: 605px" valueChangeListener="#{loginMBean.cambio}" onkeypress="return numOnly(event)" maxlength="15"></h:inputText>						
					<rich:spacer height="15"/>
					<h:panelGrid columns="1" styleClass="tableBO" style="width: 650px; margin: auto; padding:10px;">
			     		<h:panelGroup style="display:block; text-align:center">
				     		<a4j:commandButton value="Aceptar" action="#{loginMBean.validarOtp}" reRender="pMsjOtp,panelExit" style="width: 100px;">	</a4j:commandButton>
				     		<rich:spacer width="10"/>
				     		<h:commandButton value="Cancelar" action="#{loginMBean.hideModalOtpPSE}"  style="width: 100px;"/>
				     	</h:panelGroup>	
			    	</h:panelGrid>
			    </h:form>	
				<br />				
			</rich:modalPanel>
			
			<!--Modal muestra mensaje depués de validación del OTP para usuarios Nuevos -->
			<rich:modalPanel id="pMsjOtp" width="350" height="115" showWhenRendered="#{loginMBean.showMsjOtp}" autosized="true" onresize="return false;" moveable="false">
				<f:facet name="header">
					<h:panelGroup>
						<h:outputText styleClass="bold" value="Mensaje - Ingreso OTP"/>
					</h:panelGroup>
				</f:facet>
				<f:facet name="controls">
					<h:panelGroup>
						<a4j:form>
							<h:commandButton action="#{loginMBean.hideModalOtpPSE}" image="/img/close.png" />	
						</a4j:form>
					</h:panelGroup>
				</f:facet>
				<h:outputText value="#{loginMBean.modalMessage}" style="min-height: 115px;max-height: 500px;"/>
				<rich:spacer height="10"/>
				<h:form>
					<h:panelGrid columns="3" styleClass="tableBO">
						<h:panelGroup>
							<div class="span-1"></div>
						</h:panelGroup>
			     		<h:panelGroup>
				     		<h:commandButton value="Aceptar" action="#{loginMBean.hideModalOtpPSE}" style="width: 80px"/>
				     	</h:panelGroup>
			    	</h:panelGrid>
			    </h:form>			
			</rich:modalPanel>
			
			<!--Modal que Muestra mensaje de verificación vigencia del tiempo para definir las preguntas de seguridad Usuarios Existentes  -->
			<rich:modalPanel id="pMsjUsExis" width="350" height="115" showWhenRendered="#{loginMBean.showMsjUsExis}" autosized="true" onresize="return false;" moveable="false">
				<f:facet name="header">
					<h:panelGroup>
						<h:outputText value="Mensaje - Definición preguntas de seguridad"/>
					</h:panelGroup>
				</f:facet>
				<f:facet name="controls">
					<h:panelGroup>
						<a4j:form>
							<h:commandButton action="#{loginMBean.hideModalOtpPSE}" image="/img/close.png" />	
						</a4j:form>
					</h:panelGroup>
				</f:facet>
				<h:outputText value="#{loginMBean.modalMessage}" style=" word-wrap: break-word; max-width: 330px;  max-height: 100px; resize: none; overflow: auto; display: block;" escape="false"/>	
				<rich:spacer width="5"/>
				<h:form>
				   <div align="center">	
			    	<h:commandButton value="Aceptar" action="#{loginMBean.rediConfig}" style="width: 80px"/>
				   </div>
			    </h:form>	
			</rich:modalPanel>
			
			<!--Modal que Muestra mensaje de verificación vigencia del tiempo para definir las preguntas de seguridad Usuarios Existentes  -->
			<rich:modalPanel id="CreateQuestions" width="430" height="115" showWhenRendered="#{loginMBean.showCreateQuestions}" autosized="true" onresize="return false;" moveable="false">
				<f:facet name="header">
					<h:panelGroup>
						<h:outputText value="Mensaje - Definición preguntas de seguridad"/>
					</h:panelGroup>
				</f:facet>
				<f:facet name="controls">
					<h:panelGroup>
						<a4j:form>
							<h:commandButton action="#{loginMBean.hideModalOtpPSE}" image="/img/close.png" />	
						</a4j:form>
					</h:panelGroup>
				</f:facet>
					<h:outputText value="#{loginMBean.modalMessage}" style=" word-wrap: break-word; max-width: 420px;  max-height: 100px; resize: none; overflow: auto; display: block;" escape="false"/>	
					<h:form>
					 <rich:spacer height="15"/> 
					 <h:panelGrid columns="4" styleClass="tableBO">
						<h:panelGroup><div class="span-1"></div></h:panelGroup>
						<h:panelGroup>						
	                        <h:commandButton value="Aceptar" action="#{loginMBean.rediConfig}" style="width: 135px"/>
		                    <rich:spacer width="10"/>  
				     		<h:commandButton value="Recordar más tarde" action="#{loginMBean.rememberQuestionsAfter}" style="width: 135px"/>
			     		</h:panelGroup>
			     	</h:panelGrid>     	
			    </h:form>	
			</rich:modalPanel>
			
			<rich:modalPanel id="panelExit" width="360" height="115" onresize="return false;" moveable="false" onbeforeshow="getFocust()">
				<f:facet name="header">
					<h:panelGroup>
						<h:outputText value="Mensaje - Login"/>
					</h:panelGroup>
				</f:facet>
				<f:facet name="controls">
					<h:panelGroup>
						<a4j:form>
							<h:commandButton action="#{loginMBean.logout}" image="/img/close.png" />	
						</a4j:form>
					</h:panelGroup>
				</f:facet>
				<h:outputText value="Su Sesión ha Terminado" styleClass="messageLabel"/>
				<rich:spacer height="10"/>
				<h:form id="formfocust">
					<h:panelGrid columns="3" styleClass="tableBO">
						<h:panelGroup>
							<div class="span-1"></div>
						</h:panelGroup>
			     		<h:panelGroup>
			     			<h:commandButton id="alertAceptst" value="Aceptar" action="#{loginMBean.logout}"  style="width: 80px;"/>
			     		</h:panelGroup>	     	
			    	</h:panelGrid>
			    </h:form>	
				<br />
			</rich:modalPanel>
		</f:view>		
	</body>
</html>