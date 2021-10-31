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

import java.net.Inet4Address;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Map;
import java.sql.Date;

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

    class Issue {
        private double id;
        private String client_id;
        private int project_id;
        private Boolean done;
        private String title;
        private Date due_date;
        private String priority;


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

        public int getProject_id() {
            return project_id;
        }

        public void setProject_id(int project_id) {
            this.project_id = project_id;
        }

        public Boolean getDone() {
            return done;
        }

        public void setDone(Boolean done) {
            this.done = done;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Date getDue_date() {
            return due_date;
        }

        public void setDue_date(Date due_date) {
            this.due_date = due_date;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }

        @Override
        public String toString() {
            return "{\"id\":\"" + id + "\",\"client_id\":\"" + client_id + "\",\"project_id\":\"" + project_id + "\",\"done\":\"" + done + "\",\"due_date\":\"" + due_date + "\",\"title\":\"" + title + "\",\"priority\":\"" + priority + "\"}";
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

    /**
     * DELETE Project
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

    /**
     * UPDATE Project
     */
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

    /**
     * CREATE Project
     */
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

    /**
     * READ Project BY ID
     */
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

    /**
     * READ ALL Projects
     */
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

    /**
     * CREATE ISSUE
     */
    @PostMapping("/api/project/{project_id}/issues")
    List createIssue(@PathVariable int project_id, @RequestBody Map<String, Object> project) throws Exception {
        ArrayList<Issue> output = new ArrayList<Issue>();

        int id = (Integer) project.get("id");
        String client_id = (String) project.get("client_id");
        Object done = project.get("done");
        String title = (String) project.get("title");
        Date due_date = new Date((Long) project.get("due_date"));
        String priority = (String) project.get("priority");

        String postSql = "INSERT INTO Issue (id, client_id, project_id, done, title, due_date, priority) VALUES(?,?,?,?,?,?,?)";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(postSql);
            pstmt.setInt(1, id);
            pstmt.setString(2, client_id);
            pstmt.setInt(3, project_id);
            pstmt.setBoolean(4, (Boolean) done);
            pstmt.setString(5, title);
            pstmt.setDate(6, due_date);
            pstmt.setString(7, priority);
            pstmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Issue issue = new Issue();
        issue.setId(id);
        issue.setClient_id(client_id);
        issue.setProject_id(project_id);
        issue.setDone((Boolean) done);
        issue.setTitle(title);
        issue.setDue_date(due_date);
        issue.setPriority(priority);

        output.add(issue);
        return output;
    }

    /**
     * GET Issue
     */

    @RequestMapping(
            value = "/api/project/{project_id}/issues",
            method = {RequestMethod.GET})
    public String getIssueById(@PathVariable int project_id) {

        Issue issue = new Issue();
        String getSql = "SELECT id, client_id, project_id, done, title, due_date, priority FROM Issue WHERE project_id=?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(getSql);
            pstmt.setInt(1, project_id);
            ResultSet rs = pstmt.executeQuery();
            int id2 = 0;
            String client_id = "";
            Boolean done = false;
            String title = "";
            Date due_date = null;
            String priority = "";
            while (rs.next()) {
                id2 = rs.getInt("id");
                client_id = rs.getString("client_id");
                done = rs.getBoolean("done");
                title = rs.getString("title");
                due_date = rs.getDate("due_date");
                priority = rs.getString("priority");
                System.out.println("ROW : id=" + id2 + " client_id" + client_id + " project_id" + project_id + " title=" + title + "due_date" + due_date + "priority=" + priority);
            }
            issue.setId(id2);
            issue.setClient_id(client_id);
            issue.setProject_id(project_id);
            issue.setDone(done);
            issue.setTitle(title);
            issue.setDue_date(due_date);
            issue.setPriority(priority);
            return issue.toString();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "{\"message\":\"error\"}";
    }

    /**
     * UPDATE Issue
     */

    @RequestMapping(
            value = "/api/project/{project_id}/issues",
            produces = "application/json",
            method = {RequestMethod.PUT})
    List<Issue> updateIssue(@PathVariable int project_id, @RequestBody Map<String, Object> issue_param) throws Exception {
        ArrayList<Issue> output = new ArrayList<Issue>();

        String id = (String) issue_param.get("id");
        String client_id = (String) issue_param.get("client_id");
        String project_id2 = (String) issue_param.get("project_id");
        String done2 = (String) issue_param.get("done");
        Boolean done = ("true".equals(done2))? true : false;
        String title = (String) issue_param.get("title");
        String due_date = (String) issue_param.get("due_date");
        String priority = (String) issue_param.get("priority");

        String putSql = "UPDATE Issue SET client_id=?, project_id=?, done=?, title=?, due_date=?, priority=? WHERE project_id=?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(putSql);
            pstmt.setString(1, client_id);
            pstmt.setInt(2, Integer.parseInt(project_id2));
            pstmt.setBoolean(3, done);
            pstmt.setString(4, title);

            java.util.Date utilStartDate =  new SimpleDateFormat("yyyy-MM-dd").parse(due_date);
            java.sql.Date date1 = new java.sql.Date(utilStartDate.getTime());

            pstmt.setDate(5, date1);

            pstmt.setString(6, priority);
            pstmt.setInt(7, project_id);
            pstmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Issue issue = new Issue();
        issue.setClient_id(client_id);
        issue.setProject_id(project_id);
        issue.setDone((Boolean) done);
        issue.setTitle(title);
        java.util.Date utilStartDate =  new SimpleDateFormat("yyyy-MM-dd").parse(due_date);
        java.sql.Date date1 = new java.sql.Date(utilStartDate.getTime());
        issue.setDue_date(date1);
        issue.setPriority(priority);
        output.add(issue);
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
