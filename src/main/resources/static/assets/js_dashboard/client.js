$(document).ready(function () {

	$('#tableReponse tbody').on('click', '.edit-client', function () {
		var clientId = $(this).data('id'); // Get the category ID from data attribute
		$.ajax({
			url: '/client/edit/' + clientId, // Adjust the URL to match your endpoint
			method: 'GET',
			success: function (data) {
                // Assuming data is of type CategorysResponse
				$('#edit_clients input[name="id"]').val(data.id);
				$('#edit_clients input[name="name"]').val(data.name);
				$('#edit_clients input[name="phoneNum"]').val(data.phoneNum);
				$('#edit_clients textarea[name="description"]').val(data.description);
				$('#edit_clients input[name="status"]').val(data.status);

				// Show the modal after setting the values
				$('#edit_clients').modal('show'); // This line will display the modal

			},
			error: function () {
				alert("Error fetching category data");
			}
		});
	});


	$('#name').on('input', function () {
		var name = $(this).val();

		// Make AJAX call to check for duplicate title
		$.ajax({
			url: '/client/checkNameDuplicate', // Adjust the path if necessary
			method: 'GET',
			data: { name : name },
			success: function (response) {
				if (response.isDuplicate) {
					$('#duplicateMessage').show(); // Show message if duplicate
					$('#btn-submit').prop('disabled', true);
				} else {
					$('#duplicateMessage').hide(); // Hide message if not duplicate
					$('#btn-submit').prop('disabled', false);
				}
			},
			error: function () {
				console.error("Error checking name duplicate status.");
			}
		});
	});

	let clientId = ''; // Store the ARTICLE_ID for deletion
    // When the modal is triggered, set the articleId
    $('#delete_project').on('show.bs.modal', function (event) {
        const button = $(event.relatedTarget); // Button that triggered the modal
        clientId = button.data('id'); // Extract ARTICLE_ID from data-id
    });

    // Handle the delete confirmation button click
    $('#confirmDelete').on('click', function () {
        if (clientId) {
            const deleteUrl = `/client/delete/${clientId}`; // Construct the delete URL dynamically
            $.ajax({
                url: deleteUrl,
                type: 'DELETE', // HTTP DELETE request
                success: function (response) {
                    // Notify user and refresh the page
                    location.reload(); // Optionally reload to update the UI
                },
                error: function (xhr, status, error) {
                    // Handle the error
                    alert(`Failed to delete the article: ${xhr.responseText || error}`);
                }
            });
        } else {
            alert('Invalid article ID.');
        }
    });

});