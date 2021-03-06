var main = {
    init : function () {
        var _this = this;
        var nextPageToken = $("#nextPageToken").val();

        $('#search').on('dblclick', function () {
                _this.search();
            }
        );

        $('#nextPageToken').on('dblclick', function() {
                _this.search();
            }
        )
    },
    search : function () {
        //events.preventDefault();

        var _this = this;
        var API_KEY = getProperty("youtube.apikey");
        var video = "";
        var videos = $("#videos");
        var searchKeywords = $("#search").val();
        var nextPageToken = $("#nextPageToken").val();

        console.log("search.clicked ==> " + searchKeywords)

        if (nextPageToken == "") {
            _this.videoLists(API_KEY, searchKeywords, 5, "");
        }
        else {
            _this.videoLists(API_KEY, searchKeywords, 5, nextPageToken);
        }




        /* -- 추후 PostsService나 YouTubeService 쪽으로 이관 전까지 주석 @ 2021.01.04.
        $.ajax({
            type: 'GET',
            url: '/youtube',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert('검색하신 키워드의 유튜브 콘텐츠를 검색하였습니다.');
            window.location.href = '/youtube';
        }).fail(function (error) {
            alert(JSON.stringify(error))
        });
        */
    },
    videoLists : function(key, search, maxResults, nextPageToken){
        var _this = this;

        //$("#nextPageToken").empty()
        $("#totalResults").empty()
        $("#videos").empty()
        $("#contents").empty()

        $.get("https://www.googleapis.com/youtube/v3/search?key="+key
            + "&type=video&part=snippet&maxResults="+maxResults+ "&q="+search+"&pageToken="+nextPageToken, function(data){
            console.log('1')
            console.log(data)

            //$("#nextPageToken").append(data.nextPageToken)

            document.getElementById("nextPageToken").value = data.nextPageToken;

            $("#totalResults").append("totalResults : " + data.pageInfo.totalResults)

            data.items.forEach(function(item, index, data) {
                console.log('2-1', index)
                console.log(data[index])

                /*
                $.ajax({
                    type: "GET",
                    url: "/Users/holynomad/Downloads/ds-yt-csv-yyyymmdd.csv",
                    dataType: "text",
                    success: function(csvText) {checkCsvHistory(csvText, data[index].id.videoId);}
                });
                */

                console.log('3-1 : start parsing csv')

                // 기존 CSV 크롤링 이력 참조여부 함수추가 @ 2020.12.08.
                // ref. : jQuery 이용 - https://sweetpotatocat.tistory.com/5
                //      : d3 이용 - https://m.blog.naver.com/PostView.nhn?blogId=wideeyed&logNo=221120158854&proxyReferer=https:%2F%2Fwww.google.com%2F
                function checkCsvHistory(csvList, newVideoId) {

                    // 기존 로컬 경로 CSV 파일 읽어와서, newVideoId와 겹치는 부분 있으면 "YES", 없으면 "NO"
                    // ...
                    var allTextLines = csvList.split(/\r\n|\n/);
                    var headers = allTextLines[0].split(',');
                    var lines = [];

                    console.log('3-2 : csvList, newVideoId, headers - ', csvList, newVideoId, headers)

                    for (var i=1; i<allTextLines.length; i++) {
                        var data = allTextLines[i].split(',');

                        console.log('3-3 : data - ', data)
                        if (data.length == headers.length) {

                            var tarr = [];
                            for (var j=0; j<headers.length; j++) {
                                tarr.push(headers[j]+":"+data[j]);
                                console.log('3-4 : data[j] - ', data[j])
                            }
                            lines.push(tarr);
                        }
                    }



                    // ... 업뎃예정 !! 일단 무조건 크롤링 가져오도록 반환
                    return "NO";

                }

                // 기존 CSV 크롤링 이력 참조여부 check @ 2020.12.08.
                let isExistsCsv = checkCsvHistory(data[index].id.videoId);

                if (isExistsCsv == "YES") {
                    // No action for existing CSV crawling history
                }
                else {

                    // 표 형식 추가 @ 2020.11.21
                    // 참조 : https://blog.naver.com/neo_start/220322362255
                    video = `
                          <tr>
                            <td id='myiframe'>
                              <iframe src="https://www.youtube.com/embed/${item.id.videoId}" height="315" width="420" frameboarder="0" allowfullscreen></iframe>
                            </td>
                            <td>
                              <h6>${item.id.videoId}</h6>
                            </td>
                            <td>
                              <h6>${item.snippet.channelId}</h6>
                            </td>
                            <td>
                              <h6>${item.snippet.channelTitle}</h6>
                            </td>
                            <td>
                              <h6>${item.snippet.title}</h6>
                            </td>
                            <td>
                              <h6>${item.snippet.description}</h6>
                            </td>
                            <td>
                              <h6>${item.snippet.publishedAt}</h6>   
                            </td>
                        `
                    // video 상세정보 조회 + 추가한 다음, tbody에 표 형식으로 추가
                    _this.videoDetails(key, data[index].id.videoId, data[index].snippet.channelId, video);
                }
            });
        })
    },
    videoDetails : function(key, videoId, channelId, videoinfo){

        console.log("called videoDetails") // --> ", videoId, channelId)

        $("#contents").empty()

        $.get("https://www.googleapis.com/youtube/v3/videos?id="+videoId + "&key="+key
            + "&part=snippet,contentDetails,statistics,status", function(detail){
            console.log("called getting videoDetails")

            //$("#details").append("totalResults : " + detail.snippet.tags[])
            detail.items.forEach(function(item, index, det) {
                console.log('3-2', index, channelId)

                if (channelId == det[index].snippet.channelId) {
                    videoDetail = videoinfo + `
                                                  <td>
                                                    <h6>${det[index].contentDetails.duration}</h6>
                                                  </td>
                                                  <td>
                                                    <h6>${det[index].statistics.viewCount} </h6>
                                                  </td>
                                                  <td>
                                                    <h6>${det[index].statistics.commentCount} </h6>
                                                  </td>
                                                  <td>
                                                    <h6 style="height: 320px; overflow: auto">${det[index].snippet.tags} </h6>
                                                  </td>
                                                  <td>
                                                    <select>
                                                      <option disabled="disabled" selected="selected"></option>
                                                      <option value="1">Accept</option>
                                                      <option value="2">Review</option>
                                                      <option value="3">Reject</option>
                                                    </select>
                                                  </td>
                                                </tr>
                                              `

                    $("#videos").append(videoDetail);


                    //console.log(det[index].contentDetails.duration)
                    //console.log(det[index].statistics.viewCount)
                    //console.log(det[index].statistics.commentCount)
                    //console.log(det[index].snippet.tags)

                }

                //$("#contents").append("<br>contentDetails.duration : " + det[index].contentDetails.duration);
                //$("#contents").append("<br>statistics.viewCount : " + det[index].statistics.viewCount);
                //$("#contents").append("<br>statistics.commentCount : " + det[index].statistics.commentCount);
                //$("#contents").append("<br>statistics.favoriteCount : " + det[index].statistics.favoriteCount);
                //$("#contents").append("<br>snippet.tags : " + det[index].snippet.tags);

            })
        })
    }
};

main.init();


/*
$(document).ready(function(){
    var API_KEY = "AIzaSyAO0Ka2xeQ4Y2KcY465Nc1DGmCl9nzH2ic"
    var video = ""
    var videos = $("#videos")

    $("#form").on('click', function (event) {
        event.preventDefault();

        var search = $("#search").val();

        console.log("search.clicked ==> " + search)

        // videoLists 호출
        alert('videoLists call');

        videoLists(API_KEY, search, 5, "");
    })

    $("#channelInfo").on('click', function(event){
        event.preventDefault();
        //alert("form is submitted");

        var search = $("#search").val();
        var nextPageToken = $("#nextPageToken").val();

        //console.log(nextPageToken)
        videoLists(API_KEY, search, 5, nextPageToken);
    })

    $("#videoInfo").submit(function(event){
        event.preventDefault();

        var videoId = $("#videoId").val();
        //var nextPageToken = $("#nextPageToken").val();

        console.log("detail-info.clicked ==>" + videoId)

        videoDetails(API_KEY, videoId, channelId, videoinfo);
    })


    function videoLists(key, search, maxResults, nextPageToken){

        //$("#nextPageToken").empty()
        $("#totalResults").empty()
        $("#videos").empty()
        $("#contents").empty()

        $.get("https://www.googleapis.com/youtube/v3/search?key="+key
            + "&type=video&part=snippet&maxResults="+maxResults+ "&q="+search+"&pageToken="+nextPageToken, function(data){
            console.log('1')
            console.log(data)

            //$("#nextPageToken").append(data.nextPageToken)

            document.getElementById("nextPageToken").value = data.nextPageToken;

            $("#totalResults").append("totalResults : " + data.pageInfo.totalResults)

            data.items.forEach(function(item, index, data) {
                console.log('2-1', index)
                console.log(data[index])

                $.ajax({
                    type: "GET",
                    url: "/Users/holynomad/Downloads/ds-yt-csv-yyyymmdd.csv",
                    dataType: "text",
                    success: function(csvText) {checkCsvHistory(csvText, data[index].id.videoId);}
                });

                console.log('3-1 : start parsing csv')

                // 기존 CSV 크롤링 이력 참조여부 함수추가 @ 2020.12.08.
                // ref. : jQuery 이용 - https://sweetpotatocat.tistory.com/5
                //      : d3 이용 - https://m.blog.naver.com/PostView.nhn?blogId=wideeyed&logNo=221120158854&proxyReferer=https:%2F%2Fwww.google.com%2F
                function checkCsvHistory(csvList, newVideoId) {

                    // 기존 로컬 경로 CSV 파일 읽어와서, newVideoId와 겹치는 부분 있으면 "YES", 없으면 "NO"
                    // ...
                    var allTextLines = csvList.split(/\r\n|\n/);
                    var headers = allTextLines[0].split(',');
                    var lines = [];

                    console.log('3-2 : csvList, newVideoId, headers - ', csvList, newVideoId, headers)

                    for (var i=1; i<allTextLines.length; i++) {
                        var data = allTextLines[i].split(',');

                        console.log('3-3 : data - ', data)
                        if (data.length == headers.length) {

                            var tarr = [];
                            for (var j=0; j<headers.length; j++) {
                                tarr.push(headers[j]+":"+data[j]);
                                console.log('3-4 : data[j] - ', data[j])
                            }
                            lines.push(tarr);
                        }
                    }



                    // ... 업뎃예정 !!
                    return "NO";

                }

                // 기존 CSV 크롤링 이력 참조여부 check @ 2020.12.08.
                let isExistsCsv = checkCsvHistory(data[index].id.videoId);

                if (isExistsCsv == "YES") {
                    // No action for existing CSV crawling history
                }
                else {

                    // 표 형식 추가 @ 2020.11.21
                    // 참조 : https://blog.naver.com/neo_start/220322362255
                    video = `
                      <tr>
                        <td id='myiframe'>
                          <iframe src="https://www.youtube.com/embed/${item.id.videoId}" height="315" width="420" frameboarder="0" allowfullscreen></iframe>
                        </td>
                        <td>
                          <h6>${item.id.videoId}</h6>
                        </td>
                        <td>
                          <h6>${item.snippet.channelId}</h6>
                        </td>
                        <td>
                          <h6>${item.snippet.channelTitle}</h6>
                        </td>
                        <td>
                          <h6>${item.snippet.title}</h6>
                        </td>
                        <td>
                          <h6>${item.snippet.description}</h6>
                        </td>
                        <td>
                          <h6>${item.snippet.publishedAt}</h6>   
                        </td>
                    `
                    // video 상세정보 조회 + 추가한 다음, tbody에 표 형식으로 추가
                    videoDetails(API_KEY, data[index].id.videoId, data[index].snippet.channelId, video);
                }
            });
        })
    }

    function videoDetails(key, videoId, channelId, videoinfo){
        console.log("called videoDetails") // --> ", videoId, channelId)

        $("#contents").empty()

        $.get("https://www.googleapis.com/youtube/v3/videos?id="+videoId + "&key="+key
            + "&part=snippet,contentDetails,statistics,status", function(detail){
            console.log("called getting videoDetails")

            //$("#details").append("totalResults : " + detail.snippet.tags[])
            detail.items.forEach(function(item, index, det) {
                console.log('3-2', index, channelId)

                if (channelId == det[index].snippet.channelId) {

                    videoDetail = videoinfo + `
              <td>
                <h6>${det[index].contentDetails.duration}</h6>
              </td>
              <td>
                <h6>${det[index].statistics.viewCount} </h6>
              </td>
              <td>
                <h6>${det[index].statistics.commentCount} </h6>
              </td>
              <td>
                <h6 style="height: 320px; overflow: auto">${det[index].snippet.tags} </h6>
              </td>
              <td>
                <select>
                  <option disabled="disabled" selected="selected"></option>
                  <option value="1">Accept</option>
                  <option value="2">Review</option>
                  <option value="3">Reject</option>
                </select>
              </td>
            </tr>
          `

                    $("#videos").append(videoDetail);


                    //console.log(det[index].contentDetails.duration)
                    //console.log(det[index].statistics.viewCount)
                    //console.log(det[index].statistics.commentCount)
                    //console.log(det[index].snippet.tags)

                }

                //$("#contents").append("<br>contentDetails.duration : " + det[index].contentDetails.duration);
                //$("#contents").append("<br>statistics.viewCount : " + det[index].statistics.viewCount);
                //$("#contents").append("<br>statistics.commentCount : " + det[index].statistics.commentCount);
                //$("#contents").append("<br>statistics.favoriteCount : " + det[index].statistics.favoriteCount);
                //$("#contents").append("<br>snippet.tags : " + det[index].snippet.tags);

            })
        })
    }
})*/