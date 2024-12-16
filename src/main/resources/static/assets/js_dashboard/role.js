$(document).ready(function () {

	$('#tableReponse tbody').on('click', '.edit-role', function () {
		var roleId = $(this).data('id'); // Get the category ID from data attribute
		$.ajax({
			url: '/role/edit/' + roleId, // Adjust the URL to match your endpoint
			method: 'GET',
			success: function (data) {
                // Assuming data is of type CategorysResponse
				$('#edit_roles input[name="id"]').val(data.id);
				$('#edit_roles input[name="name"]').val(data.name);
				$('#edit_roles textarea[name="description"]').val(data.description);
				$('#edit_roles input[name="status"]').val(data.status);

				// Show the modal after setting the values
				$('#edit_roles').modal('show'); // This line will display the modal

			},
			error: function () {
				alert("Error fetching role data");
			}
		});
	});


	$('#name').on('input', function () {
		var name = $(this).val();

		// Make AJAX call to check for duplicate title
		$.ajax({
			url: '/role/checkNameDuplicate', // Adjust the path if necessary
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
});