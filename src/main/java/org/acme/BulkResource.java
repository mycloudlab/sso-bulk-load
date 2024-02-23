package org.acme;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/bulk")
public class BulkResource {

  @Inject
  BulkService2 service;

  @GET
  @Path("/insert/{amountUsers}")
  @Produces(MediaType.TEXT_PLAIN)
  @Tag(name = "Users", description = "Manage users in RHSSO")
  @Operation(summary = "Insert users", description = "Insert users into RHSSO database, **all users have the same password: 123456**")
  public String insert(Integer amountUsers) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException, ClassNotFoundException {

    StringBuilder sb = new StringBuilder();

    Instant startTime = Instant.now();

    service.insertUsers(amountUsers);

    Instant endTime = Instant.now();
    Duration duration = Duration.between(startTime, endTime);
    sb.append(amountUsers);
    sb.append(" users inserted.\r\n");
    sb.append("Total time:");
    sb.append(duration.toString().substring(2).replaceAll("(\\d[HMS])(?!$)", "$1 ").toLowerCase());

    return sb.toString();
  }

  // @DELETE
  // @Path("/delete/{adminUserName}")
  // @Produces(MediaType.TEXT_PLAIN)
  // public String delete(String adminUserName) throws SQLException {
  // StringBuilder sb = new StringBuilder();
  //
  // Date d1 = new Date();
  // service.deleteAllUser(adminUserName);
  // Date d2 = new Date();
  //
  // long difference_In_Time = d2.getTime() - d1.getTime();
  // sb.append("----------------------------------------");
  // sb.append("\nTotal Time : " + String.format("%02d:%02d:%02d:%03d", difference_In_Time / (3600 *
  // 1000), (difference_In_Time % (3600 * 1000)) / (60 * 1000), ((difference_In_Time % (3600 * 1000))
  // % (60 * 1000)) / 1000,
  // ((difference_In_Time % (3600 * 1000)) % (60 * 1000)) % 1000));
  // sb.append("\n----------------------------------------\n");
  //
  // return sb.toString();
  // }
}
