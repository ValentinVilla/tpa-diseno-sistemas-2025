(function () {
    const MAX_FILES = 10;
    const MAX_FILE_SIZE = 20 * 1024 * 1024; // 20MB
    const ALLOWED_TYPES = new Set([
        'image/jpeg', 'image/png', 'image/webp',
        'video/mp4', 'video/webm', 'video/ogg'
    ]);

    const input = document.getElementById('medias');
    const previewList = document.getElementById('previewList');
    const errorsEl = document.getElementById('mediasErrors');
    const submitBtn = document.getElementById('submitBtn');
    if (!input) return;

    const dt = new DataTransfer();

    function humanSize(bytes) {
        if (bytes < 1024) return bytes + ' B';
        if (bytes < 1024*1024) return (bytes/1024).toFixed(1) + ' KB';
        return (bytes/(1024*1024)).toFixed(2) + ' MB';
    }

    function showError(msg) { errorsEl.textContent = msg; }
    function clearError() { errorsEl.textContent = ''; }

    function isAllowed(file) {
        if (!ALLOWED_TYPES.has(file.type)) return `Tipo no permitido: ${file.name}`;
        if (file.size > MAX_FILE_SIZE) return `Archivo demasiado grande: ${file.name} (${humanSize(file.size)}). Límite ${humanSize(MAX_FILE_SIZE)}.`;
        return null;
    }

    function renderPreviews() {
        previewList.innerHTML = '';
        for (let i = 0; i < dt.files.length; i++) {
            const file = dt.files[i];
            const card = document.createElement('div');
            card.className = 'preview-card';

            const removeBtn = document.createElement('button');
            removeBtn.type = 'button';
            removeBtn.className = 'preview-remove';
            removeBtn.dataset.index = i;
            removeBtn.setAttribute('aria-label', `Eliminar ${file.name}`);
            removeBtn.textContent = '×';
            removeBtn.addEventListener('click', onRemoveFile);
            card.appendChild(removeBtn);

            if (file.type.startsWith('image/')) {
                const img = document.createElement('img');
                img.alt = file.name;
                img.src = URL.createObjectURL(file);
                img.onload = () => URL.revokeObjectURL(img.src);
                card.appendChild(img);
            } else if (file.type.startsWith('video/')) {
                const video = document.createElement('video');
                video.controls = true;
                video.muted = true;
                video.width = 170;
                video.src = URL.createObjectURL(file);
                video.onloadeddata = () => URL.revokeObjectURL(video.src);
                card.appendChild(video);
            } else {
                const p = document.createElement('p');
                p.textContent = file.name;
                card.appendChild(p);
            }

            const meta = document.createElement('div');
            meta.className = 'preview-meta';
            meta.textContent = `${file.name} — ${humanSize(file.size)}`;
            card.appendChild(meta);

            previewList.appendChild(card);
        }
        input.files = dt.files;
    }

    function onFilesSelected(e) {
        clearError();
        const files = Array.from(e.target.files || []);
        if (!files.length) return;

        if (dt.files.length + files.length > MAX_FILES) {
            showError(`Podés adjuntar como máximo ${MAX_FILES} archivos.`);
            input.value = ''; return;
        }

        for (const f of files) {
            const err = isAllowed(f);
            if (err) { showError(err); input.value = ''; return; }
            dt.items.add(f);
        }

        renderPreviews();
        input.value = '';
    }

    function onRemoveFile(evt) {
        const idx = Number(evt.currentTarget.dataset.index);
        if (Number.isNaN(idx)) return;
        const newDt = new DataTransfer();
        for (let i = 0; i < dt.files.length; i++) {
            if (i === idx) continue;
            newDt.items.add(dt.files[i]);
        }
        while (dt.items.length) dt.items.remove(0);
        for (let i = 0; i < newDt.files.length; i++) dt.items.add(newDt.files[i]);
        renderPreviews();
        clearError();
    }

    const form = input.closest('form');
    if (form) {
        form.addEventListener('submit', (ev) => {
            clearError();
            if (dt.files.length > MAX_FILES) { ev.preventDefault(); showError(`Máximo ${MAX_FILES} archivos permitidos.`); return; }
            for (let i = 0; i < dt.files.length; i++) {
                const err = isAllowed(dt.files[i]);
                if (err) { ev.preventDefault(); showError(err); return; }
            }
            submitBtn.disabled = true;
            submitBtn.textContent = 'Enviando...';
        });
    }

    input.addEventListener('change', onFilesSelected);

    if (input.files && input.files.length) {
        for (let i = 0; i < input.files.length; i++) dt.items.add(input.files[i]);
        renderPreviews();
    }
})();