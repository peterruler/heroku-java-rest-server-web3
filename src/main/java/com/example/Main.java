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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.awt.*;
import java.sql.*;
import java.util.List;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Map;

@RestController
@SpringBootApplication
public class Main {

  class Project {
    private double id;
    private String client_id;
    private String title;
    private boolean active;

    public double getId() {
      return id;
    }

    public void setId(double id) {
      this.id = id;
    }

    public String getClient_id() {
      return client_id;
    }

    public void setClient_id(String client_id) {
      this.client_id = client_id;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public boolean isActive() {
      return active;
    }

    public void setActive(boolean active) {
      this.active = active;
    }

  }
  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Autowired
  private DataSource dataSource;

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);
  }

  @RequestMapping("/")
  String index() {
    return "index";
  }

  @PostMapping("/api/projects")
  String addProject(@RequestBody Map<String, Object> project)   throws Exception {
    ArrayList<Project> output = new ArrayList<Project>();
    System.out.println(project);
    String postSql = "INSERT INTO Project (id, client_id, title, active) VALUES(?,?,?,?)";
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement pstmt = connection.prepareStatement(postSql);

      pstmt.setInt(1, (Integer) project.get("id"));
      pstmt.setString(2, (String) project.get("client_id"));
      pstmt.setString(3, (String) project.get("title"));
      pstmt.setBoolean(4, (Boolean) project.get("active"));
      pstmt.executeUpdate();



    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    /*
    Project proj = new Project();
    proj.setId(id);
    proj.setClient_id(client_id);
    proj.setTitle(title);
    proj.setActive(active);
    output.add(proj);
    */
    return "foo";

  }

  @GetMapping("/api/projects")
  List db(Map<String, Object> model) {
    ArrayList<Project> output = new ArrayList<Project>();
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      /*stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Project (" +
              "id double precision PRIMARY KEY," +
              "client_id VARCHAR ( 255 ) UNIQUE NOT NULL," +
              "title VARCHAR ( 255 ) NOT NULL," +
              "active boolean NOT NULL" +
              ");");
       */
      //stmt.executeUpdate("INSERT INTO Project VALUES (1,'bar','foo',true)");
      ResultSet rs = stmt.executeQuery("SELECT id, client_id, title, active FROM Project");


      while (true) {
        try {
          if (!rs.next()) break;
        } catch (SQLException e) {
          e.printStackTrace();
        }
        Project proj = new Project();
        proj.setId(rs.getInt("id"));
        proj.setClient_id(rs.getString("client_id"));
        proj.setTitle(rs.getString("title"));
        proj.setActive(rs.getBoolean("active"));

        output.add(proj);

        model.put("records", output);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return output;
  }

    @Bean
  public DataSource dataSource() throws SQLException {
    if (dbUrl == null || dbUrl.isEmpty()) {
      return new HikariDataSource();
    } else {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(dbUrl);

      String un = System.getenv().get("username");
      config.setUsername(un);
      String pw = System.getenv().get("password");
      config.setPassword(pw);

      return new HikariDataSource(config);
    }
  }

}
