package com.michael.reflect;

/**
 * Field
 */
public class Field {
    private boolean visible;
    private String defaultValue;
    private String cssClass;
    private String id;
    private String label;
    private String name;
    private String type;

    public Field(boolean visible, String defaultValue, String cssClass, String id, String label, String name, String type) {
        this.setVisible(visible);
        this.setDefaultValue(defaultValue);
        this.setCssClass(cssClass);
        this.setId(id);
        this.setLabel(label);
        this.setName(name);
        this.setType(type);
    }
    
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }
    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the visible
     */
    public boolean isVisible() {
        return visible;
    }
    /**
     * @param visible the visible to set
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * @return the cssClass
     */
    public String getCssClass() {
        return cssClass;
    }
    /**
     * @param cssClass the cssClass to set
     */
    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    /**
     * @return the defaultValue
     */
    public String getDefaultValue() {
        return defaultValue;
    }
    /**
     * @param defaultValue the defaultValue to set
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }
    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}