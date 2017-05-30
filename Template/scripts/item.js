function adjustAnnotationsHeight() {
    $('#annotations').css('opacity', '1');
    $('#annotations').height($(window).height());
    $('#btn-annotations').height($(window).height());
}
$(document).ready(function () {
    $('#btn-annotations').click(function() {
        var right = parseInt($('#annotations').css('right'),10) == -240 ? 0 : -240;
        $('#annotations').animate({
           right: right
        }, {start: function () {
            $('#btn-annotations').fadeOut(300);
            }
        });
    });
    $('#btn-annotations-close').click(function() {
        var right = parseInt($('#annotations').css('right'),10) == -240 ? 0 : -240;
        $('#annotations').animate({
           right: right
        }, {start: function () {
            $('#btn-annotations').fadeIn(300);
            }
        });
    });
  adjustAnnotationsHeight();
});
$(window).resize(function() {
    adjustAnnotationsHeight();
});