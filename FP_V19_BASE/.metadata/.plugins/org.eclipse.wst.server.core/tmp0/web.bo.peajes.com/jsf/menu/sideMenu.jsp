<?xml version="1.0" encoding="ISO-8859-1" ?>
 <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns     = "http://www.w3.org/1999/xhtml" 
	  xmlns:jsp = "http://java.sun.com/JSP/Page"
	  xmlns:f   = "http://java.sun.com/jsf/core"
	  xmlns:h   = "http://java.sun.com/jsf/html" 
	  xmlns:ui  = "http://java.sun.com/jsf/facelets"
	  xmlns:rich= "http://richfaces.org/rich"
	  xmlns:a4j="http://richfaces.org/a4j">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7; IE=EmulateIE8"></meta>
		<style type="text/css">
			.left-menu {display: block;width: 190px; margin-top: 2px;}
			.rich-pmenu-group {
			   border-top-color: #ffffff;
			   font-size: 13px; } 
			.rich-pmenu-item { font-size: 13px; border-top-color: #ffffff; }			
			.rich-pmenu-selected-element { background-color: #FFFF99; color: #000000; font-size: 12px;}
			.rich-pmenu-top-group
			{
			  font-weight: bold;
  			  font-family: Arial,Helvetica,sans-serif;
  			  font-size: 13px;
  			  color: #000000;
  			  background-color: #C6C6E8;
  			  background-image: none;		  
			}
			.rich-pmenu-top-group:hover
			{
			  font-weight: bold;
  			  font-family: Arial,Helvetica,sans-serif;
  			  font-size: 13px;
  			  color: #ffffff;
  			  background-color: #7171C6;
  			  background-image: none;		  
			}				
		</style>
	</head>	
	<body>
		<f:view>
			<div class="left-menu">
				<rich:panelMenu id="menuPpal" binding="#{sideMenuBean.panelMenu}"  
					topGroupStyle="font-family: 'Arial Black'; font-size: 13px;" selectedChild="#{sideMenuBean.itemSelected}"
					rendered="#{loginMBean.logged}" />
			</div>			
			<rich:modalPanel id="pnlslidemenu" width="350" height="115" showWhenRendered="#{sideMenuBean.showmodal}" onresize="return false;" moveable="false">
				<f:facet name="header">
					<h:panelGroup>
						<h:outputText value="Mensaje - .:Login:."/>
					</h:panelGroup>
				</f:facet>
				<f:facet name="controls">
					<h:panelGroup>
						<a4j:form>
							<h:commandButton action="#{sideMenuBean.logout}" image="/img/close.png" />	
						</a4j:form>
					</h:panelGroup>
				</f:facet>
				<h:outputText value="#{sideMenuBean.msgmodal}" styleClass="messageLabel"/>
				<br />
				<div align="center">
					<h:form>
						<h:commandButton action="#{sideMenuBean.logout}" value="Aceptar" />
					</h:form>
				</div>
			</rich:modalPanel>	
		</f:view>	
	</body>
</html>