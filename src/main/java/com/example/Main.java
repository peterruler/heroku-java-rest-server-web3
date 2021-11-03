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

import com.fasterxml.jackson.databind.*;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.json.JSONObject;
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
        private int id;
        private String client_id;
        private String project_id;
        private String title;
        private Object active;

        public void setId(int id) {
            this.id = id;
        }

        public void setClient_id(String client_id) {
            this.client_id = client_id;
        }

        public void setProject_id(String project_id) {
            this.project_id = project_id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setActive(Object active) {
            this.active = active;
        }

        @Override
        public String toString() {
            String jsonString = new JSONObject()
                    .put("id", id)
                    .put("client_id", client_id)
                    .put("title", title)
                    .put("active", active)
                    .toString();

            System.out.println(jsonString);
            return jsonString;
        }
    }

    class Issue {
        private int id;
        private String client_id;
        private int project_id;
        private Boolean done;
        private String title;
        private Date due_date;
        private String priority;

        public void setId(int id) {
            this.id = id;
        }

        public void setClient_id(String client_id) {
            this.client_id = client_id;
        }

        public void setProject_id(int project_id) {
            this.project_id = project_id;
        }

        public void setDone(Boolean done) {
            this.done = done;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setDue_date(Date due_date) {
            this.due_date = due_date;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }

        @Override
        public String toString() {
            String jsonString = new JSONObject()
                    .put("id", id)
                    .put("client_id", client_id)
                    .put("project_id", project_id)
                    .put("done", done)
                    .put("due_date", due_date)
                    .put("title", title)
                    .put("priority", priority)
                    .toString();

            System.out.println(jsonString);
            return jsonString;
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
        return "POST JSON: /api/projects & GET: /api/projects/{id} & PUT JSON: /api/projects & GET: /api/projects" +
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
    String addProject(@RequestBody Map<String, Object> project) throws Exception {
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
            int id = rs.getInt(1);
            Project proj = new Project();
            proj.setId(id);
            proj.setClient_id(client_id);
            proj.setProject_id(client_id);
            proj.setTitle(title);
            proj.setActive((Boolean) active);
            System.out.println("POST: id=" + id + "project_id:" + client_id);
            return proj.toString();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "undefined";
    }

    /**
     * READ Project BY ID
     * not implemented
     */

    /**
     * READ ALL Projects
     */
    @CrossOrigin(maxAge = 3600)
    @GetMapping("/api/projects")
    String getAllProjects(Map<String, Object> model) {
        ArrayList<String> output = new ArrayList<String>();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "";
        try (Connection connection = dataSource.getConnection()) {
            int counter = 0;
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, client_id, title, active FROM Project;");
            json = "[";
            while (true) {
                try {
                    if (!rs.next()) {
                        break;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Project proj = new Project();
                proj.setId(rs.getInt("id"));
                proj.setClient_id(rs.getString("client_id"));
                proj.setTitle(rs.getString("title"));
                proj.setActive(rs.getBoolean("active"));
                //output.add(proj.toString());
                json += proj.toString();
                json += ",";
                counter++;
            }
            try {
                //json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(output);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (counter > 0) {
                return json.substring(0, json.length() - 1) + "]";
            } else {
                return "undefined";
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "undefined";
    }

    /**
     * ISSUES
     */

    /**
     * CREATE ISSUE
     */
    @CrossOrigin(maxAge = 3600)
    @PostMapping("/api/projects/{project_id}/issues")
    String createIssue(@PathVariable int project_id, @RequestBody Map<String, Object> project) throws Exception {
        ArrayList<Issue> output = new ArrayList<Issue>();

        String client_id = (String) project.get("client_id");

        //if "done":"true" in json instead of "done": true
        //String done2 = (String) project.get("done");
        //Boolean done = ("true".equals(done2)) ? true : false;

        //"done":true
        Boolean done = (Boolean) project.get("done");
        String title = (String) project.get("title");
        String due_date = (String) project.get("due_date");
        String priority = (String) project.get("priority");

        String postSql = "INSERT INTO Issue (client_id, project_id, done, title,  due_date, priority) VALUES(?,?,?,?,?,?) RETURNING id";
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
            int id = rs.getInt(1);
            Issue issue = new Issue();
            issue.setId(id);
            issue.setClient_id(client_id);
            issue.setProject_id(project_id);
            issue.setDone((Boolean) done);
            issue.setTitle(title);
            java.util.Date utilStartDate = new SimpleDateFormat("yyyy-MM-dd").parse(due_date);
            java.sql.Date date2 = new java.sql.Date(utilStartDate.getTime());
            issue.setDue_date(date2);

            issue.setPriority(priority);

            return issue.toString();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "undefined";
    }

    /**
     * GET Issue
     * Not impmemented
     */

    /**
     * UPDATE Issue
     */
    @CrossOrigin
    @RequestMapping(
            value = "/api/projects/{project_id}/issues/{id}",
            produces = "application/json",
            method = {RequestMethod.PUT, RequestMethod.OPTIONS})
    String updateIssue(@PathVariable int project_id, @PathVariable int id, @RequestBody Map<String, Object> issue_param) throws Exception {
        String client_id = (String) issue_param.get("client_id");
        Boolean done = (Boolean) issue_param.get("done");
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
            return "undefined";
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
        return issue.toString();
    }

    /**
     * DELETE Issue
     */
    @CrossOrigin
    @RequestMapping(
            value = "/api/projects/{project_id}/issues/{id}",
            produces = "application/json",
            method = {RequestMethod.DELETE})
    boolean deleteIssue(@PathVariable int id, @PathVariable int project_id) throws Exception {

        String deleteSql = "DELETE FROM Issue WHERE project_id=?";
        int affectedrows = 0;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(deleteSql);
            pstmt.setInt(1, project_id);
            System.out.println("Issue delete project id" + project_id);
            affectedrows = pstmt.executeUpdate();
            if (affectedrows > 0) {
                System.out.println("Issue delete success");
                return true;
            } else {
                System.out.println("Issue delete fail");
                return false;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }


    /**
     * READ ALL Issues
     */
    @CrossOrigin(maxAge = 3600)
    @GetMapping("/api/projects/{project_id}")
    String getProjectById(@PathVariable int project_id) {

        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT id, client_id, project_id, done, title, due_date, priority FROM Issue WHERE project_id=?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, project_id);
            System.out.println("DEBUG : project_id=" + project_id);
            ResultSet rs = pstmt.executeQuery();
            int id2 = 0;
            String client_id = "";
            String project_id2 = "0";
            Boolean done = false;
            String title = "";
            Date due_date = null;
            String priority = "";

            String json = "[";
            int counter = 0;
            while (rs.next()) {
                id2 = rs.getInt("id");
                client_id = rs.getString("client_id");
                project_id2 = rs.getString("project_id");
                done = rs.getBoolean("done");
                title = rs.getString("title");
                due_date = rs.getDate("due_date");
                priority = rs.getString("priority");
                System.out.println("ROW : id=" + id2 + " client_id" + client_id + " project_id" + project_id2 + " title=" + title + "due_date" + due_date + "priority=" + priority);

                Issue issue = new Issue();
                issue.setId(id2);
                issue.setClient_id(client_id);
                issue.setProject_id(project_id);
                issue.setDone(done);
                issue.setTitle(title);
                issue.setDue_date(due_date);
                issue.setPriority(priority);
                json += issue.toString();
                json += ",";
                counter++;
            }
            if (counter > 0) {
                return json.substring(0, json.length() - 1) + "]";
            } else {
                return "undefined";
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "undefined";
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
