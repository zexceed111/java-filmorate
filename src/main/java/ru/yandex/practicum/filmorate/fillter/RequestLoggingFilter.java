package ru.yandex.practicum.filmorate.fillter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class RequestLoggingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        log.debug("Incoming request: method={}, uri={}, headers={}", httpRequest.getMethod(), httpRequest.getRequestURI(), httpRequest.getHeaderNames());
        chain.doFilter(request, response);
    }
}
