document.addEventListener("DOMContentLoaded", () => {
    const toggleBtn = document.getElementById("toggle-filtros");
    const filtrosPanel = document.getElementById("filtros-panel");

    toggleBtn.addEventListener("click", () => {
        filtrosPanel.classList.toggle("oculto");
    });
});