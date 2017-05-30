<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns     = "http://www.w3.org/1999/xhtml" 
	  xmlns:jsp = "http://java.sun.com/JSP/Page"
	  xmlns:f   = "http://java.sun.com/jsf/core"
	  xmlns:h   = "http://java.sun.com/jsf/html" 
	  xmlns:ui  = "http://java.sun.com/jsf/facelets"
	  xmlns:rich= "http://richfaces.org/rich"
	  xmlns:a4j = "http://richfaces.org/a4j">
	<head>
		<style type="text/css">
			.loginLabel{font: Arial, Helvetica, sans-serif; font-size:12px; color:#ffffff;}
			
		</style>
		<script type="text/javascript">
		 var sessUser = "#{sessionScope['user']}";
         var host = document.location.host;   
         var prot = document.location.protocol;
		  if(history.forward(1)){
			  if(sessUser==""){
				  location.replace(prot+"//"+host+"/web.bo.peajes.com/");
			  }else{
				  alert(history.forward(1));
				  location.replace(history.forward(1));  
			  }		    
		  }
		</script>
	</head>
	<body>  
		<f:view>
			<div class="navbar">
			<div style="width: 300px;height: 100px; position: absolute; top: 0; left: 50; opacity:0; filter: alpha(opacity=0);-moz-opacity:0;">
				<a href="http://www.facilpass.com" target="_blank">
				<img id="layer3" alt="Facilpass" src="/web.bo.peajes.com/img/login.png" height="100px" width="300px" /></a> 
			</div>
				<div class="span-24" style="height:125px">  
					<div class="span-24" style="height:20px" >&nbsp;</div>
					<div class="span-12" align="left" style="margin-left:70%">					
						<h:form id="logged" rendered="#{loginMBean.logged}">
							<h:outputLabel value="Bienvenido(a) #{loginMBean.userName} #{loginMBean.lastName}" styleClass="loginLabel"/>
							<br/>
							<h:panelGroup rendered="#{loginMBean.showClientMaster}">
								<h:outputLabel value="Cliente #{loginMBean.userMasterName} #{loginMBean.lastMasterName}" styleClass="loginLabel"/>
								<br/>
							</h:panelGroup>
							<h:outputLabel value="Último Ingreso#{loginMBean.lastEntry}" styleClass="loginLabel" style="font-weight:normal;"/>
							<br/>			
							<h:commandLink value="Salir" action="#{loginMBean.executeRedirect}" styleClass="link" style="font-weight:bold; font-size:12px;"/>								
						</h:form>
					</div>   
				</div>   
			</div>		
		</f:view>
	</body>
</html>