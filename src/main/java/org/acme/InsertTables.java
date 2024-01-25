package org.acme;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Random;
import java.util.UUID;

import org.eclipse.microprofile.config.ConfigProvider;

import java.util.Date;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class InsertTables {
   
    private static final String credentailData = "{\"hashIterations\":27500,\"algorithm\":\"pbkdf2-sha256\",\"additionalParameters\":{}}";
    //private static final String userPassword = "123456";
    private String realmID = null;
    private String defaultRoleID = null;
    private Connection connection = null;
    //private static String salt = null;
    //private static String hash = null;

    private static final String DB_URL = "config.datasource.url";
    private static final String DB_USER = "config.datasource.username";
    private static final String DB_PASS = "config.datasource.password";
    private static final String REALM_ID = "config.keycloak.realm";

    private String dbUrl;
    private String dbUser;
    private String dbPass;
    private String realmName;

    public InsertTables() throws SQLException, ClassNotFoundException {

        dbUrl = ConfigProvider.getConfig().getValue(DB_URL, String.class);
        dbUser = ConfigProvider.getConfig().getValue(DB_USER, String.class);
        dbPass = ConfigProvider.getConfig().getValue(DB_PASS, String.class);
        realmName = ConfigProvider.getConfig().getValue(REALM_ID, String.class);

        // System.out.println("dbUrl: " + dbUrl);
        // System.out.println("dbUser: " + dbUser);
        // System.out.println("dbPass: " + dbPass);
        // System.out.println("realmName: " + realmName);

        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        
        connection = getConnection();


        // String [] values = PBKDF2.hashPassword(userPassword);
        // salt = values[0];
        // hash = values[1];
        
        realmID = getRealmID();
        defaultRoleID = getDefaultRoleID();
    }


    private Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
        conn.setAutoCommit(false);
        
        return conn;
    }

    private String generateID() {
        return UUID.randomUUID().toString();
    }

    private String generateName() {
        Random random = new Random();
        StringBuilder nome = new StringBuilder();

        int comprimentoDoNome = 8;

        for (int i = 0; i < comprimentoDoNome; i++) {
            char letra = (char) (random.nextBoolean() ? 'A' + random.nextInt(26) : 'a' + random.nextInt(26));
            nome.append(letra);
        }

        return nome.toString();
    }

    private String sqlInsertIntoUserEntity() {
        StringBuilder sql = new StringBuilder();

        sql.append("insert ")
            .append("into ")
            .append("USER_ENTITY ")
            .append("(CREATED_TIMESTAMP, EMAIL, EMAIL_CONSTRAINT, EMAIL_VERIFIED, ENABLED, FEDERATION_LINK, FIRST_NAME, LAST_NAME, NOT_BEFORE, REALM_ID, SERVICE_ACCOUNT_CLIENT_LINK, USERNAME, ID) ")
            .append("values ")
            .append("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        return sql.toString();
    }

    private String sqlInsertIntoUserRoleMapping() {
        StringBuilder sql = new StringBuilder();

        sql.append("insert ")
            .append("into ")
            .append("USER_ROLE_MAPPING ")
            .append("(ROLE_ID, USER_ID) ")
            .append("values ")
            .append("(?, ?)");

        return sql.toString();
    }

    private String sqlInsertIntoCredencial() {
        StringBuilder sql = new StringBuilder();

        sql.append("insert ")
            .append("into ")
            .append("CREDENTIAL ")
            .append("(CREATED_DATE, CREDENTIAL_DATA, PRIORITY, SALT, SECRET_DATA, TYPE, USER_ID, USER_LABEL, ID) ")
            .append("values ")
            .append("(?, ?, ?, ?, ?, ?, ?, ?, ?)");

        return sql.toString();
    }

    private String sqlRealmID() {

        String sql = "SELECT ID FROM REALM where name = ?";

        return sql;
    }


    private String getRealmID() throws SQLException {

        if(connection == null || connection.isClosed()) {
            connection = getConnection();
        }

        PreparedStatement stmt = connection.prepareStatement(sqlRealmID());
        ResultSet rs = null;
        String realmID = null;

        try {
            stmt.setString(1, realmName);

            rs = stmt.executeQuery();
            rs.next();

            realmID = rs.getString("ID");

        } finally {
            stmt.close();
            
            if(rs != null) {
                rs.close();
            }

        }

        return realmID;
    }

    private String getDefaultRoleID() throws SQLException {
        String sql = "SELECT ID FROM KEYCLOAK_ROLE where name = 'default-roles-master'";

        if(connection == null || connection.isClosed()) {
            connection = getConnection();
        }

        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = null;
        String id = null;
       
        try {
            rs = stmt.executeQuery();
            rs.next();

            id = rs.getString("ID");

        } finally {
            stmt.close();
            
            if(rs != null) {
                rs.close();
            }
        }

        return id;
    }

    private String insertInsertIntoUserEntity() throws SQLException {

        if(connection == null || connection.isClosed()) {
            connection = getConnection();
        }

        PreparedStatement stmt = connection.prepareStatement(sqlInsertIntoUserEntity());
        
        try {
            String firstName = generateName();
            Date currentDate = new Date();
            String id = generateID();

            if(realmID == null || realmID.isEmpty()) {
                return null;
            }

            stmt.setLong(1, currentDate.getTime());
            stmt.setString(2, null);
            stmt.setString(3, generateID());
            stmt.setBoolean(4, false);
            stmt.setBoolean(5, true);
            stmt.setString(6, null);
            stmt.setString(7, firstName);
            stmt.setString(8, generateName());
            stmt.setLong(9, 0);
            stmt.setString(10, realmID);
            stmt.setString(11, null);
            stmt.setString(12, firstName.toLowerCase());
            stmt.setString(13, id);

            if(stmt.executeUpdate() == 1) {
                return id;
            }

            return null;

            
        } finally {
            stmt.close();
        }
    }

    private Boolean insertInsertIntoUserRoleMapping(String id) throws SQLException {

        if(connection == null || connection.isClosed()) {
            connection = getConnection();
        }

        PreparedStatement stmt = connection.prepareStatement(sqlInsertIntoUserRoleMapping());
        
        if(id == null || id.isEmpty()) {
            return false;
        }

        try {
            stmt.setString(1, defaultRoleID);
            stmt.setString(2, id);

            if(stmt.executeUpdate() == 1) {
                return true;
            }

            return false;
        } finally {
            stmt.close();
        }
    }

    private Boolean insertInsertIntoCredencial(String userID) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
        
        if(connection == null || connection.isClosed()) {
            connection = getConnection();
        }

        PreparedStatement stmt = connection.prepareStatement(sqlInsertIntoCredencial());
        
        Date currentDate = new Date();

        // StringBuilder secretData = new StringBuilder();

        // secretData.append("{\"value\":\"")
        //     .append(hash)
        //     .append("\",\"salt\":\"")
        //     .append(salt)
        //     .append("\"")
        //     .append(",\"additionalParameters\":{}")
        //     .append("}");

        String secretData = "{\"value\":\"b2H7Szu4/6pD7CVPSK4gInipDYGI9UkWDBslc2qRUirxlMpg/73Bxr1FjpfnMd+Tz+a1bCx5dR9mz84tbX+efA==\",\"salt\":\"AgZJjkrNemX6GXi5HlTyQg==\",\"additionalParameters\":{}}";

        try {
            stmt.setLong(1, currentDate.getTime());
            stmt.setString(2, credentailData);
            stmt.setInt(3, 10);
            stmt.setBytes(4, null);
            stmt.setString(5, secretData);
            stmt.setString(6, "password");
            stmt.setString(7, userID);
            stmt.setString(8, null);
            stmt.setString(9, generateID());

            return stmt.executeUpdate() == 1;
        } finally {
            stmt.close();
        }
    }

    public void addUser() throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
        
        String id = insertInsertIntoUserEntity();

        if(id == null || id.isEmpty()) {
            connection.rollback();
            return;
        }

        if(insertInsertIntoUserRoleMapping(id)) {
            
            if(insertInsertIntoCredencial(id)) {
                connection.commit();
            } else {
                connection.rollback();
                return;
            }
        } else {
            connection.rollback();
            return;
        }
        
    }


    public static void main(String[] args) {

        Date d1 = new Date();
        try {
            new InsertTables().addUser();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Date d2 = new Date();

        //show the time difference between two dates
        long difference_In_Time = d2.getTime() - d1.getTime();

        //show the time difference between two dates in miliseconds
        long difference_In_MilliSeconds = difference_In_Time % 1000;
        System.out.println("Total Time: " + difference_In_MilliSeconds + " miliseconds");
        System.out.println();
    }
}
