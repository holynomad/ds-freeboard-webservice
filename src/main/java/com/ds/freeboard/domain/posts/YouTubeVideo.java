package com.ds.freeboard.domain.posts;

import com.google.api.client.util.DateTime;

import java.math.BigInteger;
import java.util.List;

public class YouTubeVideo {
    private String title;
    private String url;
    private String thumbnailUrl;
    private DateTime publishDate;
    private String description;
    // Channel 및 Video정보 추가 @ 2021.01.06.
    private String channelId;   // 채널 ID
    private String channelTitle; // 채널명
    private String duration;    // 콘텐츠 재생시간
    private BigInteger viewCount;   // 조회수
    private BigInteger commentCount;    // 댓글수
    private List<String> tags;        // 태그
    private String crawlYn;     // 크롤링 검수여부(Accept/Review/Reject)

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
    public DateTime getPublishDate() {
        return publishDate;
    }
    public void setPublishDate(DateTime publishDate) {
        this.publishDate = publishDate;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    // 채널 및 비디오 상세항목 getter-setter 추가 @ 2021.01.06.
    public String getChannelId() {
        return channelId;
    }
    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
    public String getChannelTitle() {
        return channelTitle;
    }
    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }
    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }
    public BigInteger getViewCount() {
        return viewCount;
    }
    public void setViewCount(BigInteger viewCount) {
        this.viewCount = viewCount;
    }
    public BigInteger getCommentCount() {
        return commentCount;
    }
    public void setCommentCount(BigInteger commentCount) {
        this.commentCount = commentCount;
    }
    public List<String> getTags() {
        return tags;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    public String getCrawlYn() {
        return crawlYn;
    }
    public void setCrawlYn(String crawlYn) {
        this.crawlYn = crawlYn;
    }
}
