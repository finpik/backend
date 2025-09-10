package finpik.config.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ServerTimingFilter implements Filter {
    @Override public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws java.io.IOException, ServletException {
        long t0 = System.nanoTime();
        try {
            chain.doFilter(req, res);
        } finally {
            long totalMs = (System.nanoTime() - t0) / 1_000_000;
            ((HttpServletResponse)res).addHeader("Server-Timing", "app;desc=\"total\";dur=" + totalMs);
            HttpServletRequest r = (HttpServletRequest) req;
            log.info("[TIMING] {} {} -> {} ms", r.getMethod(), r.getRequestURI(), totalMs);
        }
    }
}
