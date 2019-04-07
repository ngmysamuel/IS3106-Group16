// Disable all the dates in the booking calendar except for the dates of active experience date listings

function disableAllTheseDays(date) {
    var disabledDays = ["4-15-2019", "4-5-2019"];
    var m = date.getMonth(), d = date.getDate(), y = date.getFullYear();
    for (i = 0; i < disabledDays.length; i++) {
        if ($.inArray((m + 1) + '-' + d + '-' + y, disabledDays) != -1) {
            return [false,''];
        }
    }
    return [true,''];
}