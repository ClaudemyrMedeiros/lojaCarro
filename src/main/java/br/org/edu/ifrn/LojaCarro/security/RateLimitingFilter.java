package br.org.edu.ifrn.LojaCarro.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitingFilter.class);

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    private final int limit;
    private final long refillSeconds;

    public RateLimitingFilter(@Value("${app.rate.limit:60}") int limit,
                              @Value("${app.rate.refillSeconds:60}") long refillSeconds) {
        this.limit = limit;
        this.refillSeconds = refillSeconds;
    }

    private Bucket createBucket() {
        Refill refill = Refill.intervally(limit, Duration.ofSeconds(refillSeconds));
        Bandwidth limitBw = Bandwidth.classic(limit, refill);
        return Bucket4j.builder().addLimit(limitBw).build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String key = request.getRemoteAddr();
        Bucket bucket = buckets.computeIfAbsent(key, k -> createBucket());

        boolean consumed = bucket.tryConsume(1);
        if (!consumed) {
            logger.warn("Rate limit exceeded for key={}. Allowed={} per {}s", key, limit, refillSeconds);
            response.setStatus(429);
            response.getWriter().write("Too Many Requests");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
