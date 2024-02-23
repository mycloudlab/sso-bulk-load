package org.acme;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BulkService2  {

  public static final String DB_DRIVER = "config.datasource.driver";
  public static final String DB_URL = "config.datasource.url";
  public static final String DB_USER = "config.datasource.username";
  public static final String DB_PASS = "config.datasource.password";
  public static final String REALM_ID = "config.keycloak.realm";


  @Inject
  @ConfigProperty(name = "config.datasource.driver")
  private String database;

  @Inject
  @ConfigProperty(name = "config.datasource.url")
  private String dbUrl;

  @Inject
  @ConfigProperty(name = "config.datasource.username")
  private String dbUser;

  @Inject
  @ConfigProperty(name = "config.datasource.password")
  private String dbPass;

  @Inject
  @ConfigProperty(name = "config.keycloak.realm")
  private String realmName;



  @PostConstruct
  public void validate() throws SQLException, ClassNotFoundException {

    StringBuilder sb = new StringBuilder();

    if (database == null || database.isEmpty()) {
      sb.append("Invalid database type: ").append(database).append(". Valid values are: sqlserver, postgres, mariadb, mysql, and oracle");
    }

    if (dbUrl == null || dbUrl.isEmpty()) {
      sb.append("Invalid database url: ").append(dbUrl);
    }

    if (dbUser == null || dbUser.isEmpty()) {
      sb.append("Invalid database user: ").append(dbUser);
    }

    if (dbPass == null || dbPass.isEmpty()) {
      sb.append("Invalid database password: ").append(dbPass);
    }

    if (sb.length() > 0) {
      throw new IllegalArgumentException(sb.toString());
    }

    if (database.equals("sqlserver")) {
      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    } else if (database.equals("postgres")) {
      Class.forName("org.postgresql.Driver");
    } else if (database.equals("mariadb")) {
      Class.forName("org.mariadb.jdbc.Driver");
    } else if (database.equals("mysql")) {
      Class.forName("com.mysql.cj.jdbc.Driver");
    } else if (database.equals("oracle")) {
      Class.forName("oracle.jdbc.driver.OracleDriver");
    } else {
      throw new IllegalArgumentException("Invalid database type: " + database + ". Valid values are: sqlserver, postgres, mariadb, mysql, oracle");
    }
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
    String realmID = null;
    try {
      try (//
          Connection connection = getConnection();
          PreparedStatement stmt = connection.prepareStatement("SELECT ID FROM REALM where name = ?");//
      ) {
        stmt.setString(1, realmName);
        try (ResultSet rs = stmt.executeQuery()) {
          rs.next();
          realmID = rs.getString("ID");
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return realmID;
  }



  public void insertUsers(int size) {

    String credentialData = """
        {"hashIterations":27500,"algorithm":"pbkdf2-sha256","additionalParameters":{}}
        """;
    String secretData = """
        {"value":"b2H7Szu4/6pD7CVPSK4gInipDYGI9UkWDBslc2qRUirxlMpg/73Bxr1FjpfnMd+Tz+a1bCx5dR9mz84tbX+efA==","salt":"AgZJjkrNemX6GXi5HlTyQg==","additionalParameters":{}}
        """;


    String sqlUser = """
            insert into USER_ENTITY (CREATED_TIMESTAMP, EMAIL, EMAIL_CONSTRAINT, EMAIL_VERIFIED, ENABLED, FEDERATION_LINK, FIRST_NAME, LAST_NAME, NOT_BEFORE, REALM_ID, SERVICE_ACCOUNT_CLIENT_LINK, USERNAME, ID)
            values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

    String sqlRoles = """
              insert into USER_ROLE_MAPPING(ROLE_ID, USER_ID)
        select kr.id, ue.id from user_entity ue
        inner join KEYCLOAK_ROLE kr on kr.name = 'default-roles-master'
        left outer join USER_ROLE_MAPPING urm on urm.USER_ID = ue.ID and urm.ROLE_ID = kr.id
        where urm.role_id is null
              """;

    String sqlCredential = """
        insert into CREDENTIAL(CREATED_DATE, CREDENTIAL_DATA, PRIORITY, SALT, SECRET_DATA, TYPE, USER_ID, USER_LABEL, ID)
        select
        ue.created_timestamp,
        ?,
        10,
        null,
        ?,
        'password',
        ue.id,
        null,
        gen_random_uuid()
        from user_entity ue
        left outer join CREDENTIAL c on c.user_id = ue.id
        where c.id is null
        """;


    try {

      String realmID = getRealmID();

      try (Connection connection = getConnection()) {
        try (//
            PreparedStatement stmtUser = connection.prepareStatement(sqlUser); //
            PreparedStatement stmtRoleMapping = connection.prepareStatement(sqlRoles);
            PreparedStatement stmtCredential = connection.prepareStatement(sqlCredential);) {

          for (int x = 1; x <= size;) {

            String firstName = null;
            String lastName = null;
            String id = null;

            firstName = generateName();
            lastName = generateName();
            id = generateID();

            stmtUser.setLong(1, new Date().getTime()); // CREATED_TIMESTAMP
            stmtUser.setString(2, null); // EMAIL
            stmtUser.setString(3, generateID()); // EMAIL_CONSTRAINT
            stmtUser.setBoolean(4, false); // EMAIL_VERIFIED
            stmtUser.setBoolean(5, true); // ENABLED
            stmtUser.setString(6, null); // FEDERATION_LINK
            stmtUser.setString(7, firstName); // FIRST_NAME
            stmtUser.setString(8, lastName); // LAST_NAME
            stmtUser.setLong(9, 0); // NOT_BEFORE
            stmtUser.setString(10, realmID); // REALM_ID
            stmtUser.setString(11, null); // SERVICE_ACCOUNT_CLIENT_LINK
            stmtUser.setString(12, firstName.toLowerCase() + "." + lastName.toLowerCase()); // USERNAME
            stmtUser.setString(13, id); // ID

            int updated = stmtUser.executeUpdate();
            x += updated;
          }
          
          // atualiza todos os usuários sem roles
          stmtRoleMapping.executeUpdate();

          // atualiza todos os usuários sem credenciais
          stmtCredential.setString(1, credentialData);
          stmtCredential.setString(2, secretData);
          stmtCredential.executeUpdate();

          connection.commit();
        } catch (SQLException e) {
          connection.rollback();
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
