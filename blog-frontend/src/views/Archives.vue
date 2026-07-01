<template>
  <div class="page-container">
    <h1 class="page-title neon-text">归档</h1>

    <div v-if="loading" class="archives-state">
      <p>加载中...</p>
    </div>

    <div v-else-if="error" class="archives-state archives-state--error">
      <p>{{ error }}</p>
      <button class="btn btn--ghost" style="margin-top: 12px" @click="fetchArchives">重试</button>
    </div>

    <div v-else-if="!grouped.length" class="archives-state">
      <p>还没有文章</p>
    </div>

    <template v-else>
      <section v-for="yearGroup in grouped" :key="yearGroup.year" class="archives-year">
        <h2 class="archives-year__title">{{ yearGroup.year }} 年</h2>

        <div v-for="monthGroup in yearGroup.months" :key="monthGroup.month" class="archives-month">
          <h3 class="archives-month__title">{{ monthGroup.month }} 月</h3>

          <FrostedCard v-for="item in monthGroup.items" :key="item.id" class="archives-item">
            <router-link :to="`/article/${item.slug}`" class="archives-item__link">
              <span class="archives-item__title">{{ item.title }}</span>
              <span class="archives-item__day">{{ formatDay(item.createTime) }}</span>
            </router-link>
          </FrostedCard>
        </div>
      </section>
    </template>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import FrostedCard from '@/components/FrostedCard.vue'
import { getArchives } from '@/api/article'

const loading = ref(true)
const error = ref(null)
const articles = ref([])

function formatDay(str) {
  if (!str) return ''
  const d = new Date(str)
  return `${String(d.getDate()).padStart(2, '0')} 日`
}

const grouped = computed(() => {
  const yearMap = new Map()

  for (const a of articles.value) {
    const d = new Date(a.createTime)
    const year = d.getFullYear()
    const month = d.getMonth() + 1

    if (!yearMap.has(year)) yearMap.set(year, new Map())
    const monthMap = yearMap.get(year)
    if (!monthMap.has(month)) monthMap.set(month, [])
    monthMap.get(month).push(a)
  }

  const result = []
  for (const [year, monthMap] of yearMap) {
    const months = []
    for (const [month, items] of monthMap) {
      months.push({ month, items })
    }
    result.push({ year, months })
  }
  return result
})

async function fetchArchives() {
  loading.value = true
  error.value = null
  try {
    articles.value = await getArchives()
  } catch (err) {
    console.error(err)
    error.value = err.message || '加载失败'
  } finally {
    loading.value = false
  }
}

onMounted(fetchArchives)
</script>

<style scoped>
.page-title {
  font-size: 32px;
  font-weight: 700;
  margin-bottom: 36px;
  text-align: center;
}

.archives-state {
  text-align: center;
  padding: 60px 0;
  color: var(--text-secondary);
  font-size: 15px;
}

.archives-state--error {
  color: #e57373;
}

/* ========== 年 ========== */
.archives-year {
  margin-bottom: 36px;
}

.archives-year__title {
  font-size: 26px;
  font-weight: 700;
  color: var(--accent);
  margin-bottom: 20px;
  text-shadow: 0 0 10px var(--accent-glow);
  padding-left: 4px;
}

/* ========== 月 ========== */
.archives-month {
  margin-bottom: 20px;
  padding-left: 28px;
  border-left: 2px solid var(--border);
}

.archives-month__title {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-secondary);
  margin-bottom: 12px;
}

/* ========== 文章条目 ========== */
.archives-item {
  padding: 14px 20px;
  margin-bottom: 10px;
}

.archives-item__link {
  display: flex;
  align-items: center;
  justify-content: space-between;
  text-decoration: none;
  color: var(--text-primary);
  gap: 16px;
  transition: color 0.2s;
}

.archives-item__link:hover {
  color: var(--accent);
}

.archives-item__title {
  font-size: 15px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.archives-item__day {
  font-size: 13px;
  color: var(--text-secondary);
  white-space: nowrap;
  flex-shrink: 0;
}

@media (max-width: 768px) {
  .page-title {
    font-size: 26px;
    margin-bottom: 28px;
  }

  .archives-year__title {
    font-size: 22px;
  }

  .archives-month {
    padding-left: 16px;
  }

  .archives-item {
    padding: 12px 16px;
  }
}
</style>
