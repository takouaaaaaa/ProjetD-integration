<template>
  <div>
    <button class="open-modal-btn" @click="openModal">
      Créer un événement
    </button>
    <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal-content">
        <header class="modal-header">
          <h2>Nouvel Événement</h2>
          <button class="close-btn" @click="closeModal">×</button>
        </header>

        <form class="event-form" @submit.prevent="submitForm">
          <div class="form-group">
            <label for="nom">Nom</label>
            <input id="nom" v-model="form.nom" required type="text" />
          </div>
          <div class="form-group">
            <label for="description">Description</label>
            <textarea
              id="description"
              v-model="form.description"
              required
              rows="3"
            ></textarea>
          </div>
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
          <div class="form-group">
            <label for="lieu">Ville (lieu)</label>
            <input id="lieu" v-model="form.lieu" required type="text" />
          </div>
          <div class="form-group">
            <label for="localisation">Localisation détaillée</label>
            <input
              id="localisation"
              v-model="form.localisation"
              required
              type="text"
            />
          </div>
          <div class="form-group">
            <label for="imageFile">Image de l'événement</label>
            <input
              id="imageFile"
              ref="fileInput"
              type="file"
              @change="onFileChange"
            />
          </div>
          <div class="form-group">
            <label for="animateur">Animateur</label>
            <input id="animateur" v-model="form.animateur" type="text" />
          </div>
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
import { ref, reactive } from "vue";
import eventService from "@/services/eventService"; 

const emit = defineEmits(["event-added"]);

const showModal = ref(false);
const imageFile = ref(null); 

const form = reactive({
  nom: "",
  description: "",
  date: "",
  time: "",
  lieu: "", 
  localisation: "",
  animateur: "",
  companyId: null, 
});



function openModal() {
  Object.keys(form).forEach((key) => {
    if (key !== 'companyId') { 
      form[key] = "";
    }
  });
  imageFile.value = null; 
  const fileInput = document.getElementById('imageFile');
  if (fileInput) {
    fileInput.value = null; 
  }

  if (!form.companyId) {
    form.companyId = 1; 
    console.warn(`AddEventModal: Using test companyId: ${form.companyId}. This should be replaced with actual authenticated company ID logic in openModal or onMount.`);
  }

  showModal.value = true;
}

function closeModal() {
  showModal.value = false;
}

function onFileChange(event) {
  const file = event.target.files[0];
  if (file) {
    imageFile.value = file;
  } else {
    imageFile.value = null;
  }
}

async function submitForm() {
  if (!form.companyId) {
    alert("Company ID is missing. Cannot create event. Please ensure you are logged in as a company.");
    console.error("AddEventModal: submitForm - companyId is missing.");
    return;
  }

  const formData = new FormData();

  formData.append("nom", form.nom);
  formData.append("description", form.description);
  formData.append("date", form.date);
  formData.append("time", form.time ? form.time + ":00" : ""); 
  formData.append("lieu", form.lieu);
  formData.append("localisation", form.localisation);
  formData.append("animateur", form.animateur);
  formData.append("company_id", form.companyId); 

  if (imageFile.value) {
    formData.append("image", imageFile.value);
  }

  try {
 
    await eventService.addEvent(formData); 
    alert("Événement créé avec succès !");
    closeModal();
    emit("event-added"); 
  } catch (err) {
    console.error("Erreur création événement:", err);
    let errorMessage = "Une erreur est survenue lors de la création de l'événement.";

    if (err.response && err.response.data) {
      if (err.response.data.message && err.response.status !== 422) { 
        errorMessage = err.response.data.message;
      } else if (err.response.data.errors) { 
        const errors = err.response.data.errors;
        
        let validationMessages = [];
        for (const key in errors) {
          if (errors[key] && errors[key].length > 0) {
            validationMessages.push(`${errors[key][0]}`); 
          }
        }
        if (validationMessages.length > 0) {
          errorMessage = "Validation Failed:\n- " + validationMessages.join("\n- ");
        } else if (err.response.data.message) { 
             errorMessage = err.response.data.message;
        }
      }
    } else if (err.request) {
      errorMessage = "Aucune réponse du serveur. Vérifiez votre connexion internet.";
    } else {
      errorMessage = err.message || errorMessage;
    }
    alert(errorMessage);
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
