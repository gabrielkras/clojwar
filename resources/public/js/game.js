/*
USER ALERTS FUNCTIONS
*/
function alertSuccess(message) {
    var htmlAlert = "<div class=\"alert alert-success alert-dismissible fade show\" role=\"alert\">" +
                    message +
                    "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                    "<span aria-hidden=\"true\">&times;</span>" +
                    "</button> </div>"
    $('#alerts').html(htmlAlert);
};

function alertError(message) {
    var htmlAlert = "<div class=\"alert alert-danger alert-dismissible fade show\" role=\"alert\">" +
                    message +
                    "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                    "<span aria-hidden=\"true\">&times;</span>" +
                    "</button> </div>"
    $('#alerts').html(htmlAlert);
};

function alertWarning(message) {
    var htmlAlert = "<div class=\"alert alert-warning alert-dismissible fade show\" role=\"alert\">" +
                    message +
                    "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\">" +
                    "<span aria-hidden=\"true\">&times;</span>" +
                    "</button> </div>"
    $('#alerts').html(htmlAlert);
};
