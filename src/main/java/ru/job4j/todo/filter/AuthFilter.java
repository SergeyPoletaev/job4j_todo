package ru.job4j.todo.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

import static ru.job4j.todo.enumeration.UnfilteredUri.LOGIN_PAGE;
import static ru.job4j.todo.enumeration.UnfilteredUri.values;

@Component
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        if (check(uri)) {
            chain.doFilter(req, res);
            return;
        }
        if (req.getSession().getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + LOGIN_PAGE.getValue());
            return;
        }
        chain.doFilter(req, res);
    }

    private boolean check(String uri) {
        return Arrays.stream(values())
                .anyMatch(inst -> uri.endsWith(inst.getValue()));
    }
}
