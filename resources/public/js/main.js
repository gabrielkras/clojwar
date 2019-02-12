// LET'S GO BUTTON ACTION
$("#create-grid").click(function(){
    var parameters = "{\"name\":\""+$('#gameId').val()+"\"}"
    if ($('#gameId').val() != "") {
        $.ajax({
            type: "POST",
            url: "/api/grid",
            data: parameters,
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function(result){
                $(location).attr("href", "/game/"+$('#gameId').val());
            },
            error: function(result){
                alertError(result.responseJSON.message);
            }
        });
    }
});