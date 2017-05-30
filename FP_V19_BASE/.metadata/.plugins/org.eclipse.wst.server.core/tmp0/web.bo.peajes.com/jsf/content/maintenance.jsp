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
<script charset="utf-8" src="http://widgets.twimg.com/j/2/widget.js"></script>
</head>
<body>
<f:view>
	<div>	  
	    <div class="span-23">
<div class="span-23" align="left">
<h:form styleClass="white">
</h:form>
</div>
	     <div class="span-7">
<br /><br />
<script>
new TWTR.Widget({
  version: 2,
  type: 'profile',
  rpp: 4,
  interval: 30000,
  width: 250,
  height: 300,
  theme: {
    shell: {
      background: '#333333',
      color: '#ffffff'
    },
    tweets: {
      background: '#000000',
      color: '#ffffff',
      links: '#ebeb07'
    }
  },
  features: {
    scrollbar: false,
    loop: false,
    live: false,
    behavior: 'all'
  }
}).render().setUser('viarapidapeajes').start();
</script>
	     </div>
<div class="span-16 last" align="center">
	       <h:form styleClass="white">	
				<h:outputText value="Actualmente Nos Encontramos en Mantenimiento" styleClass="bold" />
				<rich:spacer height="10"/>
				<h:outputText value="  Disculpe las Molestias" styleClass="bold"/>
		  </h:form>
	     </div>		     
	  </div>
	</div>
</f:view>
</body>
</html>
				
				
		  