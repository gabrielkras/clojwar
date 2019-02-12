// ROBOT GLOBAL CONTROLLER POSITION VARIABLES
var robotLastXPos = "";
var robotLastYPos = "";

// GRID GARBAGE COLLECTOR
function clearGrid() {
    $('#game-grid').find('tr').each(function(){
        $(this).find('td').each(function(){
            $(this).empty();
        })
    })
}

// REDRAW GRID FUNCTION
function redrawnGrid(grid) {
    clearGrid();
    $.each(grid.dinos, function (index, value) {
        if (value.facing == "down") {
            $("#x-" + value.position.x + "-y-" + value.position.y).html("<img src=\"/images/dino-down.gif\">")
        } else if (value.facing == "up") {
            $("#x-" + value.position.x + "-y-" + value.position.y).html("<img src=\"/images/dino-up.gif\">")
        } else if (value.facing == "left") {
            $("#x-" + value.position.x + "-y-" + value.position.y).html("<img src=\"/images/dino-left.gif\">")
        } else if (value.facing == "right") {
            $("#x-" + value.position.x + "-y-" + value.position.y).html("<img src=\"/images/dino-right.gif\">")
        }
    });
    $("#x-" + robotLastXPos + "-y-" + robotLastYPos).empty();
    robotLastXPos = grid.robot.position.x;
    robotLastYPos = grid.robot.position.y;
    if (grid.robot.facing == "down") {
        $("#x-" + robotLastXPos + "-y-" + robotLastYPos).html("<img src=\"/images/robot-down.gif\" height=\"45\">")
    } else if (grid.robot.facing == "up") {
        $("#x-" + robotLastXPos + "-y-" + robotLastYPos).html("<img src=\"/images/robot-up.gif\" height=\"45\">")
    } else if (grid.robot.facing == "left") {
        $("#x-" + robotLastXPos + "-y-" + robotLastYPos).html("<img src=\"/images/robot-left.gif\" height=\"45\">")
    } else if (grid.robot.facing == "right") {
        $("#x-" + robotLastXPos + "-y-" + robotLastYPos).html("<img src=\"/images/robot-right.gif\" height=\"45\">")
    }
}

// UPDATE GAME STATUS FUNCTION
function updateGameStatus(grid) {
    $('#game-status').html(grid.status);
    $('#robot-name').html(grid.robot.name);
    $('#robot-points').html(grid.robot.points);
    $('#robot-x-pos').html(grid.robot.position.x);
    $('#robot-y-pos').html(grid.robot.position.y);
    $('#dino-left').html(grid.dinos.length);
    if (grid.status == "created") {
        $('#create-dino').removeAttr('disabled')
        $('#prepare-game').removeAttr('disabled')
        $('#start-game').attr('disabled', 'disabled')
        $('#stop-game').attr('disabled', 'disabled')
        if ((grid.status == "created") && (jQuery.isEmptyObject(grid.robot))) {
            $('#create-robot').removeAttr('disabled')
        }
    } else if (grid.status == "ready") {
        $('#create-robot').attr('disabled', 'disabled')
        $('#create-dino').attr('disabled', 'disabled')
        $('#prepare-game').attr('disabled', 'disabled')
        $('#start-game').removeAttr('disabled')
        $('#stop-game').attr('disabled', 'disabled')
    } else if (grid.status == "running") {
        $('#create-robot').attr('disabled', 'disabled')
        $('#create-dino').attr('disabled', 'disabled')
        $('#prepare-game').attr('disabled', 'disabled')
        $('#start-game').attr('disabled', 'disabled')
        $('#stop-game').removeAttr('disabled')
    } else if (grid.status == "finished") {
        $('#create-robot').attr('disabled', 'disabled')
        $('#create-dino').removeAttr('disabled')
        $('#prepare-game').removeAttr('disabled', 'disabled')
        $('#start-game').attr('disabled', 'disabled')
        $('#stop-game').attr('disabled', 'disabled')
    }
}

// REFRESH GRID FUNCTION
function refreshGrid(gameId) {
    $.ajax({
        type: "GET",
        url: "/api/grid/" + gameId,
        success: function (result) {
            if (result.data.robot) {
                updateGameStatus(result.data);
                redrawnGrid(result.data);
            }
        },
        error: function (result) {
            alertError(result.responseJSON.message);
        }
    });
};

// ROBOT WALK COMMAND FUNCTION
function robotWalk(gameId, direction) {
    var robotParams = "{\"grid-id\":\"" + gameId+ "\"," +
        "\"action\":\"walk\"," +
        "\"value\":\"" + direction +"\"}"

    $.ajax({
        type: "PUT",
        url: "/api/grid/" + $('#game-id').val() + "/robot/actions",
        data: robotParams,
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function (result) {
            // CALL REFRESH PAGE
            refreshGrid(gameId);
        },
        error: function (result) {
            alertError(result.responseJSON.message);
        }
    });
}

// ROBOT ATTACK COMMAND FUNCTION
function robotAttack(gameId) {
    var robotParams = "{\"grid-id\":\"" + gameId+ "\"," +
        "\"action\":\"attack\"}"

    $.ajax({
        type: "PUT",
        url: "/api/grid/" + $('#game-id').val() + "/robot/actions",
        data: robotParams,
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function (result) {
            // CALL REFRESH PAGE
            refreshGrid(gameId);
        },
        error: function (result) {
            alertError(result.responseJSON.message);
        }
    });
}

// ROBOT OPERATIONS
$("#saveRobot").click(function () {
    var robotParams = "{\"grid-id\":\"" + $('#game-id').val() + "\"," +
        "\"name\":\"" + $('#robotName').val() + "\"," +
        "\"robot-pos-x\":" + $('#robot-pos-x').val() + "," +
        "\"robot-pos-y\":" + $('#robot-pos-y').val() + "," +
        "\"facing-direction\":\"" + $('#robotFacing').val() + "\"}"

    if ($('#robotName').val() != "") {
        $.ajax({
            type: "POST",
            url: "/api/grid/" + $('#game-id').val() + "/robot",
            data: robotParams,
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function (result) {
                alertSuccess("Robot was created successfully!");
                $('#robotModal').modal('hide');
                $('#create-robot').attr('disabled', 'disabled');
                $('#robot-name').html(result.data.robot.name);
                robotLastXPos = result.data.robot.position.x;
                robotLastYPos = result.data.robot.position.y;
                $('#robot-x-pos').html(robotLastXPos);
                $('#robot-y-pos').html(robotLastYPos);
                $('#robot-points').html(result.data.robot.points);
                // CALL REFRESH PAGE
                refreshGrid($('#game-id').val());
            },
            error: function (result) {
                alertError(result.responseJSON.message);
                $('#robotModal').modal('hide');
            }
        });
    }
});

// DINOSAUR OPERATIONS
$("#saveDino").click(function () {
    var robotParams = "{\"grid-id\":\"" + $('#game-id').val() + "\"," +
        "\"dino-pos-x\":" + $('#dino-pos-x').val() + "," +
        "\"dino-pos-y\":" + $('#dino-pos-y').val() + "}"

    $.ajax({
        type: "POST",
        url: "/api/grid/" + $('#game-id').val() + "/dino",
        data: robotParams,
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function (result) {
            alertSuccess("Dinosaur was add successfully!");
            $('#dinoModal').modal('hide');
            // CALL REFRESH PAGE
            refreshGrid($('#game-id').val());
        },
        error: function (result) {
            alertError(result.responseJSON.message);
            $('#dinoModal').modal('hide');
        }
    });
});

// ENABLES AND DISABLES GRID BORDER
$('#game-border').click(function(){
    if ($('#game-grid').hasClass("table-light")) {
        $('#game-grid').removeClass("table-light")
        $('#game-border').html("Add Grid Lines")
    } else {
        $('#game-grid').addClass("table-light")
        $('#game-border').html("Remove Grid Lines")
    }
})

// PREPARE GAME OPERATIONS
$("#prepare-game").click(function () {
    var robotParams = "{\"grid-id\":\"" + $('#game-id').val() + "\"," +
        "\"action\":\"prepare\"}"
    $.ajax({
        type: "PUT",
        url: "/api/grid/" + $('#game-id').val(),
        data: robotParams,
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function (result) {
            alertSuccess("The Game is Ready!");
            // CALL REFRESH PAGE
            refreshGrid($('#game-id').val());
        },
        error: function (result) {
            alertError(result.responseJSON.message);
        }
    });
});

// START GAME OPERATIONS
$("#start-game").click(function () {
    var robotParams = "{\"grid-id\":\"" + $('#game-id').val() + "\"," +
        "\"action\":\"start\"}"
    $.ajax({
        type: "PUT",
        url: "/api/grid/" + $('#game-id').val(),
        data: robotParams,
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function (result) {
            alertSuccess("The Game is Started");
            // CALL REFRESH PAGE
            refreshGrid($('#game-id').val());
        },
        error: function (result) {
            alertError(result.responseJSON.message);
        }
    });
});

// STOP GAME OPERATIONS
$("#stop-game").click(function () {
    var robotParams = "{\"grid-id\":\"" + $('#game-id').val() + "\"," +
        "\"action\":\"stop\"}"
    $.ajax({
        type: "PUT",
        url: "/api/grid/" + $('#game-id').val(),
        data: robotParams,
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function (result) {
            alertSuccess("The Game is Started");
            // CALL REFRESH PAGE
            refreshGrid($('#game-id').val());
        },
        error: function (result) {
            alertError(result.responseJSON.message);
        }
    });
});

// GAMEPLAY
$(document).on('keydown', function (event) {
    if ((event.keyCode === 87) || (event.keyCode === 38)) {
        // ROBOT WALK FORWARD
        robotWalk($('#game-id').val(), "forward");
    } else if ((event.keyCode === 83) || (event.keyCode === 40)) {
        // ROBOT WALK BACKWARD
        robotWalk($('#game-id').val(), "backward");
    } else if ((event.keyCode === 65) || (event.keyCode === 37)) {
        // ROBOT WALK LEFTWARD
        robotWalk($('#game-id').val(), "leftward");
    } else if ((event.keyCode === 68) || (event.keyCode === 39)) {
        // ROBOT WALK RIGHTWARD
        robotWalk($('#game-id').val(), "rightward");
    } else if ((event.keyCode === 13) || (event.keyCode === 32)) {
        // ROBOT ATTACK (SPACE or ENTER)
        robotAttack($('#game-id').val());
    }
});

$(document).ready(function () {
    refreshGrid($('#game-id').val());
})