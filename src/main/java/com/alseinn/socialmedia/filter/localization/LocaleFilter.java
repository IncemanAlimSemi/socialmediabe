package com.alseinn.socialmedia.filter.localization;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Locale;

@Component
public class LocaleFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String languageHeader = request.getHeader("Accept-Language");

        Locale locale = Locale.getDefault();
        if (languageHeader != null) {
            locale = Locale.forLanguageTag(languageHeader);
        }
        request.getSession().setAttribute("locale", locale);
        LocaleContextHolder.setLocale(locale);

        filterChain.doFilter(request, response);
    }

}
