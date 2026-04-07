/**
 * Datos mock alineados con enums de dominio (RoleEnum, ProjectStatus, TeamType,
 * PriorityEnum, TicketState). Sustituir por respuesta de API (fetch).
 *
 * RoleEnum: PROJECT_MANAGER, FRONT_END_DEV, BACK_END_DEV, TESTER_QA, TESTER_QS,
 *           TEAM_LEADER, BUSINESS_ANALYST
 * ProjectStatus: ACTIVE, ARCHIVED, ON_HOLD, ENDED
 * TeamType: DATA, DEVOPS, MANAGMENT, DEVELOPMENT
 * PriorityEnum: TRIVIAL, BLOCKER, LOW, MEDIUM, CRITICAL, HIGH
 * TicketState: OPEN, IN_PROGRESS, RESOLVED, CLOSED
 */
const INITIAL_DATA = {
  users: [
    {
      id: 1,
      name: "Marta",
      surname: "López",
      mail: "marta.l@toba.com",
      role: "PROJECT_MANAGER",
      teamId: 1,
      credentials: { userField: "marta.l", password: "1234" },
    },
    {
      id: 2,
      name: "Santiago",
      surname: "Ríos",
      mail: "santiago.r@toba.com",
      role: "BACK_END_DEV",
      teamId: 1,
      credentials: { userField: "santiago.r", password: "1234" },
    },
    {
      id: 3,
      name: "Juan",
      surname: "Pérez",
      mail: "juan.p@toba.com",
      role: "FRONT_END_DEV",
      teamId: 2,
      credentials: { userField: "juan.p", password: "1234" },
    },
    {
      id: 4,
      name: "Laura",
      surname: "Gómez",
      mail: "laura.g@toba.com",
      role: "TESTER_QA",
      teamId: 2,
      credentials: { userField: "laura.g", password: "1234" },
    },
  ],
  teams: [
    { id: 1, name: "back-1", teamType: "DEVELOPMENT", memberIds: [1, 2] },
    { id: 2, name: "front-2", teamType: "DEVELOPMENT", memberIds: [3, 4] },
  ],
  projects: [
    {
      id: 1,
      name: "BACKEND-API",
      description: "API REST principal en Spring Boot",
      startDate: "2026-01-10",
      status: "ACTIVE",
      teamIds: [1],
    },
    {
      id: 2,
      name: "FRONTEND-MVP",
      description: "Interfaz HTML/CSS/JS del kanban",
      startDate: "2026-03-01",
      status: "ENDED",
      teamIds: [2],
    },
    {
      id: 3,
      name: "INFRA-DEPLOY",
      description: "Docker, CI/CD y hosting",
      startDate: "2026-02-15",
      status: "ON_HOLD",
      teamIds: [1, 2],
    },
  ],
  tickets: [
    {
      id: 1,
      topic: "Modelar entidad Ticket y TicketStage",
      priority: "HIGH",
      currentState: "RESOLVED",
      projectId: 1,
    },
    {
      id: 2,
      topic: "Docker Compose con MySQL 8",
      priority: "MEDIUM",
      currentState: "CLOSED",
      projectId: 1,
    },
    {
      id: 3,
      topic: "CRUD básico de proyectos",
      priority: "LOW",
      currentState: "CLOSED",
      projectId: 1,
    },
    {
      id: 7,
      topic: "Validar relación Team → Project",
      priority: "MEDIUM",
      currentState: "IN_PROGRESS",
      projectId: 1,
    },
    {
      id: 8,
      topic: "Review PR: ticket stage history",
      priority: "HIGH",
      currentState: "IN_PROGRESS",
      projectId: 1,
    },
    {
      id: 9,
      topic: "Implementar autenticación con JWT",
      priority: "CRITICAL",
      currentState: "IN_PROGRESS",
      projectId: 1,
    },
    {
      id: 11,
      topic: "Crear endpoint de transición de estados",
      priority: "MEDIUM",
      currentState: "IN_PROGRESS",
      projectId: 1,
    },
    {
      id: 12,
      topic: "Deploy en servidor de staging",
      priority: "BLOCKER",
      currentState: "OPEN",
      projectId: 3,
    },
    {
      id: 14,
      topic: "Definir esquema de permisos por rol",
      priority: "HIGH",
      currentState: "OPEN",
      projectId: 1,
    },
    {
      id: 15,
      topic: "Configurar variables de entorno en prod",
      priority: "MEDIUM",
      currentState: "OPEN",
      projectId: 3,
    },
    {
      id: 16,
      topic: "Documentar endpoints de tickets",
      priority: "TRIVIAL",
      currentState: "OPEN",
      projectId: 1,
    },
  ],
  ticketStages: [
    {
      id: 1,
      ticketId: 9,
      userId: 1,
      state: "OPEN",
      msg: "Ticket creado. Depende de la entidad Credentials ya mergeada.",
      createTime: "2026-03-31",
    },
    {
      id: 2,
      ticketId: 9,
      userId: 2,
      state: "IN_PROGRESS",
      msg: "Empecé a trabajar en la integración del filtro de seguridad. Falta configurar el token refresh.",
      createTime: "2026-04-02",
    },
  ],
};
