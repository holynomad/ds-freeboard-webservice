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
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.Video;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class YouTubeService implements YouTubeRepository {

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final long NUMBER_OF_VIDEOS_RETURNED = 5;
    private static YouTube youtube;
    /** Global instance properties filename. */
    private static String PROPERTIES_FILENAME = "youtube.properties";

    //private static void prettyPrint(Iterator<SearchResult> iteratorSearchResults, YouTubeSearchDto youTubeDto, String apiKey, List<YouTubeSearchDto> videosJson) {
    private static void prettyPrint(Iterator<SearchResult> iteratorSearchResults, String apiKey, List<YouTubeSearchDto> videosJson) {

        System.out.println("\n=============================================================");
        System.out.println("=============================================================\n");

        if (!iteratorSearchResults.hasNext()) {
            System.out.println(" There aren't any results for your query.");
        }

        while (iteratorSearchResults.hasNext()) {
            // youTubeDto 인스턴스를 searchResults 인덱스 갱신 시점마다 새롭게 생성 @ 2021.01.13.
            YouTubeSearchDto youTubeDto = new YouTubeSearchDto();

            // 게시일시 포맷변환 관련 추가 @ 2021.01.11.
            DateFormat df = new SimpleDateFormat("MMM dd, yyyy");

            SearchResult singleVideo = iteratorSearchResults.next();

            // Double checks the kind is video.
            if (singleVideo.getKind().equals("youtube#searchResult")) {

                Thumbnail thumbnail = (Thumbnail) singleVideo.getSnippet().getThumbnails().get("default");

                //parse the date
                DateTime dateTime = singleVideo.getSnippet().getPublishedAt();
                Date date = new Date(dateTime.getValue());
                String dateString = df.format(date);

                System.out.println(" Video Id" + singleVideo.getId().getVideoId());
                System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
                System.out.println(" Desc. : " + singleVideo.getSnippet().getDescription());
                //System.out.println(" contentDetails Duration: " + singleVideo.getContentDetails().getDuration());

                System.out.println(" Thumbnail: " + thumbnail.getUrl());
                System.out.println(" Channel Id: " + singleVideo.getSnippet().getChannelId());
                System.out.println(" Channel Title: " + singleVideo.getSnippet().getChannelTitle());
                //System.out.println(" PublishedAt : " + singleVideo.getSnippet().getPublishedAt());

                System.out.println("\n-------------------------------------------------------------\n");

                youTubeDto.setThumbnailPath(thumbnail.getUrl());
                youTubeDto.setTitle(singleVideo.getSnippet().getTitle());
                youTubeDto.setVideoId(singleVideo.getId().getVideoId());
                // 채널 및 비디오 상세항목 추가 @ 2021.01.06.
                youTubeDto.setDescription(singleVideo.getSnippet().getDescription());
                youTubeDto.setChannelId(singleVideo.getSnippet().getChannelId());
                youTubeDto.setChannelTitle(singleVideo.getSnippet().getChannelTitle());
                youTubeDto.setPublishedDate(dateString);

                // Video Id별 콘텐츠 상세보기 @ 2021.01.08.
                getVideoDetails(singleVideo.getId().getVideoId(), youTubeDto, apiKey, videosJson);
            }
        }
    }

    // Video Id별 콘텐츠 상세정보 조회 추가 @ 2021.01.08.
    private static void getVideoDetails(String videoId, YouTubeSearchDto youTubeDto, String apiKey, List<YouTubeSearchDto> videosJson) {

        System.out.println("\n********************");

        try {
            youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("youtube-video-contents-get").build();

            //내가 원하는 정보 지정할 수 있어요. 공식 API문서를 참고해주세요.
            String key = apiKey;
            //YouTube.Videos.List videos = youtube.videos().list("id,snippet,contentDetails", "");
            // API v3 video:list --> search:list 차환 @ 2021.01.07.
            YouTube.Videos.List videos = youtube.videos().list("id,snippet,statistics,contentDetails");
            videos.setKey(key); //### 여기에 앞서 받은 API키를 입력해야 합니다.
            //videos.setId("EAyo3_zJj5c"); //### 여기에는 유튜브 동영상의 ID 값을 입력해야 합니다.
            videos.setId(videoId);
            videos.setPart("statistics,snippet,contentDetails");

            // 아래 execute 부분 개선 필요...
            List<Video> videoContents = videos.execute().getItems();

            // test
            System.out.println("&&&& success getting videoContents : " + videoContents);

            if (videoContents != null) {

                // while 문에서 무한 loop 발생하여 주석 @ 2021.01.10.
                //while (videoContents.iterator().hasNext()) {

                    Video singleContents = videoContents.iterator().next();

                    if (singleContents.getKind().equals("youtube#video")) {

                        System.out.println(" contentDetails Duration: " + singleContents.getContentDetails().getDuration());
                        System.out.println(" View Counts: " + singleContents.getStatistics().getViewCount());
                        System.out.println(" Comment Counts: " + singleContents.getStatistics().getCommentCount());
                        System.out.println(" Tags : " + singleContents.getSnippet().getTags());

                        youTubeDto.setDuration(singleContents.getContentDetails().getDuration());
                        youTubeDto.setViewCount(singleContents.getStatistics().getViewCount());
                        youTubeDto.setCommentCount(singleContents.getStatistics().getCommentCount());
                        youTubeDto.setTags(singleContents.getSnippet().getTags());

                        // getVideoDetails로 상세내역 한번 받아오면 JSonArray에 추가 @ 2021.01.13.
                        videosJson.add(youTubeDto);
                    }
                //}
            }

        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }


        System.out.println("********************\n");

    }

    @Override
    //public YouTubeSearchDto get() {
    public List<YouTubeSearchDto> get() {

        Properties properties = new Properties();

        try {
            InputStream in = YouTube.Search.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
            properties.load(in);

        } catch (IOException e) {
            System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause()
                    + " : " + e.getMessage());
            System.exit(1);
        }

        //YouTubeSearchDto youTubeDto = new YouTubeSearchDto();

        // NUMBER_OF_VIDEOS_RETURNED 만큼 loop 돌면서 video search 결과를 JSonArray List 형식으로 최종리턴할 변수 @ 2021.01.13.
        List<YouTubeSearchDto> videosJson = new ArrayList<>();

        try {
            youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("youtube-search-list-get").build();

            //내가 원하는 정보 지정할 수 있어요. 공식 API문서를 참고해주세요.
            String apiKey = properties.getProperty("youtube.apikey");
            //YouTube.Videos.List videos = youtube.videos().list("id,snippet,contentDetails", "");
            // API v3 video:list --> search:list 차환 @ 2021.01.07.
            YouTube.Search.List videos = youtube.search().list("id,snippet,contentDetails");
            videos.setKey(apiKey); //### 여기에 앞서 받은 API키를 입력해야 합니다.
            //videos.setId("EAyo3_zJj5c"); //### 여기에는 유튜브 동영상의 ID 값을 입력해야 합니다.
            videos.setType("video");
            videos.setPart("snippet");

            // encoded URI 적용위한 변수 @ 2021.01.11.
            String queryNotEncoded = "Python";
            videos.setQ("IT+" + URLEncoder.encode(queryNotEncoded, "UTF-8"));

            videos.setMaxResults(NUMBER_OF_VIDEOS_RETURNED); //조회 최대 갯수.

            // 아래 execute 부분 개선 필요...
            List<SearchResult> videoList = videos.execute().getItems();

            // test
            //System.out.println(videoList);

            if (videoList != null) {
                //prettyPrint(videoList.iterator(), youTubeDto, apiKey, videosJson);
                prettyPrint(videoList.iterator(), apiKey, videosJson);

            }

        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }

        //return youTubeDto;
        return videosJson;

    }


}


