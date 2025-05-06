<template>
  <div class="events-container">
    <table v-if="events.length" class="events-table">
      <thead>
        <tr>
          <th>Nom</th>
          <th>Description</th>
          <th>Date</th>
          <th>Heure</th>
          <th>Localisation</th>
          <th>Animateur</th>
          <th>État</th>
          <th>Société</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="evt in events" :key="evt.id">
          <td>{{ evt.nom }}</td>
          <td>{{ evt.description }}</td>
          <td>{{ evt.date }}</td>
          <td>{{ evt.time.slice(0, 5) }}</td>
          <td>{{ evt.localisation }}</td>
          <td>{{ evt.animateur || "—" }}</td>
          <td>{{ evt.etat }}</td>
          <td>{{ evt.company?.name || "—" }}</td>
        </tr>
      </tbody>
    </table>
    <div v-else class="no-events">
      <p>Aucun événement à afficher.</p>
    </div>
  </div>
</template>

<script>
export default {
  name: "EventsList",
  props: {
    events: {
      type: Array,
      required: true,
    },
  },
};
</script>

<style scoped>
.events-container {
  padding: 1rem;
  width: 100%;
  overflow-x: auto;
}
.events-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}
.events-table th,
.events-table td {
  padding: 0.5rem;
  border: 1px solid #ddd;
  text-align: left;
}
.events-table th {
  background-color: #f5f5f5;
  font-weight: bold;
}
.events-container tr:hover {
  background-color: #f9f9f9;
}
.no-events {
  text-align: center;
  padding: 2rem;
  color: #666;
}
</style>
