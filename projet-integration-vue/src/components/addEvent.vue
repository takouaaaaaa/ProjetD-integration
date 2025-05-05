<template>
  <div>
    <button class="btn" @click="openModal">Add an Event</button>

    <div v-if="showModal" class="modal">
      <div class="modal-content">
        <h2>Add an Event</h2>
        <form @submit.prevent="submitForm">
          <input
            v-model="formData.name"
            placeholder="Name"
            required
            type="text"
          />
          <input
            v-model="formData.email"
            placeholder="Email"
            required
            type="email"
          />
          <button type="submit">Submit</button>
          <button type="button" @click="closeModal">Cancel</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";

const showModal = ref(false);
const formData = ref({
  name: "",
  email: "",
});

function openModal() {
  showModal.value = true;
}

function closeModal() {
  showModal.value = false;
}

function submitForm() {
  console.log("Form Submitted:", formData.value);
  closeModal();
}
</script>

<style scoped>
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  padding: 1rem;
  border-radius: 8px;
  min-width: 300px;
}
</style>
