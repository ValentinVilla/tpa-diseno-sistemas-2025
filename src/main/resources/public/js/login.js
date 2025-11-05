document.addEventListener('DOMContentLoaded', function() {
  const login = document.getElementById('login');
  const loginBtn = document.getElementById('login-btn');
  const loginBtnMobile = document.getElementById('login-btn-mobile');
  const loginClose = document.getElementById('login-close');
  
  const openLogin = () => {
    if (login) {
      login.classList.add('show-login');
      history.pushState(null, '', '/login');
    }
  };
  
  if (login && loginBtn) {
    loginBtn.addEventListener('click', openLogin);
  }
  if (login && loginBtnMobile) {
    loginBtnMobile.addEventListener('click', openLogin);
  }
  if (login && loginClose) {
    loginClose.addEventListener('click', () => {
      login.classList.remove('show-login');
      history.pushState(null, '', '/home');
    });
  }

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

  // Responsive reset: ensure nav state is correct when changing viewport size
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
});
