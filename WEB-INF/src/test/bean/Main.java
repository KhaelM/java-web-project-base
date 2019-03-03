package test.bean;

import java.sql.Connection;

import com.michael.database.ConnectionManager;
import com.michael.database.GeneralizedFunction;

/**
 * Main
 */
public class Main {

    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = ConnectionManager.getOracleConnection("michael", "oracle4loser");
            GeneralizedFunction generalizedFunction = new GeneralizedFunction();
            Person person = new Person("Arnaud");
            person.setId("T21");
            person.setSomeAttribute("Loan");
            generalizedFunction.update(connection, person, "test_wannabe");
            
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch(Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}