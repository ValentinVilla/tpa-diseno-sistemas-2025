(function(){
    document.addEventListener('DOMContentLoaded', function(){
        try {
            const container = document.getElementById('mapaHecho');
            if (!container) return;

            const lat = parseFloat(container.dataset.lat);
            const lon = parseFloat(container.dataset.lon);
            if (!Number.isFinite(lat) || !Number.isFinite(lon)) return;

            function escapeHtml(str) {
                return String(str || '').replace(/[&<>"']/g, function(m) {
                    return ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[m]);
                });
            }

            const titulo = container.dataset.titulo || 'Hecho';
            const categoria = container.dataset.categoria || '';

            const mapa = L.map('mapaHecho').setView([lat, lon], 13);
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                maxZoom: 19,
                attribution: '&copy; OpenStreetMap contributors'
            }).addTo(mapa);

            const marker = L.marker([lat, lon]).addTo(mapa);
            const popupHtml = `<strong>${escapeHtml(titulo)}</strong>${categoria ? '<br/>' + escapeHtml(categoria) : ''}`;
            marker.bindPopup(popupHtml).openPopup();
        } catch (e) {
            console.warn('No se pudo inicializar el mapa del hecho:', e);
        }
    });
})();