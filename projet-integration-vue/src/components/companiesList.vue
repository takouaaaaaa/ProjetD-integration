<template>
  <div class="company-container">
    <div v-if="companies.length > 0" class="table-responsive">
      <table class="company-table">
        <thead>
          <tr>
            <th class="name-col">Name</th>
            <th class="responsable-col">Responsable</th>
            <th class="category-col">Category</th>
            <th class="email-col">Email</th>
            <th class="phone-col">Phone</th>
            <th class="status-col">Status</th>
            <th class="actions-col">Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="company in companies" :key="company.id">
            <td class="company-name">{{ company.name }}</td>
            <td class="company-responsable">{{ company.responsable }}</td>
            <td class="company-category">{{ company.category }}</td>
            <td class="company-email">{{ company.email }}</td>
            <td class="company-phone">{{ company.numTel }}</td>
            <td>
              <span :class="['company-status-badge', company.confirmed ? 'status-confirmed' : 'status-pending']">
                {{ company.confirmed ? "Confirmed" : "Pending" }}
              </span>
            </td>
            <td class="actions-cell">
              <button 
                v-if="!company.confirmed" 
                @click="confirmCompany(company.id)" 
                class="action-btn confirm-btn"
                :disabled="loading"
              >
                <span v-if="loading && actionId === company.id">Processing...</span>
                <span v-else>Confirm</span>
              </button>
              <button 
                v-else 
                @click="unconfirmCompany(company.id)" 
                class="action-btn unconfirm-btn"
                :disabled="loading"
              >
                <span v-if="loading && actionId === company.id">Processing...</span>
                <span v-else>Unconfirm</span>
              </button>
              <button 
                @click="deleteCompany(company.id)" 
                class="action-btn delete-btn"
                :disabled="loading"
              >
                <span v-if="loading && actionId === company.id">Processing...</span>
                <span v-else>Delete</span>
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div v-else class="no-companies">
      <svg class="empty-icon" viewBox="0 0 24 24" width="48" height="48">
        <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0118 0z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
        <path d="M12 13a3 3 0 100-6 3 3 0 000 6z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
      </svg>
      <h3>No companies to show</h3>
      <p>When companies are available, they will appear here.</p>
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
  width: 100%;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.table-responsive {
  overflow-x: auto;
  -webkit-overflow-scrolling: touch;
}

.company-table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0;
  font-size: 0.9rem;
}

.company-table th {
  position: sticky;
  top: 0;
  background-color: #f8fafc;
  color: #64748b;
  font-weight: 600;
  text-transform: uppercase;
  font-size: 0.75rem;
  letter-spacing: 0.5px;
  padding: 12px 16px;
  border-bottom: 1px solid #e2e8f0;
  z-index: 10;
}

.company-table td {
  padding: 16px;
  border-bottom: 1px solid #f1f5f9;
  vertical-align: middle;
}

.company-table tr:last-child td {
  border-bottom: none;
}

.company-table tr:hover td {
  background-color: #f8fafc;
}

.company-name {
  font-weight: 600;
  color: #1e293b;
}

.company-responsable, 
.company-category,
.company-email,
.company-phone {
  color: #475569;
}

/* Status badges */
.company-status-badge {
  display: inline-block;
  padding: 4px 10px;
  border-radius: 20px;
  font-size: 0.75rem;
  font-weight: 600;
  letter-spacing: 0.3px;
  text-align: center;
  min-width: 80px;
}

.status-confirmed {
  background-color: #dcfce7;
  color: #166534;
}

.status-pending {
  background-color: #ffedd5;
  color: #9a3412;
}

/* Action buttons */
.actions-cell {
  white-space: nowrap;
}

.action-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 0.8rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  border: 1px solid transparent;
  margin-right: 8px;
}

.action-btn:last-child {
  margin-right: 0;
}

.action-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.confirm-btn {
  background-color: #dcfce7;
  color: #166534;
  border-color: #bbf7d0;
}

.confirm-btn:hover:not(:disabled) {
  background-color: #bbf7d0;
}

.unconfirm-btn {
  background-color: #ffedd5;
  color: #9a3412;
  border-color: #fed7aa;
}

.unconfirm-btn:hover:not(:disabled) {
  background-color: #fed7aa;
}

.delete-btn {
  background-color: #fee2e2;
  color: #991b1b;
  border-color: #fecaca;
}

.delete-btn:hover:not(:disabled) {
  background-color: #fecaca;
}

/* Empty state */
.no-companies {
  padding: 48px 24px;
  text-align: center;
  color: #64748b;
}

.empty-icon {
  color: #cbd5e1;
  margin-bottom: 16px;
}

.no-companies h3 {
  font-size: 1.1rem;
  font-weight: 600;
  margin-bottom: 8px;
  color: #334155;
}

.no-companies p {
  font-size: 0.9rem;
  margin: 0;
}

/* Responsive adjustments */
@media (max-width: 1200px) {
  .company-table {
    font-size: 0.85rem;
  }
  
  .company-table th, 
  .company-table td {
    padding: 12px;
  }
}

@media (max-width: 768px) {
  .category-col, 
  .email-col {
    display: none;
  }
  
  .action-btn {
    padding: 6px;
  }
  
  .action-btn span {
    display: none;
  }
}

@media (max-width: 576px) {
  .responsable-col, 
  .phone-col {
    display: none;
  }
  
  .company-status-badge {
    min-width: auto;
    padding: 4px 8px;
  }
}
</style>