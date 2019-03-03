/* MICHAEL RANDRIANARISONA n41 PROMO11 B */
package com.michael.reflect;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GeneralizedFunction {

    public String getHtml(Object object, String action, String method) throws Exception {
        String html = new String();    
        html = "<form action=\""+ action+"\" method=\""+method+"\">"; 
        Class<?> baseClass = object.getClass();
        java.lang.reflect.Field[] fields = baseClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            html+= "<div><label for=\""+fields[i].getName()+"\">"+fields[i].getName()+"</label><input type=\""+fields[i].getType()+"\" name=\""+fields[i].getName()+"\">"+"</div>";
        }
        html += "<input type=\"reset\" value=\"Reset\"><input type=\"submit\" value=\"ok\"></form>";
        return html;
    }

    public Object getObject(String fullClassName) throws Exception {
        /**
         * LOGIC:
         * - create a new instance of our class and return it
         */
        Class<?> baseClass = Class.forName(fullClassName);
        Object result = baseClass.getDeclaredConstructor().newInstance();
        return result;
    }

    // Fonction trier
    public static void sortAscending(Object[] objects, String attribute) throws Exception {
        boolean permutation = false;
        
        // Turning first letter of attribute to Uppercase
        String functionName = "get" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1);
        
        // Detect the type of the objects
        Class<?> clazz = objects[0].getClass(); // Now c represents the cpublic static void sortAscending(Object[] objects, String attribute) throws Exceptionlass of our objects
        
        // Detect the accessor which have acces to the attribute
        Method method = clazz.getDeclaredMethod(functionName);
        if(isNumber(clazz)) {

            do {
                permutation = false;
                for(int i = 0; i < objects.length - 1; ++i) {
                    if( (double) method.invoke(objects[i]) > (double)method.invoke(objects[i+1])) {
                        Object tempo = objects[i];
                        objects[i] = objects[i+1];
                        objects[i+1] = tempo;
                        permutation = true;
                    }
                }
            } while(permutation);
        }

        else {
            System.out.println("Données non numeriques");
        }
    }

    public static int[] trierCroissant(int[] liste){
        boolean permutation = false;
        int tempo = 0;
        do {
            // Tokony mlam
            permutation = false;
            for(int i = 0; i < liste.length-1; ++i){
                if(liste[i] > liste[i+1]) {
                    tempo = liste[i];
                    liste[i] = liste[i+1];
                    liste[i+1] = tempo;
                    permutation = true;
                    // Mkrotana
                }
            }
        } while(permutation);
        return liste;
    }

    public double sumAnythingByAttribute(Object[] objects, String attribute) throws Exception {
        double sum = 0;
        // Get the class
        Class<?> baseClass = objects[0].getClass();
        String functionName = "get" + attribute.substring(0,1).toUpperCase() + attribute.substring(1);
        Method function = baseClass.getMethod(functionName);
        // We check that the getter for the attribute return a number
        if(!isNumber(function.getReturnType())) {
            return -1;
        } 
        for (int i = 0; i < objects.length; i++) {
            sum += Double.parseDouble(function.invoke(objects[i]).toString()) ;
        }

        return sum;
    }

    public static boolean isNumber(Class<?> clazz) {
        return Number.class.isAssignableFrom(clazz)
        || clazz.toString().compareTo("int")==0
        || clazz.toString().compareTo("double")==0
        || clazz.toString().compareTo("float")==0
        || clazz.toString().compareTo("byte")==0
        || clazz.toString().compareTo("short")==0
        || clazz.toString().compareTo("long")==0;
    }

    // return source with elements that it doesn't have in common with array
    public static ArrayList<? extends Object> deleteCommonElement(ArrayList<? extends Object> source, ArrayList<? extends Object> array) {
        
        for (Object element : array) {
            Iterator<? extends Object> iterator = source.iterator();
            while (iterator.hasNext()) {
                Object object = iterator.next();
                if(object.equals(element)) {
                    iterator.remove();
                }
            }
        }
        
        return source;
    }

    public static List<Object> findCommonElements(Object[] array1, Object[] array2) {
        List<Object> commonElements = new ArrayList<Object>();

        for (int i = 0; i < array1.length; i++) {
            for (int j = 0; j < array2.length; j++) {
                if(array1[i].equals(array2[j])) {
                    if(!commonElements.contains(array1[i])) {
                        commonElements.add(array1[i]);
                    }
                }
            }
        }
        return commonElements;
    }

}
