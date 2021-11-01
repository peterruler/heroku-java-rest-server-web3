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
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
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
    @CrossOrigin(maxAge = 3600)
    @RequestMapping("/")
    String index() {
        return "POST JSON: /api/projects & GET: /api/projects/{id} & PUT JSON: /api/projects & GET: /api/projects"+
                "POST Issue: /api/project/{project_id}/issues , GET Issue: /api/project/{project_id}/issues, UPDATE Issue: /api/project/{project_id}/issues/{id}, DELETE Issue /api/projects/2222|<project_id>/issues/2|<id>, READ ALL Issues: /api/projects/issues";
    }

    /**
     * Project Endpoints
     */

    /**
     * DELETE Project
     */
    @CrossOrigin(maxAge = 3600)
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
    @CrossOrigin(maxAge = 3600)
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
    @CrossOrigin(maxAge = 3600)
    @PostMapping("/api/projects")
    List addProject(@RequestBody Map<String, Object> project) throws Exception {
        ArrayList<Project> output = new ArrayList<Project>();

        String client_id = (String) project.get("client_id");
        String title = (String) project.get("title");
        Object active = project.get("active");

        String postSql = "INSERT INTO Project (client_id, title, active) VALUES(?,?,?) RETURNING id";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(postSql);
            pstmt.setString(1, client_id);
            pstmt.setString(2, title);
            pstmt.setBoolean(3, (Boolean) active);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            Project proj = new Project();
            proj.setId(rs.getInt(1));
            proj.setClient_id(client_id);
            proj.setTitle(title);
            proj.setActive((Boolean) active);
            output.add(proj);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return output;
    }

    /**
     * READ Project BY ID
     */
    @CrossOrigin(maxAge = 3600)
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
    @CrossOrigin(maxAge = 3600)
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
    @CrossOrigin(maxAge = 3600)
    @PostMapping("/api/project/{project_id}/issues")
    List createIssue(@PathVariable int project_id, @RequestBody Map<String, Object> project) throws Exception {
        ArrayList<Issue> output = new ArrayList<Issue>();

        Double id = (Double) project.get("id");
        String client_id = (String) project.get("client_id");

        //if "done":"true" in json instead of "done": true
        String done2 = (String) project.get("done");
        Boolean done = ("true".equals(done2)) ? true : false;

        //"done":true
        //Boolean done = (Boolean) project.get("done");
        String title = (String) project.get("title");
        String due_date = (String) project.get("due_date");
        String priority = (String) project.get("priority");

        String postSql = "INSERT INTO Issue (client_id, project_id, done, title,  due_date, priority) VALUES(?,?,?,?,?,?)";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(postSql);
            pstmt.setString(1, client_id);
            pstmt.setDouble(2, project_id);
            pstmt.setBoolean(3, done);
            pstmt.setString(4, title);
            java.util.Date utilStartDate1 = new SimpleDateFormat("yyyy-MM-dd").parse(due_date);
            java.sql.Date date1 = new java.sql.Date(utilStartDate1.getTime());
            pstmt.setDate(5, date1);

            pstmt.setString(6, priority);
            ResultSet rs = pstmt.executeQuery();
            rs.next();

            Issue issue = new Issue();
            issue.setId(rs.getInt(1));
            issue.setClient_id(client_id);
            issue.setProject_id(project_id);
            issue.setDone((Boolean) done);
            issue.setTitle(title);
            java.util.Date utilStartDate = new SimpleDateFormat("yyyy-MM-dd").parse(due_date);
            java.sql.Date date2 = new java.sql.Date(utilStartDate.getTime());
            issue.setDue_date(date2);

            issue.setPriority(priority);

            output.add(issue);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return output;
    }

    /**
     * GET Issue
     */
    @CrossOrigin(maxAge = 3600)
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
    @CrossOrigin(maxAge = 3600)
    @RequestMapping(
            value = "/api/project/{project_id}/issues/{id}",
            produces = "application/json",
            method = {RequestMethod.PUT})
    List<Issue> updateIssue(@PathVariable int project_id, @PathVariable int id,@RequestBody Map<String, Object> issue_param) throws Exception {
        ArrayList<Issue> output = new ArrayList<Issue>();

        String client_id = (String) issue_param.get("client_id");
        String done2 = (String) issue_param.get("done");
        Boolean done = ("true".equals(done2)) ? true : false;
        String title = (String) issue_param.get("title");
        String due_date = (String) issue_param.get("due_date");
        String priority = (String) issue_param.get("priority");

        String putSql = "UPDATE Issue SET client_id=?, project_id=?, done=?, title=?, due_date=?, priority=? WHERE project_id=? AND id=?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(putSql);
            pstmt.setString(1, client_id);
            pstmt.setDouble(2, project_id);
            pstmt.setBoolean(3, done);
            pstmt.setString(4, title);
            java.util.Date utilStartDate = new SimpleDateFormat("yyyy-MM-dd").parse(due_date);
            java.sql.Date date1 = new java.sql.Date(utilStartDate.getTime());
            pstmt.setDate(5, date1);

            pstmt.setString(6, priority);
            pstmt.setDouble(7, project_id);
            pstmt.setDouble(8, id);
            pstmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        Issue issue = new Issue();
        issue.setClient_id(client_id);
        issue.setProject_id(project_id);
        issue.setDone((Boolean) done);
        issue.setTitle(title);
        java.util.Date utilStartDate = new SimpleDateFormat("yyyy-MM-dd").parse(due_date);
        java.sql.Date date1 = new java.sql.Date(utilStartDate.getTime());
        issue.setDue_date(date1);

        issue.setPriority(priority);
        output.add(issue);
        return output;
    }

    /**
     * DELETE Issue
     */
    @CrossOrigin(maxAge = 3600)
    @RequestMapping(
            value = "/api/project/{project_id}/issues/{id}",
            produces = "application/json",
            method = {RequestMethod.DELETE})
    boolean deleteIssue(@PathVariable int project_id, @PathVariable int id) throws Exception {

        String deleteSql = "DELETE FROM Issue WHERE project_id=?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(deleteSql);
            pstmt.setDouble(1, project_id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }


    /**
     * READ ALL Issues
     */
    @CrossOrigin(maxAge = 3600)
    @GetMapping("/api/projects/issues")
    List getAllIssues(Map<String, Object> model) {
        ArrayList<Issue> output = new ArrayList<Issue>();
        try (Connection connection = dataSource.getConnection()) {
            Statement pstmt = connection.createStatement();
            ResultSet rs = pstmt.executeQuery("SELECT id, client_id, project_id, done, title, due_date, priority FROM Issue");

            int id2 = 0;
            String client_id = "";
            String project_id = "0";
            Boolean done = false;
            String title = "";
            Date due_date = null;
            String priority = "";
            while (rs.next()) {
                id2 = rs.getInt("id");
                client_id = rs.getString("client_id");
                project_id = rs.getString("project_id");
                done = rs.getBoolean("done");
                title = rs.getString("title");
                due_date = rs.getDate("due_date");
                priority = rs.getString("priority");
                System.out.println("ROW : id=" + id2 + " client_id" + client_id + " project_id" + project_id + " title=" + title + "due_date" + due_date + "priority=" + priority);

                Issue issue = new Issue();
                issue.setId(id2);
                issue.setClient_id(client_id);
                issue.setProject_id(Integer.parseInt(project_id));
                issue.setDone(done);
                issue.setTitle(title);
                issue.setDue_date(due_date);
                issue.setPriority(priority);
                output.add(issue);
            }
            return output;
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
