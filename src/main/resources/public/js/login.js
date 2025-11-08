document.addEventListener('DOMContentLoaded', function() {
    const login = document.getElementById('login');
    const loginBtn = document.getElementById('login-btn');
    const loginBtnMobile = document.getElementById('login-btn-mobile');
    const loginClose = document.getElementById('login-close');

    const form = login ? login.querySelector('.login__form') : null;
    const errorBox = login ? login.querySelector('.login-error') : null;
    const inputs = login ? login.querySelectorAll('input[type="email"], input[type="password"]') : [];

    const openLogin = () => {
        if (login) {
            inputs.forEach(i => i.value = '');
            login.classList.add('show-login');
        }
    };

    const closeLogin = () => {
        if (login) {
            login.classList.remove('show-login');
        }
    };

    window.openLogin = openLogin;
    window.closeLogin = closeLogin;

    if (login && loginBtn) loginBtn.addEventListener('click', openLogin);
    if (login && loginBtnMobile) loginBtnMobile.addEventListener('click', openLogin);
    if (login && loginClose) loginClose.addEventListener('click', closeLogin);

    const menuToggle = document.getElementById('menu-toggle');
    const primaryNav = document.getElementById('primary-navigation');
    if (menuToggle && primaryNav) {
        menuToggle.addEventListener('click', () => {
            const open = primaryNav.classList.toggle('is-open');
            menuToggle.setAttribute('aria-expanded', String(open));
        });
    }

    const profileBtn = document.getElementById('profile-btn');
    const profileMenu = document.getElementById('profile-menu');
    if (profileBtn && profileMenu) {
        profileBtn.addEventListener('click', (e) => {
            e.stopPropagation();
            const open = profileMenu.classList.toggle('is-open');
            profileBtn.setAttribute('aria-expanded', String(open));
        });
        document.addEventListener('click', (e) => {
            if (!profileMenu.classList.contains('is-open')) return;
            if (e.target === profileBtn || profileBtn.contains(e.target)) return;
            if (!profileMenu.contains(e.target)) {
                profileMenu.classList.remove('is-open');
                profileBtn.setAttribute('aria-expanded', 'false');
            }
        });
    }

    function updateResponsiveNav() {
        if (!menuToggle || !primaryNav) return;
        const isMobile = window.matchMedia('(max-width: 1024px)').matches;
        if (!isMobile) {
            primaryNav.classList.remove('is-open');
            menuToggle.setAttribute('aria-expanded', 'false');
        }
    }
    updateResponsiveNav();
    window.addEventListener('resize', updateResponsiveNav);

    function hideErrorBoxWithFade() {
        if (!errorBox) return;
        errorBox.classList.add('fade-out');
        setTimeout(() => {
            try {
                errorBox.style.display = 'none';
                errorBox.classList.remove('fade-out');
            } catch (e) {}
        }, 260);
    }

    function showErrorBox() {
        if (!errorBox) return;
        errorBox.style.display = '';
        errorBox.classList.remove('fade-out');
    }

    if (inputs && inputs.length) {
        inputs.forEach(input => {
            input.addEventListener('input', () => {
                input.classList.remove('input-error');

                const anyErrorLeft = Array.from(inputs).some(i => i.classList.contains('input-error'));
                if (!anyErrorLeft && form) {
                    form.classList.remove('has-error');
                    hideErrorBoxWithFade();
                }
            });

            input.addEventListener('paste', () => input.classList.remove('input-error'));
            input.addEventListener('change', () => {
                input.classList.remove('input-error');
                const anyErrorLeft = Array.from(inputs).some(i => i.classList.contains('input-error'));
                if (!anyErrorLeft && form) {
                    form.classList.remove('has-error');
                    hideErrorBoxWithFade();
                }
            });
        });
    }

    try {
        if (login && login.dataset && login.dataset.openLogin === 'true') {
            if (errorBox) {
                showErrorBox();
            }
            openLogin();
            const input = login.querySelector('input[name="email"], input[type="email"]');
            if (input) input.focus();
        }
    } catch (e) {
        console.error('login.js auto-open check failed', e);
    }
});