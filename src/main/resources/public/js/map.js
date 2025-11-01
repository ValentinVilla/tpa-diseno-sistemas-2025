const centroInicial = [-34.6037, -58.3816];
const map = L.map('map').setView(centroInicial, 5);

// Hacer el mapa accesible globalmente para poder redimensionarlo
window.map = map;

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; OpenStreetMap contributors'
}).addTo(map);

let markers = [];

function renderHechosMapa(hechos) {
    markers.forEach(m => map.removeLayer(m));
    markers = [];

    hechos.forEach(h => {
        if (h.lat != null && h.lon != null) {
            const marker = L.marker([h.lat, h.lon])
                .addTo(map)
                .bindPopup(`<strong>${h.titulo}</strong><br>${h.categoria}<br>${h.fecha}`);
            markers.push(marker);
        }
    });

    if (markers.length > 0) {
        const group = L.featureGroup(markers);
        map.fitBounds(group.getBounds().pad(0.2));
    } else {
        map.setView(centroInicial, 5);
    }
}

function obtenerParametrosUrl() {
    const searchParams = new URLSearchParams(window.location.search);
    const params = {};
    for (const [key, value] of searchParams.entries()) {
        params[key] = value;
    }
    return params;
}

window.addEventListener('DOMContentLoaded', async () => {
    let url;
    // Si estamos en una página de colección específica, usar ese endpoint
    if (window.coleccionId) {
        url = `/colecciones/${window.coleccionId}/hechos.json`;
    } else {
        // Usar el endpoint general de hechos
        const params = obtenerParametrosUrl();
        url = `/hechos.json?${new URLSearchParams(params)}`;
    }
    
    const res = await fetch(url);
    const hechos = await res.json();
    renderHechosMapa(hechos);
    
    // Redimensionar el mapa después de cargar los hechos
    setTimeout(() => {
        if (window.map) {
            window.map.invalidateSize();
        }
    }, 100);
});