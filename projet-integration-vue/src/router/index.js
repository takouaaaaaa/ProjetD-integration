import { createRouter, createWebHashHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import CompanyView from "@/views/CompanyView.vue";
import EventView from "@/views/EventView.vue";
import AdminView from "@/views/AdminView.vue";
import CompaniesView from "@/views/CompaniesView.vue";
import LoginView from "@/views/LoginView.vue";
const routes = [
  {
    path: "/home",
    name: "home",
    component: HomeView,
  },
  {
    path: "/company",
    name: "company",
    component: CompanyView,
  },
  {
    path: "/events",
    name: "events",
    component: EventView,
  },
  {
    path: "/admin",
    name: "admin",
    component: AdminView,
    meta: { requiresAuth: true },
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
  
];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});
router.beforeEach((to, from, next) => {
  const authToken = localStorage.getItem('authToken');
  if (to.meta.requiresAuth && !authToken) {
    next('/');
  } else {
    next();
  }
});

export default router;
