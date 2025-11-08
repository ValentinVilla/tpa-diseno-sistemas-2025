document.addEventListener('DOMContentLoaded', function () {
    const list = document.querySelector('.hechos-lista');
    if (!list) return;

    function sendPreviewByPost(data) {
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = '/hechos/preview';
        form.style.display = 'none';
        // añadir inputs
        Object.keys(data).forEach(key => {
            if (data[key] == null) return;
            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = key;
            input.value = data[key];
            form.appendChild(input);
        });
        document.body.appendChild(form);
        form.submit();
    }

    list.addEventListener('click', function (ev) {
        // si clicaron el icono de eliminar, no interferimos
        if (ev.target.closest('.delete-icon')) return;

        const card = ev.target.closest('.hecho-card');
        if (!card) return;

        const rawId = card.dataset.id;
        const id = rawId ? parseInt(rawId, 10) : NaN;
        const hasValidId = Number.isInteger(id) && id > 0;

        if (hasValidId) {
            // navegar a /hechos/{id} (persistidos)
            window.location.href = '/hechos/' + encodeURIComponent(id);
            return;
        }

        // no hay id: enviamos POST a /hechos/preview con los datos de la card
        ev.preventDefault();
        const payload = {
            titulo: card.dataset.titulo || '',
            descripcion: card.dataset.descripcion || '',
            categoria: card.dataset.categoria || '',
            provincia: card.dataset.provincia || '',
            fechaAcontecimiento: card.dataset.fecha || '',
            fechaCarga: card.dataset.fechaCarga || '',
            latitud: card.dataset.lat || '',
            longitud: card.dataset.lon || '',
            origen: card.dataset.origen || ''
        };
        sendPreviewByPost(payload);
    });

    // accesibilidad: Enter abre igual
    list.addEventListener('keydown', function (ev) {
        if (ev.key !== 'Enter') return;
        if (ev.target.closest('.delete-icon')) return;
        const card = ev.target.closest('.hecho-card');
        if (!card) return;

        const rawId = card.dataset.id;
        const id = rawId ? parseInt(rawId, 10) : NaN;
        const hasValidId = Number.isInteger(id) && id > 0;

        if (hasValidId) {
            window.location.href = '/hechos/' + encodeURIComponent(id);
            return;
        }

        ev.preventDefault();
        const payload = {
            titulo: card.dataset.titulo || '',
            descripcion: card.dataset.descripcion || '',
            categoria: card.dataset.categoria || '',
            provincia: card.dataset.provincia || '',
            fechaAcontecimiento: card.dataset.fecha || '',
            fechaCarga: card.dataset.fechaCarga || '',
            latitud: card.dataset.lat || '',
            longitud: card.dataset.lon || '',
            origen: card.dataset.origen || ''
        };
        sendPreviewByPost(payload);
    });
});