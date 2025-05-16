<template>
  <div class="p-4">
    <h2 class="text-xl font-bold mb-4">Liste des participants</h2>
    <table class="table-auto w-full border border-gray-300">
      <thead class="bg-gray-200">
        <tr>
          <th class="px-4 py-2 border">Nom</th>
          <th class="px-4 py-2 border">Prénom</th>
          <th class="px-4 py-2 border">Email</th>
          <th class="px-4 py-2 border">Téléphone</th>
        </tr>
      </thead>
      <tbody>
        <tr
          v-for="participant in participants"
          :key="participant.id"
          class="text-center"
        >
          <td class="px-4 py-2 border">{{ participant.lastName }}</td>
          <td class="px-4 py-2 border">{{ participant.firstName }}</td>
          <td class="px-4 py-2 border">{{ participant.email }}</td>
          <td class="px-4 py-2 border">{{ participant.numTel }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "ParticipantsView",
  data() {
    return {
      participants: [],
    };
  },
  mounted() {
    axios
      .get("http://localhost:8000/api/participants")
      .then((response) => {
        this.participants = response.data;
      })
      .catch((error) => {
        console.error(
          "Erreur lors de la récupération des participants :",
          error
        );
      });
  },
};
</script>

<style scoped>
table {
  border-collapse: collapse;
}
</style>
