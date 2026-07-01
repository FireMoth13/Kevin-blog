<template>
  <div class="dashboard">
    <h2 class="dashboard__title">概览</h2>

    <div class="dashboard__cards">
      <div class="dashboard__card frosted-card">
        <div class="dashboard__card-value">{{ articles.length }}</div>
        <div class="dashboard__card-label">文章总数</div>
      </div>
      <div class="dashboard__card frosted-card">
        <div class="dashboard__card-value">{{ categories.length }}</div>
        <div class="dashboard__card-label">分类数量</div>
      </div>
      <div class="dashboard__card frosted-card">
        <div class="dashboard__card-value">{{ tags.length }}</div>
        <div class="dashboard__card-label">标签数量</div>
      </div>
    </div>

    <div class="dashboard__section" v-if="articles.length">
      <div class="dashboard__section-header">
        <h3>文章列表</h3>
        <router-link to="/admin/article/new" class="btn">+ 写文章</router-link>
      </div>

      <div class="dashboard__table-wrapper frosted-card">
        <table class="dashboard__table" v-if="articles.length">
          <thead>
            <tr>
              <th>标题</th>
              <th>状态</th>
              <th>浏览量</th>
              <th>更新时间</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="a in articles" :key="a.id">
              <td class="dashboard__table-title">{{ a.title }}</td>
              <td>
                <span class="dashboard__status" :class="{ 'dashboard__status--published': a.status === 1 }">
                  {{ a.status === 1 ? '已发布' : '草稿' }}
                </span>
              </td>
              <td>{{ a.viewCount || 0 }}</td>
              <td class="dashboard__table-date">{{ formatTime(a.updateTime) }}</td>
              <td class="dashboard__table-actions">
                <router-link :to="`/admin/article/${a.id}`" class="btn btn--ghost btn--sm">编辑</router-link>
                <button class="btn btn--ghost btn--sm btn--danger" @click="confirmDelete(a)">删除</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <div v-else class="dashboard__empty">
      <p>还没有文章，开始写第一篇吧</p>
      <router-link to="/admin/article/new" class="btn" style="margin-top: 12px">+ 写文章</router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAdminArticles, deleteArticle } from '@/api/article'
import { getPublicCategories } from '@/api/category'
import { getPublicTags } from '@/api/tag'

const articles = ref([])
const categories = ref([])
const tags = ref([])

function formatTime(str) {
  if (!str) return ''
  const d = new Date(str)
  const y = d.getFullYear()
  const m = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hh = String(d.getHours()).padStart(2, '0')
  const mm = String(d.getMinutes()).padStart(2, '0')
  return `${y}/${m}/${day} ${hh}:${mm}`
}

async function confirmDelete(article) {
  if (!confirm(`确定删除「${article.title}」吗？此操作不可撤销。`)) return
  try {
    await deleteArticle(article.id)
    articles.value = articles.value.filter(a => a.id !== article.id)
  } catch (err) {
    alert('删除失败: ' + (err.message || '未知错误'))
  }
}

onMounted(async () => {
  try {
    const [art, cat, t] = await Promise.all([
      getAdminArticles(),
      getPublicCategories(),
      getPublicTags()
    ])
    articles.value = art
    categories.value = cat
    tags.value = t
  } catch (err) {
    console.error('加载概览数据失败:', err)
  }
})
</script>

<style scoped>
.dashboard__title {
  font-size: 22px;
  font-weight: 700;
  margin-bottom: 24px;
  color: var(--text-primary);
}

.dashboard__cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  margin-bottom: 36px;
}

.dashboard__card {
  text-align: center;
  padding: 24px;
}

.dashboard__card-value {
  font-size: 36px;
  font-weight: 700;
  color: var(--accent);
  text-shadow: 0 0 10px var(--accent-glow);
}

.dashboard__card-label {
  font-size: 14px;
  color: var(--text-secondary);
  margin-top: 4px;
}

.dashboard__section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}

.dashboard__section-header h3 {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
}

.dashboard__table-wrapper {
  padding: 0;
  overflow: hidden;
}

.dashboard__table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}

.dashboard__table th {
  padding: 12px 16px;
  text-align: left;
  font-weight: 600;
  color: var(--text-secondary);
  border-bottom: 1px solid var(--border);
  background: rgba(128, 128, 128, 0.05);
}

.dashboard__table td {
  padding: 12px 16px;
  border-bottom: 1px solid var(--border);
  color: var(--text-primary);
}

.dashboard__table tr:last-child td {
  border-bottom: none;
}

.dashboard__table-title {
  max-width: 260px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.dashboard__table-date {
  white-space: nowrap;
  color: var(--text-secondary);
  font-size: 13px;
}

.dashboard__table-actions {
  white-space: nowrap;
  display: flex;
  gap: 8px;
}

.dashboard__status {
  font-size: 12px;
  padding: 2px 10px;
  border-radius: 10px;
  background: rgba(128, 128, 128, 0.15);
  color: var(--text-secondary);
}

.dashboard__status--published {
  background: rgba(100, 181, 246, 0.15);
  color: var(--accent);
}

.btn--sm {
  padding: 4px 12px;
  font-size: 12px;
}

.btn--danger:hover {
  color: #e57373 !important;
  border-color: #e57373 !important;
}

.dashboard__empty {
  text-align: center;
  padding: 60px 0;
  color: var(--text-secondary);
}

@media (max-width: 768px) {
  .dashboard__cards {
    grid-template-columns: 1fr;
  }

  .dashboard__table th:nth-child(3),
  .dashboard__table td:nth-child(3),
  .dashboard__table th:nth-child(4),
  .dashboard__table td:nth-child(4) {
    display: none;
  }
}
</style>
