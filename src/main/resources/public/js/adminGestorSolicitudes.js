// Lógica para admin solicitudes: sticky bar y navegación suave entre secciones
(function(){
  document.addEventListener('DOMContentLoaded', function(){
    try {
      const bar = document.querySelector('.admin-tabs--bar');
      const header = document.querySelector('.header');
      const headerHeight = () => (header ? header.offsetHeight : 64);

      // Sticky behavior (combina lo del adminSticky.js)
      if (bar) {
        const placeholder = document.createElement('div');
        placeholder.className = 'admin-tabs-placeholder';
        placeholder.style.display = 'none';
        bar.parentNode.insertBefore(placeholder, bar.nextSibling);

        // sentinel para IntersectionObserver
        const sentinel = document.createElement('div');
        sentinel.style.position = 'absolute';
        sentinel.style.width = '1px';
        sentinel.style.height = '1px';
        sentinel.style.marginTop = '-1px';
        bar.parentNode.insertBefore(sentinel, bar);

        const setRootAdminTop = (value) => {
          try {
            document.documentElement.style.setProperty('--admin-sticky-top', value);
          } catch (e) { /* silent */ }
        };

        const removeRootAdminTop = () => {
          try {
            document.documentElement.style.removeProperty('--admin-sticky-top');
          } catch (e) { /* silent */ }
        };

        const obs = new IntersectionObserver(entries => {
          entries.forEach(entry => {
            if (!entry.isIntersecting) {
              // fijar
              bar.classList.add('is-sticky');
              placeholder.style.height = bar.offsetHeight + 'px';
              placeholder.style.display = '';
              setRootAdminTop(headerHeight() + 'px');
            } else {
              bar.classList.remove('is-sticky');
              placeholder.style.display = 'none';
              removeRootAdminTop();
            }
          });
        }, { root: null, threshold: [0], rootMargin: `-${headerHeight()}px 0px 0px 0px` });

        obs.observe(sentinel);

        // actualizar top si cambia header size
        window.addEventListener('resize', function(){
          if (bar.classList.contains('is-sticky')) {
            setRootAdminTop(headerHeight() + 'px');
            placeholder.style.height = bar.offsetHeight + 'px';
          }
        });

        // pequeño fallback en scroll para ajustar top variable
        window.addEventListener('scroll', function(){
          if (bar.classList.contains('is-sticky')) {
            setRootAdminTop(headerHeight() + 'px');
          }
        });
      }

      // Manejo de clicks en los nav links: scroll suave hasta el top de cada sección
      function scrollToSection(id) {
        const el = document.getElementById(id);
        if (!el) return;
        const barHeight = (bar ? bar.offsetHeight : 0);
        const topOffset = headerHeight() + barHeight + 8; // 8px de margen extra
        const targetY = el.getBoundingClientRect().top + window.scrollY - topOffset;
        window.scrollTo({ top: targetY, behavior: 'smooth' });
      }

      document.querySelectorAll('.admin-tabs--bar .nav .nav-link').forEach(a => {
        a.addEventListener('click', function(e){
          // sólo si es enlace interno
          const href = a.getAttribute('href') || '';
          if (href.startsWith('#')) {
            e.preventDefault();
            const id = href.substring(1);
            scrollToSection(id);
            // actualizar hash sin salto inmediato
            history.replaceState(null, '', '#' + id);
          }
        });
      });

      // Toggle Ver más / Ver menos para fundamentaciones largas (event delegation)
      // Mostrar el texto completo en una fila expandida debajo de la fila para evitar solapamientos
      function escapeHtml(unsafe) {
        return String(unsafe)
          .replace(/&/g, '&amp;')
          .replace(/</g, '&lt;')
          .replace(/>/g, '&gt;')
          .replace(/"/g, '&quot;')
          .replace(/'/g, '&#039;');
      }

      document.addEventListener('click', function(e){
        const t = e.target.closest('.more-toggle');
        if (!t) return;
        e.preventDefault();
        const tr = t.closest('tr');
        if (!tr) return;
        const table = tr.closest('table');
        const thead = table ? table.querySelector('thead tr') : null;
        const cols = thead ? thead.children.length : tr.children.length;

        // if next row is the expanded row for this tr, remove it (toggle close)
        const next = tr.nextElementSibling;
        if (next && next.classList.contains('fundamentacion-expanded-row') && next.dataset.for === tr.id) {
          next.remove();
          t.textContent = 'Ver más';
          return;
        }

        // otherwise insert an expanded row below
        const fundamentacionEl = tr.querySelector('.fundamentacion');
        const content = fundamentacionEl ? fundamentacionEl.textContent.trim() : '';

        const expandedTr = document.createElement('tr');
        expandedTr.className = 'fundamentacion-expanded-row';
        expandedTr.setAttribute('data-for', tr.id || '');
        const td = document.createElement('td');
        td.setAttribute('colspan', cols);
        td.innerHTML = '<div class="fundamentacion-full">' + escapeHtml(content).replace(/\n/g, '<br>') + '</div>';
        expandedTr.appendChild(td);
        tr.parentNode.insertBefore(expandedTr, tr.nextSibling);
        t.textContent = 'Ver menos';
      });

      // keyboard support: Enter / Space
      document.addEventListener('keydown', function(e){
        const active = document.activeElement;
        if (!active) return;
        if (!active.classList || !active.classList.contains('more-toggle')) return;
        if (e.key === 'Enter' || e.key === ' ') {
          e.preventDefault();
          active.click();
        }
      });

      // Si hay hash en la URL al cargar, navegar suavemente
      if (location.hash) {
        const id = location.hash.substring(1);
        // esperar un tick para que el layout esté listo
        setTimeout(() => scrollToSection(id), 60);
      }
    } catch (err) {
      console.error('adminGestorSolicitudes error', err);
    }
  });
})();
