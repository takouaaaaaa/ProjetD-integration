<template>
  <div>
    <!-- 1️⃣ Open Modal Button -->
    <button class="open-modal-btn" @click="showModal = true">
      Créer un événement
    </button>

    <!-- 2️⃣ Modal -->
    <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal-content">
        <header class="modal-header">
          <h2>Nouvel Événement</h2>
          <button class="close-btn" @click="closeModal">×</button>
        </header>

        <form class="event-form" @submit.prevent="submitForm">
          <!-- nom -->
          <div class="form-group">
            <label for="nom">Nom</label>
            <input id="nom" v-model="form.nom" required type="text" />
          </div>

          <!-- description -->
          <div class="form-group">
            <label for="description">Description</label>
            <textarea
              id="description"
              v-model="form.description"
              required
              rows="3"
            ></textarea>
          </div>

          <!-- date + time -->
          <div class="form-row">
            <div class="form-group">
              <label for="date">Date</label>
              <input id="date" v-model="form.date" required type="date" />
            </div>
            <div class="form-group">
              <label for="time">Heure</label>
              <input id="time" v-model="form.time" required type="time" />
            </div>
          </div>

          <!-- lieu -->
          <div class="form-group">
            <label for="lieu">Ville (lieu)</label>
            <input id="lieu" v-model="form.lieu" required type="text" />
          </div>

          <!-- localisation -->
          <div class="form-group">
            <label for="localisation">Localisation détaillée</label>
            <input
              id="localisation"
              v-model="form.localisation"
              required
              type="text"
            />
          </div>

          <!-- image -->
          <div class="form-group">
            <label for="image">URL de l'image</label>
            <input id="image" v-model="form.image" required type="text" />
          </div>

          <!-- animateur -->
          <div class="form-group">
            <label for="animateur">Animateur</label>
            <input id="animateur" v-model="form.animateur" type="text" />
          </div>

          <!-- company -->
          <div class="form-group">
            <label for="company">Société</label>
            <select id="company" v-model="form.companyId" required>
              <option disabled value="">Sélectionnez une société</option>
              <option v-for="c in companies" :key="c.id" :value="c.id">
                {{ c.name }}
              </option>
            </select>
          </div>

          <!-- actions -->
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
import axiosInstance from "@/services/axiosInstance"; // your configured axios

// modal visibility
const showModal = ref(false);
function closeModal() {
  showModal.value = false;
}

// form state
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

// companies list
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
      time: form.time + ":00", // ensure "HH:mm:ss"
      lieu: form.lieu,
      localisation: form.localisation,
      image: form.image,
      animateur: form.animateur,
      company: { id: form.companyId },
    };

    await axiosInstance.post("/api/events/addEvent", payload);

    alert("Événement créé avec succès !");
    // reset
    Object.keys(form).forEach((k) => (form[k] = ""));
    closeModal();
  } catch (err) {
    console.error("Erreur création événement :", err);
    alert("Une erreur est survenue lors de la création");
  }
}
</script>

<style scoped>
.open-modal-btn {
  padding: 0.75rem 1.5rem;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 1.5rem;
  border-radius: 8px;
  width: 90%;
  max-width: 600px;
  position: relative;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.close-btn {
  background: transparent;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
}

.event-form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.form-row {
  display: flex;
  gap: 1rem;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 1rem;
}

button[type="submit"] {
  padding: 0.75rem 1.5rem;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button[type="button"] {
  padding: 0.75rem 1.5rem;
  background-color: #ccc;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  opacity: 0.9;
}
</style>
