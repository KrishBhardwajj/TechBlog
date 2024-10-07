/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tech.blog.dao;

import com.tech.blog.entities.Category;
import com.tech.blog.entities.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostDao {

    private static final Logger LOGGER = Logger.getLogger(PostDao.class.getName());
    private final Connection con;

    public PostDao(Connection con) {
        this.con = con;
    }

    public ArrayList<Category> getAllCategories() {
        ArrayList<Category> list = new ArrayList<>();
        String q = "select * from categories";
        
        try (Statement st = this.con.createStatement();
             ResultSet set = st.executeQuery(q)) {
            
            while (set.next()) {
                int cid = set.getInt("cid");
                String name = set.getString("name");
                String description = set.getString("description");
                Category c = new Category(cid, name, description);
                list.add(c);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching categories", e);
        }
        return list;
    }

    public boolean savePost(Post p) {
        boolean f = false;
        String q = "insert into posts(pTitle,pContent,pCode,pPic,catId,userId) values(?,?,?,?,?,?)";
        
        try (PreparedStatement pstmt = con.prepareStatement(q)) {
            pstmt.setString(1, p.getpTitle());
            pstmt.setString(2, p.getpContent());
            pstmt.setString(3, p.getpCode());
            pstmt.setString(4, p.getpPic());
            pstmt.setInt(5, p.getCatId());
            pstmt.setInt(6, p.getUserId());

            pstmt.executeUpdate();
            f = true;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving post", e);
        }
        return f;
    }

    // Get all posts
    public List<Post> getAllPosts() {
        List<Post> list = new ArrayList<>();
        String q = "select * from posts order by pid desc";
        
        try (PreparedStatement p = con.prepareStatement(q);
             ResultSet set = p.executeQuery()) {

            while (set.next()) {
                int pid = set.getInt("pid");
                String pTitle = set.getString("pTitle");
                String pContent = set.getString("pContent");
                String pCode = set.getString("pCode");
                String pPic = set.getString("pPic");
                Timestamp date = set.getTimestamp("pDate");
                int catId = set.getInt("catId");
                int userId = set.getInt("userId");
                Post post = new Post(pid, pTitle, pContent, pCode, pPic, date, catId, userId);

                list.add(post);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching all posts", e);
        }
        return list;
    }

    // Get posts by category ID
    public List<Post> getPostByCatId(int catId) {
        List<Post> list = new ArrayList<>();
        String q = "select * from posts where catId=?";
        
        try (PreparedStatement p = con.prepareStatement(q)) {
            p.setInt(1, catId);
            try (ResultSet set = p.executeQuery()) {
                while (set.next()) {
                    int pid = set.getInt("pid");
                    String pTitle = set.getString("pTitle");
                    String pContent = set.getString("pContent");
                    String pCode = set.getString("pCode");
                    String pPic = set.getString("pPic");
                    Timestamp date = set.getTimestamp("pDate");
                    int userId = set.getInt("userId");
                    Post post = new Post(pid, pTitle, pContent, pCode, pPic, date, catId, userId);

                    list.add(post);
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching posts by category ID", e);
        }
        return list;
    }

    // Get post by post ID
    public Optional<Post> getPostByPostId(int postId) {
        Optional<Post> post = Optional.empty();
        String q = "select * from posts where pid=?";
        
        try (PreparedStatement p = this.con.prepareStatement(q)) {
            p.setInt(1, postId);
            try (ResultSet set = p.executeQuery()) {
                if (set.next()) {
                    int pid = set.getInt("pid");
                    String pTitle = set.getString("pTitle");
                    String pContent = set.getString("pContent");
                    String pCode = set.getString("pCode");
                    String pPic = set.getString("pPic");
                    Timestamp date = set.getTimestamp("pDate");
                    int catId = set.getInt("catId");
                    int userId = set.getInt("userId");
                    post = Optional.of(new Post(pid, pTitle, pContent, pCode, pPic, date, catId, userId));
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching post by post ID", e);
        }
        return post;
    }
}
