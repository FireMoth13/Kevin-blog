<template>
  <div class="manager">
    <div class="manager__header">
      <h2 class="manager__title">标签管理</h2>
      <button class="btn" @click="openAdd">+ 新建标签</button>
    </div>

    <div class="manager__table-wrapper frosted-card">
      <table class="manager__table" v-if="items.length">
        <thead>
          <tr>
            <th>名称</th>
            <th>Slug</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in items" :key="item.id">
            <td>
              <span class="manager__tag-badge">{{ item.name }}</span>
            </td>
            <td>{{ item.slug }}</td>
            <td class="manager__actions">
              <button class="btn btn--ghost btn--sm" @click="openEdit(item)">编辑</button>
              <button class="btn btn--ghost btn--sm btn--danger" @click="confirmDelete(item)">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-else class="manager__empty">暂无标签</div>
    </div>

    <!-- 弹窗 -->
    <div v-if="showForm" class="manager__overlay" @click.self="closeForm">
      <div class="manager__modal frosted-card">
        <h3 class="manager__modal-title">{{ editingId ? '编辑标签' : '新建标签' }}</h3>
        <form @submit.prevent="handleSubmit">
          <div class="editor__group">
            <label class="editor__label">名称</label>
            <input v-model="form.name" type="text" class="editor__input" required />
          </div>
          <div class="editor__group">
            <label class="editor__label">Slug</label>
            <input v-model="form.slug" type="text" class="editor__input" required />
          </div>
          <div class="manager__modal-actions">
            <button type="button" class="btn btn--ghost" @click="closeForm">取消</button>
            <button type="submit" class="btn">保存</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getTags, addTag, updateTag, deleteTag } from '@/api/tag'

const items = ref([])
const showForm = ref(false)
const editingId = ref(null)
const form = ref({ name: '', slug: '' })

async function loadData() {
  try {
    items.value = await getTags()
  } catch (err) {
    console.error('加载标签失败:', err)
  }
}

function openAdd() {
  editingId.value = null
  form.value = { name: '', slug: '' }
  showForm.value = true
}

function openEdit(item) {
  editingId.value = item.id
  form.value = { name: item.name, slug: item.slug }
  showForm.value = true
}

function closeForm() {
  showForm.value = false
}

async function handleSubmit() {
  try {
    if (editingId.value) {
      await updateTag({ id: editingId.value, ...form.value })
    } else {
      await addTag(form.value)
    }
    closeForm()
    loadData()
  } catch (err) {
    alert('保存失败: ' + (err.message || '未知错误'))
  }
}

async function confirmDelete(item) {
  if (!confirm(`确定删除标签「${item.name}」吗？`)) return
  try {
    await deleteTag(item.id)
    loadData()
  } catch (err) {
    alert('删除失败: ' + (err.message || '未知错误'))
  }
}

onMounted(loadData)
</script>

<style scoped>
.manager__header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.manager__title {
  font-size: 22px;
  font-weight: 700;
  color: var(--text-primary);
}

.manager__table-wrapper {
  padding: 0;
  overflow: hidden;
}

.manager__table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}

.manager__table th {
  padding: 12px 16px;
  text-align: left;
  font-weight: 600;
  color: var(--text-secondary);
  border-bottom: 1px solid var(--border);
}

.manager__table td {
  padding: 12px 16px;
  border-bottom: 1px solid var(--border);
  color: var(--text-primary);
}

.manager__table tr:last-child td {
  border-bottom: none;
}

.manager__actions {
  white-space: nowrap;
  display: flex;
  gap: 8px;
}

.manager__tag-badge {
  padding: 2px 10px;
  border-radius: 10px;
  font-size: 12px;
  background: var(--accent-glow);
  color: var(--accent);
}

.manager__empty {
  padding: 40px;
  text-align: center;
  color: var(--text-secondary);
}

/* 弹窗 */
.manager__overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 100;
}

.manager__modal {
  width: 100%;
  max-width: 420px;
  padding: 28px 24px;
}

.manager__modal-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 20px;
  color: var(--text-primary);
}

.manager__modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 24px;
}

.btn--sm {
  padding: 4px 12px;
  font-size: 12px;
}

.btn--danger:hover {
  color: #e57373 !important;
  border-color: #e57373 !important;
}
</style>
