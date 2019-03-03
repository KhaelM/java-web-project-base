package com.michael.utility;

/**
 * TimeUtility
 */
public class TimeUtility {

    public static String formatDate(String date) throws Exception {
        /**
         * Format accepted:
         * YYYY-MM-DD => ENG format
         * DD-MM-YYYY => FR format
         * 
         */
        // Let's check if it contains a Date first
        String regexENG = "([0-9]{4}).([0-9]{2}).([0-9]{2})( )?([0-9]{2}.[0-9]{2}.[0-9]{2})?";
        String regexFR = "([0-9]{2}).([0-9]{2}).([0-9]{4})( )?([0-9]{2}.[0-9]{2}.[0-9]{2})?";
        String replacement = null;

        if(!date.matches(regexENG) && !date.matches(regexFR)) {
            return date;
        }
        
        if(date.matches(regexENG)) {
            replacement = "$1-$2-$3$4$5";
            return date.replaceAll(regexENG, replacement);
        } else {
            replacement = "$3-$2-$1$4$5";
            return date.replaceAll(regexFR, replacement);
        }
    }    
}