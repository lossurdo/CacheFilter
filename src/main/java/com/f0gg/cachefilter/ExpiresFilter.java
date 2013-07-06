package com.f0gg.cachefilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.apache.log4j.Logger;

/**
 * Filter controlling expiration
 *
 * Example of web.xml file:
 * 
 *    <filter>
 *        <filter-name>ExpiresFilter</filter-name>
 *        <filter-class>com.f0gg.cachefilter.ExpiresFilter</filter-class>
 *        <init-param>
 *            <param-name>nocache</param-name>
 *            <param-value>file1.js,file2.js</param-value>
 *        </init-param>
 *    </filter>
 *    <filter-mapping>
 *        <filter-name>ExpiresFilter</filter-name>
 *        <url-pattern>/*</url-pattern>
 *        <dispatcher>REQUEST</dispatcher>
 *        <dispatcher>FORWARD</dispatcher>
 *        <dispatcher>INCLUDE</dispatcher>
 *        <dispatcher>ERROR</dispatcher>
 *    </filter-mapping>
 * 
 * @author Rafael
 * @see http://github.com/f0gg
 */
public class ExpiresFilter implements Filter {

    private static final Logger logger = Logger.getLogger(ExpiresFilter.class);
    private List<String> params;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;

            boolean expire = false;
            for (String s : params) {
                if (httpRequest.getRequestURL().toString().endsWith(s)) {
                    logger.debug("Expiring " + httpRequest.getRequestURI());
                    expire = true;
                    break;
                }
            }

            if (expire) {
                logger.debug("Changing file Header: " + httpRequest.getRequestURI());
                chain.doFilter(httpRequest, new AddExpiresHeaderResponse(httpResponse));
            } else {
                chain.doFilter(request, response);
            }
        }
    }

    public void init(FilterConfig config) throws ServletException {
        logger.debug("Web.xml NOCACHE parameter: " + config.getInitParameter("nocache"));
        params = new ArrayList<String>();
        params.addAll(Arrays.asList(config.getInitParameter("nocache").split(",")));
    }

    public void destroy() {
        // nothing
    }
}

/**
 * Class used to expire browser file cache
 *
 * @author Rafael
 * @see http://github.com/f0gg
 */
class AddExpiresHeaderResponse extends HttpServletResponseWrapper {

    private static final Logger logger = Logger.getLogger(AddExpiresHeaderResponse.class);

    public AddExpiresHeaderResponse(HttpServletResponse response) {
        super(response);
    }

    @Override
    public void setContentType(String contentType) {
        super.setHeader("Expires", "-1");
        super.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        logger.debug("Header changed successfully");

        super.setContentType(contentType);
    }
}