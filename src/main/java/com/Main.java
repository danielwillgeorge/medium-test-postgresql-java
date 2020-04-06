package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import static org.junit.Assert.assertEquals;


public class Main {

    public static void main(String[] args) {

        try {
            testInsert();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void testInsert() throws Exception {

        try {

            Process p = Runtime.getRuntime().exec("pg_tmp -t");
            p.waitFor();

            BufferedReader input = new BufferedReader
                    (new InputStreamReader(p.getInputStream()));

            String s = input.readLine();
            String pg_uri = "jdbc:postgresql://" + s.substring(s.lastIndexOf('@') + 1);
            input.close();

            Connection conn = DriverManager.getConnection(pg_uri);
            Statement stmt = conn.createStatement();

            stmt.execute("CREATE TABLE trees (id VARCHAR (13), " +
                    "common_name VARCHAR (50), " +
                    "scientific_name VARCHAR (50));");

            stmt.execute("INSERT INTO trees (id, common_name, scientific_name)" +
                    "VALUES (1, 'California Redwood', 'Sequoia sempervirens')");

            ResultSet rs = stmt.executeQuery("SELECT * " +
                    "FROM trees;");

            while(rs.next()) {
                assertEquals("Sequoia sempervirens", rs.getString(3));
            }

        } catch (IOException | SQLException e) {
            System.out.println(e);
        }
    }
}
