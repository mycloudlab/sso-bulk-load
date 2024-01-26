package org.acme;

/**
 * Querys
 */
interface Constants {

    public static final String INSERT_INTO_USER_ENTITY = sqlInsertIntoUserEntity();
    public static final String INSERT_INTO_USER_ROLE_MAPPING = sqlInsertIntoUserRoleMapping();
    public static final String SELECT_ROLE_ID = "SELECT ID FROM KEYCLOAK_ROLE where name = 'default-roles-master'";
    public static final String SELECT_REALM_ID = "SELECT ID FROM REALM where name = ?";
    public static final String INSERT_INTO_CREDENTIAL = sqlInsertIntoCredencial();

    public static final String CREDENTIAL_DATA = "{\"hashIterations\":27500,\"algorithm\":\"pbkdf2-sha256\",\"additionalParameters\":{}}";

    public static final String DB_URL = "config.datasource.url";
    public static final String DB_USER = "config.datasource.username";
    public static final String DB_PASS = "config.datasource.password";
    public static final String REALM_ID = "config.keycloak.realm";

    private static String sqlInsertIntoUserEntity() {
        StringBuilder sql = new StringBuilder();

        sql.append("insert ")
            .append("into ")
            .append("USER_ENTITY ")
            .append("(CREATED_TIMESTAMP, EMAIL, EMAIL_CONSTRAINT, EMAIL_VERIFIED, ENABLED, FEDERATION_LINK, FIRST_NAME, LAST_NAME, NOT_BEFORE, REALM_ID, SERVICE_ACCOUNT_CLIENT_LINK, USERNAME, ID) ")
            .append("values ")
            .append("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        return sql.toString();
    }
    
    private static String sqlInsertIntoUserRoleMapping() {
        StringBuilder sql = new StringBuilder();

        sql.append("insert ")
            .append("into ")
            .append("USER_ROLE_MAPPING ")
            .append("(ROLE_ID, USER_ID) ")
            .append("values ")
            .append("(?, ?)");

        return sql.toString();
    }

    private static String sqlInsertIntoCredencial() {
        StringBuilder sql = new StringBuilder();

        sql.append("insert ")
            .append("into ")
            .append("CREDENTIAL ")
            .append("(CREATED_DATE, CREDENTIAL_DATA, PRIORITY, SALT, SECRET_DATA, TYPE, USER_ID, USER_LABEL, ID) ")
            .append("values ")
            .append("(?, ?, ?, ?, ?, ?, ?, ?, ?)");

        return sql.toString();
    }

} 
