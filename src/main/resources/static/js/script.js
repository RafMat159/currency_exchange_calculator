function clearOtherInputValue(inputId, otherInputId) {
    let input = document.getElementById(inputId);
    let otherInput = document.getElementById(otherInputId);

    if (input.value !== "") {
        otherInput.value = "";
    }
}