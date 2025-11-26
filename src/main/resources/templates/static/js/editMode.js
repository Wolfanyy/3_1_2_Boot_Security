let editButtons = document.querySelectorAll('.button-edit');

editButtons.forEach(button => {

    button.addEventListener('click', function () {
        const row = button.closest('tr');
        const fields = row.querySelectorAll('.user-field');

        if (button.textContent === 'Edit') {
            fields.forEach(field => {
                field.disabled = false;
                editButtons.forEach(btn => {
                    if (btn !== button) {
                        btn.disabled = true;
                    }
                });
            });
            button.textContent = 'Save';
        } else {
            button.type = 'submit';
        }
    });
});