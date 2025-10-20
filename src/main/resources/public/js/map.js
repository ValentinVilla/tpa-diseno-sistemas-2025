if (L.DomUtil.get('map') != null) {
    L.DomUtil.get('map')._leaflet_id = null; // resetea el div
}
const map = L.map('map').setView([-34.6037, -58.3816], 5);


L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; OpenStreetMap contributors'
}).addTo(map);


fetch('/hechos')
    .then(res => res.json())
    .then(hechos => {
        if (hechos.length === 0) {
            console.warn("No hay hechos con coordenadas para mostrar.");
        }
        hechos.forEach(h => {
            const contenido = `
                <strong>${h.titulo}</strong><br>
                <em>Categoría:</em> ${h.categoria}<br>
                <em>Fecha:</em> ${h.fecha}<br>
                <p>${h.descripcion}</p>
            `;

            L.marker([h.lat, h.lon])
                .addTo(map)
                .bindPopup(contenido);
        });
    })
    .catch(err => console.error("Error cargando los hechos:", err));;