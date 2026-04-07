/**
 * Aplicación SPA Toba — navegación por currentView + render().
 * Sustituir lecturas de appState por fetch() a Spring Boot cuando corresponda.
 */

// ----- Referencias DOM -----
const el = (id) => document.getElementById(id);

const viewLogin = el("view-login");
const shell = el("shell");
const viewBoard = el("view-board");
const viewDetail = el("view-detail");
const viewProjects = el("view-projects");
const viewTeams = el("view-teams");

const loginError = el("login-error");
const formLogin = el("form-login");

const projectBadgeWrap = el("project-badge-wrap");
const selectProject = el("select-project");
const userAvatar = el("user-avatar");
const userName = el("user-name");
const btnNewTicket = el("btn-new-ticket");
const btnLogout = el("btn-logout");
const navHome = el("nav-home");
const navBoard = el("nav-board");
const navProjects = el("nav-projects");
const navTeams = el("nav-teams");

const kanbanRoot = el("kanban-root");
const btnBackBoard = el("btn-back-board");
const detailBreadcrumb = el("detail-breadcrumb");
const detailTopic = el("detail-topic");
const detailStates = el("detail-states");
const detailTimeline = el("detail-timeline");
const detailComment = el("detail-comment");
const detailTransitionError = el("detail-transition-error");
const detailSidebar = el("detail-sidebar");

const projectsMetrics = el("projects-metrics");
const projectsList = el("projects-list");
const btnNewProject = el("btn-new-project");
const teamsRoot = el("teams-root");

const modalTicketBackdrop = el("modal-ticket-backdrop");
const formNewTicket = el("form-new-ticket");
const ntState = el("nt-state");
const ntProject = el("nt-project");
const ntCancel = el("nt-cancel");

const modalProjectBackdrop = el("modal-project-backdrop");
const formNewProject = el("form-new-project");
const npTeams = el("np-teams");
const npCancel = el("np-cancel");

// ----- Utilidades UI -----
function initials(user) {
  if (!user) return "?";
  return (user.name[0] + user.surname[0]).toUpperCase();
}

function priorityClass(p) {
  const map = {
    BLOCKER: "blocker",
    CRITICAL: "critical",
    HIGH: "high",
    MEDIUM: "medium",
    LOW: "low",
    TRIVIAL: "trivial",
  };
  return map[p] || "medium";
}

function priorityLabel(p) {
  return p;
}

function hideTransitionError() {
  detailTransitionError.textContent = "";
  detailTransitionError.classList.add("hidden");
}

function showTransitionError(msg) {
  detailTransitionError.textContent = msg;
  detailTransitionError.classList.remove("hidden");
}

function openModalTicket() {
  if (!loggedUser) return;
  fillProjectSelect(ntProject, projectsForUser(loggedUser));
  ntState.innerHTML = "";
  TICKET_STATES.forEach((s) => {
    const o = document.createElement("option");
    o.value = s;
    o.textContent = s.replace(/_/g, " ");
    ntState.appendChild(o);
  });
  formNewTicket.reset();
  const vis = projectsForUser(loggedUser);
  if (vis.length) ntProject.value = String(selectedProjectId || vis[0].id);
  modalTicketBackdrop.classList.remove("hidden");
}

function closeModalTicket() {
  modalTicketBackdrop.classList.add("hidden");
}

function openModalProject() {
  if (!loggedUser || !isProjectManager(loggedUser)) return;
  npTeams.innerHTML = "";
  appState.teams.forEach((t) => {
    const id = `np-team-${t.id}`;
    const lab = document.createElement("label");
    lab.innerHTML = `<input type="checkbox" name="team" value="${t.id}" id="${id}" /> ${escapeHtml(t.name)} <span style="color:var(--text-muted)">(${t.teamType})</span>`;
    npTeams.appendChild(lab);
  });
  formNewProject.reset();
  modalProjectBackdrop.classList.remove("hidden");
}

function closeModalProject() {
  modalProjectBackdrop.classList.add("hidden");
}

function fillProjectSelect(selectEl, projectList) {
  selectEl.innerHTML = "";
  projectList.forEach((p) => {
    const o = document.createElement("option");
    o.value = String(p.id);
    o.textContent = p.name;
    selectEl.appendChild(o);
  });
}

function refreshProjectDropdown() {
  if (!loggedUser) return;
  const vis = projectsForUser(loggedUser);
  const cur = String(selectedProjectId);
  selectProject.innerHTML = "";
  vis.forEach((p) => {
    const o = document.createElement("option");
    o.value = String(p.id);
    o.textContent = p.name;
    if (String(p.id) === cur) o.selected = true;
    selectProject.appendChild(o);
  });
}

// ----- Render principal -----
function render() {
  const loggedIn = !!loggedUser;

  viewLogin.classList.toggle("hidden", loggedIn);
  shell.classList.toggle("hidden", !loggedIn);

  if (!loggedIn) return;

  userAvatar.textContent = initials(loggedUser);
  userName.textContent = `${loggedUser.name} ${loggedUser.surname}`;

  syncSelectedProjectForUser(loggedUser);
  refreshProjectDropdown();

  document.querySelectorAll(".btn-nav").forEach((b) => {
    b.classList.toggle("is-active", b.dataset.nav === currentView);
  });

  const onBoard = currentView === "board";
  const visibleProjects = projectsForUser(loggedUser);
  projectBadgeWrap.classList.toggle(
    "hidden",
    !onBoard || visibleProjects.length === 0
  );
  btnNewTicket.classList.toggle(
    "hidden",
    !onBoard || visibleProjects.length === 0
  );
  btnNewProject.classList.toggle("hidden", !isProjectManager(loggedUser));

  viewBoard.classList.toggle("hidden", currentView !== "board");
  viewDetail.classList.toggle("hidden", currentView !== "detail");
  viewProjects.classList.toggle("hidden", currentView !== "projects");
  viewTeams.classList.toggle("hidden", currentView !== "teams");

  if (currentView === "board") renderBoard();
  if (currentView === "detail") renderDetail();
  if (currentView === "projects") renderProjects();
  if (currentView === "teams") renderTeams();
}

function renderBoard() {
  if (!loggedUser) return;
  const vis = projectsForUser(loggedUser);
  if (!vis.length || !selectedProjectId) {
    kanbanRoot.innerHTML =
      '<p class="kanban-empty" style="grid-column:1/-1;color:var(--text-secondary);padding:1rem 1.25rem">No tenés proyectos asignados a tu equipo.</p>';
    return;
  }

  const cols = [...TICKET_STATES];
  const tickets = ticketsForProject(selectedProjectId);

  kanbanRoot.innerHTML = cols
    .map((state) => {
      const inCol = tickets.filter((t) => t.currentState === state);
      const cards = inCol
        .map((t) => {
          const actor = lastActorForTicket(t.id);
          const av = actor
            ? `<span class="avatar sm" title="${actor.name}">${initials(actor)}</span>`
            : `<span class="avatar sm" title="Sin asignar">—</span>`;
          return `
            <article class="kanban-card" data-ticket-id="${t.id}" role="button" tabindex="0">
              <p class="kanban-card__topic">${escapeHtml(t.topic)}</p>
              <div class="kanban-card__meta">
                <span class="badge-priority ${priorityClass(t.priority)}">${priorityLabel(t.priority)}</span>
                <span class="kanban-card__id">TBA-${t.id}</span>
                ${av}
              </div>
            </article>`;
        })
        .join("");
      return `
        <div class="kanban-col" data-state="${state}">
          <h2 class="kanban-col__title">${state.replace(/_/g, " ")}</h2>
          ${cards}
        </div>`;
    })
    .join("");

  kanbanRoot.querySelectorAll(".kanban-card").forEach((card) => {
    const id = Number(card.dataset.ticketId);
    card.addEventListener("click", () => goTicketDetail(id));
    card.addEventListener("keydown", (e) => {
      if (e.key === "Enter" || e.key === " ") {
        e.preventDefault();
        goTicketDetail(id);
      }
    });
  });
}

function escapeHtml(s) {
  const d = document.createElement("div");
  d.textContent = s;
  return d.innerHTML;
}

function goTicketDetail(id) {
  if (!loggedUser || !userCanAccessTicket(loggedUser, id)) return;
  currentTicketId = id;
  currentView = "detail";
  detailComment.value = "";
  hideTransitionError();
  render();
}

function renderDetail() {
  if (!loggedUser) return;
  const ticket = appState.tickets.find((t) => t.id === currentTicketId);
  if (!ticket || !userCanAccessTicket(loggedUser, ticket.id)) {
    currentView = "board";
    render();
    return;
  }
  const project = findProject(ticket.projectId);
  detailBreadcrumb.innerHTML = `${escapeHtml(project.name)} / <strong>TBA-${ticket.id}</strong>`;
  detailTopic.textContent = ticket.topic;

  hideTransitionError();

  detailStates.innerHTML = TICKET_STATES.map((s) => {
    const cur = ticket.currentState === s;
    return `<button type="button" class="state-btn${cur ? " is-current" : ""}" data-state="${s}">${s.replace(/_/g, " ")}</button>`;
  }).join("");

  detailStates.querySelectorAll("[data-state]").forEach((btn) => {
    btn.addEventListener("click", () => {
      const fresh = appState.tickets.find((t) => t.id === currentTicketId);
      if (!fresh || !loggedUser) return;
      const newState = btn.dataset.state;
      if (newState === fresh.currentState) return;

      const msg = detailComment.value.trim();
      if (!msg) {
        showTransitionError(
          "Tenés que escribir un mensaje para poder cambiar de estado."
        );
        return;
      }

      setTicketState(fresh.id, newState);
      addTicketStage({
        ticketId: fresh.id,
        userId: loggedUser.id,
        state: newState,
        msg,
      });
      detailComment.value = "";
      hideTransitionError();
      render();
    });
  });

  const stages = stagesForTicket(ticket.id);
  detailTimeline.innerHTML = stages
    .map((st) => {
      const u = findUser(st.userId);
      const nm = u ? `${u.name} ${u.surname}` : "Usuario";
      const msg = st.msg
        ? escapeHtml(st.msg)
        : "<em style='color:var(--text-muted)'>Sin mensaje</em>";
      return `<li>
        <span class="avatar">${initials(u)}</span>
        <div class="timeline__body">
          <div class="timeline__head">
            <span class="timeline__name">${escapeHtml(nm)}</span>
            <span class="timeline__date">${st.createTime}</span>
            <span class="badge-state ${st.state}">${st.state.replace(/_/g, " ")}</span>
          </div>
          <p class="timeline__msg">${msg}</p>
        </div>
      </li>`;
    })
    .join("");

  const teamLabels = (project.teamIds || [])
    .map((tid) => {
      const team = findTeam(tid);
      return team ? `<span class="team-pill">${escapeHtml(team.name)}</span>` : "";
    })
    .join("");

  const created = ticketCreationHint(ticket.id);
  const stageCount = stages.length;

  detailSidebar.innerHTML = `
    <h3>Resumen</h3>
    <div class="sidebar-row"><strong>Proyecto</strong>${escapeHtml(project.name)}</div>
    <div class="sidebar-row"><strong>Prioridad</strong><span class="badge-priority ${priorityClass(ticket.priority)}">${priorityLabel(ticket.priority)}</span></div>
    <div class="sidebar-row"><strong>Equipos</strong>${teamLabels || "—"}</div>
    <div class="sidebar-row"><strong>Fecha de creación</strong>${created}</div>
    <div class="sidebar-row"><strong>Cantidad de stages</strong>${stageCount}</div>
  `;
}

function renderProjects() {
  if (!loggedUser) return;
  const m = globalTicketMetrics(loggedUser);
  projectsMetrics.innerHTML = `
    <div class="metric"><div class="metric__val">${m.total}</div><div class="metric__lbl">Total tickets</div></div>
    <div class="metric"><div class="metric__val">${m.inProgress}</div><div class="metric__lbl">En progreso</div></div>
    <div class="metric"><div class="metric__val">${m.resolved}</div><div class="metric__lbl">Resueltos</div></div>
    <div class="metric"><div class="metric__val">${m.closed}</div><div class="metric__lbl">Cerrados</div></div>
  `;

  const list = projectsForUser(loggedUser);
  projectsList.innerHTML = list
    .map((p) => {
      const ts = ticketsForProject(p.id);
      const total = ts.length;
      const done = projectDoneCount(ts);
      const pct = total ? Math.round((done / total) * 100) : 0;
      const counts = TICKET_STATES.map((st) => {
        const c = ts.filter((t) => t.currentState === st).length;
        return `<span class="pill">${st.replace(/_/g, " ")}: ${c}</span>`;
      }).join("");
      const teamsHtml = (p.teamIds || [])
        .map((tid) => {
          const team = findTeam(tid);
          return team
            ? `<span class="team-pill">${escapeHtml(team.name)}</span>`
            : "";
        })
        .join("");
      const members = new Set();
      (p.teamIds || []).forEach((tid) => {
        const team = findTeam(tid);
        if (team)
          team.memberIds.forEach((mid) => members.add(mid));
      });
      const avatars = [...members]
        .map((uid) => {
          const u = findUser(uid);
          return u
            ? `<span class="avatar sm" title="${escapeHtml(u.mail)}">${initials(u)}</span>`
            : "";
        })
        .join("");

      return `<article class="card project-row">
        <div>
          <div class="project-row__title">
            <h3>${escapeHtml(p.name)}</h3>
            <span class="badge-proj ${p.status}">${p.status.replace(/_/g, " ")}</span>
          </div>
          <p class="project-row__desc">${escapeHtml(p.description)}</p>
          <div class="progress-wrap">
            <div class="progress-bar" title="${done} / ${total} (resueltos+cerrados)"><div class="progress-bar__fill" style="width:${pct}%"></div></div>
          </div>
          <div class="pills">${counts}</div>
          <div>${teamsHtml}</div>
          <div class="members-row">${avatars || "<span style=\"color:var(--text-muted)\">Sin miembros</span>"}</div>
        </div>
      </article>`;
    })
    .join("");

  if (!list.length) {
    projectsList.innerHTML =
      '<p style="color:var(--text-secondary);padding:0 1.25rem">No tenés proyectos asignados a tu equipo.</p>';
  }
}

function renderTeams() {
  if (!loggedUser) return;
  const teams = teamsForUser(loggedUser);
  teamsRoot.innerHTML = teams
    .map((team) => {
      const members = team.memberIds
        .map((id) => findUser(id))
        .filter(Boolean);
      const projs = appState.projects.filter(
        (p) =>
          (p.teamIds || []).includes(team.id) &&
          userCanAccessProject(loggedUser, p.id)
      );
      const projList =
        projs.length > 0
          ? projs.map((p) => escapeHtml(p.name)).join(", ")
          : "—";
      const rows = members
        .map(
          (u) =>
            `<tr><td>${escapeHtml(u.name)} ${escapeHtml(u.surname)}</td><td>${escapeHtml(u.mail)}</td><td>${u.role}</td></tr>`
        )
        .join("");
      return `<article class="card team-block">
        <h3>${escapeHtml(team.name)} <span class="team-pill">${team.teamType}</span></h3>
        <p class="team-meta">Proyectos: ${projList}</p>
        <table class="data-table">
          <thead><tr><th>Miembro</th><th>Mail</th><th>Rol</th></tr></thead>
          <tbody>${rows}</tbody>
        </table>
      </article>`;
    })
    .join("");

  if (!teams.length) {
    teamsRoot.innerHTML =
      '<p style="color:var(--text-secondary)">No se encontró equipo asignado a tu usuario.</p>';
  }
}

// ----- Navegación -----
function goBoard() {
  currentView = "board";
  render();
}

function goProjects() {
  currentView = "projects";
  render();
}

function goTeams() {
  currentView = "teams";
  render();
}

function logout() {
  clearSession();
  loginError.textContent = "";
  formLogin.reset();
  closeModalTicket();
  closeModalProject();
  render();
}

// ----- Eventos -----
formLogin.addEventListener("submit", (e) => {
  e.preventDefault();
  loginError.textContent = "";
  const fd = new FormData(formLogin);
  const userField = String(fd.get("user") || "").trim();
  const password = String(fd.get("password") || "");
  const u = tryLogin(userField, password);
  if (!u) {
    loginError.textContent = "Usuario o contraseña incorrectos.";
    return;
  }
  loggedUser = u;
  syncSelectedProjectForUser(loggedUser);
  currentView = "board";
  render();
});

btnLogout.addEventListener("click", logout);

navHome.addEventListener("click", goBoard);
navBoard.addEventListener("click", goBoard);
navProjects.addEventListener("click", goProjects);
navTeams.addEventListener("click", goTeams);

selectProject.addEventListener("change", () => {
  selectedProjectId = Number(selectProject.value);
  render();
});

btnNewTicket.addEventListener("click", openModalTicket);
ntCancel.addEventListener("click", closeModalTicket);
modalTicketBackdrop.addEventListener("click", (e) => {
  if (e.target === modalTicketBackdrop) closeModalTicket();
});

formNewTicket.addEventListener("submit", (e) => {
  e.preventDefault();
  if (!loggedUser) return;
  const fd = new FormData(formNewTicket);
  const topic = String(fd.get("topic") || "").trim();
  const priority = String(fd.get("priority"));
  const currentState = String(fd.get("state"));
  const projectId = Number(fd.get("projectId"));
  const msg = String(fd.get("msg") || "").trim();
  if (!topic || !msg) return;
  if (!userCanAccessProject(loggedUser, projectId)) return;
  const ticketId = addTicket({ topic, priority, currentState, projectId });
  addTicketStage({
    ticketId,
    userId: loggedUser.id,
    state: currentState,
    msg,
  });
  closeModalTicket();
  render();
});

btnBackBoard.addEventListener("click", goBoard);

btnNewProject.addEventListener("click", openModalProject);
npCancel.addEventListener("click", closeModalProject);
modalProjectBackdrop.addEventListener("click", (e) => {
  if (e.target === modalProjectBackdrop) closeModalProject();
});

formNewProject.addEventListener("submit", (e) => {
  e.preventDefault();
  if (!loggedUser || !isProjectManager(loggedUser)) return;
  const name = String(el("np-name").value || "").trim();
  const description = String(el("np-desc").value || "").trim();
  const status = String(el("np-status").value);
  const checked = [
    ...npTeams.querySelectorAll('input[name="team"]:checked'),
  ].map((x) => Number(x.value));
  if (!name) return;
  addProject({
    name,
    description,
    status,
    teamIds: checked,
  });
  closeModalProject();
  render();
});

detailComment.addEventListener("input", hideTransitionError);

// ----- Inicio -----
render();
