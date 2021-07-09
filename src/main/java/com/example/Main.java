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
      //get number of accounts with the same username from accounts table
      String sql = "SELECT COUNT (*) FROM accounts WHERE username = '" + u.getUsername() + "'";
      System.out.println(sql);
      //get number of accounts from resultset
      ResultSet rs = stmt.executeQuery(sql);
      rs.next();
      int x = rs.getInt("count");
      System.out.println(x);
      if(x != 0){ //check if there are is at least 1 account in table
        model.put("taken", "This username has been taken");
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
    Account account = new Account();
    model.put("account", account);
    return "login";
  }

  @PostMapping(
    path = "/login",
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleLogin(Map<String, Object> model, Account u) throws Exception{
    try(Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();
      //check if the username is in the table
      String sql = "SELECT COUNT (*) FROM accounts WHERE username = '" + u.getUsername() + "'";
      //get number from resultset
      ResultSet rs = stmt.executeQuery(sql);
      rs.next();
      int x = rs.getInt("count");
      System.out.println(x);
      if(x == 0){ //check if no user matches the username
        return "login"; //if none exist
      }
      //check if password is correct
      sql = "SELECT password FROM accounts WHERE username = '" + u.getUsername() + "'";
      System.out.println(sql);
      rs = stmt.executeQuery(sql);
      rs.next();
      String pass = rs.getString("password");
      if(!pass.equals(u.getPassword())){ //check if password is correct
        return "login"; //if not correct
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
    Account account = new Account();
    model.put("account", account);
    return "forgot";
  }

  @RequestMapping("/contact") 
  String contact(Map<String, Object> model) {
    AdminMessage adminMessage = new AdminMessage();
    model.put("adminMessage", adminMessage);
    return "contact";
  }

  // Submits the contact us form
  @PostMapping(path = "/contact", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String contactAddAdminMessage(AdminMessage adminMessage) throws Exception {

    try (Connection connection = dataSource.getConnection()) 
    {
      Statement statement = connection.createStatement();

      statement.executeUpdate(
          "CREATE TABLE IF NOT EXISTS adminMessages (username varchar(20), email varchar(100), "
              + "message varchar(1000), category varchar(20));");

      adminMessage.setCategory("contact");

      statement.executeUpdate(
          "INSERT INTO adminMessages(username, email, message, category) VALUES ('" 
          + adminMessage.getUsername() + "', '" + adminMessage.getEmail() + "', '" + adminMessage.getMessage() + "', " 
          + adminMessage.getCategory() + ");");

      return "redirect:/";
    } 
    catch (Exception e) 
    {
      return "error";
    }
  }

  // Shows the table of admin messages
  @RequestMapping("/viewAdminMessages") 
  String viewAdminMessages(Map<String, Object> model) {

    try (Connection connection = dataSource.getConnection()) 
    {
      Statement statement = connection.createStatement();

      statement.executeUpdate(
          "CREATE TABLE IF NOT EXISTS adminMessages (username varchar(20), email varchar(100), "
              + "message varchar(1000), category varchar(20));");

      ResultSet rs = statement.executeQuery("SELECT * FROM adminMessages");

      ArrayList<String> usernames = new ArrayList<String>();
      ArrayList<String> emails = new ArrayList<String>();
      ArrayList<String> messages = new ArrayList<String>();
      ArrayList<String> categories = new ArrayList<String>();

      while (rs.next()) 
      {
        usernames.add(String.valueOf(rs.getString("username")));
        emails.add(rs.getString("email"));
        messages.add(rs.getString("message"));
        categories.add(rs.getString("category"));
      }

      model.put("usernames", usernames);
      model.put("emails", emails);
      model.put("messages", messages);
      model.put("categories", categories);

      return "adminMessageTable";
    } 
    catch (Exception e) 
    {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  // Selected a specific admin message, view its details
  @RequestMapping("/adminMessage")
  String adminMessage(@RequestParam(value = "id", required = false) String tag, Map<String, Object> model) {

    try (Connection connection = dataSource.getConnection()) 
    {
      Statement statement = connection.createStatement();

      ResultSet rs = statement.executeQuery("SELECT * FROM adminMessages where id=" + tag);

      while (rs.next()) 
      {
        model.put("id", String.valueOf(rs.getInt("id")));
        model.put("username", rs.getString("username"));
        model.put("email", rs.getString("email"));
        model.put("message", rs.getString("message"));
        model.put("category", rs.getString("category"));
      }

      return "adminMessage";
    } 
    catch (Exception e) 
    {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  // Prompt for deleting all messages
  @RequestMapping("/deleteAll") 
  String attemptDeleteAll(Map<String, Object> model) {

    String message = "Do you wish to delete all messages?";

    model.put("message", message);

    // Notify users when notifications are done in the future

    return "deleteAll";
  }

  // Clicked on delete all stored messages
  @PostMapping(path = "/deleteAll", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE }, params = "action=delete")
  public String deleteAllMessages(Map<String, Object> model) throws Exception {

    try (Connection connection = dataSource.getConnection()) 
    {
          Statement statement = connection.createStatement();
          statement.executeUpdate("DROP TABLE adminMessages;");

          String message = "Succesfully deleted all messages"; 
          model.put("message", message);
          return "redirect:/viewAdminMessages";
    } 
    catch (Exception e) 
    {
      return "error";
    }
  }

  // Cancel and go back to the table
  @PostMapping(path = "/deleteAll", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE }, params = "action=cancel")
  public String cancelDeleteAll(AdminMessage adminMessage) {

    return "redirect:/viewAdminMessages";
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
