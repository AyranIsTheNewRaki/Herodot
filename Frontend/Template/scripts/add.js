$(document).ready(function () {
    /*
    $('.if-tangible label').click(function () {
        $('#add-step2').hide();
        $('#add-step3').hide();
        $('#add-step4').hide();
        $('#radio1').prop('checked', false);
        $('.if-movable input').removeAttr('checked');
        if ($('.if-movable').css('display') == 'none') {
            $('.if-movable').fadeIn(200);
        }
    });
    $('.if-movable label').click(function () {
        $('#add-step3').hide();
        $('#add-step3 select')[0].selectedIndex = 0;
        $('#add-step3 select').addClass('gray');
        $('#add-step4').hide();
        if ($('#add-step3').css('display') == 'none') {
            $('#add-step3').fadeIn(200);
        }
    });
    $('#add-step3 select').change(function () {
        $(this).removeClass('gray');
        $('#add-step4').hide();
        $('#add-step4 input').val('');
        $('#add-step4 textarea').val('');
        $('#radio1').prop('checked', false);
        if ($('label[for="add-image"]').text() != '+ Add Image') {
            $('label[for="add-image"]').text('+ Add Image');
        }
        if ($('#add-step4').css('display') == 'none') {
            if ($(this).val() == '1' || $(this).val() == '3' || $(this).val() == '5') {
                $('.closing-date-wrapper').show();
                $('.replacement-wrapper').show();
                $('.still-in-its-place-wrapper').hide();
            } else {
                $('.closing-date-wrapper').hide();
                $('.replacement-wrapper').hide();
                $('.still-in-its-place-wrapper').show();
            }
            $('#add-step4').fadeIn(200);
        }
    });
    */
    $('#radio1').click(function () {
        if ($('#radio1').is(':checked') == false) {
            $('#radio1').prop('checked', true);
        } else {
            
        }
    });
    $('#add-image').click(function () {
        $('#add-image-value').trigger('click');
    });
    $('#add-image').change(function () {
        var val = $(this).val();
        var filename = val.replace(/^.*[\\\/]/, '');
        $('label[for="add-image"]').text(filename);
    });
});