package com.kumbh.design.Epost.model;

import java.util.List;

public class ResponseFestival {
    private Response response;

    public void setResponse(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}

class Response {
    private Post post;

    public void setPost(Post post) {
        this.post = post;
    }

    public Post getPost() {
        return post;
    }
}

class Post {
    private List<ListPostItem> listPost;


    private List<ListPostItemFestival> listPostById;
    private int error;

    public void setListPost(List<ListPostItem> listPost) {
        this.listPost = listPost;
    }

    public List<ListPostItem> getListPost() {
        return listPost;
    }

    public void setError(int error) {
        this.error = error;
    }

    public int getError() {
        return error;
    }
}

