$(document).ready(function () {

    var enter = new Date();
    enter.setDate(enter.getDate() + 1);
    $(".enterdatepicker").datepicker({
        todayBtn:  1,
        autoclose: true,
        format: "yyyy-mm-dd",
        startDate: enter,
    }).on('changeDate', function (selected) {
        var minDate = new Date(selected.date.valueOf());
        minDate.setDate(minDate.getDate()+1);
        $('.exitdatepicker').datepicker('setStartDate', minDate);
    });

    var exit = new Date();
    exit.setDate(exit.getDate() + 2);
    $(".exitdatepicker").datepicker({
        autoclose: true,
        format: "yyyy-mm-dd",
        startDate: exit
    }).on('changeDate', function (selected) {
        var maxDate = new Date(selected.date.valueOf());
        maxDate.setDate(maxDate.getDate()-1);
        $('.enterdatepicker').datepicker('setEndDate', maxDate);
    });

    $('#btn_modal').click(function () {

        myModal = new bootstrap.Modal(document.getElementById('modal'), {
            keyboard: false
        })
        myModal.show()

    });
});


