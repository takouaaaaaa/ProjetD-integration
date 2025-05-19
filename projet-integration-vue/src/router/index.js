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
    //meta: { requiresAuth: true },
  },
  {
    path: "/participant",
    name: "participation",
    component: ParticipationView,
  },
 
  {
    path: "/companies",
    name: "companies",
    component: CompaniesView,
    //meta: { requiresAuth: true },
  },
  {
    path: "/",
    name: "login",
    component: LoginView,
  },
  {
    path: "/events/update/:id",
    name: "UpdateEvent",
    component: UpdateEvent,
    props: true,
  },
  {
    path: "/events/delete/:id",
    name: "UpdateEvent",
    component: UpdateEvent,
    props: true,
  },
];
const router = createRouter({
  history: createWebHashHistory(),   routes,
});

export default router;
