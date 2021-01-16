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

        var data2 = $('#search').val()

        $.ajax({
            type: 'GET',
            url: '/youtube',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            //data: JSON.stringify(data)
            data: data2
        }).done(function () {
            alert('검색하신 키워드의 유튜브 콘텐츠를 검색하였습니다.');
            window.location.href = '/youtube';
        }).fail(function (error) {
            alert(JSON.stringify(error))
        });
    }
};

main.init();