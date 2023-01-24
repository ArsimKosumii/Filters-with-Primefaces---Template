package filters;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter(dispatcherTypes = { 
		DispatcherType.ERROR, 
		DispatcherType.FORWARD }, urlPatterns = { "/error403.xhtml" })

public class ErrorFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		Principal p = req.getUserPrincipal();

		if (p != null) {
			if (req.isUserInRole("admin")) {
				res.sendRedirect(req.getContextPath() + "/admin/index.xhtml");
				return;
			} else if (req.isUserInRole("user")) {
				res.sendRedirect(req.getContextPath() + "/user/index.xhtml");
				return;
			}
		}

		chain.doFilter(request, response);
	}

}
