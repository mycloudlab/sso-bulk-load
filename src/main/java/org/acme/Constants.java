package org.acme;

/**
 * Querys
 */
interface Constants {

  public static final String SELECT_ROLE_ID = "SELECT ID FROM KEYCLOAK_ROLE where name = 'default-roles-master'";
  public static final String SELECT_REALM_ID = "SELECT ID FROM REALM where name = ?";
  public static final String SELECT_ADMIN_USER_ID = "select ID from USER_ENTITY where USERNAME = ?";

  public static final String INSERT_INTO_USER_ENTITY = """
          insert into USER_ENTITY (CREATED_TIMESTAMP, EMAIL, EMAIL_CONSTRAINT, EMAIL_VERIFIED, ENABLED, FEDERATION_LINK, FIRST_NAME, LAST_NAME, NOT_BEFORE, REALM_ID, SERVICE_ACCOUNT_CLIENT_LINK, USERNAME, ID)
          values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
      """;
  public static final String INSERT_INTO_USER_ROLE_MAPPING = "insert into USER_ROLE_MAPPING (ROLE_ID, USER_ID) values(?, ?)";
  public static final String INSERT_INTO_CREDENTIAL = """
          insert into CREDENTIAL(CREATED_DATE, CREDENTIAL_DATA, PRIORITY, SALT, SECRET_DATA, TYPE, USER_ID, USER_LABEL, ID)
          values (?, ?, ?, ?, ?, ?, ?, ?, ?)
      """;

  public static final String CREDENTIAL_DATA = "{\"hashIterations\":27500,\"algorithm\":\"pbkdf2-sha256\",\"additionalParameters\":{}}";

  public static final String DB_DRIVER = "config.datasource.driver";
  public static final String DB_URL = "config.datasource.url";
  public static final String DB_USER = "config.datasource.username";
  public static final String DB_PASS = "config.datasource.password";
  public static final String REALM_ID = "config.keycloak.realm";

  public static final String DELETE_USER_ROLE_MAPPING = "delete from USER_ROLE_MAPPING where USER_ID <> ?";
  public static final String DELETE_CREDENTIAL = "delete from CREDENTIAL where USER_ID  <> ?";
  public static final String DELETE_ENTIY = "delete from USER_ENTITY  where ID  <> ?";

}
