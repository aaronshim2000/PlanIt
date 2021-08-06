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

import jdk.jfr.Description;

import org.springframework.http.MediaType;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
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
    model.put("role", request.getSession().getAttribute("ROLE"));
    return "homepage";
  }
  
  @RequestMapping("/post")
  String post(Map<String, Object> model, HttpServletRequest request) {
    if(request.getSession().getAttribute("USER") == null){
      return "redirect:/login";
    }
    model.put("user", request.getSession().getAttribute("USER"));
    model.put("role", request.getSession().getAttribute("ROLE"));
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
    model.put("role", request.getSession().getAttribute("ROLE"));
    return "post-text";
  }

  @PostMapping(path = "/post/text", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String submitTextPost(Map<String,Object> model,Post post, HttpServletRequest request) throws Exception {
    try (Connection connection = dataSource.getConnection())
    {
      Statement statement = connection.createStatement();
      statement.executeUpdate("CREATE TABLE IF NOT EXISTS posts (id serial, post_date DATE, creator varchar(20), title varchar(50), content varchar(1600),category varchar(20),visibility varchar(10),rating varchar(5),imagesNum varchar(2), mediaType varchar(7), image00 varchar(200),image01 varchar(200),image02 varchar(200),image03 varchar(200),image04 varchar(200),image05 varchar(200),image06 varchar(200),image07 varchar(200),image08 varchar(200),image09 varchar(200),video00 varchar(200))");
      post.setCategory("text-post");
      String username= (String) request.getSession().getAttribute("USER");
      post.setCreator(username);
      statement.executeUpdate("INSERT INTO posts(post_date,creator,title,content,category,visibility,image00,image01,image02,image03,image04,image05,image06,image07,image08,image09,imagesNum,video00,mediaType) VALUES (now(),$$" + username + "$$, $$" + post.getTitle() + "$$, $$" + post.getDescription() + "$$, '" + post.getCategory() + "','" + post.getVisibility() + "','" + post.getImage00() + "','" + post.getImage01() + "','" + post.getImage02() + "','" + post.getImage03() + "','" + post.getImage04() + "','" + post.getImage05() + "','" + post.getImage06() + "','" + post.getImage07() + "','" + post.getImage08() + "','" + post.getImage09() + "','" + post.getImagesNum() + "','" + post.getVideo00() + "','" + post.getMediaType() + "')");
      //send to friends
      //create notifications
      statement.executeUpdate("CREATE TABLE IF NOT EXISTS notifications (id serial PRIMARY KEY, title varchar(100), recipient varchar(50), sender varchar(50), body varchar(1000), time timestamp);");
      //get id of latest post by user
      ResultSet rs = statement.executeQuery("SELECT * FROM posts WHERE creator=$$"+post.getCreator()+"$$ ORDER BY id DESC");
      rs.next();
      String postID = rs.getString("id");
      //look for all friends of user
      rs = statement.executeQuery("SELECT * FROM friends WHERE username1='"+ post.getCreator() +"' AND isFriend=TRUE");
      ArrayList<String> friends = new ArrayList<String>();
      //for each friend, create notification
      while(rs.next()){
        System.out.println("A");
        friends.add(rs.getString("username2"));

        //create notification
        //statement.executeUpdate("INSERT INTO notifications (title, recipient, sender, body, time) VALUES ($$" + post.getCreator() + " has created a post!$$, $$" + rs.getString("username2") + "$$, $$" + request.getSession().getAttribute("USER") + "$$, $$<a th:href='@{/viewPost(id=${"+postID+"})}'>$$, now())");
        System.out.println("B");
      }
      System.out.println("C");
      for(int i=0;i<friends.size();i++){
        statement.executeUpdate("INSERT INTO notifications (title, recipient, sender, body, time) VALUES ($$" + post.getCreator() + " has created a post!$$, $$" + friends.get(i) + "$$, $$" + request.getSession().getAttribute("USER") + "$$, $$<a href='/viewPost?id="+postID+"'>See Here</a>$$, now())");
      }

      model.put("user", request.getSession().getAttribute("USER"));
      model.put("role", request.getSession().getAttribute("ROLE"));
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
    model.put("role", request.getSession().getAttribute("ROLE"));
    return "post-review";
  }

  @PostMapping(path = "/post/review", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String submitReviewPost(Map<String,Object> model,Post post,HttpServletRequest request) throws Exception {
    try (Connection connection = dataSource.getConnection())
    {
      Statement statement = connection.createStatement();
      statement.executeUpdate("CREATE TABLE IF NOT EXISTS posts (id serial, post_date DATE, creator varchar(20), title varchar(50), content varchar(1600),category varchar(20),visibility varchar(10),rating varchar(5),imagesNum varchar(2), mediaType varchar(7), image00 varchar(200),image01 varchar(200),image02 varchar(200),image03 varchar(200),image04 varchar(200),image05 varchar(200),image06 varchar(200),image07 varchar(200),image08 varchar(200),image09 varchar(200),video00 varchar(200))");
      post.setCategory("review-post");
      String username= (String) request.getSession().getAttribute("USER");
      post.setCreator(username);
      statement.executeUpdate("INSERT INTO posts(post_date,creator,title,content,category,visibility,rating,image00,image01,image02,image03,image04,imagesNum) VALUES (now(),$$" + username + "$$, $$" + post.getTitle() + "$$, $$" + post.getDescription() + "$$, '" + post.getCategory() + "','" + post.getVisibility() + "','" + post.getRating() + "','" + post.getImage00() + "','" + post.getImage01() + "','" + post.getImage02() + "','" + post.getImage03() + "','" + post.getImage04() + "','" + post.getImagesNum() + "')");
      model.put("user", request.getSession().getAttribute("USER"));
      model.put("role", request.getSession().getAttribute("ROLE"));
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
      statement.executeUpdate("CREATE TABLE IF NOT EXISTS posts (id serial, post_date DATE, creator varchar(20), title varchar(50), content varchar(1600),category varchar(20),visibility varchar(10),rating varchar(5),imagesNum varchar(2), mediaType varchar(7), image00 varchar(200),image01 varchar(200),image02 varchar(200),image03 varchar(200),image04 varchar(200),image05 varchar(200),image06 varchar(200),image07 varchar(200),image08 varchar(200),image09 varchar(200),video00 varchar(200))");
      post.setCategory("plan-post");
      String username= (String) request.getSession().getAttribute("USER");
      post.setCreator(username);
      statement.executeUpdate("INSERT INTO posts(post_date,creator,title,content,category,visibility) VALUES (now(),$$" + username + "$$,$$" + post.getTitle() + "$$, $$" + post.getDescription() + "$$, '" + post.getCategory() + "','" + post.getVisibility() + "')");
      //send notification to friends
      //statement.executeupdate("INSERT INTO notifications (title, recipient, sender, body, time) VALUES ($$New Post from" + post.getCreator() + "$$, $$FRIENDS$$, $$" + request.getSession().getAttribute("USER") + "$$, $$View Post$$, now())");
      model.put("user", request.getSession().getAttribute("USER"));
      model.put("role", request.getSession().getAttribute("ROLE"));
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
      ResultSet rs = stmt.executeQuery("SELECT * FROM posts WHERE (category='text-post' AND visibility='PUBLIC') OR (category='text-post' AND creator='" + username + "' AND visibility='PRIVATE') ORDER BY id DESC");
      ArrayList<String> text_ids = new ArrayList<String>();
      ArrayList<String> text_titles = new ArrayList<String>();
      ArrayList<String> text_descriptions = new ArrayList<String>();
      ArrayList<String> text_postDates= new ArrayList<String>();
      ArrayList<String> text_visibilities=new ArrayList<String>();
      ArrayList<String> text_creators=new ArrayList<String>();
      while (rs.next())
      {
        text_ids.add(Integer.toString(rs.getInt("id")));
        text_titles.add(rs.getString("title"));
        text_descriptions.add(rs.getString("content"));
        text_postDates.add(rs.getString("post_date"));
        text_visibilities.add(rs.getString("visibility"));
        text_creators.add(rs.getString("creator"));
      }
      model.put("text_ids", text_ids);
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
      String username= (String) request.getSession().getAttribute("USER");
      ResultSet rs = stmt.executeQuery("SELECT * FROM posts WHERE (category='review-post' AND visibility='PUBLIC') OR (category='review-post' AND creator='" + username + "' AND visibility='PRIVATE') ORDER BY id DESC");
      ArrayList<String> review_ids = new ArrayList<String>();
      ArrayList<String> review_titles = new ArrayList<String>();
      ArrayList<String> review_descriptions = new ArrayList<String>();
      ArrayList<String> review_ratings=new ArrayList<String>();
      ArrayList<String> review_postDates=new ArrayList<String>();
      ArrayList<String> review_visibilities=new ArrayList<String>();
      ArrayList<String> review_creators=new ArrayList<String>();
      while (rs.next())
      {
        review_ids.add(Integer.toString(rs.getInt("id")));
        review_titles.add(rs.getString("title"));
        review_descriptions.add(rs.getString("content"));
        review_ratings.add(rs.getString("rating"));
        review_postDates.add(rs.getString("post_date"));
        review_visibilities.add(rs.getString("visibility"));
        review_creators.add(rs.getString("creator"));
      }
      model.put("review_ids", review_ids);
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
      String username= (String) request.getSession().getAttribute("USER");
      ResultSet rs = stmt.executeQuery("SELECT * FROM posts WHERE (category='plan-post' AND visibility='PUBLIC') OR (category='plan-post' AND creator='" + username + "' AND visibility='PRIVATE') ORDER BY id DESC");
      ArrayList<String> plan_ids = new ArrayList<String>();
      ArrayList<String> plan_titles = new ArrayList<String>();
      ArrayList<String> plan_descriptions = new ArrayList<String>();
      ArrayList<String> plan_postDates = new ArrayList<String>();
      ArrayList<String> plan_visibilities = new ArrayList<String>();
      ArrayList<String> plan_creators = new ArrayList<String>();
      while (rs.next())
      {
        plan_ids.add(Integer.toString(rs.getInt("id")));
        plan_titles.add(rs.getString("title"));
        plan_descriptions.add(rs.getString("content"));
        plan_postDates.add(rs.getString("post_date"));
        plan_visibilities.add(rs.getString("visibility"));
        plan_creators.add(rs.getString("creator"));
      }
      model.put("plan_ids", plan_ids);
      model.put("plan_titles", plan_titles);
      model.put("plan_descriptions", plan_descriptions);
      model.put("plan_postDates",plan_postDates);
      model.put("plan_visibilities",plan_visibilities);
      model.put("plan_creators",plan_creators);
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
      ArrayList<String> user_ids = new ArrayList<String>(); 
      while (rs.next()){
        usernames.add(String.valueOf(rs.getString("username")));
        user_ids.add(String.valueOf(rs.getInt("id")));
      }
      model.put("usernames", usernames);
      model.put("user_ids", user_ids);
    }
    catch (Exception e)
    {
      model.put("message", e.getMessage());
      return "error";
    }

    model.put("user", request.getSession().getAttribute("USER"));
    model.put("role", request.getSession().getAttribute("ROLE"));
    return "scrollingFeed";
  }

  // Editing text post
  @RequestMapping("/editTextPost")
  String editTextPost(@RequestParam(value = "id", required = false) String tag, Map<String, Object> model, HttpServletRequest request){
    if(request.getSession().getAttribute("USER") == null){
      return "redirect:/login";
    }
    Post post = new Post();
    model.put("post", post);

    try(Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();
      
      ResultSet rs = stmt.executeQuery("SELECT * FROM posts WHERE id = " + tag + ";");
      while (rs.next()) 
      {
        model.put("tag", tag);
        model.put("title", rs.getString("title"));
        model.put("description", rs.getString("content"));
        model.put("visibility", rs.getString("visibility"));
        model.put("image00", rs.getString("image00"));
        model.put("image01", rs.getString("image01"));
        model.put("image02", rs.getString("image02"));
        model.put("image03", rs.getString("image03"));
        model.put("image04", rs.getString("image04"));
        model.put("image05", rs.getString("image05"));
        model.put("image06", rs.getString("image06"));
        model.put("image07", rs.getString("image07"));
        model.put("image08", rs.getString("image08"));
        model.put("image09", rs.getString("image09"));
        model.put("video00", rs.getString("video00"));
        model.put("imagesNum", rs.getString("imagesNum"));
        model.put("mediaType", rs.getString("mediaType"));
        model.put("user", request.getSession().getAttribute("USER"));
        model.put("role", request.getSession().getAttribute("ROLE"));
      }
      
      return "editTextPost";
    }
    catch(Exception e){
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @PostMapping(
    path = "/editTextPost",
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleEditTextPost(Map<String, Object> model, Post p, @RequestParam("id") int id, HttpServletRequest request) throws Exception{
    try(Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();

      String postId = Integer.toString(id);

      if(!p.getTitle().isEmpty()){
        stmt.executeUpdate("UPDATE posts SET title='" + p.getTitle() + "' WHERE id='" + postId + "'");
      }
      if(!p.getDescription().isEmpty()){
        stmt.executeUpdate("UPDATE posts SET content='" + p.getDescription() + "' WHERE id='" + postId + "'");
      }
      if(!p.getVisibility().isEmpty()){
        stmt.executeUpdate("UPDATE posts SET visibility='" + p.getVisibility() + "' WHERE id='" + postId + "'");
      }
      
      return "redirect:/scrollingFeed";
    }
    catch(Exception e){
      model.put("Error", e.getMessage());
      return "error";
    }
  }

  // Editing review post
  @RequestMapping("/editReviewPost")
  String editReviewPost(@RequestParam(value = "id", required = false) String tag, Map<String, Object> model, HttpServletRequest request){
    if(request.getSession().getAttribute("USER") == null){
      return "redirect:/login";
    }

    Post post = new Post();
    model.put("post", post);
    
    try(Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();

      ResultSet rs = stmt.executeQuery("SELECT * FROM posts WHERE id = " + tag + ";");
      while (rs.next()) 
      {
        model.put("tag", tag);
        model.put("title", rs.getString("title"));
        model.put("description", rs.getString("content"));
        model.put("rating", rs.getString("rating"));
        model.put("visibility", rs.getString("visibility"));
        model.put("user", request.getSession().getAttribute("USER"));
        model.put("role", request.getSession().getAttribute("ROLE"));
      }
      
      return "editReviewPost";
    }
    catch(Exception e){
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @PostMapping(
    path = "/editReviewPost",
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleEditReviewPost(Map<String, Object> model, Post p, @RequestParam("id") int id, HttpServletRequest request) throws Exception{
    try(Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();

      String postId = Integer.toString(id);

      if(!p.getTitle().isEmpty()){
        stmt.executeUpdate("UPDATE posts SET title='" + p.getTitle() + "' WHERE id='" + postId + "'");
      }
      if(!p.getDescription().isEmpty()){
        stmt.executeUpdate("UPDATE posts SET content='" + p.getDescription() + "' WHERE id='" + postId + "'");
      }
      if(!p.getRating().isEmpty()){
        stmt.executeUpdate("UPDATE posts SET rating='" + p.getRating() + "' WHERE id='" + postId + "'");
      }
      if(!p.getVisibility().isEmpty()){
        stmt.executeUpdate("UPDATE posts SET visibility='" + p.getVisibility() + "' WHERE id='" + postId + "'");
      }
      
      return "redirect:/scrollingFeed";
    }
    catch(Exception e){
      model.put("Error", e.getMessage());
      return "error";
    }
  }

  // Editing planning post
  @RequestMapping("/editPlanPost")
  String editPlanPost(@RequestParam(value = "id", required = false) String tag, Map<String, Object> model, HttpServletRequest request){
    if(request.getSession().getAttribute("USER") == null){
      return "redirect:/login";
    }

    Post post = new Post();
    model.put("post", post);

    try(Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();

      ResultSet rs = stmt.executeQuery("SELECT * FROM posts WHERE id = " + tag + ";");
      while (rs.next()) 
      {
        model.put("tag", tag);
        model.put("title", rs.getString("title"));
        model.put("description", rs.getString("content"));
        model.put("visibility", rs.getString("visibility"));
        model.put("user", request.getSession().getAttribute("USER"));
        model.put("role", request.getSession().getAttribute("ROLE"));
      }

      return "editPlanPost";
    }
    catch(Exception e){
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @PostMapping(
    path = "/editPlanPost",
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleEditPlanPost(Map<String, Object> model, Post p, @RequestParam("id") int id, HttpServletRequest request) throws Exception{
    try(Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();

      String postId = Integer.toString(id);

      if(!p.getTitle().isEmpty()){
        stmt.executeUpdate("UPDATE posts SET title='" + p.getTitle() + "' WHERE id='" + postId + "'");
      }
      if(!p.getDescription().isEmpty()){
        stmt.executeUpdate("UPDATE posts SET content='" + p.getDescription() + "' WHERE id='" + postId + "'");
      }
      if(!p.getVisibility().isEmpty()){
        stmt.executeUpdate("UPDATE posts SET visibility='" + p.getVisibility() + "' WHERE id='" + postId + "'");
      }
      
      return "redirect:/scrollingFeed";
    }
    catch(Exception e){
      model.put("Error", e.getMessage());
      return "error";
    }
  }

  @RequestMapping("/deletePost") 
  String deletePost(@RequestParam(value = "id", required = false) String tag, Map<String, Object> model) {
    String message = "Do you wish to delete your post?";

    Post post = new Post();
    model.put("post", post);
    model.put("message", message);
    model.put("tag", tag);

    return "deletePost";
  }

  @PostMapping(path = "/deletePost", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE }, params = "action=delete")
  public String confirmedDeletePost(Map<String, Object> model, Post post, @RequestParam("id") int id) throws Exception {

    try (Connection connection = dataSource.getConnection()) 
    {
          Statement statement = connection.createStatement();
          statement.executeUpdate("DELETE FROM posts WHERE id = " + id + ";");

          String message = "Succesfully deleted post"; 
          model.put("message", message);

          model.put("tag", Integer.toString(id));
          return "redirect:/scrollingFeed";
    } 
    catch (Exception e) 
    {
      return "error";
    }
  }

  @PostMapping(path = "/deletePost", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE }, params = "action=cancel")
  public String cancelDeletePost(Post post) {

    return "redirect:/scrollingFeed";
  }

  @RequestMapping("/reportPost") 
  String report(@RequestParam(value = "id", required = false) String tag, Map<String, Object> model, HttpServletRequest request) {
    AdminMessage adminMessage = new AdminMessage();
    model.put("adminMessage", adminMessage);
    model.put("tag", tag);
    model.put("user", request.getSession().getAttribute("USER"));
    model.put("role", request.getSession().getAttribute("ROLE"));
    model.put("email", request.getSession().getAttribute("EMAIL"));
    return "reportPost";
  }

  // Submits the report post form
  @PostMapping(path = "/reportPost", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String reportSent(AdminMessage adminMessage, @RequestParam("id") int id, Map<String, Object> model) throws Exception {

    try (Connection connection = dataSource.getConnection()) 
    {
      Statement statement = connection.createStatement();

      String postId = Integer.toString(id);

      statement.executeUpdate(
          "CREATE TABLE IF NOT EXISTS adminMessages (id serial PRIMARY KEY, username varchar(20), email varchar(100), "
              + "message varchar(1000), category varchar(20), postId varchar(10));");

      adminMessage.setCategory("report");

      statement.executeUpdate(
          "INSERT INTO adminMessages(username, email, message, category, postId) VALUES ($$" 
          + adminMessage.getUsername() + "$$, $$" + adminMessage.getEmail() + "$$, $$" + adminMessage.getMessage() + "$$, $$" 
          + adminMessage.getCategory() + "$$, $$" + postId + "$$);");

      model.put("message", "Thank you for reporting.");

      return "redirect:/";
    } 
    catch (Exception e) 
    {
      return "error";
    }
  }

  @RequestMapping("/costCalculator")
  String costCalculator(Map<String, Object> model, HttpServletRequest request) {
    model.put("user", request.getSession().getAttribute("USER"));
    model.put("role", request.getSession().getAttribute("ROLE"));
    return "costCalculator";
  }

  @RequestMapping("/flightSearch")
  String flightSearch(Map<String, Object> model, HttpServletRequest request) {
    model.put("user", request.getSession().getAttribute("USER"));
    model.put("role", request.getSession().getAttribute("ROLE"));
    return "flightSearch";
  }
  
  @RequestMapping("/map")
  String postMap(Map<String, Object> model, HttpServletRequest request) {
    model.put("user", request.getSession().getAttribute("USER"));
    model.put("role", request.getSession().getAttribute("ROLE"));
    return "map";
  }

  @RequestMapping("/register")
  String register(Map<String, Object> model, HttpServletRequest request){
    Account account = new Account();
    model.put("account", account);
    model.put("user", request.getSession().getAttribute("USER"));
    model.put("role", request.getSession().getAttribute("ROLE"));
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
      model.put("role", request.getSession().getAttribute("ROLE"));
      request.getSession().setAttribute("login", "OK");
      if(request.getSession().getAttribute("target")!=null){
        model.put("message","You can access the page now.");
        return "homepage";
      }

      model.put("message", "Welcome, " + request.getSession().getAttribute("USER"));
      model.put("role", request.getSession().getAttribute("ROLE"));
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
    model.put("role", request.getSession().getAttribute("ROLE"));
    return("homepage");
  }

  @RequestMapping("/forgot")
  String forgot(Map<String, Object> model, HttpServletRequest request){
    Account account = new Account();
    model.put("account", account);
    model.put("user", request.getSession().getAttribute("USER"));
    model.put("role", request.getSession().getAttribute("ROLE"));
    return "forgot";
  }

  @RequestMapping("/contact") 
  String contact(Map<String, Object> model, HttpServletRequest request) {
    AdminMessage adminMessage = new AdminMessage();
    model.put("adminMessage", adminMessage);
    model.put("user", request.getSession().getAttribute("USER"));
    model.put("role", request.getSession().getAttribute("ROLE"));
    model.put("email", request.getSession().getAttribute("EMAIL"));
    return "contact";
  }

  // Submits the contact us form
  @PostMapping(path = "/contact", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
  public String contactSent(AdminMessage adminMessage, Map<String, Object> model) throws Exception {

    try (Connection connection = dataSource.getConnection()) 
    {
      Statement statement = connection.createStatement();
      statement.executeUpdate(
        "CREATE TABLE IF NOT EXISTS adminMessages (id serial PRIMARY KEY, username varchar(20), email varchar(100), "
            + "message varchar(1000), category varchar(20), postId varchar(10));");

      adminMessage.setCategory("contact");
      
      statement.executeUpdate(
          "INSERT INTO adminMessages(username, email, message, category) VALUES ($$" 
          + adminMessage.getUsername() + "$$, $$" + adminMessage.getEmail() + "$$, $$" + adminMessage.getMessage() + "$$, $$" 
          + adminMessage.getCategory() + "$$);");

      model.put("message", "Contact sent.");
      return "redirect:/contact";
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
              + "message varchar(1000), category varchar(20), postId varchar(10));");

      ResultSet rs = statement.executeQuery("SELECT * FROM adminMessages");

      ArrayList<String> ids = new ArrayList<String>();
      ArrayList<String> usernames = new ArrayList<String>();
      ArrayList<String> emails = new ArrayList<String>();
      ArrayList<String> categories = new ArrayList<String>();
      ArrayList<String> postIds = new ArrayList<String>();

      while (rs.next()) 
      {
        ids.add(String.valueOf(rs.getString("id")));
        usernames.add(rs.getString("username"));
        emails.add(rs.getString("email"));
        categories.add(rs.getString("category"));
        postIds.add(rs.getString("postId"));
      }

      model.put("ids", ids);
      model.put("usernames", usernames);
      model.put("emails", emails);
      model.put("categories", categories);
      model.put("postIds", postIds);
      model.put("user", request.getSession().getAttribute("USER"));
      model.put("role", request.getSession().getAttribute("ROLE"));

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

      Notification notification = new Notification();

      while (rs.next()) 
      {
        model.put("id", String.valueOf(rs.getInt("id")));
        model.put("username", rs.getString("username"));
        model.put("email", rs.getString("email"));
        model.put("message", rs.getString("message"));
        model.put("category", rs.getString("category"));
        model.put("postId", rs.getString("postId"));

        notification.setRecipient(rs.getString("username"));
      }
      
      model.put("notification", notification);
      model.put("user", request.getSession().getAttribute("USER"));
      model.put("role", request.getSession().getAttribute("ROLE"));
      return "adminMessage";
    } 
    catch (Exception e) 
    {
      model.put("message", e.getMessage());
      return "error";
    }
  }
  //Sending Replies to admin messages
  @PostMapping(
    path = "/adminMessage",
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleAdminReply(Map<String, Object> model, Notification n, String username, String title, HttpServletRequest request) throws Exception{
    try(Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();
      //Create table (if it doesn't exist)
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS notifications (id serial PRIMARY KEY, title varchar(100), recipient varchar(50), sender varchar(50), body varchar(1000), time timestamp);");

      //add new notification to table
      String sql = "INSERT INTO notifications (title, recipient, sender, body, time) VALUES ($$" + n.getTitle() + "$$, $$" + n.getRecipient() + "$$, $$" + request.getSession().getAttribute("USER") + "$$, $$" + n.getBody() + "$$, now())";
      stmt.executeUpdate(sql);
      model.put("message", "Notification successfully sent");
      return "homepage";
    }
    catch(Exception e){
      model.put("Error", e.getMessage());
      return "error";
    }
  }

  // Prompt for deleting all messages
  @RequestMapping("/deleteAllMessages") 
  String attemptDeleteAll(Map<String, Object> model, HttpServletRequest request) {
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
          model.put("role", request.getSession().getAttribute("ROLE"));
          return "redirect:/viewAdminMessages";
    } 
    catch (Exception e) 
    {
      return "error";
    }
  }

   // Prompt for deleting all messages
   @RequestMapping("/deleteAllPosts") 
   String attemptDeletePosts(Map<String, Object> model, HttpServletRequest request) {
     String message = "Do you wish to delete all posts?";
     model.put("message", message);
 
     // Notify users of this when notifications are done in the future ***************************************************************
 
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
           model.put("role", request.getSession().getAttribute("ROLE"));
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
      model.put("role", request.getSession().getAttribute("ROLE"));
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
      model.put("role", request.getSession().getAttribute("ROLE"));
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
      model.put("role", request.getSession().getAttribute("ROLE"));
      return "viewAccount";
    } 
    catch (Exception e) 
    {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @RequestMapping("/viewPost")
  String viewPost(@RequestParam(value = "id", required = false) String tag, Map<String, Object> model, HttpServletRequest request) {

    try (Connection connection = dataSource.getConnection()) 
    {
      Statement statement = connection.createStatement();

      ResultSet rs = statement.executeQuery("SELECT * FROM posts WHERE id=" + tag);
      rs.next();
      String creator = rs.getString("creator");
      //check if user should have access when private
      System.out.println(rs.getString("visibility"));
      if(rs.getString("visibility").equals("PRIVATE")){
        System.out.println("Private post");
        //get id of post creator
        ResultSet rsAccount = statement.executeQuery("SELECT * FROM accounts WHERE username=$$"+ rs.getString("creator") +"$$");
        rsAccount.next();
        String creatorId = String.valueOf(rsAccount.getInt("id"));
        
        //if id of user is not the same as its creator  AND  role is not admin AND  user is not a friend, Deny access, go to homepage
        if(!(creatorId.equals(request.getSession().getAttribute("ID"))) && !(request.getSession().getAttribute("ROLE").equals("admin"))){
          System.out.println("NOT CREATOR OR ADMIN");
          //check if the creator is a friend of the user
          ResultSet rsFriend = statement.executeQuery("SELECT * FROM friends WHERE username1='"+ creator +"' AND isFriend=TRUE");
          int found = 0;
          while(rsFriend.next()){
            if(rsFriend.getString("username2").equals(request.getSession().getAttribute("USER"))){
              found = 1;
              break;
            }
          }
          //if not a friend
          if(found == 0){
            System.out.println("DENIED");
            model.put("message", "You do not have access to that post");
            model.put("user", request.getSession().getAttribute("USER"));
            model.put("role", request.getSession().getAttribute("ROLE"));
            return "homepage";
          }
        }
      }
      System.out.println("GRANTED");
      rs = statement.executeQuery("SELECT * FROM posts WHERE id=" + tag);
      rs.next();
      model.put("id", String.valueOf(rs.getInt("id")));
      model.put("title", rs.getString("title"));
      model.put("post_date", rs.getString("post_date"));
      model.put("content", rs.getString("content"));
      model.put("category", rs.getString("category"));
      model.put("visibility", rs.getString("visibility"));
      model.put("rating", rs.getString("rating"));
      model.put("creator", rs.getString("creator"));
      model.put("mediaType", rs.getString("mediaType"));
      model.put("imagesNum", rs.getString("imagesNum"));
      model.put("image00", rs.getString("image00"));
      model.put("image01", rs.getString("image01"));
      model.put("image02", rs.getString("image02"));
      model.put("image03", rs.getString("image03"));
      model.put("image04", rs.getString("image04"));
      model.put("image05", rs.getString("image05"));
      model.put("image06", rs.getString("image06"));
      model.put("image07", rs.getString("image07"));
      model.put("image08", rs.getString("image08"));
      model.put("image09", rs.getString("image09"));
      model.put("video00", rs.getString("video00"));
      

      model.put("user", request.getSession().getAttribute("USER"));
      model.put("role", request.getSession().getAttribute("ROLE"));
      return "viewPost";
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
      statement.executeUpdate("CREATE TABLE IF NOT EXISTS notifications (id serial PRIMARY KEY, title varchar(100), recipient varchar(50), sender varchar(50), body varchar(1000), time timestamp);");
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
      model.put("role", request.getSession().getAttribute("ROLE"));


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
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS notifications (id serial PRIMARY KEY, title varchar(100), recipient varchar(50), sender varchar(50), body varchar(1000), time timestamp);");
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

  @RequestMapping("/friend")
  String getFriendList(Map<String, Object> model, HttpServletRequest request){
    if(request.getSession().getAttribute("USER") == null){
      return "redirect:/login";
    }
    try(Connection connection = dataSource.getConnection()){
      
      Statement stmt = connection.createStatement();
      //Create table (if it doesn't exist)
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS friends (id serial PRIMARY KEY, username1 varchar(20), username2 varchar(20), isFriend boolean, time timestamp, CONSTRAINT constraint_name UNIQUE(username1, username2));");
      
      ResultSet rs = stmt.executeQuery("SELECT * FROM friends WHERE username1='"+request.getSession().getAttribute("USER")+"' AND isFriend=TRUE");
     
      ArrayList<HashMap> output = new ArrayList<HashMap>();
      while (rs.next()) {
        HashMap<String, String> friendsList = new HashMap<String, String>();
        String friendUsername = rs.getString("username2");
        Timestamp ts1 = rs.getTimestamp("time");
        String timestamp  = ts1.toString();
        friendsList.put("friendUsername", friendUsername);
        friendsList.put("timestamp", timestamp);

        output.add(friendsList);

      }
      model.put("records", output);
      model.put("user", request.getSession().getAttribute("USER"));
      model.put("role", request.getSession().getAttribute("ROLE"));
      return "friend";

    }
    catch(Exception e){
      model.put("Error", e.getMessage());
      return "error";
    }

  }
  
  @PostMapping(
    path = "/viewAccount/addFriend",
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleAddFriend(@RequestParam(value = "friendUsername", required = false) String user2, Map<String, Object> model, Friends f, HttpServletRequest request) throws Exception{
    try(Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();
      //Create table (if it doesn't exist)
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS friends (id serial PRIMARY KEY, username1 varchar(20), username2 varchar(20), isFriend boolean, time timestamp, CONSTRAINT constraint_name UNIQUE(username1, username2));");
      if(!user2.equals(request.getSession().getAttribute("USER"))){
        String sql = "INSERT INTO friends (username1, username2, isFriend, time) VALUES ('" + request.getSession().getAttribute("USER") + "', '" + user2 + "', TRUE , now()) ON CONFLICT ON CONSTRAINT constraint_name DO UPDATE SET isFriend = TRUE, time = now();";
        System.out.println(sql);
        stmt.executeUpdate(sql);
        sql = "INSERT INTO friends (username1, username2, isFriend, time) VALUES ('" + user2 + "', '" + request.getSession().getAttribute("USER") + "', TRUE , now())ON CONFLICT ON CONSTRAINT constraint_name DO UPDATE SET isFriend = TRUE, time = now();";
        System.out.println(sql);
        stmt.executeUpdate(sql);
        model.put("message", "Friend successfully added");
        model.put("user", request.getSession().getAttribute("USER"));
        model.put("role", request.getSession().getAttribute("ROLE"));
        return "redirect:/";
      }
      else{
        System.out.println(request.getSession().getAttribute("USER"));
        return "redirect:/";
      }
    }
    catch(SQLException e){
      e.printStackTrace();
      model.put("Error", e.getMessage());
      return "error";
    }
  }
  
  @PostMapping(
    path = "/viewAccount/removeFriend",
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleRemoveFriend(@RequestParam(value = "friendUsername", required = false) String user2, Map<String, Object> model, Friends f, HttpServletRequest request) throws Exception{
    try(Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();
      //Create table (if it doesn't exist)
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS friends (id serial PRIMARY KEY, username1 varchar(20), username2 varchar(20), isFriend boolean, time timestamp, CONSTRAINT constraint_name UNIQUE(username1, username2));");
      if(!user2.equals(request.getSession().getAttribute("USER"))){
        String sql = "UPDATE friends SET isFriend=FALSE, time=now() WHERE username1='" + request.getSession().getAttribute("USER") +"' AND username2='" + user2 +"';";
        System.out.println(sql);
        stmt.executeUpdate(sql);
        sql = "UPDATE friends SET isFriend=FALSE, time=now() WHERE username1='" + user2 +"' AND username2='" + request.getSession().getAttribute("USER") +"';";
        System.out.println(sql);
        stmt.executeUpdate(sql);
        model.put("message", "Friend successfully removed");
        model.put("user", request.getSession().getAttribute("USER"));
        model.put("role", request.getSession().getAttribute("ROLE"));
        return "redirect:/";
      }
      else{
        System.out.println(request.getSession().getAttribute("USER"));
        return "redirect:/";
      }
    }
    catch(SQLException e){
      e.printStackTrace();
    }
    return "error";
  }

  //Profile
  @RequestMapping("/profile")
  String getProfile(@RequestParam(value = "username", required = false) String tag, Map<String, Object> model, HttpServletRequest request){
    try(Connection connection = dataSource.getConnection()){
      Statement stmt = connection.createStatement();
      if(request.getSession().getAttribute("USER") == null){
        return "redirect:/login";
      }
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
      while (rs.next())
      {
        titles.add(rs.getString("title"));
        descriptions.add(rs.getString("content"));
        postDates.add(rs.getString("post_date"));
        visibilities.add(rs.getString("visibility"));
        creators.add(rs.getString("creator"));
        categories.add(rs.getString("category"));
      }
      model.put("titles", titles);
      model.put("descriptions", descriptions);
      model.put("postDates",postDates);
      model.put("visibilities",visibilities);
      model.put("creators",creators);
      model.put("categories", categories);
      model.put("user", request.getSession().getAttribute("USER"));
      model.put("role", request.getSession().getAttribute("ROLE"));
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

          model.put("user", request.getSession().getAttribute("USER"));
          model.put("role", request.getSession().getAttribute("ROLE"));
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
      model.put("user", request.getSession().getAttribute("USER"));
      model.put("role", request.getSession().getAttribute("ROLE"));
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
