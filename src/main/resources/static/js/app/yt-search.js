var main = {
    init : function () {
        var _this = this;
        // 기존 #search를 임시테스트용 전환에 따른 #search-test로 변경 @ 2021.01.04.
        $('#btn-search').on('click', function () {
                _this.search();
            }
        );
    },
    search : function () {
        alert('yt-search start !');

        //var data = {
        //    searchResults: $('#search').val()
        //};

        var searchKeyword = $('#search').val()

        $.ajax({
            type: 'POST',
            url: '/youtube/' + searchKeyword,
            //type: 'GET',
            //url: '/youtube',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            //data: JSON.stringify(data)
            //data: searchKeyword
        }).done(function () {
            alert('검색하신 키워드의 유튜브 콘텐츠를 검색하였습니다.');
            window.location.href = '/show-yt-results';
        }).fail(function (error) {
            alert(JSON.stringify(error))
        });
    }
};

main.init();