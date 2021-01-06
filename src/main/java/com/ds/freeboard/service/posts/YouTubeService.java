package com.ds.freeboard.service.posts;

import com.ds.freeboard.domain.posts.YouTubeRepository;
import com.ds.freeboard.web.dto.YouTubeSearchDto;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.Video;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

@Service
public class YouTubeService implements YouTubeRepository {

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final long NUMBER_OF_VIDEOS_RETURNED = 5;
    private static YouTube youtube;
    /** Global instance properties filename. */
    private static String PROPERTIES_FILENAME = "youtube.properties";

    private static void prettyPrint(Iterator<Video> iteratorSearchResults, YouTubeSearchDto youTubeDto) {

        System.out.println("\n=============================================================");
        System.out.println("=============================================================\n");

        if (!iteratorSearchResults.hasNext()) {
            System.out.println(" There aren't any results for your query.");
        }

        while (iteratorSearchResults.hasNext()) {

            Video singleVideo = iteratorSearchResults.next();

            // Double checks the kind is video.
            if (singleVideo.getKind().equals("youtube#video")) {
                Thumbnail thumbnail = (Thumbnail) singleVideo.getSnippet().getThumbnails().get("default");

                System.out.println(" Video Id" + singleVideo.getId());
                System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
                System.out.println(" contentDetails Duration: " + singleVideo.getContentDetails().getDuration());
                System.out.println(" Thumbnail: " + thumbnail.getUrl());
                System.out.println("\n-------------------------------------------------------------\n");

                youTubeDto.setThumbnailPath(thumbnail.getUrl());
                youTubeDto.setTitle(singleVideo.getSnippet().getTitle());
                youTubeDto.setVideoId(singleVideo.getId());
                // 채널 및 비디오 상세항목 추가 @ 2021.01.06.
                youTubeDto.setDescription(singleVideo.getSnippet().getDescription());
                youTubeDto.setChannelId(singleVideo.getSnippet().getChannelId());
                youTubeDto.setChannelTitle(singleVideo.getSnippet().getChannelTitle());
                youTubeDto.setChannelTitle(singleVideo.getSnippet().getChannelTitle());
                youTubeDto.setDuration(singleVideo.getContentDetails().getDuration());
                youTubeDto.setViewCount(singleVideo.getStatistics().getViewCount());
                youTubeDto.setCommentCount(singleVideo.getStatistics().getCommentCount());
                youTubeDto.setTags(singleVideo.getSnippet().getTags());

            }
        }
    }

    @Override
    public YouTubeSearchDto get() {

        Properties properties = new Properties();
        try {
            InputStream in = YouTube.Search.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
            properties.load(in);

        } catch (IOException e) {
            System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause()
                    + " : " + e.getMessage());
            System.exit(1);
        }

        YouTubeSearchDto youTubeDto = new YouTubeSearchDto();

        try {
            youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("youtube-video-duration-get").build();

            //내가 원하는 정보 지정할 수 있어요. 공식 API문서를 참고해주세요.
            String apiKey = properties.getProperty("youtube.apikey");
            YouTube.Videos.List videos = youtube.videos().list("id,snippet,contentDetails");
            videos.setKey(apiKey); //### 여기에 앞서 받은 API키를 입력해야 합니다.
            videos.setId("EAyo3_zJj5c"); //### 여기에는 유튜브 동영상의 ID 값을 입력해야 합니다.
            videos.setMaxResults(NUMBER_OF_VIDEOS_RETURNED); //조회 최대 갯수.
            List<Video> videoList = videos.execute().getItems();

            if (videoList != null) {
                prettyPrint(videoList.iterator(), youTubeDto);
            }

        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return youTubeDto;
    }


}


