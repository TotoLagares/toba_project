/**
 * Estado mutable en memoria. Tras integrar API: hidratar desde fetch y mutar tras POST/PATCH.
 */

function deepClone(obj) {
  return JSON.parse(JSON.stringify(obj));
}

/** @type {typeof INITIAL_DATA} */
let appState = deepClone(INITIAL_DATA);

/** @type {typeof INITIAL_DATA['users'][0] | null} */
let loggedUser = null;

/** Vista activa del SPA */
let currentView = "login";

/** @type {number | null} */
let currentTicketId = null;

/** Proyecto filtrado en el tablero */
let selectedProjectId = 1;

/** Estados de ticket (TicketState) — orden del tablero Kanban */
const TICKET_STATES = /** @type {const} */ ([
  "OPEN",
  "IN_PROGRESS",
  "RESOLVED",
  "CLOSED",
]);

const PRIORITIES = /** @type {const} */ ([
  "TRIVIAL",
  "BLOCKER",
  "LOW",
  "MEDIUM",
  "CRITICAL",
  "HIGH",
]);

/** Reinicia todo el mock (útil si se expone un botón de demo o tras recargar desde API). */
function resetAppState() {
  appState = deepClone(INITIAL_DATA);
  loggedUser = null;
  currentView = "login";
  currentTicketId = null;
  selectedProjectId = 1;
}

/** Solo sesión UI: no borra tickets/proyectos/stages en memoria. */
function clearSession() {
  loggedUser = null;
  currentView = "login";
  currentTicketId = null;
  selectedProjectId = 1;
}

function nextId(list, key = "id") {
  if (!list.length) return 1;
  return Math.max(...list.map((x) => x[key])) + 1;
}

function findUser(id) {
  return appState.users.find((u) => u.id === id);
}

function findProject(id) {
  return appState.projects.find((p) => p.id === id);
}

function findTeam(id) {
  return appState.teams.find((t) => t.id === id);
}

/**
 * Proyectos visibles: el equipo del usuario está asignado al proyecto (teamIds).
 */
function projectsForUser(user) {
  if (!user) return [];
  return appState.projects.filter((p) =>
    (p.teamIds || []).includes(user.teamId)
  );
}

function userCanAccessProject(user, projectId) {
  return projectsForUser(user).some((p) => p.id === projectId);
}

function userCanAccessTicket(user, ticketId) {
  const t = appState.tickets.find((x) => x.id === ticketId);
  if (!t || !user) return false;
  return userCanAccessProject(user, t.projectId);
}

/** Equipos visibles: solo el equipo al que pertenece el usuario (teamId). */
function teamsForUser(user) {
  if (!user) return [];
  const team = appState.teams.find((x) => x.id === user.teamId);
  return team ? [team] : [];
}

function isProjectManager(user) {
  return !!user && user.role === "PROJECT_MANAGER";
}

/**
 * Último usuario que actuó en stages del ticket (por createTime descendente, luego id).
 */
function lastActorForTicket(ticketId) {
  const stages = appState.ticketStages
    .filter((s) => s.ticketId === ticketId)
    .sort((a, b) => {
      const ta = new Date(a.createTime).getTime();
      const tb = new Date(b.createTime).getTime();
      if (tb !== ta) return tb - ta;
      return b.id - a.id;
    });
  if (!stages.length) return null;
  return findUser(stages[0].userId);
}

function ticketsForProject(projectId) {
  return appState.tickets.filter((t) => t.projectId === projectId);
}

/** Tickets de proyectos a los que el usuario tiene acceso */
function ticketsForUser(user) {
  const ids = new Set(projectsForUser(user).map((p) => p.id));
  return appState.tickets.filter((t) => ids.has(t.projectId));
}

function stagesForTicket(ticketId) {
  return appState.ticketStages
    .filter((s) => s.ticketId === ticketId)
    .sort((a, b) => {
      const ta = new Date(a.createTime).getTime();
      const tb = new Date(b.createTime).getTime();
      if (tb !== ta) return tb - ta;
      return b.id - a.id;
    });
}

function ticketCreationHint(ticketId) {
  const stages = appState.ticketStages
    .filter((s) => s.ticketId === ticketId)
    .sort((a, b) => a.id - b.id);
  if (stages.length) return stages[0].createTime;
  return "—";
}

/** Métricas solo sobre tickets de proyectos visibles para el usuario */
function globalTicketMetrics(user) {
  const t = ticketsForUser(user);
  return {
    total: t.length,
    inProgress: t.filter((x) => x.currentState === "IN_PROGRESS").length,
    resolved: t.filter((x) => x.currentState === "RESOLVED").length,
    closed: t.filter((x) => x.currentState === "CLOSED").length,
  };
}

/** Progreso: RESOLVED + CLOSED sobre total del proyecto */
function projectDoneCount(ticketsOfProject) {
  return ticketsOfProject.filter((x) =>
    x.currentState === "RESOLVED" || x.currentState === "CLOSED"
  ).length;
}

function tryLogin(userField, password) {
  const u = appState.users.find(
    (x) =>
      x.credentials.userField === userField &&
      x.credentials.password === password
  );
  return u || null;
}

function addTicket({ topic, priority, currentState, projectId }) {
  const id = nextId(appState.tickets);
  appState.tickets.push({
    id,
    topic,
    priority,
    currentState,
    projectId,
  });
  return id;
}

function addProject({ name, description, status, teamIds }) {
  const id = nextId(appState.projects);
  const today = new Date().toISOString().slice(0, 10);
  appState.projects.push({
    id,
    name,
    description,
    startDate: today,
    status,
    teamIds,
  });
  return id;
}

function addTicketStage({ ticketId, userId, state, msg }) {
  const id = nextId(appState.ticketStages);
  const createTime = new Date().toISOString().slice(0, 10);
  appState.ticketStages.push({
    id,
    ticketId,
    userId,
    state,
    msg: msg || "",
    createTime,
  });
  return id;
}

function setTicketState(ticketId, newState) {
  const ticket = appState.tickets.find((t) => t.id === ticketId);
  if (!ticket) return;
  ticket.currentState = newState;
}

/**
 * Asegura que selectedProjectId sea un proyecto visible; si no hay, deja el id inválido y la UI muestra vacío.
 */
function syncSelectedProjectForUser(user) {
  const list = projectsForUser(user);
  if (!list.length) {
    selectedProjectId = 0;
    return;
  }
  if (!list.some((p) => p.id === selectedProjectId))
    selectedProjectId = list[0].id;
}
