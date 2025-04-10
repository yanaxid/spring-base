package com.base.spring.util;



import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MessageUtil {

   private final MessageSource messageSource;

   // --- 1. DEFAULT GET METHOD (EN locale) ---
   public String get(String code, Object... args) {
      LocaleContextHolder.setLocale(new Locale("en")); // force default locale to English
      Locale locale = LocaleContextHolder.getLocale();
      return getMessage(code, locale, args);
   }

   // --- 2. GET METHOD WITH CUSTOM LOCALE ---
   public String get(Locale locale, String code, Object... args) {
      return getMessage(code, locale, args);
   }

   // --- 3. PRIVATE HELPER METHOD ---
   private String getMessage(String code, Locale locale, Object... args) {
      return messageSource.getMessage(code, args, code, locale);
   }
}

