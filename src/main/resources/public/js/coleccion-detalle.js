// Toggle para ocultar/mostrar el resumen (sin persistencia)
document.addEventListener('DOMContentLoaded', function () {
    const toggleBtn = document.getElementById('toggle-resumen');
    const resumen = document.getElementById('coleccion-resumen');
    const sectionMapa = document.querySelector('.section-mapa');

    if (!toggleBtn || !resumen || !sectionMapa) return;

    function redimensionarMapa() {
        if (window.map) {
            setTimeout(() => window.map.invalidateSize(), 300);
        }
    }

    function toggleResumen() {
        const toggleText = toggleBtn.querySelector('.toggle-text');
        const svg = toggleBtn.querySelector('svg');
        const oculto = resumen.classList.toggle('oculto');

        sectionMapa.classList.toggle('resumen-oculto', oculto);

        toggleText.textContent = oculto ? 'Mostrar' : 'Ocultar';
        toggleBtn.setAttribute('aria-label', oculto ? 'Mostrar resumen' : 'Ocultar resumen');
        toggleBtn.setAttribute('aria-expanded', (!oculto).toString());
        svg.style.transform = oculto ? 'rotate(180deg)' : 'rotate(0deg)';

        redimensionarMapa();
    }

    toggleBtn.addEventListener('click', toggleResumen);
});
