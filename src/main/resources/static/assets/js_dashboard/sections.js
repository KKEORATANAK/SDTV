$(document).ready(function() {

      $('#tableReponse tbody').on('click', '.edit-section', function() {
        var sectionId = $(this).data('id'); // Get the section ID from data attribute
        $.ajax({
            url: '/sections/edit/' + sectionId, // Adjust the URL to match your endpoint
            method: 'GET',
            success: function(data) {
                // Assuming data is of type SectionsResponse
                $('#edit_sections input[name="name"]').val(data.name);
                $('#edit_sections input[name="id"]').val(data.id);
                $('#edit_sections textarea[name="description"]').val(data.description);
                $('#edit_sections select[name="indexShow"]').val(data.indexShow);
                $('#edit_sections input[name="status"]').val(data.status);

                // Show the modal after setting the values
                $('#edit_sections').modal('show'); // This line will display the modal

            },
            error: function() {
                alert("Error fetching section data");
            }
        });
      });
});