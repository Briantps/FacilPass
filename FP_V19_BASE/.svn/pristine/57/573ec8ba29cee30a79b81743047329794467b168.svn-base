/**
 * 
 */
package ejb.taskeng;

import java.io.IOException;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 
 * @author tmolina
 */
public class TaskEngineFilter implements Filter{
	
	@SuppressWarnings("unused")
	private final static String FILTER_APPLIED = "_taskengine_filter_applied";
	@SuppressWarnings("unused")
	private InitialContext context;
	
	@EJB(mappedName = "ejb/TaskEngine")
	private TaskEngine taskEng;
	

	public TaskEngineFilter() {
		try {
			context = new InitialContext();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	 
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("Task filter init");
		taskEng.init();
		//taskEng.execute(2L, 1, "Mensaje de prueba", 2250L);
	}
	 
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	throws IOException, ServletException {
		
		// Deliver request to next filter 
		//System.out.println("request: "+request);
		//System.out.println("response: "+response);
		chain.doFilter(request, response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
	}
}