package com.timadeshola.vendingmachine.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 12:56 PM
 */
public class AppConstant {

    public interface DateFormatters {
        public SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        public static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    }

    public interface RestMessage {
        public static final String success = "Success";
        public static final String pending = "Pending";
    }
}