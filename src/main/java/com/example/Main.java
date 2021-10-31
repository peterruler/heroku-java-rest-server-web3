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

import com.sun.org.apache.xpath.internal.operations.Bool;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;

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
        private Object active;

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

        public Object isActive() {
            return active;
        }

        public void setActive(Object active) {
            this.active = active;
        }

        @Override
        public String toString() {
            return "{\"id\":\"" + id + "\",\"client_id\":\"" + client_id + "\",\"title\":\"" + title + "\",\"active\":\"" + active + "\"}";
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
        return "POST JSON: /api/projects & GET: /api/projects/{id} & PUT JSON: /api/projects & GET: /api/projects";
    }

    /**
     * Project Endpoints
     */

    @RequestMapping(
            value = "/api/projects/{id}",
            produces = "application/json",
            method = {RequestMethod.DELETE})
    boolean deleteProject(@PathVariable int id) throws Exception {

        String deleteSql = "DELETE FROM Project WHERE id=?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(deleteSql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @RequestMapping(
            value = "/api/projects/{id}",
            produces = "application/json",
            method = {RequestMethod.PUT})
    List<Project> updateProject(@PathVariable int id, @RequestBody Map<String, Object> project) throws Exception {
        ArrayList<Project> output = new ArrayList<Project>();

        String client_id = (String) project.get("client_id");
        String title = (String) project.get("title");
        Object active = project.get("active");

        String putSql = "UPDATE Project SET client_id=?, title=?, active=? WHERE id=?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(putSql);
            pstmt.setString(1, client_id);
            pstmt.setString(2, title);
            pstmt.setBoolean(3, (Boolean) active);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Project proj = new Project();
        proj.setId(id);
        proj.setClient_id(client_id);
        proj.setTitle(title);
        proj.setActive((Boolean) active);
        output.add(proj);
        return output;
    }

    @PostMapping("/api/projects")
    List addProject(@RequestBody Map<String, Object> project) throws Exception {
        ArrayList<Project> output = new ArrayList<Project>();

        int id = (Integer) project.get("id");
        String client_id = (String) project.get("client_id");
        String title = (String) project.get("title");
        Object active = project.get("active");

        String postSql = "INSERT INTO Project (id, client_id, title, active) VALUES(?,?,?,?)";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(postSql);
            pstmt.setInt(1, id);
            pstmt.setString(2, client_id);
            pstmt.setString(3, title);
            pstmt.setBoolean(4, (Boolean) active);
            pstmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Project proj = new Project();
        proj.setId(id);
        proj.setClient_id(client_id);
        proj.setTitle(title);
        proj.setActive((Boolean) active);
        output.add(proj);
        return output;
    }

    @RequestMapping(
            value = "/api/projects/{id}",
            method = {RequestMethod.GET})
    public String getProjectById(@PathVariable int id) {

        Project proj = new Project();
        String getSql = "SELECT id, client_id, title, active FROM Project WHERE id=? LIMIT 1";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(getSql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            int id2 = 0;
            String client_id = "";
            String title = "";
            Boolean active = false;
            while (rs.next()) {
                id2 = rs.getInt("id");
                client_id = rs.getString("client_id");
                title = rs.getString("title");
                active = rs.getBoolean("active");
                //System.out.println("ROW : id=" + id + " client_id" + client_id + " title=" + title + "active=" + active);
            }
            proj.setId(id2);
            proj.setClient_id(client_id);
            proj.setTitle(title);
            proj.setActive(active);
            return proj.toString();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "{\"message\":\"error\"}";
    }

    @GetMapping("/api/projects")
    List getAllProjects(Map<String, Object> model) {
        ArrayList<Project> output = new ArrayList<Project>();
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement();
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

    /**
     * ISSUES
     */



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
