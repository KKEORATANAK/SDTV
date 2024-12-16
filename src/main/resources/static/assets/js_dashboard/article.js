$(document).ready(function() {
        let articleId; // Variable to hold the article ID

        // Capture the article ID when the modal is shown
        $('#delete_project').on('show.bs.modal', function(event) {
            const button = $(event.relatedTarget); // Button that triggered the modal
            articleId = button.data('id'); // Extract info from data-* attributes
        });

        // Handle the confirm delete button click
        $('#confirmDelete').on('click', function() {
            $.ajax({
                url: '/api/news/delete/' + articleId, // Your delete endpoint
                type: 'DELETE',
                success: function(response) {
                    // Handle success (you can refresh the page or remove the item from the DOM)
                    alert(response.message); // Display success message
                    location.reload(); // Optionally refresh the page
                },
                error: function(xhr, status, error) {
                    // Handle error
                    alert('Error deleting the article: ' + error);
                }
            });
            $('#delete_project').modal('hide'); // Hide the modal after clicking delete
        });
    });