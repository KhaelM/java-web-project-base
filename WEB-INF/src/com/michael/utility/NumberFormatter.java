package com.michael.utility;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Formatter
 */
public class NumberFormatter {

    public static String formatNumber(Number number, String integerSeparator, String separator, int precision) {
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(precision);
        String numberToString = df.format(number);
        String[] splitted = numberToString.split("[.]");
        String integerString = splitted[0];
        int counter = 3;
        String numberFormattedReversed = new String();

        for (int i = splitted[0].length(); i > 0; i--) {
            if(counter == 0) {
                counter = 3;
                numberFormattedReversed += integerSeparator;
            }
            numberFormattedReversed += integerString.charAt(i-1);
            counter--;
        }

        String numberFormatted = new String();

        for (int i = numberFormattedReversed.length(); i > 0; i--) {
            numberFormatted += numberFormattedReversed.charAt(i-1);
        }
        
        if(splitted.length == 2) {
            numberFormatted += separator;
            String decimalString = splitted[1];
            numberFormatted += decimalString;
        }

        return numberFormatted;
    }

    public String toFrench(Double decimal) {
        /*
        LOGIC:
        1) Split decimal and integer part
        2) treat integer part
        3) treat decimal part
        4) concatenate both part 
        */
        String result = null;
        // 1) Split decimal and integer part
        int[] splittedByComa = splitDecimalAndInt(decimal);
        // 2) treat integer part
        int[] integerParts = splitInNumberCategory(splittedByComa[0]);
        String integerPartsToString = convertToString(integerParts).trim();
        // 3) treat decimal part
        int[] decimalParts = splitInNumberCategory(splittedByComa[1]);
        String decimalPartsToString = convertToString(decimalParts).trim();
        // 4) concatenate both part         
        result = integerPartsToString;
        if(decimalPartsToString.compareTo("") != 0) {
            result += " virgule " + decimalPartsToString;
        }
        
        return result;
    }

    // Convert an array of integer to String
    public String convertToString(int[] numbersSplittedByCategory) {
        String result = new String();
        String[] allNumberToString = getAllNumbersFromZeroToHundred();
        int[] hundredAndTens;
        // billion part
        if(numbersSplittedByCategory[0] != 0) {
            hundredAndTens = splitHundredFromTens(numbersSplittedByCategory[0]);
            if(hundredAndTens[0] != 0) {
                if(hundredAndTens[0] == 1) {
                    result += " cent ";
                } else {
                    result += allNumberToString[ hundredAndTens[0] ] + " cent ";
                }
            }
            if(hundredAndTens[1] !=0) {
                result += allNumberToString[ hundredAndTens[1] ] + " ";
            }
            result += "milliard ";
        }
        // million part
        if(numbersSplittedByCategory[1] != 0) {
            hundredAndTens = splitHundredFromTens(numbersSplittedByCategory[1]);
            if(hundredAndTens[0] != 0) {
                if(hundredAndTens[0] == 1) {
                    result += " cent ";
                } else {
                    result += allNumberToString[ hundredAndTens[0] ] + " cent ";
                }
            }
            if(hundredAndTens[1] !=0) {
                result += allNumberToString[ hundredAndTens[1] ] + " ";
            }
            result += "million ";
        }
        // thousand part
        if(numbersSplittedByCategory[2] != 0) {
            hundredAndTens = splitHundredFromTens(numbersSplittedByCategory[2]);
            if(hundredAndTens[0] != 0) {
                if(hundredAndTens[0] == 1) {
                    result += " cent ";
                } else {
                    result += allNumberToString[ hundredAndTens[0] ] + " cent ";
                }
            }
            if(hundredAndTens[1] !=0) {
                result += allNumberToString[ hundredAndTens[1] ] + " ";
            }
            result += "mille ";
        }
        // hundred part
        if(numbersSplittedByCategory[3] != 0) {
            hundredAndTens = splitHundredFromTens(numbersSplittedByCategory[3]);
            if(hundredAndTens[0] != 0) {
                if(hundredAndTens[0] == 1) {
                    result += " cent ";
                } else {
                    result += allNumberToString[ hundredAndTens[0] ] + " cent ";
                }
            }
            if(hundredAndTens[1] !=0) {
                result += allNumberToString[ hundredAndTens[1] ] + " ";
            }
        }
        return result;
    }

    public int[] splitHundredFromTens(int number) {
        int[] result = new int[2];
        result[0] = number/100;
        result[1] = number-(result[0]*100);
        return result;
    }

    // split a number in his category number => eg: 125 455 966 => [125] for million [455] for thousands [966] for unit
    // here we will limit it to billion
    public int[] splitInNumberCategory(long someNumber) {
        int[] result = new int[4];
        // billion part
        int billionPart =(int) someNumber/1000000000;
        result[0] = billionPart;
        // million part
        int tempoMillion =(int) (someNumber-(billionPart*1000000000));
        int millionPart = tempoMillion/1000000;
        result[1] = millionPart;
        // thousand part
        int tempoThousand =(int) (someNumber-(millionPart*1000000)+(billionPart*1000000000));
        int thousandPart = tempoThousand/1000;
        result[2] = thousandPart;
        // hundred part
        int hundredPart =(int) someNumber - ( (billionPart*1000000000) + (millionPart*1000000) + (thousandPart*1000) );
        result[3] = hundredPart;
        
        return result;
    }

    public int[] splitDecimalAndInt(double number) {
        // Avoid exponential form
        System.out.println(number);
        String nbToString = BigDecimal.valueOf(number).toPlainString();
        String[] splittedByComa = nbToString.split("[.]");
        int integerPart = Integer.parseInt(splittedByComa[0]);
        System.out.println("sp0:"+splittedByComa[0]);
        int decimalPart;
        int result[] = new int[2];
        result[0] = integerPart;
        result[1] = 0;
        if(splittedByComa.length == 2) {
            System.out.println("sp1:"+splittedByComa[1]);
            String tempo = splittedByComa[1];
            while(tempo.lastIndexOf('0') != -1) {
                tempo = tempo.substring(0, tempo.length()-1);
            }
            if(tempo.compareTo("") != 0) {
                decimalPart = Integer.parseInt(tempo);
            } else {
                decimalPart = 0;
            }
            result[1] = decimalPart;
        }
        return result;
    }

    public String[] getAllNumbersFromZeroToHundred() {
        String[] numbers = new String[101];
        numbers[0]="zero";
        numbers[1]="un";
        numbers[2]="deux";
        numbers[3]="trois";
        numbers[4]="quatre";
        numbers[5]="cinq";
        numbers[6]="six";
        numbers[7]="sept";
        numbers[8]="huit";
        numbers[9]="neuf";
        numbers[10]="dix";
        numbers[11]="onze";
        numbers[12]="douze";
        numbers[13]="treize";
        numbers[14]="quatorze";
        numbers[15]="quinze";
        numbers[16]="seize";
        numbers[17]="dix-sept";
        numbers[18]="dix-huit";
        numbers[19]="dix-neuf";
        numbers[20]="vingt";
        numbers[21]="vingt-et-un";
        numbers[22]="vingt-deux";
        numbers[23]="vingt-trois";
        numbers[24]="vingt-quatre";
        numbers[25]="vingt-cinq";
        numbers[26]="vingt-six";
        numbers[27]="vingt-sept";
        numbers[28]="vingt-huit";
        numbers[29]="vingt-neuf";
        numbers[30]="trente";
        numbers[31]="trente-et-un";
        numbers[32]="trente-deux";
        numbers[33]="trente-trois";
        numbers[34]="trente-quatre";
        numbers[35]="trente-cinq";
        numbers[36]="trente-six";
        numbers[37]="trente-sept";
        numbers[38]="trente-huit";
        numbers[39]="trente-neuf";
        numbers[40]="quarante";
        numbers[41]="quarante-et-un";
        numbers[42]="quarante-deux";
        numbers[43]="quarante-trois";
        numbers[44]="quarante-quatre";
        numbers[45]="quarante-cinq";
        numbers[46]="quarante-six";
        numbers[47]="quarante-sept";
        numbers[48]="quarante-huit";
        numbers[49]="quarante-neuf";
        numbers[50]="cinquante";
        numbers[51]="cinquante-et-un";
        numbers[52]="cinquante-deux";
        numbers[53]="cinquante-trois";
        numbers[54]="cinquante-quatre";
        numbers[55]="cinquante-cinq";
        numbers[56]="cinquante-six";
        numbers[57]="cinquante-sept";
        numbers[58]="cinquante-huit";
        numbers[59]="cinquante-neuf";
        numbers[60]="soixante";
        numbers[61]="soixante-et-un";
        numbers[62]="soixante-deux";
        numbers[63]="soixante-trois";
        numbers[64]="soixante-quatre";
        numbers[65]="soixante-cinq";
        numbers[66]="soixante-six";
        numbers[67]="soixante-sept";
        numbers[68]="soixante-huit";
        numbers[69]="soixante-neuf";
        numbers[70]="soixante dix";
        numbers[71]="soixante onze";
        numbers[72]="soixante douze";
        numbers[73]="soixante treize";
        numbers[74]="soixante quatorze";
        numbers[75]="soixante quinze";
        numbers[76]="soixante seize";
        numbers[77]="soixante dix-sept";
        numbers[78]="soixante dix-huit";
        numbers[79]="soixante dix-neuf";
        numbers[80]="quatre vingt";
        numbers[81]="quatre vingt-et-un";
        numbers[82]="quatre vingt-deux";
        numbers[83]="quatre vingt-trois";
        numbers[84]="quatre vingt-quatre";
        numbers[85]="quatre vingt-cinq";
        numbers[86]="quatre vingt-six";
        numbers[87]="quatre vingt-sept";
        numbers[88]="quatre vingt-huit";
        numbers[89]="quatre vingt-neuf";
        numbers[90]="quatre vingt dix";
        numbers[91]="quatre vingt onze";
        numbers[92]="quatre vingt douze";
        numbers[93]="quatre vingt treize";
        numbers[94]="quatre vingt quatorze"; // 22 
        numbers[95]="quatre vingt quinze";
        numbers[96]="quatre vingt seize";
        numbers[97]="quatre vingt dix-sept";
        numbers[98]="quatre vingt dix-huit";
        numbers[99]="quatre vingt dix-neuf";
        numbers[100]="cent";
        return numbers;
    }
}