package com.ds.freeboard.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class YouTubeSearchDto {
    private String title;       // 동영상 제목
    private String thumbnailPath; //동영상 썸네일 경로
    private String videoId;     // 동영상 식별 ID
    // Channel 및 Video정보 추가 @ 2021.01.06.
    private String description; // 콘텐츠 설명
    private String channelId;   // 채널 ID
    private String channelTitle; // 채널명
    private String publishedDate; // 콘텐츠 게시일시
    private String duration;    // 콘텐츠 재생시간
    private BigInteger viewCount;   // 조회수
    private BigInteger commentCount;    // 댓글수
    private List<String> tags;        // 태그
    private String crawlYn;     // 크롤링 검수여부(Accept/Review/Reject)

    @Builder(toBuilder = true)
    public YouTubeSearchDto(String title, String thumbnailPath, String videoId, String description,
                            String channelId, String channelTitle, String publishedDate, String duration,
                            BigInteger viewCount, BigInteger commentCount, List<String> tags, String crawlYn
                            ) {
        this.title = title;
        this.thumbnailPath = thumbnailPath;
        this.videoId = videoId;
        // Channel 및 Video정보 추가 @ 2021.01.06.
        this.description = description;
        this.channelId = channelId;
        this.channelTitle = channelTitle;
        this.publishedDate = publishedDate;
        this.duration = duration;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.tags = tags;
        this.crawlYn = crawlYn;

    }
}
