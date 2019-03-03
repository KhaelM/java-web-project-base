package test.bean;

import java.sql.Date;
import java.sql.Timestamp;


/**
 * Person
 */
public class Person {

    private String id;
    private String someAttribute;
    private int someIntegerAttribute;
    private Timestamp timestampTest;
    private Date dateTest;
    private String oneMore;

    public Person(){}

    public Person(String someAttribute) {
        this.setSomeAttribute(someAttribute);
    }

    /**
     * @return the oneMore
     */
    public String getOneMore() {
        return oneMore;
    }
    /**
     * @param oneMore the oneMore to set
     */
    public void setOneMore(String oneMore) {
        this.oneMore = oneMore;
    }

    /**
     * @return the dateTest
     */
    public Date getDateTest() {
        return dateTest;
    }
    /**
     * @param dateTest the dateTest to set
     */
    public void setDateTest(Date dateTest) {
        this.dateTest = dateTest;
    }

    /**
     * @return the timestampTest
     */
    public Timestamp getTimestampTest() {
        return timestampTest;
    }
    /**
     * @param timestampTest the timestampTest to set
     */
    public void setTimestampTest(Timestamp timestampTest) {
        this.timestampTest = timestampTest;
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
     * @return the someAttribute
     */
    public String getSomeAttribute() {
        return someAttribute;
    }
    /**
     * @param someAttribute the someAttribute to set
     */
    public void setSomeAttribute(String someAttribute) {
        this.someAttribute = someAttribute;
    }

    /**
     * @return the someIntegerAttribute
     */
    public int getSomeIntegerAttribute() {
        return someIntegerAttribute;
    }
    /**
     * @param someIntegerAttribute the someIntegerAttribute to set
     */
    public void setSomeIntegerAttribute(int someIntegerAttribute) {
        this.someIntegerAttribute = someIntegerAttribute;
    }
}