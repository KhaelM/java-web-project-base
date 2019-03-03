package com.michael.database;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.michael.utility.StringUtility;

/**
 * Function
 */
public class GeneralizedFunction {

    private static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass == null) {
                throw e;
            }   else {
                return getField(superClass, fieldName);
            }
        }
    }

    public static List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<Field>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }

    public void update(Connection connection, Object object, String tableName) throws Exception {
        DatabaseMetaData meta = connection.getMetaData();
        String databaseProductName = meta.getDatabaseProductName();
        PreparedStatement preparedStatement = null;

        if(databaseProductName.compareToIgnoreCase("Oracle") == 0) {
            preparedStatement = connection.prepareStatement("ALTER SESSION SET NLS_TIMESTAMP_FORMAT = 'YYYY-MM-DD HH24:MI:SS.FF'");
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("ALTER SESSION SET NLS_DATE_FORMAT = 'YYYY-MM-DD'");
            preparedStatement.executeUpdate();     
        }

        ArrayList<String> primaryKeys = getPrimaryKeysColumn(tableName, connection);
        ArrayList<String> columnNames = getTableColumns(tableName, connection);

        for (int i = 0; i < columnNames.size(); i++) {
            columnNames.set(i, StringUtility.fromUnderscoreToCamelCase(columnNames.get(i)));
        }
        
        Class<?> baseClass = object.getClass();
        ArrayList<String> fieldsWithoutPrimary = new ArrayList<String>();
        Field[] fieldList = baseClass.getDeclaredFields();

        for (int i = 0; i < fieldList.length; i++) {
            fieldsWithoutPrimary.add(fieldList[i].getName());
        }

        com.michael.reflect.GeneralizedFunction.deleteCommonElement(fieldsWithoutPrimary, primaryKeys);
        List<String> commonFieldsList = com.michael.reflect.GeneralizedFunction.findCommonElements(fieldsWithoutPrimary.toArray(new String[0]), columnNames.toArray(new String[0])).stream().map(obj -> Objects.toString(obj, null)).collect(Collectors.toList());

        String request = "UPDATE " + tableName + " SET ";
        
        for(int i = 0; i < commonFieldsList.size(); i++) {
            request += StringUtility.fromCamelCaseToUnderscore(commonFieldsList.get(i)) + " = ?";
            
            if(i != commonFieldsList.size() - 1) {
                request += ", ";
            } else {
                request += " WHERE ";
            }
        }        

        for(int i = 0; i < primaryKeys.size(); i++) {
            request += primaryKeys.get(i) +" = ?";

            if(i != primaryKeys.size() - 1) {
                request += " AND ";
            }
        }
        
        preparedStatement = connection.prepareStatement(request);

        int index = 1;
        for(int i = 0; i < commonFieldsList.size(); i++) {
            Method get = baseClass.getDeclaredMethod("get" + StringUtility.firstUpper(commonFieldsList.get(i), false));
            preparedStatement.setObject(index, get.invoke(object));
            index++;
        }

        for (int i = 0; i < primaryKeys.size(); i++) {
            Method get = baseClass.getDeclaredMethod("get"+ StringUtility.fromUnderscoreToPascalCase(primaryKeys.get(i)));
            preparedStatement.setObject(index, get.invoke(object));
            index++;
        }
        
        System.out.println("Request = " + request);

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public Object[] select(String fullClassName, Connection connection, String table, String[] columns, String[] values)
            throws Exception {

        // Check if there is no connection to db
        if (connection == null) {
            throw new Exception("connection can't be null");
        }

        // check if one of the arg is null
        if ((columns == null && values != null) || (columns != null && values == null)) {
            throw new Exception("To each column must be associated a value");
        }

        // Find the matching class
        Class<?> baseClass = Class.forName(fullClassName);
        String request = "SELECT * FROM " + table;

        PreparedStatement preparedStatement = connection.prepareStatement(request, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE, ResultSet.HOLD_CURSORS_OVER_COMMIT);
        
        ResultSet resultSet = preparedStatement.executeQuery();
        int nbColumn = resultSet.getMetaData().getColumnCount();
        int nbRow = 0;

        if(resultSet.last()) {
            nbRow = resultSet.getRow();
        } else {
            return null;
        }

        Object[] finalObjects = new Object[nbRow];
        resultSet.beforeFirst();

        List<Field> allFields = getAllFields(baseClass);

        Field[] fields = allFields.toArray(new Field[0]);
        
        // fieldToString is the same as fields in String without the package and the
        // return type though
        String[] fieldToString = getOnlyFields(fields);
        String[] columnNames = new String[nbColumn];
        
        for (int i = 1; i <= nbColumn; i++) {
            columnNames[i-1] = StringUtility.fromUnderscoreToCamelCase(resultSet.getMetaData().getColumnName(i).toLowerCase());    
        }

        List<String> concernedField = com.michael.reflect.GeneralizedFunction.findCommonElements(fieldToString, columnNames).stream().map(object -> Objects.toString(object, null)).collect(Collectors.toList());

        int i = 0;
        while (resultSet.next()) {
            Object object = baseClass.getDeclaredConstructor().newInstance();
            baseClass.cast(object);
            for (int j = 0; j < concernedField.size(); j++) {
                Method method = baseClass.getMethod("set"+ StringUtility.firstUpper(concernedField.get(j), false), getField(baseClass,concernedField.get(j)).getType());
                String returnType = method.getGenericParameterTypes()[0].toString();

                if (returnType.compareTo("int") == 0 || returnType.compareTo("class java.lang.Integer") == 0) {
                    method.invoke(object, resultSet.getInt(StringUtility.fromCamelCaseToUnderscore(concernedField.get(j))));
                } else if (returnType.compareTo("float") == 0 || returnType.compareTo("class java.lang.Float") == 0) {
                    method.invoke(object, resultSet.getFloat(StringUtility.fromCamelCaseToUnderscore(concernedField.get(j))));
                } else if (returnType.compareTo("double") == 0 || returnType.compareTo("class java.lang.Double") == 0) {
                    method.invoke(object, resultSet.getDouble(StringUtility.fromCamelCaseToUnderscore(concernedField.get(j))));
                } else if(returnType.compareTo("class java.util.Date") == 0 || returnType.compareTo("class java.sql.Date") == 0) {
                    method.invoke(object, resultSet.getDate(StringUtility.fromCamelCaseToUnderscore(concernedField.get(j))));
                } else if(returnType.compareTo("class java.sql.Timestamp") == 0) {
                    method.invoke(object, resultSet.getTimestamp(StringUtility.fromCamelCaseToUnderscore(concernedField.get(j))));
                }else {
                    method.invoke(object, resultSet.getString(StringUtility.fromCamelCaseToUnderscore(concernedField.get(j))));
                }
            }
            finalObjects[i] = object;
            i++;
        }

        resultSet.close();
        preparedStatement.close();

        return finalObjects;
    }

    public String[] getOnlyFields(Field[] fields) {
        String[] strings = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            strings[i] = fields[i].getName();
        }
        return strings;
    }

    public void insert(Object object, String tableName, Connection connection) throws Exception {        
        if(tableName == null || tableName == "") {
            throw new Exception("Table name must be set.");
        }
        
        DatabaseMetaData meta = connection.getMetaData();
        String databaseProductName = meta.getDatabaseProductName();
        PreparedStatement preparedStatement = null;
        if(databaseProductName.compareToIgnoreCase("Oracle") == 0) {
            preparedStatement = connection.prepareStatement("ALTER SESSION SET NLS_TIMESTAMP_FORMAT = 'YYYY-MM-DD HH24:MI:SS.FF'");
            preparedStatement.execute();
            preparedStatement = connection.prepareStatement("ALTER SESSION SET NLS_DATE_FORMAT = 'YYYY-MM-DD'");
            preparedStatement.execute();     
        }
        
        Class<?> baseClass = object.getClass();
        ArrayList<String> primaryKeys = getPrimaryKeysColumn(tableName, connection);
        ArrayList<String> columnNames = getTableColumns(tableName, connection);

        for (int i = 0; i < columnNames.size(); i++) {
            columnNames.set(i, StringUtility.fromUnderscoreToCamelCase(columnNames.get(i)));
        }
        
        ArrayList<String> fieldsWithoutPrimary = new ArrayList<String>();
        Field[] fieldList = baseClass.getDeclaredFields();

        for (int i = 0; i < fieldList.length; i++) {
            fieldsWithoutPrimary.add(fieldList[i].getName());
        }

        com.michael.reflect.GeneralizedFunction.deleteCommonElement(fieldsWithoutPrimary, primaryKeys);
        List<String> commonFieldsList = com.michael.reflect.GeneralizedFunction.findCommonElements(fieldsWithoutPrimary.toArray(new String[0]), columnNames.toArray(new String[0])).stream().map(obj -> Objects.toString(obj, null)).collect(Collectors.toList());

        String request = "INSERT INTO " + tableName + " (";
        for (int i = 0; i < commonFieldsList.size(); i++) {
            if (i != commonFieldsList.size() - 1) {
                request += StringUtility.fromCamelCaseToUnderscore(commonFieldsList.get(i))+ ", ";
            } else {
                request +=StringUtility.fromCamelCaseToUnderscore(commonFieldsList.get(i)) + ") VALUES(";
            }
        }

        for (int i = 0; i < commonFieldsList.size(); i++) {
            if (i != commonFieldsList.size() - 1) {
                request += "?, ";
            } else {
                request +=  "?)";
            }
        }

        preparedStatement = connection.prepareStatement(request);
        Method method = null;

        for (int i = 0; i < commonFieldsList.size(); i++) {
            method = baseClass.getDeclaredMethod("get" + StringUtility.firstUpper(commonFieldsList.get(i), false));
            preparedStatement.setObject(i+1, method.invoke(object));
        }

        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public ArrayList<String> getTableColumns(String table, Connection connection) throws SQLException {
        ArrayList<String> columnNames = new ArrayList<String>();
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet resultSet = databaseMetaData.getColumns(null, null, table.toUpperCase(), null);

        while (resultSet.next()) {
            columnNames.add(resultSet.getString("COLUMN_NAME").toLowerCase());            
        }
        
        return columnNames;
    }

    public ArrayList<String> getPrimaryKeysColumn(String table, Connection connection) throws SQLException {
        ArrayList<String> primaryKeys = new ArrayList<String>();
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet resultSet = databaseMetaData.getPrimaryKeys(null, null, table.toUpperCase());

        while(resultSet.next()) {
            primaryKeys.add(resultSet.getString("COLUMN_NAME").toLowerCase());
        }

        return primaryKeys;
    }
}