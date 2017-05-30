<?xml version="1.0" encoding="ISO-8859-1" ?>
 <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns     = "http://www.w3.org/1999/xhtml" 
	  xmlns:jsp = "http://java.sun.com/JSP/Page"
	  xmlns:f   = "http://java.sun.com/jsf/core"
	  xmlns:h   = "http://java.sun.com/jsf/html" 
	  xmlns:ui  = "http://java.sun.com/jsf/facelets"
	  xmlns:rich= "http://richfaces.org/rich">

	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7; IE=EmulateIE8"></meta>
		<style type="text/css">
			.left-menu {display: block;width: 190px; margin-top: 2px;}
			.rich-pmenu-selected-element { background-color: #DFFF9D; color: #000000; }
			.rich-pmenu-group { font-weight:normal; }	 		
		</style>
	</head>	
	<body>
		<f:view>
			<div class="left-menu">
				<rich:panelMenu id="menuPpalPrev" binding="#{previewMenuBean.panelMenuPrev}" 
					topGroupStyle="font-family: 'Arial Black'; font-size: 11px;" selectedChild="#{previewMenuBean.itemSelectedPrev}"/>
			</div>
		</f:view>
	</body>
</html>