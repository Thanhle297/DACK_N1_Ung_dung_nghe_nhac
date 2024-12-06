package com.example.dack1;

public class Music {
    private String id;
    private String downloadImg;
    private String downloadUrl;
    private String name;
    private String author;
    private int type;

    public Music() {
    }

    public Music(String id, String downloadImg, String downloadUrl, String name, String author, int type) {
        this.id = id;
        this.downloadImg = downloadImg;
        this.downloadUrl = downloadUrl;
        this.name = name;
        this.author = author;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDownloadImg() {
        return downloadImg;
    }

    public void setDownloadImg(String downloadImg) {
        this.downloadImg = downloadImg;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Music{" +
                "id='" + id + '\'' +
                ", downloadImg='" + downloadImg + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", type=" + type +
                '}';
    }
}
