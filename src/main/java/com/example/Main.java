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

import com.Account;
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

import javax.net.ssl.HandshakeCompletedEvent;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
  
  @RequestMapping("/post")
  String post() {
    return "redirect:/post/text";
  }
  
  @RequestMapping("/filetest")
  String fileTest() {
    return "fileupload";
  }


  @RequestMapping("/post/text")
  String postText(Map<String,Object> model) {
    Post post=new Post();
    model.put("post",post);
    return "post-text";
  }

  @PostMapping(path = "/post/text", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String submitTextPost(Map<String,Object> model,Post post, HttpServletRequest request) throws Exception {
    try (Connection connection = dataSource.getConnection())
    {
      Statement statement = connection.createStatement();
      statement.executeUpdate("CREATE TABLE IF NOT EXISTS posts (id serial, post_date DATE, creator varchar(20), title varchar(50), content varchar(1600),category varchar(20),visibility varchar(10),rating varchar(5))");
      post.setCategory("text-post");
      String username= (String) request.getSession().getAttribute("USER");
      post.setCreator(username);
      statement.executeUpdate("INSERT INTO posts(post_date, creator, title, content,category,visibility) VALUES (now(),'" + username + "', '" + post.getTitle() + "', '" + post.getDescription() + "', '" + post.getCategory() + "','" + post.getVisibility() + "')");
      return "redirect:/scrollingFeed";
    }
    catch (Exception e)
    {
      model.put("message",e.getMessage());
      return "error";
    }
  }

  @RequestMapping("/post/review")
  String postReview(Map<String,Object> model) {
    Post post=new Post();
    model.put("post",post);
    return "post-review";
  }

  @PostMapping(path = "/post/review", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String submitReviewPost(Map<String,Object> model,Post post,HttpServletRequest request) throws Exception {
    try (Connection connection = dataSource.getConnection())
    {
      Statement statement = connection.createStatement();
      statement.executeUpdate("CREATE TABLE IF NOT EXISTS posts (id serial, post_date DATE, creator varchar(20), title varchar(50), content varchar(1600),category varchar(20),visibility varchar(10),rating varchar(5))");
      post.setCategory("review-post");
      String username= (String) request.getSession().getAttribute("USER");
      post.setCreator(username);
      statement.executeUpdate("INSERT INTO posts(post_date,creator,title,content,category,visibility,rating) VALUES (now(),'" + username + "', '" + post.getTitle() + "', '" + post.getDescription() + "', '" + post.getCategory() + "','" + post.getVisibility() + "','" + post.getRating() + "')");
      return "redirect:/scrollingFeed";
    }
    catch (Exception e)
    {
      model.put("message",e.getMessage());
      return "error";
    }
  }

  @RequestMapping("/post/plan")
  String postPlan(Map<String,Object> model) {
    Post post=new Post();
    model.put("post",post);
    return "post-plan";
  }

  @PostMapping(path = "/post/plan", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String submitPlanPost(Map<String,Object> model,Post post,HttpServletRequest request) throws Exception {
    try (Connection connection = dataSource.getConnection())
    {
      Statement statement = connection.createStatement();
      statement.executeUpdate("CREATE TABLE IF NOT EXISTS posts (id serial, post_date DATE, creator varchar(20), title varchar(50), content varchar(1600),category varchar(20),visibility varchar(10),rating varchar(5))");
      post.setCategory("plan-post");
      String username= (String) request.getSession().getAttribute("USER");
      post.setCreator(username);
      statement.executeUpdate("INSERT INTO posts(post_date,creator,title,content,category,visibility) VALUES (now(),'" + username + "','" + post.getTitle() + "', '" + post.getDescription() + "', '" + post.getCategory() + "','" + post.getVisibility() + "')");
      return "redirect:/scrollingFeed";
    }
    catch (Exception e)
    {
      model.put("message",e.getMessage());
      return "error";
    }
  }

  @RequestMapping("/scrollingFeed")
  public String getPosts(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws IOException {
    // check if user already login
    HttpSession session = request.getSession(false);
    String target = request.getRequestURI();
    if (session == null)  {
      // if not yet
      session = request.getSession(true);
      session.setAttribute("target", target);
      response.sendRedirect("/login");
    }
    else{
      Object loginCheck = session.getAttribute("login");
      if (loginCheck == null){
        //if not yet
        session.setAttribute("target", target);
        response.sendRedirect("/login");
      }
    }

    //dealing with text posts
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      ResultSet rs_text = stmt.executeQuery("SELECT * FROM posts WHERE category='text-post' AND visibility='PUBLIC' ORDER BY id DESC");
      ArrayList<String> text_titles = new ArrayList<String>();
      ArrayList<String> text_descriptions = new ArrayList<String>();
      ArrayList<String> text_postDates= new ArrayList<String>();
      ArrayList<String> text_visibilities=new ArrayList<String>();
      ArrayList<String> text_creators=new ArrayList<String>();
      while (rs_text.next())
      {
        text_titles.add(rs_text.getString("title"));
        text_descriptions.add(rs_text.getString("content"));
        text_postDates.add(rs_text.getString("post_date"));
        text_visibilities.add(rs_text.getString("visibility"));
        text_creators.add(rs_text.getString("creator"));
      }
      model.put("text_titles", text_titles);
      model.put("text_descriptions", text_descriptions);
      model.put("text_postDates",text_postDates);
      model.put("text_visibilities",text_visibilities);
      model.put("text_creators",text_creators);
      //return "scrollingFeed";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
    //dealing with review posts
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      ResultSet rs_review = stmt.executeQuery("SELECT * FROM posts WHERE category='review-post' AND visibility='PUBLIC' ORDER BY id DESC");
      ArrayList<String> review_titles = new ArrayList<String>();
      ArrayList<String> review_descriptions = new ArrayList<String>();
      ArrayList<String> review_ratings=new ArrayList<String>();
      ArrayList<String> review_postDates=new ArrayList<String>();
      ArrayList<String> review_visibilities=new ArrayList<String>();
      ArrayList<String> review_creators=new ArrayList<String>();
      while (rs_review.next())
      {
        review_titles.add(rs_review.getString("title"));
        review_descriptions.add(rs_review.getString("content"));
        review_ratings.add(rs_review.getString("rating"));
        review_postDates.add(rs_review.getString("post_date"));
        review_visibilities.add(rs_review.getString("visibility"));
        review_creators.add(rs_review.getString("creator"));
      }
      model.put("review_titles", review_titles);
      model.put("review_descriptions", review_descriptions);
      model.put("review_ratings",review_ratings);
      model.put("review_postDates",review_postDates);
      model.put("review_visibilities",review_visibilities);
      model.put("review_creators",review_creators);
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
    //dealing with plan posts
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      ResultSet rs_plan = stmt.executeQuery("SELECT * FROM posts WHERE category='plan-post' AND visibility='PUBLIC' ORDER BY id DESC");
      ArrayList<String> plan_titles = new ArrayList<String>();
      ArrayList<String> plan_descriptions = new ArrayList<String>();
      ArrayList<String> plan_postDates = new ArrayList<String>();
      ArrayList<String> plan_visibilities = new ArrayList<String>();
      ArrayList<String> plan_creators = new ArrayList<String>();
      while (rs_plan.next())
      {
        plan_titles.add(rs_plan.getString("title"));
        plan_descriptions.add(rs_plan.getString("content"));
        plan_postDates.add(rs_plan.getString("post_date"));
        plan_visibilities.add(rs_plan.getString("visibility"));
        plan_creators.add(rs_plan.getString("creator"));
      }
      model.put("plan_titles", plan_titles);
      model.put("plan_descriptions", plan_descriptions);
      model.put("plan_postDates",plan_postDates);
      model.put("plan_visibilities",plan_visibilities);
      model.put("plan_creators",plan_creators);
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
    return "scrollingFeed";
  }


  @RequestMapping("/costCalculator")
  String costCalculator() {
    return "costCalculator";
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
      + "password varchar(30), email varchar(64), fname varchar(20), lname varchar(20), role varchar(20))");
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
      sql = "INSERT INTO accounts (username, password, email, fname, lname, role) VALUES ('" + u.getUsername() + "', '" + u.getPassword() + "', '" + u.getEmail() + "', '" + u.getFname() + "', '" + u.getLname() + "', 'default')";
      stmt.executeUpdate(sql);
      //System.out.println(u.getUsername() + " " +u.getPassword() + " " + u.getEmail() + " " + u.getFname() + " " + u.getLname());
      model.put("message", "Account successfully created");
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
  public String handleLogin(Map<String, Object> model, Account u, HttpServletRequest request) throws Exception{
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
        model.put("message", "Invalid Username");
        return "login"; //if none exist
      }
      //check if password is correct
      sql = "SELECT password FROM accounts WHERE username = '" + u.getUsername() + "'";
      System.out.println(sql);
      rs = stmt.executeQuery(sql);
      rs.next();
      String pass = rs.getString("password");
      if(!pass.equals(u.getPassword())){ //check if password is correct
        model.put("message", "Incorrect Password");
        return "login"; //if not correct
      }
      //GIVE USER INFORMATION TO SESSION *************************************************************************
      request.getSession().setAttribute("USER", u.getUsername()); //put username into session
      request.getSession().setAttribute("PASSWORD", u.getPassword()); //put password into session

      rs = stmt.executeQuery("SELECT email FROM accounts WHERE username = '" + u.getUsername() + "'"); //find email from database
      rs.next();
      String email = rs.getString("email");
      request.getSession().setAttribute("EMAIL", email); //put email into session

      rs = stmt.executeQuery("SELECT fname FROM accounts WHERE username = '" + u.getUsername() + "'"); //find firstname from database
      rs.next();
      String fname = rs.getString("fname");
      request.getSession().setAttribute("FNAME", fname); //put firstname into session

      rs = stmt.executeQuery("SELECT lname FROM accounts WHERE username = '" + u.getUsername() + "'"); //find lastname from database
      rs.next();
      String lname = rs.getString("lname");
      request.getSession().setAttribute("LNAME", lname); //put lastname into session

      rs = stmt.executeQuery("SELECT role FROM accounts WHERE username = '" + u.getUsername() + "'"); //find role from database
      rs.next();
      String role = rs.getString("role");
      request.getSession().setAttribute("ROLE", role);


      request.getSession().setAttribute("login", "OK");
      if(request.getSession().getAttribute("target")!=null){
        model.put("message","You can access the page now.");
        return "homepage";
      }

      model.put("message", "Welcome, " + request.getSession().getAttribute("USER"));
      return "homepage"; //go to main page
    }
    catch(Exception e){
      model.put("Error", e.getMessage());
      return "error";
    }
  }

  @RequestMapping("/logout")
  String logout(HttpServletRequest request){
    request.getSession().invalidate();
    return("homepage");
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
  public String contactSent(AdminMessage adminMessage) throws Exception {

    try (Connection connection = dataSource.getConnection()) 
    {
      Statement statement = connection.createStatement();

      statement.executeUpdate(
          "CREATE TABLE IF NOT EXISTS adminMessages (id serial PRIMARY KEY, username varchar(20), email varchar(100), "
              + "message varchar(1000), category varchar(20));");

      adminMessage.setCategory("contact");

      statement.executeUpdate(
          "INSERT INTO adminMessages(username, email, message, category) VALUES ('" 
          + adminMessage.getUsername() + "', '" + adminMessage.getEmail() + "', '" + adminMessage.getMessage() + "', '" 
          + adminMessage.getCategory() + "');");

      return "redirect:/viewAdminMessages";
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
          "CREATE TABLE IF NOT EXISTS adminMessages (id serial PRIMARY KEY, username varchar(20), email varchar(100), "
              + "message varchar(1000), category varchar(20));");

      ResultSet rs = statement.executeQuery("SELECT * FROM adminMessages");

      ArrayList<String> ids = new ArrayList<String>();
      ArrayList<String> usernames = new ArrayList<String>();
      ArrayList<String> emails = new ArrayList<String>();
      ArrayList<String> categories = new ArrayList<String>();

      while (rs.next()) 
      {
        ids.add(String.valueOf(rs.getString("id")));
        usernames.add(rs.getString("username"));
        emails.add(rs.getString("email"));
        categories.add(rs.getString("category"));
      }

      model.put("ids", ids);
      model.put("usernames", usernames);
      model.put("emails", emails);
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
  @RequestMapping("/deleteAllMessages") 
  String attemptDeleteAll(Map<String, Object> model) {

    String message = "Do you wish to delete all messages?";

    model.put("message", message);

    // Notify users of this when notifications are done in the future

    return "deleteAllMessages";
  }

  // Clicked on delete all stored messages
  @PostMapping(path = "/deleteAllMessages", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE }, params = "action=delete")
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
  @PostMapping(path = "/deleteAllMessages", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE }, params = "action=cancel")
  public String cancelDeleteAll(AdminMessage adminMessage) {

    return "redirect:/viewAdminMessages";
  }

  //get a list of accounts for viewAccountsTable.html
  @RequestMapping("/viewAccountsTable")
  String viewAccountsTable(Map<String, Object> model){
    try(Connection connection = dataSource.getConnection()){
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery("SELECT * FROM accounts");

      ArrayList<String> ids = new ArrayList<String>();
      ArrayList<String> usernames = new ArrayList<String>();
      ArrayList<String> emails = new ArrayList<String>();
      ArrayList<String> fnames = new ArrayList<String>();
      ArrayList<String> lnames = new ArrayList<String>();

      while(rs.next()){
        ids.add(String.valueOf(rs.getString("id")));
        usernames.add(String.valueOf(rs.getString("username")));
        emails.add(String.valueOf(rs.getString("email")));
        fnames.add(String.valueOf(rs.getString("fname")));
        lnames.add(String.valueOf(rs.getString("lname")));
      }
      model.put("ids", ids);
      model.put("usernames", usernames);
      model.put("emails", emails);
      model.put("fnames", fnames);
      model.put("lnames", lnames);
      return "viewAccountsTable";
    }
    catch(Exception e){
      model.put("message", e.getMessage());
      return "error";
    }
  }

  //get a specific account for viewAccounts
  @RequestMapping("/viewAccount")
  String viewAccount(@RequestParam(value = "id", required = false) String tag, Map<String, Object> model) {

    try (Connection connection = dataSource.getConnection()) 
    {
      Statement statement = connection.createStatement();

      ResultSet rs = statement.executeQuery("SELECT * FROM accounts where id=" + tag);

      while (rs.next()) 
      {
        model.put("id", String.valueOf(rs.getInt("id")));
        model.put("username", rs.getString("username"));
        model.put("email", rs.getString("email"));
        model.put("fname", rs.getString("fname"));
        model.put("lname", rs.getString("lname"));
        model.put("role", rs.getString("role"));
      }

      return "viewAccount";
    } 
    catch (Exception e) 
    {
      model.put("message", e.getMessage());
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
