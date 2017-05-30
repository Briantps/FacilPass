<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:a4j="http://richfaces.org/a4j">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
		<meta content="no-cache" http-equiv="Pragma" />
		<meta content="no-cache" http-equiv="Cache-Control" />
		<meta content="no-store" http-equiv="Cache-Control" />
		<meta content="max-age=0" http-equiv="Cache-Control" />
		<meta content="-1" http-equiv="Expires" />
		<link rel="stylesheet" href="/web.bo.peajes.com/css/blueprint/screen.css" type="text/css" media="screen, projection" />
		<link rel="stylesheet" href="/web.bo.peajes.com/css/blueprint/print.css" type="text/css" media="print" />
		<link rel="stylesheet" href="/web.bo.peajes.com/css/styles.css" type="text/css" media="screen, projection" />
		<script language="javascript" type="text/javascript" src="/web.bo.peajes.com/js/validation.js"></script>
		<title>Dashboard</title>
	</head>
	<body>
		<div class="container">
			<f:view>
				<f:subview id="header">
					<ui:include src="../content/header.jsp"/>
				</f:subview>		
				<div class="span-24">
					<f:subview id="headerMenu">
						<ui:include src="../menu/menu.jsp"/>
					</f:subview>
				</div>
				<div class="span-5">
					<ui:include src="../menu/sideMenu.jsp"/>
				</div>		
				<div class="bkContents span-18 prepend-1">		
					<rich:spacer height="5"/>	
					<h:form>
							<rich:panel align="center">
			        			<a4j:mediaOutput style="width:600px;height:500px;" element="img"
			  					cacheable="false" session="true" createContent="#{chartBean.drawChart}"
			     				mimeType="image/gif" />
		    				</rich:panel>
		    				<rich:spacer height="20"/>	
							<rich:panel style="width:200 px;margin:auto;">
								<f:facet name="header">
									<h:outputText value="Detalle para:" styleClass="bold"/>
								</f:facet>
								<h:commandLink value="Preinscripción" action="#{homeBean.preRegistration}" style="text-decoration:none;color:#4ca939;">
								</h:commandLink>
								<br/>
								<h:commandLink value="Validación Documentos" action="#{homeBean.validationDocuments}" style="text-decoration:none;color:red;">
								</h:commandLink>
								<br/>
								<h:commandLink value="Validados" action="#{homeBean.validationClients}" style="text-decoration:none;color:magenta;">
								</h:commandLink>
								<br/>
								<h:commandLink value="Creación Producto Bancario" action="#{homeBean.bankingProductCreation}" style="text-decoration:none;color:2b84e2;">
								</h:commandLink>
								<br/>
								<h:commandLink value="Asociación Producto Bancario" action="#{homeBean.associationBankingProduct}" style="text-decoration:none;color:orange;">
								</h:commandLink>
							</rich:panel>
					</h:form>
					<rich:spacer height="20"/>		
					<rich:modalPanel id="panelPR" width="700" height="600" showWhenRendered="#{homeBean.modalPreReg}" onresize="return false;" moveable="false" style="overflow: scroll">
						<f:facet name="header">
							<h:panelGroup>
								<h:outputText value="Clientes Preinscritos" styleClass="bold"/>
							</h:panelGroup>
						</f:facet>
						<f:facet name="controls">
							<h:panelGroup>
								<a4j:form>
									<h:commandButton action="#{homeBean.cancel}" image="/img/close.png" />
								</a4j:form>
							</h:panelGroup>
						</f:facet>
							<f:subview id="preRegistration">
								<ui:include src="preRegistration.jspx"/>
							</f:subview>
						<br />
					</rich:modalPanel>
					<rich:modalPanel id="panelVD" width="700" height="600" showWhenRendered="#{homeBean.modalValDoc}" onresize="return false;" moveable="false" style="overflow: scroll">
						<f:facet name="header">
							<h:panelGroup>
								<h:outputText value="Clientes Validación Documentos" styleClass="bold"/>
							</h:panelGroup>
						</f:facet>
						<f:facet name="controls">
							<h:panelGroup>
								<a4j:form>
									<h:commandButton action="#{homeBean.cancel}" image="/img/close.png" />
								</a4j:form>
							</h:panelGroup>
						</f:facet>
							<f:subview id="validationDocuments">
								<ui:include src="validationDocuments.jspx"/>
							</f:subview>
						<br />
					</rich:modalPanel>
					<rich:modalPanel id="panelVC" width="700" height="600" showWhenRendered="#{homeBean.modalValCli}" onresize="return false;" moveable="false" style="overflow: scroll">
						<f:facet name="header">
							<h:panelGroup>
								<h:outputText value="Clientes Validados" styleClass="bold"/>
							</h:panelGroup>
						</f:facet>
						<f:facet name="controls">
							<h:panelGroup>
								<a4j:form>
									<h:commandButton action="#{homeBean.cancel}" image="/img/close.png" />
								</a4j:form>
							</h:panelGroup>
						</f:facet>
							<f:subview id="validationClients">
								<ui:include src="validationClients.jspx"/>
							</f:subview>
						<br />
					</rich:modalPanel>
					<rich:modalPanel id="panelBPC" width="700" height="600" showWhenRendered="#{homeBean.modalBanCre}" onresize="return false;" moveable="false" style="overflow: scroll">
						<f:facet name="header">
							<h:panelGroup>
								<h:outputText value="Clientes Creación Producto Bancario" styleClass="bold"/>
							</h:panelGroup>
						</f:facet>
						<f:facet name="controls">
							<h:panelGroup>
								<a4j:form>
									<h:commandButton action="#{homeBean.cancel}" image="/img/close.png" />
								</a4j:form>
							</h:panelGroup>
						</f:facet>
							<f:subview id="bankingProductCreation">
								<ui:include src="bankingProductCreation.jspx"/>
							</f:subview>
						<br />
					</rich:modalPanel>
					<rich:modalPanel id="panelABP" width="700" height="600" showWhenRendered="#{homeBean.modalBanAso}" onresize="return false;" moveable="false" style="overflow: scroll">
						<f:facet name="header">
							<h:panelGroup>
								<h:outputText value="Clientes Asociación Producto Bancario" styleClass="bold"/>
							</h:panelGroup>
						</f:facet>
						<f:facet name="controls">
							<h:panelGroup>
								<a4j:form>
									<h:commandButton action="#{homeBean.cancel}" image="/img/close.png" />
								</a4j:form>
							</h:panelGroup>
						</f:facet>
							<f:subview id="associationBankingProduct">
								<ui:include src="associationBankingProduct.jspx"/>
							</f:subview>
						<br />
					</rich:modalPanel>
					<rich:modalPanel id="panel" width="350" height="115" showWhenRendered="#{homeBean.modal}" onresize="return false;" moveable="false">
						<f:facet name="header">
							<h:panelGroup>
								<h:outputText value="Mensaje" styleClass="bold"/>
							</h:panelGroup>
						</f:facet>
						<f:facet name="controls">
							<h:panelGroup>
								<a4j:form>
									<h:commandButton action="#{homeBean.cancel}" image="/img/close.png" />
								</a4j:form>
							</h:panelGroup>
						</f:facet>
						<h:outputText value="#{homeBean.modalMsg}" styleClass="normal"/>
						<rich:spacer height="10"/>
						<h:form>
							<h:panelGrid columns="3" styleClass="tableBO">
								<h:panelGroup>
									<div class="span-1"></div>
								</h:panelGroup>
					     		<h:panelGroup>
					     			<h:commandButton value="Aceptar" action="#{homeBean.cancel}"  />
					     		</h:panelGroup>	     	
					    	</h:panelGrid>
					    </h:form>
					    <br/>
					</rich:modalPanel>
				</div>
				<f:subview id="footer">
					<ui:include src="../content/footer.jsp"/>
				</f:subview>
			</f:view>
		</div>
	</body>
</html>