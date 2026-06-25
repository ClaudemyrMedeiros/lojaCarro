package br.org.edu.ifrn.LojaCarro.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private static class Window {
        long windowStart;
        int count;
    }

    private final Map<String, Window> windows = new ConcurrentHashMap<>();

    private final int limit;
    private final long windowMillis;

    public RateLimitingFilter(@Value("${app.rate.limit:60}") int limit,
                              @Value("${app.rate.windowMillis:60000}") long windowMillis) {
        this.limit = limit;
        this.windowMillis = windowMillis;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String key = request.getRemoteAddr();
        long now = System.currentTimeMillis();
        Window w = windows.computeIfAbsent(key, k -> {
            Window nw = new Window(); nw.windowStart = now; nw.count = 0; return nw;
        });

        synchronized (w) {
            if (now - w.windowStart >= windowMillis) {
                w.windowStart = now;
                w.count = 0;
            }
            if (w.count >= limit) {
                response.setStatus(429);
                response.getWriter().write("Too Many Requests");
                return;
            }
            w.count++;
        }

        filterChain.doFilter(request, response);
    }
}
