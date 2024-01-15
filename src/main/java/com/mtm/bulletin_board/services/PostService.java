package com.mtm.bulletin_board.services;

import java.util.List;

import com.mtm.bulletin_board.dao.PostDAO;
import com.mtm.bulletin_board.models.Post;

public class PostService {

    private PostDAO postDAO;

    public PostService() {
        this.postDAO = new PostDAO();
    }

    public Post getById(int id) {
        try {
            return postDAO.getById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Post> searchPosts(String keyword, int currentPage, int recordsPerPage) {
        try {
            return postDAO.searchPosts(keyword, currentPage, recordsPerPage);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getTotalPostCountBySearch(String keyword) {
         try {
            return postDAO.getTotalPostCountBySearch(keyword);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<Post> getAllPostsForCsvDownload() {
        try {
            return postDAO.getAllPostsForCsvDownload();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int savePostsFromCsvUpload(List<Post> posts) {
        try {
            return postDAO.savePostsFromCsvUpload(posts);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Post createPost(Post post) {
        int generatedPostId = postDAO.save(post);
        if (generatedPostId > 0) {
            return getById(generatedPostId);
        } else {
            return null;
        }
    }

    public Post updatePost(Post post) {
        int rowUpdated = postDAO.update(post);
        if (rowUpdated > 0) {
            return getById(post.getId());
        } else {
            return null;
        }
    }

    public boolean deletePost(Post post) {
        int rowUpdated = postDAO.delete(post);
        if (rowUpdated > 0) {
            return true;
        } else {
            return false;
        }
    }

}
