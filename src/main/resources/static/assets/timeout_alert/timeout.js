
$(document).ready(function () {
    // Function to show alert with animation
    function showAlert(alertId, duration) {
        $(alertId)
            .fadeIn(300) // Fade in the alert
            .delay(duration) // Keep it visible for the specified duration
            .fadeOut(300); // Fade out after the delay
    }

    // Show update alert if exists
    if ($("#updateAlert").length) {
        showAlert("#updateAlert", 10000); // Show for 10 seconds
    }

    // Show delete alert if exists
    if ($("#deleteAlert").length) {
        showAlert("#deleteAlert", 10000); // Show for 10 seconds
    }

    // Show create alert if exists
    if ($("#createAlert").length) {
        showAlert("#createAlert", 10000); // Show for 10 seconds
    }

    // Show active alert if exists
    if ($("#activeAlert").length) {
        showAlert("#activeAlert", 10000); // Show for 10 seconds
    }

    // Show error warning alert if exists
    if ($("#warningAlert").length) {
        showAlert("#warningAlert", 10000); // Show for 10 seconds
    }

    // Close button functionality
    $(".close").on("click", function () {
        $(this).closest(".alert").fadeOut(300); // Fade out the alert on close
    });
});