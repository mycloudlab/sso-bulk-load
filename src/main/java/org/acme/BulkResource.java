package org.acme;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.Date;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/bulk")
public class BulkResource {
    
    @POST
    @Path("/insert{amountUsers}")
    @Produces(MediaType.TEXT_PLAIN)
    public String insert(Integer amountUsers) throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException, ClassNotFoundException{

        StringBuilder sb = new StringBuilder();
        Date d1 = new Date();
       
        InsertTables service = new InsertTables();

        for(int i = 0; i < amountUsers; i++) {
            service.addUser();
        }
        Date d2 = new Date();

        long difference_In_Time = d2.getTime() - d1.getTime();

       
        sb.append("----------------------------------------\n");
        sb.append("Inserted " + amountUsers + " users\n");
        sb.append("----------------------------------------");

        //show the time difference between two dates in minutes
        long difference_In_Minutes = (difference_In_Time / (1000 * 60)) % 60;
        sb.append("\nTotal Time: " + difference_In_Minutes + " minutes");
        long difference_In_Seconds = (difference_In_Time / 1000) % 60;
        sb.append("\nTotal Time: " + difference_In_Seconds + " seconds");
        long difference_In_MilliSeconds = difference_In_Time % 1000;
        sb.append("\nTotal Time: " + difference_In_MilliSeconds + " miliseconds");
        sb.append("\n----------------------------------------\n");

        return sb.toString();
    }
}
