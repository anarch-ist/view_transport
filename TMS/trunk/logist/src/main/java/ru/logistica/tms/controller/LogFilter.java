package ru.logistica.tms.controller;

import javax.servlet.*;
import java.io.IOException;

public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        throw new IllegalStateException("not implemented");
    }

    @Override
    public void destroy() {
        throw new IllegalStateException("not implemented");
    }
}
