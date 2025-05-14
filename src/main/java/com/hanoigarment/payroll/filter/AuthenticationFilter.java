package com.hanoigarment.payroll.filter;

import com.hanoigarment.payroll.config.HostConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class AuthenticationFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
    private final HostConfig config;

    public AuthenticationFilter(HostConfig config) {
        this.config = config;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
            logger.info("Request URI: {}", httpRequest.getRequestURI());

            // Cho phép qua một số endpoint cụ thể không cần xác thực
            if (httpRequest.getRequestURI().endsWith("/statistic-service/v1.0/average")
                    || httpRequest.getRequestURI().endsWith("/statistic-service/v1.0/meetings")
                    ) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            // TODO: Bổ sung xác thực JWT tại đây nếu cần
            // Authentication authentication = TokenJwtUtil.getAuthentication(httpRequest, config);
            // if (authentication == null) {
            //     ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            //     return;
            // }
            // SecurityContextHolder.getContext().setAuthentication(authentication);
            // filterChain.doFilter(servletRequest, servletResponse);

            // Nếu chưa xử lý xác thực, tạm cho phép qua để tránh chặn (tùy chỉnh sau)
            filterChain.doFilter(servletRequest, servletResponse);

        } catch (Exception e) {
            logger.error("Error in AuthenticationFilter", e);
            String message = e.toString();
            if (message.toLowerCase().contains("jwt")) {
                ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                throw e;
            }
        }
    }
}
