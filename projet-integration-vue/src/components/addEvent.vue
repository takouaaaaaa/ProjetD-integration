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
            <textarea id="description" v-model="form.description" required rows="3"></textarea>
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
            <input id="localisation" v-model="form.localisation" required type="text" />
          </div>

          <!-- image -->
          <div class="form-group">
            <label for="imageFile">Image de l'événement</label>
            <input id="imageFile" ref="fileInput" type="file" @change="onFileChange" />
          </div>

          <!-- animateur -->
          <div class="form-group">
            <label for="animateur">Animateur</label>
            <input id="animateur" v-model="form.animateur" type="text" />
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
import companyService from "@/services/companyService"; // ✅ use companyService

const emit = defineEmits(["event-added"]);
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
  animateur: "",
  companyId: "",
});
const imageFile = ref(null);
const companies = ref([]);

// Load companies from service
onMounted(async () => {
  try {
    companies.value = await companyService.fetchCompanies();
  } catch (err) {
    console.error("Erreur chargement sociétés :", err);
    alert("Impossible de récupérer la liste des sociétés");
  }
});

function onFileChange(e) {
  imageFile.value = e.target.files[0];
}

async function submitForm() {
  try {
    const data = new FormData();
    data.append("image", imageFile.value);
    data.append("nom", form.nom);
    data.append("description", form.description);
    data.append("date", form.date);
    data.append("time", form.time + ":00");
    data.append("lieu", form.lieu);
    data.append("localisation", form.localisation);
    data.append("animateur", form.animateur);
    data.append("companyId", form.companyId);

    // Use service to submit
    await companyService.registerEvent(data);

    alert("Événement créé avec succès !");
    Object.keys(form).forEach((k) => (form[k] = ""));
    imageFile.value = null;
    closeModal();
    emit("event-added");
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
