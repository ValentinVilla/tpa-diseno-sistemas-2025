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

window.onload = () => {
    window.scrollTo(0, 0);
};
