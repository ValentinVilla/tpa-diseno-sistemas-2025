(function () {
    const grid = document.getElementById('mediaGrid');
    const modal = document.getElementById('mediaModal');
    const modalContent = document.getElementById('mediaModalContent');
    const closeBtn = document.getElementById('modalClose');

    if (!grid || !modal || !modalContent || !closeBtn) return;

    function openMedia(src, isVideo) {
        modalContent.innerHTML = '';
        if (isVideo) {
            const v = document.createElement('video');
            v.controls = true;
            v.autoplay = true;
            v.src = src;
            v.style.maxWidth = '100%';
            v.style.maxHeight = '88vh';
            modalContent.appendChild(v);
        } else {
            const img = document.createElement('img');
            img.src = src;
            img.alt = '';
            img.style.maxWidth = '100%';
            img.style.maxHeight = '88vh';
            modalContent.appendChild(img);
        }
        modal.setAttribute('aria-hidden', 'false');
        document.body.style.overflow = 'hidden';
    }

    function closeModal() {
        modal.setAttribute('aria-hidden', 'true');
        modalContent.innerHTML = '';
        document.body.style.overflow = '';
    }

    grid.addEventListener('click', (e) => {
        let el = e.target;
        while (el && !el.classList.contains('media-thumb')) el = el.parentElement;
        if (!el) return;
        const src = el.getAttribute('data-src');
        const child = el.querySelector('.thumb-media');
        const isVideo = child && child.tagName.toLowerCase() === 'video' || /\.mp4|\.webm|\.ogg$/i.test(src);
        openMedia(src, isVideo);
    });

    grid.addEventListener('keydown', (e) => {
        if (e.key === 'Enter' || e.key === ' ') {
            let el = e.target;
            while (el && !el.classList.contains('media-thumb')) el = el.parentElement;
            if (!el) return;
            const src = el.getAttribute('data-src');
            const isVideo = el.querySelector('.thumb-media') && el.querySelector('.thumb-media').tagName.toLowerCase() === 'video';
            openMedia(src, isVideo);
        }
    });

    closeBtn.addEventListener('click', closeModal);
    modal.addEventListener('click', (e) => { if (e.target === modal) closeModal(); });
    document.addEventListener('keydown', (e) => { if (e.key === 'Escape') closeModal(); });
})();