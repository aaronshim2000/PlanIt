/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import com.AdminMessage;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import javax.sql.DataSource;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

@Controller
@SpringBootApplication
public class Main {

  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Autowired
  private DataSource dataSource;

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);
  }

  @RequestMapping("/")
  String index() {
    return "homepage";
  }

  @RequestMapping("/register")
  String register(Map<String, Object> model){
    Account account = new Account();
    model.put("account", account);
    return "register";
  }

  @PostMapping(
    path = "/register",
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleRegister(Map<String, Object> model, Account u) throws Exception{
    try(Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();
      //Create table (if it doesn't exist)
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS accounts (id serial PRIMARY KEY, username varchar(20), "
      + "password varchar(30), email varchar(64), fname varchar(20), lname varchar(20))");
      String sql = "SELECT COUNT (*) FROM accounts WHERE username = '" + u.getUsername() + "'";
      System.out.println(sql);
      ResultSet rs = stmt.executeQuery(sql);
      int x = rs.getInt(0);
      if(x != 0){ //check if there are any users that match the username
        return "register"; //if one exists
      }
      //add new account to table
      sql = "INSERT INTO accounts (username, password, email, fname, lname) VALUES ('" + u.getUsername() + "', '" + u.getPassword() + "', '" + u.getEmail() + "', '" + u.getFname() + "', '" + u.getLname() + "')";
      stmt.executeUpdate(sql);
      System.out.println(u.getUsername() + " " +u.getPassword() + " " + u.getEmail() + " " + u.getFname() + " " + u.getLname());
      return "login";
    }
    catch(Exception e){
      model.put("Error", e.getMessage());
      return "error";
    }
  }

  @RequestMapping("/login")
  String login(Map<String, Object> model){
    Account user = new Account();
    model.put("user", user);
    return "login";
  }

  @PostMapping(
    path = "/login",
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleLogin(Map<String, Object> model, Account u) throws Exception{
    try(Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();
      String sql = "SELECT COUNT (*) FROM accounts WHERE username = '" + u.getUsername() + "'";
      System.out.println(sql);
      ResultSet rs = stmt.executeQuery(sql);
      int x = rs.getInt(0);
      if(x == 0){ //check if no user matches the username
        return "login"; //if none exist
      }
      sql = "SELECT password FROM users WHERE username = '" + u.getUsername() + "'";
      rs = stmt.executeQuery(sql);
      String pass = rs.getString(0);
      if(pass != u.getPassword()){ //check if password is correct
        return "login";
      }
      return "homepage"; //go to main page
    }
    catch(Exception e){
      model.put("Error", e.getMessage());
      return "error";
    }
  }

  @RequestMapping("/forgot")
  String forgot(Map<String, Object> model){
    Account user = new Account();
    model.put("user", user);
    return "forgot";
  }

  @RequestMapping("/contact") 
  String contact(Map<String, Object> model) {
    AdminMessage adminMessage = new AdminMessage();
    model.put("adminMessage", adminMessage);
    return "contact";
  }

  @PostMapping(path = "/contact", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String contactAddAdminMessage(AdminMessage adminMessage) throws Exception {

    try (Connection connection = dataSource.getConnection()) 
    {
      Statement statement = connection.createStatement();

      statement.executeUpdate(
          "CREATE TABLE IF NOT EXISTS adminMessages (user varchar(20), email varchar(100), "
              + "message varchar(1000), category ENUM('CONTACT', 'REPORT'));");

      adminMessage.setCategory(MessageCategory.CONTACT);

      statement.executeUpdate(
          "INSERT INTO adminMessages(user, email, message, category) VALUES ('" 
          + adminMessage.getUsername() + "', '" + adminMessage.getEmail() + "', '" + adminMessage.getMessage() + "', " 
          + adminMessage.getCategory() + ");");

      return "redirect:/";
    } 
    catch (Exception e) 
    {
      return "error";
    }
  }


  @RequestMapping("/db")
  String db(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
      stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
      ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

      ArrayList<String> output = new ArrayList<String>();
      while (rs.next()) {
        output.add("Read from DB: " + rs.getTimestamp("tick"));
      }

      model.put("records", output);
      return "db";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @Bean
  public DataSource dataSource() throws SQLException {
    if (dbUrl == null || dbUrl.isEmpty()) {
      return new HikariDataSource();
    } else {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(dbUrl);
      return new HikariDataSource(config);
    }
  }

}
