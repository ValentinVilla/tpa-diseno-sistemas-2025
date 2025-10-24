document.querySelectorAll('.nav-link').forEach(link => {
    link.addEventListener('click', e => {
        e.preventDefault();
        const targetId = link.getAttribute('href').substring(1);
        const targetSection = document.getElementById(targetId);
        if (targetSection) {
            const offset = document.querySelector('.admin-tabs').offsetHeight;
            const topPos = targetSection.getBoundingClientRect().top + window.scrollY - offset;
            window.scrollTo({ top: topPos, behavior: 'smooth' });
        }
    });
});

document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.btn-success, .btn-danger').forEach(btn => {
        btn.addEventListener('click', async (e) => {
            e.preventDefault(); // evita que el <a> navegue
            const url = btn.dataset.url || btn.getAttribute('href');
            try {
                await fetch(url, { method: 'POST' }); // llamás al endpoint
                window.location.reload(); // recargás la tabla
            } catch (err) {
                console.error('Error al procesar la solicitud', err);
            }
        });
    });
});

window.onload = () => {
    window.scrollTo(0, 0);
};

