<template>
    <div class="company-container">
      <table v-if="companies.length > 0" class="company-table">
        <thead>
          <tr>
            <th>Name</th>
            <th>Responsable</th>
            <th>Category</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="company in companies" :key="company.id">
            <td>{{ company.name }}</td>
            <td>{{ company.responsable }}</td>
            <td>{{ company.category }}</td>
            <td>{{ company.email }}</td>
            <td>{{ company.numTel }}</td>
            <td :class="company.confirmed ? 'confirmed' : 'pending'">
              {{ company.confirmed ? "Confirmed" : "Pending" }}
            </td>
            <td class="actions">
              <button 
                v-if="!company.confirmed" 
                @click="confirmCompany(company.id)" 
                class="confirm-btn"
                :disabled="loading"
              >
                <span v-if="loading && actionId === company.id">Processing...</span>
                <span v-else>Confirm</span>
              </button>
              <button 
                v-else 
                @click="unconfirmCompany(company.id)" 
                class="unconfirm-btn"
                :disabled="loading"
              >
                <span v-if="loading && actionId === company.id">Processing...</span>
                <span v-else>Unconfirm</span>
              </button>
              <button 
                @click="deleteCompany(company.id)" 
                class="delete-btn"
                :disabled="loading"
              >
                <span v-if="loading && actionId === company.id">Processing...</span>
                <span v-else>Delete</span>
              </button>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-else class="no-companies">
        <p>No companies to show.</p>
      </div>
    </div>
  </template>
  
  <script>
  import companyService from '@/services/companyService';
  
  export default {
    name: "companiesList",
    props: {
      companies: {
        type: Array,
        required: true,
      },
    },
    data() {
      return {
        loading: false,
        actionId: null
      }
    },
    methods: {
      async refreshCompanies() {
        // Emit event to parent to refresh the companies list
        this.$emit('refresh-companies');
      },
      async confirmCompany(id) {
        this.loading = true;
        this.actionId = id;
        try {
          await companyService.confirmCompany(id);
          await this.refreshCompanies();
        } catch (error) {
          console.error('Error confirming company:', error);
          alert('Failed to confirm company');
        } finally {
          this.loading = false;
          this.actionId = null;
        }
      },
      async unconfirmCompany(id) {
        this.loading = true;
        this.actionId = id;
        try {
          await companyService.unconfirmCompany(id);
          await this.refreshCompanies();
        } catch (error) {
          console.error('Error unconfirming company:', error);
          alert('Failed to unconfirm company');
        } finally {
          this.loading = false;
          this.actionId = null;
        }
      },
      async deleteCompany(id) {
        if (confirm('Are you sure you want to delete this company?')) {
          this.loading = true;
          this.actionId = id;
          try {
            await companyService.deleteCompany(id);
            await this.refreshCompanies();
          } catch (error) {
            console.error('Error deleting company:', error);
            alert('Failed to delete company');
          } finally {
            this.loading = false;
            this.actionId = null;
          }
        }
      }
    }
  };
  </script>
  <style scoped>
  .company-container {
    padding: 20px;
    width: 100%;
    overflow-x: auto;
  }
  
  .company-table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 20px;
  }
  
  .company-table th, .company-table td {
    padding: 12px 15px;
    text-align: left;
    border-bottom: 1px solid #ddd;
  }
  
  .company-table th {
    background-color: #f2f2f2;
    font-weight: bold;
  }
  
  .company-table tr:hover {
    background-color: #f5f5f5;
  }
  
  .confirmed {
    color: #4CAF50;
    font-weight: bold;
  }
  
  .pending {
    color: #FF9800;
    font-weight: bold;
  }
  
  .actions {
    display: flex;
    gap: 8px;
  }
  
  button {
    padding: 6px 12px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 14px;
    transition: background-color 0.3s;
  }
  
  .confirm-btn {
    background-color: #4CAF50;
    color: white;
  }
  
  .confirm-btn:hover {
    background-color: #45a049;
  }
  
  .unconfirm-btn {
    background-color: #FF9800;
    color: white;
  }
  
  .unconfirm-btn:hover {
    background-color: #e68a00;
  }
  
  .delete-btn {
    background-color: #f44336;
    color: white;
  }
  
  .delete-btn:hover {
    background-color: #d32f2f;
  }
  
  .no-companies {
    text-align: center;
    padding: 20px;
    color: #666;
  }
  </style>