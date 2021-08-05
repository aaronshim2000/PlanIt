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
import com.Notification;
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
import java.util.Arrays;
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
  String index(Map<String, Object> model, HttpServletRequest request) { 
    model.put("user", request.getSession().getAttribute("USER"));
    return "homepage";
  }
  
  @RequestMapping("/post")
  String post(Map<String, Object> model, HttpServletRequest request) {
    if(request.getSession().getAttribute("USER") == null){
      return "redirect:/login";
    }
    model.put("user", request.getSession().getAttribute("USER"));
    return "redirect:/post/text";
  }
  
  @RequestMapping("/filetest")
  String fileTest() {
    return "fileupload";
  }

  @RequestMapping("/post/text")
  String postText(Map<String, Object> model, HttpServletRequest request) {
    Post post=new Post();
    model.put("post",post);
    model.put("user", request.getSession().getAttribute("USER"));
    return "post-text";
  }

  @PostMapping(path = "/post/text", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String submitTextPost(Map<String,Object> model,Post post, HttpServletRequest request) throws Exception {
    try (Connection connection = dataSource.getConnection())
    {
      Statement statement = connection.createStatement();
      statement.executeUpdate("CREATE TABLE IF NOT EXISTS posts (id serial, post_date DATE, creator varchar(20), title varchar(50), content varchar(1600),category varchar(20),visibility varchar(10),rating varchar(5),imagesNum varchar(2), mediaType varchar(7), image00 varchar(200),image01 varchar(200),image02 varchar(200),image03 varchar(200),image04 varchar(200),image05 varchar(200),image06 varchar(200),image07 varchar(200),image08 varchar(200),image09 varchar(200),video00 varchar(200), likes text[])");
      post.setCategory("text-post");
      String username= (String) request.getSession().getAttribute("USER");
      post.setCreator(username);
      statement.executeUpdate("INSERT INTO posts(post_date,creator,title,content,category,visibility,image00,image01,image02,image03,image04,image05,image06,image07,image08,image09,imagesNum,video00,mediaType,likes) VALUES (now(),$$" + username + "$$, $$" + post.getTitle() + "$$, $$" + post.getDescription() + "$$, '" + post.getCategory() + "','" + post.getVisibility() + "','" + post.getImage00() + "','" + post.getImage01() + "','" + post.getImage02() + "','" + post.getImage03() + "','" + post.getImage04() + "','" + post.getImage05() + "','" + post.getImage06() + "','" + post.getImage07() + "','" + post.getImage08() + "','" + post.getImage09() + "','" + post.getImagesNum() + "','" + post.getVideo00() + "','" + post.getMediaType() + "','{" + username + "}')");
      model.put("user", request.getSession().getAttribute("USER"));
      return "redirect:/scrollingFeed";
    }
    catch (Exception e)
    {
      model.put("message",e.getMessage());
      return "error";
    }
  }

  @RequestMapping("/post/review")
  String postReview(Map<String, Object> model, HttpServletRequest request) {
    Post post=new Post();
    model.put("post",post);
    model.put("user", request.getSession().getAttribute("USER"));
    return "post-review";
  }

  @PostMapping(path = "/post/review", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String submitReviewPost(Map<String,Object> model,Post post,HttpServletRequest request) throws Exception {
    try (Connection connection = dataSource.getConnection())
    {
      Statement statement = connection.createStatement();
      statement.executeUpdate("CREATE TABLE IF NOT EXISTS posts (id serial, post_date DATE, creator varchar(20), title varchar(50), content varchar(1600),category varchar(20),visibility varchar(10),rating varchar(5),imagesNum varchar(2), mediaType varchar(7), image00 varchar(200),image01 varchar(200),image02 varchar(200),image03 varchar(200),image04 varchar(200),image05 varchar(200),image06 varchar(200),image07 varchar(200),image08 varchar(200),image09 varchar(200),video00 varchar(200), likes text[])");
      post.setCategory("review-post");
      String username= (String) request.getSession().getAttribute("USER");
      post.setCreator(username);
      statement.executeUpdate("INSERT INTO posts(post_date,creator,title,content,category,visibility,rating,image00,image01,image02,image03,image04,imagesNum,likes) VALUES (now(),$$" + username + "$$, $$" + post.getTitle() + "$$, $$" + post.getDescription() + "$$, '" + post.getCategory() + "','" + post.getVisibility() + "','" + post.getRating() + "','" + post.getImage00() + "','" + post.getImage01() + "','" + post.getImage02() + "','" + post.getImage03() + "','" + post.getImage04() + "','" + post.getImagesNum() + "','{" + username + "}')");
      model.put("user", request.getSession().getAttribute("USER"));
      return "redirect:/scrollingFeed";
    }
    catch (Exception e)
    {
      model.put("message",e.getMessage());
      return "error";
    }
  }

  @RequestMapping("/post/plan")
  String postPlan(Map<String, Object> model, HttpServletRequest request) {
    Post post=new Post();
    model.put("post",post);
    model.put("user", request.getSession().getAttribute("USER"));
    return "post-plan";
  }

  @PostMapping(path = "/post/plan", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String submitPlanPost(Map<String,Object> model,Post post,HttpServletRequest request) throws Exception {
    try (Connection connection = dataSource.getConnection())
    {
      Statement statement = connection.createStatement();
      statement.executeUpdate("CREATE TABLE IF NOT EXISTS posts (id serial, post_date DATE, creator varchar(20), title varchar(50), content varchar(1600),category varchar(20),visibility varchar(10),rating varchar(5),imagesNum varchar(2), mediaType varchar(7), image00 varchar(200),image01 varchar(200),image02 varchar(200),image03 varchar(200),image04 varchar(200),image05 varchar(200),image06 varchar(200),image07 varchar(200),image08 varchar(200),image09 varchar(200),video00 varchar(200), likes text[])");
      post.setCategory("plan-post");
      String username= (String) request.getSession().getAttribute("USER");
      post.setCreator(username);
      statement.executeUpdate("INSERT INTO posts(post_date,creator,title,content,category,visibility,likes) VALUES (now(),$$" + username + "$$,$$" + post.getTitle() + "$$, $$" + post.getDescription() + "$$, '" + post.getCategory() + "','" + post.getVisibility() + "','{" + username + "}')");
      model.put("user", request.getSession().getAttribute("USER"));
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
      String username= (String) request.getSession().getAttribute("USER");
      ResultSet rs_text = stmt.executeQuery("SELECT * FROM posts WHERE (category='text-post' AND visibility='PUBLIC') OR (category='text-post' AND creator='" + username + "' AND visibility='PRIVATE') ORDER BY id DESC");
      ArrayList<String> text_titles = new ArrayList<String>();
      ArrayList<String> text_descriptions = new ArrayList<String>();
      ArrayList<String> text_postDates= new ArrayList<String>();
      ArrayList<String> text_visibilities=new ArrayList<String>();
      ArrayList<String> text_creators=new ArrayList<String>();
      ArrayList<String[]> likes = new ArrayList<String[]>();
      ArrayList<String> likesCount = new ArrayList<String>();
      while (rs_text.next())
      {
        text_titles.add(rs_text.getString("title"));
        text_descriptions.add(rs_text.getString("content"));
        text_postDates.add(rs_text.getString("post_date"));
        text_visibilities.add(rs_text.getString("visibility"));
        text_creators.add(rs_text.getString("creator"));
        Array sqlLikes = rs_text.getArray("likes");
        String[] arr = (String[])sqlLikes.getArray();
        likes.add(arr);
        likesCount.add(Integer.toString(arr.length));
      }
      model.put("text_titles", text_titles);
      model.put("text_descriptions", text_descriptions);
      model.put("text_postDates",text_postDates);
      model.put("text_visibilities",text_visibilities);
      model.put("text_creators",text_creators);
      model.put("likes", likes);
      model.put("likesCount", likesCount);

      //return "scrollingFeed";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
    //dealing with review posts
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      String username= (String) request.getSession().getAttribute("USER");
      ResultSet rs_review = stmt.executeQuery("SELECT * FROM posts WHERE (category='review-post' AND visibility='PUBLIC') OR (category='review-post' AND creator='" + username + "' AND visibility='PRIVATE') ORDER BY id DESC");
      ArrayList<String> review_titles = new ArrayList<String>();
      ArrayList<String> review_descriptions = new ArrayList<String>();
      ArrayList<String> review_ratings=new ArrayList<String>();
      ArrayList<String> review_postDates=new ArrayList<String>();
      ArrayList<String> review_visibilities=new ArrayList<String>();
      ArrayList<String> review_creators=new ArrayList<String>();
      ArrayList<String[]> likes = new ArrayList<String[]>();
      ArrayList<String> likesCount = new ArrayList<String>();
      while (rs_review.next())
      {
        review_titles.add(rs_review.getString("title"));
        review_descriptions.add(rs_review.getString("content"));
        review_ratings.add(rs_review.getString("rating"));
        review_postDates.add(rs_review.getString("post_date"));
        review_visibilities.add(rs_review.getString("visibility"));
        review_creators.add(rs_review.getString("creator"));
        Array sqlLikes = rs_review.getArray("likes");
        String[] arr = (String[])sqlLikes.getArray();
        likes.add(arr);
        likesCount.add(Integer.toString(arr.length));
      }
      model.put("review_titles", review_titles);
      model.put("review_descriptions", review_descriptions);
      model.put("review_ratings",review_ratings);
      model.put("review_postDates",review_postDates);
      model.put("review_visibilities",review_visibilities);
      model.put("review_creators",review_creators);
      model.put("likes", likes);
      model.put("likesCount", likesCount);
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
    //dealing with plan posts
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      String username= (String) request.getSession().getAttribute("USER");
      ResultSet rs_plan = stmt.executeQuery("SELECT * FROM posts WHERE (category='plan-post' AND visibility='PUBLIC') OR (category='plan-post' AND creator='" + username + "' AND visibility='PRIVATE') ORDER BY id DESC");
      ArrayList<String> plan_titles = new ArrayList<String>();
      ArrayList<String> plan_descriptions = new ArrayList<String>();
      ArrayList<String> plan_postDates = new ArrayList<String>();
      ArrayList<String> plan_visibilities = new ArrayList<String>();
      ArrayList<String> plan_creators = new ArrayList<String>();
      ArrayList<String[]> likes = new ArrayList<String[]>();
      ArrayList<String> likesCount = new ArrayList<String>();
      while (rs_plan.next())
      {
        plan_titles.add(rs_plan.getString("title"));
        plan_descriptions.add(rs_plan.getString("content"));
        plan_postDates.add(rs_plan.getString("post_date"));
        plan_visibilities.add(rs_plan.getString("visibility"));
        plan_creators.add(rs_plan.getString("creator"));
        Array sqlLikes = rs_plan.getArray("likes");
        String[] arr = (String[])sqlLikes.getArray();
        likes.add(arr);
        likesCount.add(Integer.toString(arr.length));
      }
      model.put("plan_titles", plan_titles);
      model.put("plan_descriptions", plan_descriptions);
      model.put("plan_postDates",plan_postDates);
      model.put("plan_visibilities",plan_visibilities);
      model.put("plan_creators",plan_creators);
      model.put("likes", likes);
      model.put("likesCount", likesCount);
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }

    //dealing with users
    try (Connection connection = dataSource.getConnection())
    {
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery("SELECT * FROM accounts where role='default'");
      ArrayList<String> usernames = new ArrayList<String>();
      while (rs.next())
        usernames.add(String.valueOf(rs.getString("username")));
      model.put("usernames", usernames);
    }
    catch (Exception e)
    {
      model.put("message", e.getMessage());
      return "error";
    }

    model.put("user", request.getSession().getAttribute("USER"));
    return "scrollingFeed";
  }

  @RequestMapping("/likePost")
  String likePost(@RequestParam("id") int id, Map<String, Object> model, HttpServletRequest request) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement(); 
      String username= (String) request.getSession().getAttribute("USER");
      ResultSet rs = stmt.executeQuery("SELECT * FROM posts WHERE id = " + id + ";");

      while(rs.next())
      {
        Array likes = rs.getArray("likes");
        // Turn sql array into string array
        String[] likesArray = (String[])likes.getArray();
        // Iterate through array to check for name
        boolean alreadyLiked = Arrays.asList(likesArray).contains(username);
        ArrayList<String> likesList = new ArrayList<String>();
        if (!alreadyLiked)
        {
          for (String s : likesArray)
            likesList.add(s);
          likesList.add(username);
        }
        else
        {
          for (String s : likesArray)
            if (s != username)
              likesList.add(s);
        }
        likesArray = (String[])likesList.toArray();
        likes = connection.createArrayOf("text", likesArray);
        String sql = "UPDATE posts SET likes = ? WHERE id = " + id + ";";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setArray(1, likes);
        pstmt.executeUpdate();
        connection.commit();
      }

    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }

    model.put("user", request.getSession().getAttribute("USER"));
    return "scrollingFeed";
  }

  @RequestMapping("/costCalculator")
  String costCalculator(Map<String, Object> model, HttpServletRequest request) {
    model.put("user", request.getSession().getAttribute("USER"));
    return "costCalculator";
  }

  @RequestMapping("/flightCalculator")
  String flightCalculator(Map<String, Object> model, HttpServletRequest request) {
    model.put("user", request.getSession().getAttribute("USER"));
    return "flightCalculator";
  }

  @RequestMapping("/register")
  String register(Map<String, Object> model, HttpServletRequest request){
    Account account = new Account();
    model.put("account", account);
    model.put("user", request.getSession().getAttribute("USER"));
    return "register";
  }

  @RequestMapping("/map")
  String postMap() {
    return "map";
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
      if(x != 0){ //check if there are is at least 1 account in table
        model.put("taken", "This username has been taken");
        return "register"; //if one exists
      }
      //add new account to table
      sql = "INSERT INTO accounts (username, password, email, fname, lname, role) VALUES ($$" + u.getUsername() + "$$, $$" + u.getPassword() + "$$, $$" + u.getEmail() + "$$, $$" + u.getFname() + "$$, $$" + u.getLname() + "$$, 'default')";
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
  String login(Map<String, Object> model, HttpServletRequest request){
    if(request.getSession().getAttribute("USER") != null){
      model.put("message", "Already logged in");
      return "homepage";
    }
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
      if(x == 0){ //check if no user matches the username
        model.put("message", "Invalid Username");
        System.out.println("Login username found");
        return "login"; //if none exist
      }
      //check if password is correct
      sql = "SELECT password FROM accounts WHERE username = '" + u.getUsername() + "'";
      rs = stmt.executeQuery(sql);
      rs.next();
      String pass = rs.getString("password");
      if(!pass.equals(u.getPassword())){ //check if password is correct
        model.put("message", "Incorrect Password");
        System.out.println("Login password found");
        return "login"; //if not correct
      }
      //GIVE USER INFORMATION TO SESSION *************************************************************************
      request.getSession().setAttribute("USER", u.getUsername()); //put username into session
      request.getSession().setAttribute("PASSWORD", u.getPassword()); //put password into session

      rs = stmt.executeQuery("SELECT id FROM accounts WHERE username = '" + u.getUsername() + "'"); //find id from database
      rs.next();
      String id = rs.getString("id");
      request.getSession().setAttribute("ID", id); //put id into session

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

      model.put("user", request.getSession().getAttribute("USER"));
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
  String logout(Map<String, Object> model, HttpServletRequest request){
    request.getSession().invalidate();
    model.put("message", "Successfully logged out");
    model.put("user", request.getSession().getAttribute("USER"));
    return("homepage");
  }

  @RequestMapping("/forgot")
  String forgot(Map<String, Object> model, HttpServletRequest request){
    Account account = new Account();
    model.put("account", account);
    model.put("user", request.getSession().getAttribute("USER"));
    return "forgot";
  }

  @RequestMapping("/contact") 
  String contact(Map<String, Object> model, HttpServletRequest request) {
    AdminMessage adminMessage = new AdminMessage();
    model.put("adminMessage", adminMessage);
    model.put("user", request.getSession().getAttribute("USER"));
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
          "INSERT INTO adminMessages(username, email, message, category) VALUES ($$" 
          + adminMessage.getUsername() + "$$, $$" + adminMessage.getEmail() + "$$, $$" + adminMessage.getMessage() + "$$, $$" 
          + adminMessage.getCategory() + "$$);");

      return "redirect:/viewAdminMessages";
    } 
    catch (Exception e) 
    {
      return "error";
    }
  }

  // Shows the table of admin messages
  @RequestMapping("/viewAdminMessages") 
  String viewAdminMessages(Map<String, Object> model, HttpServletRequest request) {

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
      model.put("user", request.getSession().getAttribute("USER"));

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
  String adminMessage(@RequestParam(value = "id", required = false) String tag, Map<String, Object> model, HttpServletRequest request) {

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
      model.put("user", request.getSession().getAttribute("USER"));

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
  public String deleteAllMessages(Map<String, Object> model, HttpServletRequest request) throws Exception {

    try (Connection connection = dataSource.getConnection()) 
    {
          Statement statement = connection.createStatement();
          statement.executeUpdate("DROP TABLE adminMessages;");

          String message = "Succesfully deleted all messages"; 
          model.put("message", message);
          model.put("user", request.getSession().getAttribute("USER"));
          return "redirect:/viewAdminMessages";
    } 
    catch (Exception e) 
    {
      return "error";
    }
  }

   // Prompt for deleting all messages
   @RequestMapping("/deleteAllPosts") 
   String attemptDeletePosts(Map<String, Object> model) {
 
     String message = "Do you wish to delete all posts?";
 
     model.put("message", message);
 
     // Notify users of this when notifications are done in the future
 
     return "deleteAllPosts";
   }
 
   // Clicked on delete all stored messages
   @PostMapping(path = "/deleteAllPosts", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE }, params = "action=delete")
   public String deleteAllPosts(Map<String, Object> model, HttpServletRequest request) throws Exception {
 
     try (Connection connection = dataSource.getConnection()) 
     {
           Statement statement = connection.createStatement();
           statement.executeUpdate("DROP TABLE posts;");
 
           String message = "Succesfully deleted all posts"; 
           model.put("message", message);
           model.put("user", request.getSession().getAttribute("USER"));
           return "redirect:/";
     } 
     catch (Exception e) 
     {
       return "error";
     }
   }
 
   // Cancel and go back to the table
   @PostMapping(path = "/deleteAllPosts", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE }, params = "action=cancel")
   public String cancelDeletePosts(AdminMessage adminMessage) {
 
     return "redirect:/";
   }

  // Cancel and go back to the table
  @PostMapping(path = "/deleteAllMessages", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE }, params = "action=cancel")
  public String cancelDeleteAll(AdminMessage adminMessage) {

    return "redirect:/viewAdminMessages";
  }

  //get a list of accounts for viewAccountsTable.html
  @RequestMapping("/viewAccountsTable")
  String viewAccountsTable(Map<String, Object> model, HttpServletRequest request){
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
      model.put("user", request.getSession().getAttribute("USER"));
      return "viewAccountsTable";
    }
    catch(Exception e){
      model.put("message", e.getMessage());
      return "error";
    }
  }

	//get a list of posts for viewPostsTable.html
  @RequestMapping("/viewPostsTable")
  String viewPostsTable(Map<String, Object> model, HttpServletRequest request){
    try(Connection connection = dataSource.getConnection()){
      Statement statement = connection.createStatement();
      ResultSet rs = statement.executeQuery("SELECT * FROM posts");

      ArrayList<String> ids = new ArrayList<String>();
      ArrayList<String> titles = new ArrayList<String>();
      ArrayList<String> post_dates = new ArrayList<String>();
      ArrayList<String> contents = new ArrayList<String>();
      ArrayList<String> categories = new ArrayList<String>();
      ArrayList<String> visibilities = new ArrayList<String>();
      ArrayList<String> ratings = new ArrayList<String>();
      ArrayList<String> creators = new ArrayList<String>();
      ArrayList<String> mediaTypes = new ArrayList<String>();
      ArrayList<String> imagesNums = new ArrayList<String>();
      ArrayList<String> image00s = new ArrayList<String>();
      ArrayList<String> image01s = new ArrayList<String>();
      ArrayList<String> image02s = new ArrayList<String>();
      ArrayList<String> image03s = new ArrayList<String>();
      ArrayList<String> image04s = new ArrayList<String>();
      ArrayList<String> image05s = new ArrayList<String>();
      ArrayList<String> image06s = new ArrayList<String>();
      ArrayList<String> image07s = new ArrayList<String>();
      ArrayList<String> image08s = new ArrayList<String>();
      ArrayList<String> image09s = new ArrayList<String>();
      ArrayList<String> video00s = new ArrayList<String>();

      while(rs.next()){
        ids.add(String.valueOf(rs.getString("id")));
        titles.add(String.valueOf(rs.getString("title")));
        post_dates.add(String.valueOf(rs.getString("post_date")));
        contents.add(String.valueOf(rs.getString("content")));
        categories.add(String.valueOf(rs.getString("category")));
        visibilities.add(String.valueOf(rs.getString("visibility")));
        ratings.add(String.valueOf(rs.getString("rating")));
        creators.add(String.valueOf(rs.getString("creator")));
        mediaTypes.add(String.valueOf(rs.getString("mediaType")));
        imagesNums.add(String.valueOf(rs.getString("imagesNum")));
        image00s.add(String.valueOf(rs.getString("image00")));
        image01s.add(String.valueOf(rs.getString("image01")));
        image02s.add(String.valueOf(rs.getString("image02")));
        image03s.add(String.valueOf(rs.getString("image03")));
        image04s.add(String.valueOf(rs.getString("image04")));
        image05s.add(String.valueOf(rs.getString("image05")));
        image06s.add(String.valueOf(rs.getString("image06")));
        image07s.add(String.valueOf(rs.getString("image07")));
        image08s.add(String.valueOf(rs.getString("image08")));
        image09s.add(String.valueOf(rs.getString("image09")));
        video00s.add(String.valueOf(rs.getString("video00")));
      }
      model.put("ids", ids);
      model.put("titles", titles);
      model.put("post_dates", post_dates);
      model.put("contents", contents);
      model.put("categories", categories);
      model.put("visibilities", visibilities);
      model.put("ratings", ratings);
      model.put("creators", creators);
      model.put("mediaTypes", mediaTypes);
      model.put("imagesNums", imagesNums);
      model.put("image00s", image00s);
      model.put("image01s", image01s);
      model.put("image02s", image02s);
      model.put("image03s", image03s);
      model.put("image04s", image04s);
      model.put("image05s", image05s);
      model.put("image06s", image06s);
      model.put("image07s", image07s);
      model.put("image08s", image08s);
      model.put("image09s", image09s);
      model.put("video00s", video00s);
      model.put("user", request.getSession().getAttribute("USER"));
      return "viewPostsTable";
    }
    catch(Exception e){
      model.put("message", e.getMessage());
      return "error";
    }
  }

  //get a specific account for viewAccounts
  @RequestMapping("/viewAccount")
  String viewAccount(@RequestParam(value = "id", required = false) String tag, Map<String, Object> model, HttpServletRequest request) {

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

      model.put("user", request.getSession().getAttribute("USER"));
      return "viewAccount";
    } 
    catch (Exception e) 
    {
      model.put("message", e.getMessage());
      return "error";
    }
  }
  

  //Get notifications for logged in user
  @RequestMapping("/notifications")
  String getNotifications(Map<String, Object> model, HttpServletRequest request){
    if(request.getSession().getAttribute("USER") == null){
      return "redirect:/login";
    }
    try(Connection connection = dataSource.getConnection()){
      Statement statement = connection.createStatement();
      statement.executeUpdate("CREATE TABLE IF NOT EXISTS notifications (id serial PRIMARY KEY, title varchar(20), recipient varchar(20), sender varchar(20), body varchar(1000), time timestamp);");
      ResultSet rs = statement.executeQuery("SELECT * FROM notifications WHERE recipient='"+request.getSession().getAttribute("USER")+"'");

      //ArrayList<String> ids = new ArrayList<String>();
      ArrayList<String> titles = new ArrayList<String>();
      // ArrayList<String> recipients = new ArrayList<String>();
      ArrayList<String> senders = new ArrayList<String>();
      ArrayList<String> bodies = new ArrayList<String>();
      ArrayList<String> times = new ArrayList<String>();

      while(rs.next()){
        //ids.add(String.valueOf(rs.getString("id")));
        titles.add(String.valueOf(rs.getString("title")));
        // recipients.add(String.valueOf(rs.getString("recipient")));
        senders.add(String.valueOf(rs.getString("sender")));
        bodies.add(String.valueOf(rs.getString("body")));
        times.add(String.valueOf(rs.getTimestamp("time")));
      }

      //model.put("ids", ids);
      model.put("titles", titles);
      // model.put("recipients", recipients);
      model.put("senders", senders);
      model.put("bodies", bodies);
      model.put("times", times);


      //Form to send notifications
      Notification notification = new Notification();
      model.put("notification", notification);

      model.put("user", request.getSession().getAttribute("USER"));


      return "notifications";
    }
    catch(Exception e){
      model.put("message", e.getMessage());
      return "error";
    }
  }

  //Sending Notifications
  @PostMapping(
    path = "/notifications",
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleNotifications(Map<String, Object> model, Notification n, HttpServletRequest request) throws Exception{
    try(Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();
      //Create table (if it doesn't exist)
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS notifications (id serial PRIMARY KEY, title varchar(20), recipient varchar(20), sender varchar(20), body varchar(1000), time timestamp);");
      //add new notification to table
      String sql = "INSERT INTO notifications (title, recipient, sender, body, time) VALUES ($$" + n.getTitle() + "$$, $$" + n.getRecipient() + "$$, $$" + request.getSession().getAttribute("USER") + "$$, $$" + n.getBody() + "$$, now())";
      stmt.executeUpdate(sql);
      model.put("message", "Notification successfully created");
      return "notifications";
    }
    catch(Exception e){
      model.put("Error", e.getMessage());
      return "error";
    }
  }

  //Profile
  @RequestMapping("/profile")
  String getProfile(@RequestParam(value = "username", required = false) String tag, Map<String, Object> model, HttpServletRequest request){
    if(request.getSession().getAttribute("USER") == null){
      return "redirect:/login";
    }
    try(Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();
      
      //user who's profile is being viewed
      String user = tag;
      //default to current user profile
      if(tag == null || tag == ""){
        user = request.getSession().getAttribute("USER").toString();
      }
      System.out.println("user="+user);
      ResultSet rs = stmt.executeQuery("SELECT * FROM accounts WHERE username='"+user+"'");
      System.out.println("SELECT * FROM accounts WHERE username='"+user+"'");
      rs.next();

      //send information to page
      model.put("username", rs.getString("username"));
      model.put("email", rs.getString("email"));
      model.put("fname", rs.getString("fname"));
      model.put("lname", rs.getString("lname"));

      model.put("user", request.getSession().getAttribute("USER"));
      //check if viewer is the owner of the profile
      if(user == request.getSession().getAttribute("USER").toString()){
        model.put("edit", true);
        Account account = new Account();
        model.put("account", account);
        
        rs = stmt.executeQuery("SELECT * FROM posts WHERE (creator='"+user+"') ORDER BY id DESC");
      }
      else{
        //if viewing someone else's profile
        rs = stmt.executeQuery("SELECT * FROM posts WHERE (creator='"+user+"' AND visibility='PUBLIC') ORDER BY id DESC");
      }
      ArrayList<String> titles = new ArrayList<String>();
      ArrayList<String> descriptions = new ArrayList<String>();
      ArrayList<String> postDates= new ArrayList<String>();
      ArrayList<String> visibilities=new ArrayList<String>();
      ArrayList<String> creators=new ArrayList<String>();
      ArrayList<String> categories=new ArrayList<String>();
      //ArrayList<String[]> likes = new ArrayList<String[]>();
      //ArrayList<String> likesCount = new ArrayList<String>();
      while (rs.next())
      {
        titles.add(rs.getString("title"));
        descriptions.add(rs.getString("content"));
        postDates.add(rs.getString("post_date"));
        visibilities.add(rs.getString("visibility"));
        creators.add(rs.getString("creator"));
        categories.add(rs.getString("category"));
        // Array sqlLikes = rs.getArray("likes");
        // String[] arr = (String[])sqlLikes.getArray();
        //likes.add(arr);
        //likesCount.add(Integer.toString(arr.length));
      }
      model.put("titles", titles);
      model.put("descriptions", descriptions);
      model.put("postDates",postDates);
      model.put("visibilities",visibilities);
      model.put("creators",creators);
      model.put("categories", categories);
      //model.put("likes", likes);
      //model.put("likesCount", likesCount);

      return "profile";
    }
    catch(Exception e){
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @PostMapping(
    path = "/profile",
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleEdit(Map<String, Object> model, Account u, HttpServletRequest request) throws Exception{
    try(Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();

      ResultSet rs;
      //update account to table
      //sql = "UPDATE accounts SET username='" + u.getUsername() + "', password='" + u.getPassword() + "', email='" + u.getEmail() + "', fname='" + u.getFname() + "', lname='" + u.getLname() + "' WHERE id='"+request.getSession().getAttribute("ID")+"'";
      //if any username was inputted
      if(!u.getUsername().isEmpty()){
        //get number of accounts with the same username from accounts table
        String sql = "SELECT COUNT (*) FROM accounts WHERE username = '" + u.getUsername() + "'";
        System.out.println(sql);
        //get number of accounts from resultset
        rs = stmt.executeQuery(sql);
        rs.next();
        int x = rs.getInt("count");
        if(x != 0){ //check if there are is at least 1 account in table
          System.out.println("username taken");
          model.put("taken", "This username has been taken");
          model.put("edit", true);
          Account account = new Account();
          model.put("account", account);

          model.put("username", request.getSession().getAttribute("USER"));
          model.put("email", request.getSession().getAttribute("EMAIL"));
          model.put("fname", request.getSession().getAttribute("FNAME"));
          model.put("lname", request.getSession().getAttribute("LNAME"));
          return "profile"; //if one exists
        }
        stmt.executeUpdate("UPDATE accounts SET username='"+u.getUsername()+"' WHERE id='"+request.getSession().getAttribute("ID")+"'");
        request.getSession().setAttribute("USER", u.getUsername()); //put id into session
      }
      //if any password was inputted
      if(!u.getPassword().isEmpty()){
        stmt.executeUpdate("UPDATE accounts SET password='"+u.getPassword()+"' WHERE id='"+request.getSession().getAttribute("ID")+"'");
      }
      
      //if any email was inputted
      if(!u.getEmail().isEmpty()){
        stmt.executeUpdate("UPDATE accounts SET email='"+u.getEmail()+"' WHERE id='"+request.getSession().getAttribute("ID")+"'");
        request.getSession().setAttribute("EMAIL", u.getEmail()); //put email into session
      }
      //always change fnames and lnames (since they can be blank)
      stmt.executeUpdate("UPDATE accounts SET fname='"+u.getFname()+"' WHERE id='"+request.getSession().getAttribute("ID")+"'");
      stmt.executeUpdate("UPDATE accounts SET lname='"+u.getLname()+"' WHERE id='"+request.getSession().getAttribute("ID")+"'");

      rs = stmt.executeQuery("SELECT fname FROM accounts WHERE username = '" + u.getUsername() + "'");
      request.getSession().setAttribute("FNAME", u.getFname()); //put firstname into session

      rs = stmt.executeQuery("SELECT lname FROM accounts WHERE username = '" + u.getUsername() + "'");
      request.getSession().setAttribute("LNAME", u.getLname()); //put lastname into session


      //stmt.executeUpdate(sql);
      model.put("message", "Account successfully updated");
      // model.put("")
      // model.put("edit", true);
      // Account account = new Account();
      // model.put("account", account);
      
      return "homepage";
    }
    catch(Exception e){
      model.put("Error", e.getMessage());
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
