import { createRouter, createWebHashHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import CompanyView from "@/views/CompanyView.vue";
import EventView from "@/views/EventView.vue";

const routes = [
  {
    path: "/",
    name: "home",
    component: HomeView,
  },
  {
    path: "/companies",
    name: "companies",
    component: CompanyView,
  },
  {
    path: "/events",
    name: "events",
    component: EventView,
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

export default router;
