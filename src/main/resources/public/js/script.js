// Inicialización del mapa
let map;
let markers = [];

document.addEventListener('DOMContentLoaded', function() {
    initMap();
    initNavigation();
    initModals();
    initTabs();
    initCharCounter();

    // Simular modo admin (en producción esto vendría del backend)
    // document.body.classList.add('admin-mode');
});

// Inicializar mapa con Leaflet
function initMap() {
    map = L.map('map').setView([-34.6037, -58.3816], 5);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors'
    }).addTo(map);

    // Agregar marcadores de ejemplo
    const hechos = [
        { lat: -31.4201, lng: -64.1888, titulo: 'Incendio forestal en Córdoba', categoria: 'incendio' },
        { lat: -34.6037, lng: -58.3816, titulo: 'Inundación en Buenos Aires', categoria: 'desastre' },
        { lat: -31.6333, lng: -60.7000, titulo: 'Accidente múltiple en Ruta 9', categoria: 'accidente' }
    ];

    hechos.forEach(hecho => {
        const marker = L.marker([hecho.lat, hecho.lng]).addTo(map);
        marker.bindPopup(`<strong>${hecho.titulo}</strong><br><span class="badge badge-${hecho.categoria}">${hecho.categoria}</span>`);
        markers.push(marker);
    });
}

// Navegación entre secciones
function initNavigation() {
    const navLinks = document.querySelectorAll('.nav-link');
    const sections = document.querySelectorAll('.section-content, .section-mapa');

    navLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const targetId = this.getAttribute('href').substring(1);

            // Actualizar links activos
            navLinks.forEach(l => l.classList.remove('active'));
            this.classList.add('active');

            // Mostrar sección correspondiente
            sections.forEach(section => {
                section.classList.remove('active');
                if (section.id === targetId) {
                    section.classList.add('active');
                    if (targetId === 'mapa' && map) {
                        setTimeout(() => map.invalidateSize(), 100);
                    }
                }
            });
        });
    });
}

// Modales
function initModals() {
    // Modal Nueva Colección
    const btnNuevaColeccion = document.getElementById('btnNuevaColeccion');
    const modalNuevaColeccion = document.getElementById('modalNuevaColeccion');

    if (btnNuevaColeccion) {
        btnNuevaColeccion.addEventListener('click', () => {
            modalNuevaColeccion.classList.add('active');
        });
    }

    // Modal Solicitud Eliminación
    const btnsSolicitudEliminacion = document.querySelectorAll('.hecho-footer .btn-icon');
    const modalSolicitudEliminacion = document.getElementById('modalSolicitudEliminacion');

    btnsSolicitudEliminacion.forEach(btn => {
        btn.addEventListener('click', (e) => {
            e.stopPropagation();
            modalSolicitudEliminacion.classList.add('active');
        });
    });

    // Cerrar modales
    const modalCloses = document.querySelectorAll('.modal-close, .modal-overlay');
    modalCloses.forEach(close => {
        close.addEventListener('click', function() {
            this.closest('.modal').classList.remove('active');
        });
    });
}

// Tabs de administración
function initTabs() {
    const tabBtns = document.querySelectorAll('.tab-btn');
    const tabContents = document.querySelectorAll('.tab-content');

    tabBtns.forEach(btn => {
        btn.addEventListener('click', function() {
            const targetTab = this.getAttribute('data-tab');

            // Actualizar botones activos
            tabBtns.forEach(b => b.classList.remove('active'));
            this.classList.add('active');

            // Mostrar contenido correspondiente
            tabContents.forEach(content => {
                content.classList.remove('active');
                if (content.id === targetTab) {
                    content.classList.add('active');
                }
            });
        });
    });
}

// Contador de caracteres para justificación
function initCharCounter() {
    const textarea = document.getElementById('solicitudJustificacion');
    const charCount = document.getElementById('charCount');

    if (textarea && charCount) {
        textarea.addEventListener('input', function() {
            charCount.textContent = this.value.length;

            if (this.value.length >= 500) {
                charCount.style.color = 'var(--color-success)';
            } else {
                charCount.style.color = 'var(--color-text-muted)';
            }
        });
    }
}

// Modo de navegación (curado/irrestricto)
const btnModoNavegacion = document.getElementById('btnModoNavegacion');
if (btnModoNavegacion) {
    let modoCurado = true;
    btnModoNavegacion.addEventListener('click', function() {
        modoCurado = !modoCurado;
        this.innerHTML = modoCurado
            ? '<svg width="20" height="20" viewBox="0 0 20 20" fill="none"><path d="M10 3V17M3 10H17" stroke="currentColor" stroke-width="2"/></svg>Modo Curado'
            : '<svg width="20" height="20" viewBox="0 0 20 20" fill="none"><path d="M10 3V17M3 10H17" stroke="currentColor" stroke-width="2"/></svg>Modo Irrestricto';
    });
}