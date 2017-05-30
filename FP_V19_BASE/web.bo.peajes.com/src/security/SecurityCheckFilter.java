/**
 * 
 */
package security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import constant.LogAction;
import constant.LogReference;

/**
 * 
 * @author tmolina
 */
public class SecurityCheckFilter implements Filter{
	
	private final static String FILTER_APPLIED = "_security_filter_applied";
	
	@EJB(mappedName="ejb/Log")
	private ejb.Log log;

	public SecurityCheckFilter() {
	}
	 
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}
	 
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	throws IOException, ServletException {
    
		HttpServletRequest hreq = (HttpServletRequest)request;
		HttpServletResponse hres = (HttpServletResponse)response;
		HttpSession session = hreq.getSession();
		hres.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
		hres.setHeader("Pragma", "no-cache"); // HTTP 1.0
		hres.setDateHeader("Expires", 0); // Proxies.
		
		String checkforloginpage = hreq.getPathTranslated();
		String urlInfo = hreq.getPathInfo();
	
		// Don't filter index.jspx because otherwise an endless loop.
		// & only filter .jspx/.jsp otherwise it will filter all images etc as well.
	//	System.out.println(checkforloginpage);
		if ((request.getAttribute(FILTER_APPLIED) == null)
						&& (!checkforloginpage.contains("index.jspx")
						&& !checkforloginpage.contains("formInsClienOutside.jspx")
						&& !checkforloginpage.contains("changePasswordFirst.jspx")
						&& !checkforloginpage.contains("changePasswordPreenrollUser.jspx")
						&& !checkforloginpage.contains("infoContactInside.jspx")
						&& !checkforloginpage.contains("infoContact.jspx"))
						&& !checkforloginpage.contains("userValidation.jspx")
						&& !checkforloginpage.contains("verificator.jspx")
						&& !checkforloginpage.contains("login.jspx")
						&& !checkforloginpage.contains("securityQuestionsNew.jspx")
						&& !checkforloginpage.contains("passwordReset.jspx")
						&& ((!checkforloginpage.contains("Step.jspx")
						&& !checkforloginpage.contains("taskWizard.jspx")
						&& checkforloginpage.endsWith(".jspx"))
						|| checkforloginpage.endsWith(".jsp"))) {
			
			request.setAttribute(FILTER_APPLIED, Boolean.TRUE);	
		 
			 // If the session bean is not null get the session bean property userName and the list of permission for that user.
			String user= null;
			Long userId = null;
			String ip = null;
			
			// List of permissions
			List<String> permissionURLs = new ArrayList<String>();
			
			 if(((mBeans.LoginMBean)session.getAttribute("loginMBean"))!=null) {     
				 mBeans.LoginMBean login = ((mBeans.LoginMBean)session.getAttribute("loginMBean"));
				 user = login.getUserName();
				 permissionURLs = login.getPermissionPageList();
				 userId = login.getUserId();
				 ip = login.getIp();
			}
			
			if((user==null)||(user.equals(""))) {
				System.out.println("User is not logged anymore with the session.");
				hres.sendRedirect("/web.bo.peajes.com/peajes/index.jspx");
			    return;
			}
			
			// We check if the page the user is trying to access is on his permission URLs List
			boolean isURL = false;
			for(String url: permissionURLs){
				if(url.equals(urlInfo)){
					isURL = true;
					break;
				}
			}
			
			// If the user doesn't have permission to see a page, redirect to home page.
			if(!isURL){
				System.out.println("User doesn't have permission to see this page.");
				// Log to register the user attempt to enter the page.
				log.insertLog(
						"El usuario está tratando de acceder a la página: " + urlInfo,
						LogReference.PERMISSION, LogAction.ACCESSDENIED, ip, userId );
				hres.sendRedirect("/web.bo.peajes.com/peajes/jsf/home.jsp");
			    return;
			}
		} else if((request.getAttribute(FILTER_APPLIED) == null)
				&& (checkforloginpage.matches("^.*Step\\.jspx$") 
					|| checkforloginpage.contains("taskWizard.jspx"))){
			
			request.setAttribute(FILTER_APPLIED, Boolean.TRUE);	
			 
			 // If the session bean is not null get the session bean property userName and the list of permission for that user.
			String user= null;
			
			
			if(((mBeans.LoginMBean)session.getAttribute("loginMBean"))!=null) {     
				 mBeans.LoginMBean login = ((mBeans.LoginMBean)session.getAttribute("loginMBean"));
				 user = login.getUserName();
			}
			
			if((user==null)||(user.equals(""))) {
				System.out.println("User is not logged anymore with the session.");
				hres.sendRedirect("/web.bo.peajes.com/peajes/index.jspx");
			    return;
			}
		}
		
		// Deliver request to next filter 
		chain.doFilter(request, response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
	}
}