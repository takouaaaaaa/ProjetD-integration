import { createRouter, createWebHashHistory } from "vue-router";
import AdminView from "@/views/AdminView.vue";
import CompaniesView from "@/views/CompaniesView.vue";
import LoginView from "@/views/LoginView.vue";
import UpdateEvent from "@/components/UpdateEvent.vue";
import ParticipationView from "@/views/ParticipationView.vue";
const routes = [
  {
    path: "/admin",
    name: "admin",
    component: AdminView,
    meta: { requiresAuth: true },
  },
    {
    path: "/participation",
    name: "participation",
    component: ParticipationView,
  },
  {
    path: "/companies",
    name: "companies",
    component: CompaniesView,
    meta: { requiresAuth: true },
  },
  {
    path: "/",
    name: "login",
    component: LoginView,
  },
  {
    path: "/events/update/:id", // The path with the :id parameter
    name: "UpdateEvent", // <<<< THIS NAME IS CRUCIAL >>>>
    component: UpdateEvent, // The component to load for this route
    props: true, // Allows :id to be passed as a prop
    // meta: { requiresAuth: true }, // Add if editing requires login
  },
  {
    path: "/events/delete/:id", // The path with the :id parameter
    name: "UpdateEvent", // <<<< THIS NAME IS CRUCIAL >>>>
    component: UpdateEvent, // The component to load for this route
    props: true, // Allows :id to be passed as a prop
    // meta: { requiresAuth: true }, // Add if editing requires login
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});
router.beforeEach((to, from, next) => {
  const authToken = localStorage.getItem("authToken");
  if (to.meta.requiresAuth && !authToken) {
    next("/");
  } else {
    next();
  }
});

export default router;
