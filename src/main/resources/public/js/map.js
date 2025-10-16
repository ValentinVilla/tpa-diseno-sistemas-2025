const map = L.map('map').setView([-34.6037, -58.3816], 12);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; OpenStreetMap contributors'
}).addTo(map);

const hechos = [
    {
        titulo: "Incendio forestal en Córdoba",
        lat: -31.4201,
        lon: -64.1888,
        descripcion: "Foco de incendio reportado en zona montañosa, sin víctimas hasta el momento.",
        categoria: "Incendio forestal",
        fecha: "2025-01-15"
    },
    {
        titulo: "Contaminación del río Reconquista",
        lat: -34.6090,
        lon: -58.3950,
        descripcion: "Vertido de residuos industriales detectado en el cauce del río.",
        categoria: "Contaminación",
        fecha: "2025-02-10"
    },
    {
        titulo: "Desaparición forzada",
        lat: -34.6150,
        lon: -58.3800,
        descripcion: "Se reporta desaparición de persona en zona urbana, se buscan testigos.",
        categoria: "Derechos Humanos",
        fecha: "2025-03-05"
    }
];


/*
LA IDEA OBVIAMENTE A HACERLO CONTRA LA API, PERO NO ESTA DE MOMENTO
fetch('/api/hechos')
    .then(res => res.json())
    .then(hechos => {
        hechos.forEach(h => {
            L.marker([h.lat, h.lon])
                .addTo(map)
                .bindPopup(`<strong>${h.titulo}</strong>`);
        });
    });
*/

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
