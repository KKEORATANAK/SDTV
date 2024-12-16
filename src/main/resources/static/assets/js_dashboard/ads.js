$(document).ready(function () {
	$('#tableReponse tbody').on('click', '.edit-ads', function () {
		var adsId = $(this).data('id'); // Get the ads ID from data attribute
		$.ajax({
			url: '/ads/edit/' + adsId, // Adjust the URL to match your endpoint
			method: 'GET',
			success: function (data) {
				// Assuming data is of type CategorysResponse
				$('#edit_adss input[name="id"]').val(data.id);
				$('#edit_adss input[name="status"]').val(data.status);
                $('#edit_adss input[name="url"]').val(data.url);
                //$('#edit_adss select[name="local"]').val(data.local);
                $('#localSelect').val(data.local.trim()).trigger('change');
                // Update image preview if the ad has an image
                if (data.image) {
                    $('#imagePreview').attr('src', '/ads-image/' + data.image); // Update the image preview
                } else {
                    $('#imagePreview').attr('src', '/ads-image/default-image.jpg'); // Fallback default image
                }
				// Show the modal after setting the values
				$('#edit_adss').modal('show'); // This line will display the modal
			},
			error: function () {
				alert("Error fetching category data");
			}
		});
	});
//
//
//	$('#name').on('input', function () {
//		var name = $(this).val();
//
//		// Make AJAX call to check for duplicate title
//		$.ajax({
//			url: '/category/checkNameDuplicate', // Adjust the path if necessary
//			method: 'GET',
//			data: { name : name },
//			success: function (response) {
//				if (response.isDuplicate) {
//					$('#duplicateMessage').show(); // Show message if duplicate
//					$('#btn-submit').prop('disabled', true);
//				} else {
//					$('#duplicateMessage').hide(); // Hide message if not duplicate
//					$('#btn-submit').prop('disabled', false);
//				}
//			},
//			error: function () {
//				console.error("Error checking name duplicate status.");
//			}
//		});
//	});
});