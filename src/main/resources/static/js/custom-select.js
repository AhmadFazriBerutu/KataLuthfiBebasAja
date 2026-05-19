document.querySelectorAll(".custom-select").forEach(function (select) {
    const trigger = select.querySelector(".select-trigger");
    const text = select.querySelector(".select-text");
    const hiddenInput = select.querySelector("input[type='hidden']");
    const options = select.querySelectorAll(".select-option");

    trigger.addEventListener("click", function () {
        document.querySelectorAll(".custom-select").forEach(function (otherSelect) {
            if (otherSelect !== select) {
                otherSelect.classList.remove("open");
            }
        });

        select.classList.toggle("open");
    });

    options.forEach(function (option) {
        option.addEventListener("click", function () {
            text.textContent = option.textContent;
            hiddenInput.value = option.getAttribute("data-value");

            options.forEach(function (item) {
                item.classList.remove("selected");
            });

            option.classList.add("selected");
            select.classList.remove("open");
        });
    });
});

document.addEventListener("click", function (event) {
    if (!event.target.closest(".custom-select")) {
        document.querySelectorAll(".custom-select").forEach(function (select) {
            select.classList.remove("open");
        });
    }
});