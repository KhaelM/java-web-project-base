package com.michael.reflect;

import com.michael.reflect.exception.FormException;

/**
 * Form
 */
public class Form {
    private Field[] fields;
    private String fullClassName;
    private String method;
    private String action;


    public Form(Object o, String method, String action) throws Exception {
        this.setMethod(method);
        this.setAction(action);
        this.setFields(o);
        this.setFullClassName(o.getClass().getName());
    }

    public Field getField(String fieldName) throws FormException {
        Field field = null;
        for (int i = 0; i < fields.length; i++) {
            if(this.getFields()[i].getName().compareTo(fieldName) == 0) {
                field = this.getFields()[i];
            }
        }
        if(field == null) {
            throw new FormException("field doesn't exist");
        }
        return field;
    }

    public String getHtml() {
        String html = new String();
        html += "<form action=\""+this.getAction()+"\" method=\""+this.getMethod()+"\" >";
        for (int i = 0; i < this.getFields().length; i++) {
            if(this.getFields()[i].isVisible()) {
                html += "<div><label for=\""+this.getFields()[i].getName()+"\">"+this.getFields()[i].getLabel()+"</label><input type=\""+this.getFields()[i].getType()+"\" step=\"any\" name=\""+this.getFields()[i].getName()+"\" ></div>";
            }
        }
        html += "<input type=\"hidden\" name=\"fullClassName\" value=\""+this.getFullClassName()+"\"><input type=\"reset\" value=\"reset\" ><input type=\"submit\" value=\"ok\" ></form>";
        return html;
    }
    

    public void setFields(Object o) throws Exception {
        /**
         * LOGIC:
         * - for each attribute of the object we create a Field
         */
        Class<?> baseClass = o.getClass();
        java.lang.reflect.Field[] allFields = baseClass.getDeclaredFields();
        Field[] result = new Field[allFields.length];
        Field tempo = null;
        for (int i = 0; i < allFields.length; i++) {
            if(GeneralizedFunction.isNumber(allFields[i].getType()) ) {
                tempo = new Field(true, "", "", "", allFields[i].getName(), allFields[i].getName(), "number");
            } else {
                tempo = new Field(true, "", "", "", allFields[i].getName(), allFields[i].getName(), "text");
            }
            result[i] = tempo;
        }
        this.setFields(result);
    }

    /**
     * @return the action
     */
    public String getAction() {
        return action;
    }
    /**
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * @return the method
     */
    public String getMethod() {
        return method;
    }
    /**
     * @param method the method to set
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * @return the fields
     */
    public Field[] getFields() {
        return fields;
    }
    /**
     * @param fields the fields to set
     */
    public void setFields(Field[] fields) {
        this.fields = fields;
    }

    /**
     * @return the fullClassName
     */
    public String getFullClassName() {
        return fullClassName;
    }
    /**
     * @param fullClassName the fullClassName to set
     */
    public void setFullClassName(String fullClassName) {
        this.fullClassName = fullClassName;
    }
}