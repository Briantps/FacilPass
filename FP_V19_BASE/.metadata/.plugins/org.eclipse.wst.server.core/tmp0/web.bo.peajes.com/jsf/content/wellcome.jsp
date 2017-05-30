<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:a4j="http://richfaces.org/a4j"
	 class="no-js" lang="en-US">
<head>

<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
<script charset="utf-8" src="http://widgets.twimg.com/j/2/widget.js"></script>
<link rel="stylesheet" href="/web.bo.peajes.com/css/styles.css"
	type="text/css" media="screen, projection" />
		<link rel="stylesheet" href="/web.bo.peajes.com/css/eyePasswordStyle.css"
	type="text/css"/>
 <script language="javascript" type="text/javascript"
	src="/web.bo.peajes.com/js/validation.js"></script>
</head>
<script language="javascript">
	function getFocus() {
		setTimeout(function() {
			document.getElementById("formfocus:alertAcepts").focus()
		}, 1000);
	}
	function getFocust() {
		setTimeout(function() {
			document.getElementById("formfocust:alertAceptst").focus()
		}, 1000);
	}
	

		
</script>
<body ondragstart="return false" onmouseup="mouseUpBody();">


	<f:view>
		<div class="container">
			<div class="span-24">
				<h:form id="formName" name="mipassword" styleClass="white" align="center">
					<h:outputText styleClass="login-msg" value="#{loginMBean.redirectTxtLogin}" style="word-wrap: break-word;" escape="false"/>
					<rich:spacer height="60" />
					<br />
					<h:outputText  styleClass="notify-msg" value="#{loginMBean.message}" rendered="true" />
					<br />
					<div class="login-img" align="center" style="margin-top: 2px;">
						<h:form id="notlogged" rendered="#{!loginMBean.logged}">
							<table height="150px">
								<tr>
									<td colspan="2"><br/>
									</td>
								</tr>
								<tr>
									<td width="41%">
										<div style="margin-left: 80%">
											<h:outputText styleClass="login-text" value="Usuario" />
										</div></td>
									<td><h:inputText id="userId"  styleClass="login-box"
											value="#{loginMBean.mail}" style="width: 250px" /></td>
								</tr>
								<tr>
									<td colspan="2"><br />
									</td>
								</tr>
								<tr>
							
									<td>
										<div style="margin-left: 80%">
											<h:outputText styleClass="login-text" value="Contrase�a" />
										</div></td>
									<td style="vertical-align: top;">
									 <div id="divE">
										<h:graphicImage id="changeL" value="/img/transparente.png"
												style="position: absolute;z-index: 1; width: 15px;margin-top: 2px;margin-left: 236px;"
												onmouseover="showImageL();" onmouseout="showImageL1();"
												onmousedown="mouseDownL();" onmouseup="mouseUpL();"  /> 
											<h:inputSecret name="inputPass"  id="login" styleClass="login-box login"
												value="#{loginMBean.password}"
												style="position:absolute;z-index:0;width: 250px;" onkeypress="onkeypreTecL();"  />
											<h:inputText name="inputPassClear"  id="loginClear" styleClass="login-box login"
												style="position:absolute;z-index:0;width: 250px;visibility : hidden;" />
										</div>
										</td>
								</tr>
								<tr>
									<td colspan="2"><br />
									</td>
								</tr>
								<tr>
									<td colspan="2">
										<div align="center">
											<h:commandButton value="Ingresar"
												action="#{loginMBean.validateUser}" />
										</div></td>
								</tr>
							</table>
						</h:form>
					</div>
					<rich:spacer height="5" />
					<div align="center">
						<h:panelGroup>
							<h:outputLink value="http://www.facilpass.com"
								styleClass="link-blue" rendered="#{loginMBean.showRegistred}" >Volver al Inicio</h:outputLink>
							<h:outputLabel value="        " />
							<h:commandLink value="Registrarse" action="createClientOutside"
								styleClass="link-blue" rendered="#{loginMBean.showRegistred}" />
							<h:outputLabel value="        " />
							<h:commandLink value="Recuperar Contrase�a"
								action="usrValidation" styleClass="link-blue" rendered="#{loginMBean.showRegistred}" />
						</h:panelGroup>
					</div>
					<rich:spacer height="50" />
				</h:form>
			</div>
		</div>
		<rich:modalPanel id="panel" width="460" height="125"
			showWhenRendered="#{loginMBean.exist}" onresize="return false;"
			moveable="false" onbeforeshow="getFocus()">
			<f:facet name="header">
				<h:panelGroup>
					<h:outputText value="Mensaje - Login" />
				</h:panelGroup>
			</f:facet>
			<f:facet name="controls">
				<h:panelGroup>
					<a4j:form>
						<h:commandButton action="#{loginMBean.hideModal}"
							image="/img/close.png" />
					</a4j:form>
				</h:panelGroup>
			</f:facet>
			<h:outputText value="#{loginMBean.modalMessage}"
				styleClass="messageLabel" />
			<rich:spacer height="10" />
			<h:form id="formfocus">
				<h:panelGrid columns="4" styleClass="tableBO">
					<h:panelGroup>
						<div class="span-1"></div>
					</h:panelGroup>
					<h:panelGroup>
						<h:commandButton id="alertAcepts" value="Aceptar"
							action="#{loginMBean.hideModal}" style="width: 160px;" />
						<rich:spacer width="10" />
						<h:commandButton value="Restablecer Contrase�a"
							action="#{loginMBean.redirecRestartUser}" style="width: 160px" />
					</h:panelGroup>
				</h:panelGrid>
			</h:form>
			<br />
		</rich:modalPanel>

		<!--Modal que permite el ingreso del c�digo OTP para usuarios Nuevos -->
		<rich:modalPanel id="pOtp" width="630" height="130"
			showWhenRendered="#{loginMBean.showOtp}" onresize="return false;"
			moveable="false">
			<f:facet name="header">
				<h:panelGroup>
					<h:outputText value="Ingreso OTP" />
				</h:panelGroup>
			</f:facet>
			<f:facet name="controls">
				<h:panelGroup>
					<a4j:form>
						<h:commandButton action="#{loginMBean.hideModal}"
							image="/img/close.png" />
					</a4j:form>
				</h:panelGroup>
			</f:facet>
			<h:form id="frmOtp">
				<h:outputText
					value="Se�or usuario ingrese el c�digo que le fue enviado a su cuenta de correo electr�nico registrada en el sistema."
					styleClass="messageLabel" />
				<h:inputText id="otp" value="#{loginMBean.otp}" style="width: 605px"
					onkeypress="return numOnly(event)" maxlength="15"></h:inputText>
				<rich:spacer height="15" />
				<h:panelGrid columns="1" styleClass="tableBO"
					style="width: 650px; margin: auto; padding:10px;">
					<h:panelGroup style="display:block; text-align:center">
						<h:commandButton value="Aceptar" action="#{loginMBean.validarOtp}" style="width: 100px;">
							<a4j:support event="oncomplete" reRender="pMsjOtp,pMsjOtpError,panelExit"/>
						</h:commandButton>
						<rich:spacer width="10" />
						<h:commandButton value="Cancelar" action="#{loginMBean.hideModal}"
							style="width: 100px;" />
					</h:panelGroup>
				</h:panelGrid>
			</h:form>
			<br />
		</rich:modalPanel>

		<!--Modal muestra mensaje depu�s de validaci�n del OTP para usuarios Nuevos -->
		<rich:modalPanel id="pMsjOtp" width="350" height="115"
			showWhenRendered="#{loginMBean.showMsjOtp}" autosized="true"
			onresize="return false;" moveable="false">
			<f:facet name="header">
				<h:panelGroup>
					<h:outputText styleClass="bold" value="Mensaje - Ingreso OTP" />
				</h:panelGroup>
			</f:facet>
			<f:facet name="controls">
				<h:panelGroup>
					<a4j:form>
						<h:commandButton action="#{loginMBean.hideModal}"
							image="/img/close.png" />
					</a4j:form>
				</h:panelGroup>
			</f:facet>
			<h:outputText value="#{loginMBean.modalMessage}"
				style="min-height: 115px;max-height: 500px;" />
			<rich:spacer height="10" />
			<h:form>
				<h:panelGrid columns="3" styleClass="tableBO">
					<h:panelGroup>
						<div class="span-1"></div>
					</h:panelGroup>
					<h:panelGroup>
						<h:commandButton value="Aceptar"
							action="#{loginMBean.hideModalOtp}" style="width: 80px" />
					</h:panelGroup>
				</h:panelGrid>
			</h:form>
		</rich:modalPanel>

		<!--Modal muestra mensaje depu�s de validaci�n del OTP para usuarios Nuevos -->
		<rich:modalPanel id="pMsjOtpValidity" width="350" height="115"
			showWhenRendered="#{loginMBean.showMsjOtpValidity}" autosized="true"
			onresize="return false;" moveable="false">
			<f:facet name="header">
				<h:panelGroup>
					<h:outputText styleClass="bold" value="Mensaje - Login" />
				</h:panelGroup>
			</f:facet>
			<f:facet name="controls">
				<h:panelGroup>
					<a4j:form>
						<h:commandButton action="#{loginMBean.hideModal}"
							image="/img/close.png" />
					</a4j:form>
				</h:panelGroup>
			</f:facet>
			<h:outputText value="#{loginMBean.modalMessage}"
				style="min-height: 115px;max-height: 500px;" />
			<rich:spacer height="10" />
			<h:form>
				<h:panelGrid columns="3" styleClass="tableBO">
					<h:panelGroup>
						<div class="span-1"></div>
					</h:panelGroup>
					<h:panelGroup>
						<h:commandButton value="Aceptar"
							action="#{loginMBean.hideModalOtpValidity}" style="width: 80px" />
					</h:panelGroup>
				</h:panelGrid>
			</h:form>
		</rich:modalPanel>


		<!--Modal que Muestra mensaje de verificaci�n vigencia del tiempo para definir las preguntas de seguridad Usuarios Existentes  -->
		<rich:modalPanel id="pMsjUsExis" width="350" height="115"
			showWhenRendered="#{loginMBean.showMsjUsExis}" autosized="true"
			onresize="return false;" moveable="false">
			<f:facet name="header">
				<h:panelGroup>
					<h:outputText value="Mensaje - Definici�n preguntas de seguridad" />
				</h:panelGroup>
			</f:facet>
			<f:facet name="controls">
				<h:panelGroup>
					<a4j:form>
						<h:commandButton action="#{loginMBean.hideModal}"
							image="/img/close.png" />
					</a4j:form>
				</h:panelGroup>
			</f:facet>
			<h:outputText value="#{loginMBean.modalMessage}"
				style=" word-wrap: break-word; max-width: 330px;  max-height: 100px; resize: none; overflow: auto; display: block;"
				escape="false" />
			<rich:spacer width="5" />
			<h:form>
				<div align="center">
					<h:commandButton value="Aceptar" action="#{loginMBean.rediConfig}"
						style="width: 80px" />
				</div>
			</h:form>
		</rich:modalPanel>

		<!--Modal que Muestra mensaje de verificaci�n vigencia del tiempo para definir las preguntas de seguridad Usuarios Existentes  -->
		<rich:modalPanel id="CreateQuestions" width="440" height="115"
			showWhenRendered="#{loginMBean.showCreateQuestions}" autosized="true"
			onresize="return false;" moveable="false">
			<f:facet name="header">
				<h:panelGroup>
					<h:outputText value="Mensaje - Definici�n preguntas de seguridad" />
				</h:panelGroup>
			</f:facet>
			<f:facet name="controls">
				<h:panelGroup>
					<a4j:form>
						<h:commandButton action="#{loginMBean.hideModal}"
							image="/img/close.png" />
					</a4j:form>
				</h:panelGroup>
			</f:facet>
			<h:outputText value="#{loginMBean.modalMessage}"
				style=" word-wrap: break-word; max-width: 420px;  max-height: 100px; resize: none; overflow: auto; display: block;"
				escape="false" />
			<h:form>
				<rich:spacer height="15" />
				<h:panelGrid columns="4" styleClass="tableBO">
					<h:panelGroup>
						<div class="span-1"></div>
					</h:panelGroup>
					<h:panelGroup>
						<h:commandButton value="Aceptar" action="#{loginMBean.rediConfig}"
							style="width: 135px" />
						<rich:spacer width="10" />
						<h:commandButton value="Recordar m�s tarde"
							action="#{loginMBean.rememberQuestionsAfter}"
							style="width: 135px" />
					</h:panelGroup>
				</h:panelGrid>
			</h:form>
		</rich:modalPanel>

		<rich:modalPanel id="panelExit" width="360" height="115"
			showWhenRendered="#{loginMBean.logged and !loginMBean.exist}"
			onresize="return false;" moveable="false" onbeforeshow="getFocust()">
			<f:facet name="header">
				<h:panelGroup>
					<h:outputText value="Mensaje - Login" />
				</h:panelGroup>
			</f:facet>
			<f:facet name="controls">
				<h:panelGroup>
					<a4j:form>
						<h:commandButton action="#{loginMBean.logout}"
							image="/img/close.png" />
					</a4j:form>
				</h:panelGroup>
			</f:facet>
			<h:outputText value="Su Sesi�n ha Terminado."
				styleClass="messageLabel" />
			<rich:spacer height="10" />
			<h:form id="formfocust">
				<h:panelGrid columns="3" styleClass="tableBO">
					<h:panelGroup>
						<div class="span-1"></div>
					</h:panelGroup>
					<h:panelGroup>
						<h:commandButton id="alertAceptst" value="Aceptar"
							action="#{loginMBean.logout}" style="width: 80px;" />
					</h:panelGroup>
				</h:panelGrid>
			</h:form>
			<br />
		</rich:modalPanel>
	</f:view>
	

	
	

</body>
</html>