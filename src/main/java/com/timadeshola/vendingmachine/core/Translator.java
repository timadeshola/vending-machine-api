package com.timadeshola.vendingmachine.core;

import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/17/21
 * Time: 7:51 AM
 */
@Service
@RequiredArgsConstructor
public class Translator {

    private final ReloadableResourceBundleMessageSource messageSource;

    public String toLocale(String msgCode, Object... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(msgCode, args, locale);
    }

    public String toLocale(String msgCode) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(msgCode, null, locale);
    }

}