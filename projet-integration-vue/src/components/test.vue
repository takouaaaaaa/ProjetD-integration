<template>
  <div>
    <button class="open-modal-btn" @click="showModal = true">
      Créer un événement
    </button>

    <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal-content">
        <header class="modal-header">
          <h2>Nouvel Événement</h2>
          <button class="close-btn" @click="closeModal">×</button>
        </header>

        <form class="event-form" @submit.prevent="submitForm">
          <!-- Form fields for nom, description, date, time, lieu, localisation, image, animateur, company -->
          <!-- ... -->
          <div class="modal-actions">
            <button type="submit">Enregistrer</button>
            <button type="button" @click="closeModal">Annuler</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import axiosInstance from "@/services/axiosInstance";

const showModal = ref(false);
function closeModal() {
  showModal.value = false;
}

const form = reactive({
  nom: "",
  description: "",
  date: "",
  time: "",
  lieu: "",
  localisation: "",
  image: "",
  animateur: "",
  companyId: "",
});

const companies = ref([]);

onMounted(async () => {
  try {
    const res = await axiosInstance.get("/api/companies/getAll");
    companies.value = res.data;
  } catch (err) {
    console.error("Erreur chargement sociétés :", err);
    alert("Impossible de récupérer la liste des sociétés");
  }
});

async function submitForm() {
  try {
    const payload = {
      nom: form.nom,
      description: form.description,
      date: form.date,
      time: form.time + ":00",
      lieu: form.lieu,
      localisation: form.localisation,
      image: form.image,
      animateur: form.animateur,
      company: { id: form.companyId },
    };

    await axiosInstance.post("/api/events/addEvent", payload);

    alert("Événement créé avec succès !");
    Object.keys(form).forEach((k) => (form[k] = ""));
    closeModal();
  } catch (err) {
    console.error("Erreur création événement :", err);
    alert("Une erreur est survenue lors de la création");
  }
}
</script>

<style scoped>
/* Styles for modal and form */
</style>
