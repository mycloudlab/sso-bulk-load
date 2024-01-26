package org.acme;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Random;
import java.util.UUID;

import org.eclipse.microprofile.config.ConfigProvider;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Date;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@ApplicationScoped
public class BulkService implements Constants {
   
    
    //private static final String userPassword = "123456";
    private String realmID = null;
    private String defaultRoleID = null;
    private Connection connection = null;
    //private static String salt = null;
    //private static String hash = null;

    private String dbUrl;
    private String dbUser;
    private String dbPass;
    private String realmName;

    public BulkService() throws SQLException, ClassNotFoundException {

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

    private String getRealmID() throws SQLException {

        if(connection == null || connection.isClosed()) {
            connection = getConnection();
        }

        PreparedStatement stmt = connection.prepareStatement(SELECT_REALM_ID);
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
        String sql = SELECT_ROLE_ID;

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

        PreparedStatement stmt = connection.prepareStatement(INSERT_INTO_USER_ENTITY);
        
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

        PreparedStatement stmt = connection.prepareStatement(INSERT_INTO_USER_ROLE_MAPPING);
        
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

        PreparedStatement stmt = connection.prepareStatement(INSERT_INTO_CREDENTIAL);
        
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
            stmt.setString(2, CREDENTIAL_DATA);
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

    public void deleteAllUser(String adminUser) throws SQLException{
        
        if(connection == null || connection.isClosed()) {
            connection = getConnection();
        }

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = connection.prepareStatement(SELECT_ADMIN_USER_ID);

            stmt.setString(1, adminUser);

            rs = stmt.executeQuery();
            
            String adminUserID = null;
            
            if(rs.next()) {
                adminUserID = rs.getString("ID");
            } else {
                new IllegalArgumentException("User not found!");
                return;
            }
        
            stmt.close();

            stmt = connection.prepareStatement(DELETE_USER_ROLE_MAPPING);
            stmt.setString(1, adminUserID);
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement(DELETE_CREDENTIAL);
            stmt.setString(1, adminUserID);
            stmt.executeUpdate();
            stmt.close();

            stmt = connection.prepareStatement(DELETE_ENTIY);
            stmt.setString(1, adminUserID);
            stmt.executeUpdate();
            stmt.close();

            connection.commit();

        } finally {
            if(stmt != null && !stmt.isClosed()) {
                stmt.close();
            }

            if(rs != null) {
                rs.close();
            }
        }
        
    }


    // public static void main(String[] args) {

    //     Date d1 = new Date();
    //     try {
    //         new BulkService().addUser();
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }

    //     Date d2 = new Date();

    //     //show the time difference between two dates
    //     long difference_In_Time = d2.getTime() - d1.getTime();

    //     //show the time difference between two dates in miliseconds
    //     long difference_In_MilliSeconds = difference_In_Time % 1000;
    //     System.out.println("Total Time: " + difference_In_MilliSeconds + " miliseconds");
    //     System.out.println();
    // }

    // public static void main(String[] args) {
    //     try {
    //         new BulkService().deleteAllUser("Teste");
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
        
    // }
}
