<template>
  <div class="page-container">
    <h1 class="page-title neon-text">文章列表</h1>

    <div class="articles-toolbar">
      <div class="articles-toolbar__row">
        <input
          v-model="keyword"
          type="text"
          class="articles-toolbar__search"
          placeholder="搜索文章..."
          @input="onSearchInput"
        />

        <select v-model="categoryId" class="articles-toolbar__select" @change="onFilterChange">
          <option :value="null">全部分类</option>
          <option v-for="cat in categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
        </select>
      </div>

      <div v-if="tags.length" class="articles-toolbar__tags">
        <button
          v-for="tag in tags"
          :key="tag.id"
          class="articles-toolbar__tag"
          :class="{ 'articles-toolbar__tag--active': tagId === tag.id }"
          @click="toggleTag(tag.id)"
        >{{ tag.name }}</button>
      </div>
    </div>

    <div v-if="loading" class="articles-state">
      <p>加载中...</p>
    </div>

    <div v-else-if="error" class="articles-state articles-state--error">
      <p>{{ error }}</p>
      <button class="btn btn--ghost" style="margin-top: 12px" @click="fetchArticles">重试</button>
    </div>

    <div v-else-if="!articles.length" class="articles-state">
      <p>没有找到相关文章</p>
    </div>

    <template v-else>
      <ArticleCard
        v-for="item in articles"
        :key="item.id"
        :article="item"
      />

      <div class="articles-pager">
        <button
          class="btn btn--ghost"
          :disabled="pageNum <= 1"
          @click="goPage(pageNum - 1)"
        >← 上一页</button>

        <span class="articles-pager__info">{{ pageNum }} / {{ totalPages || 1 }}</span>

        <button
          class="btn btn--ghost"
          :disabled="pageNum >= totalPages"
          @click="goPage(pageNum + 1)"
        >下一页 →</button>
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import ArticleCard from '@/components/ArticleCard.vue'
import { getArticles } from '@/api/article'
import { getPublicCategories } from '@/api/category'
import { getPublicTags } from '@/api/tag'

const keyword = ref('')
const categoryId = ref(null)
const tagId = ref(null)
const pageNum = ref(1)
const totalPages = ref(1)

const loading = ref(true)
const error = ref(null)
const articles = ref([])
const categories = ref([])
const tags = ref([])

let searchTimer = null

function onSearchInput() {
  clearTimeout(searchTimer)
  searchTimer = setTimeout(() => {
    pageNum.value = 1
    fetchArticles()
  }, 400)
}

function onFilterChange() {
  pageNum.value = 1
  fetchArticles()
}

function toggleTag(id) {
  tagId.value = tagId.value === id ? null : id
  pageNum.value = 1
  fetchArticles()
}

function goPage(page) {
  if (page < 1 || page > totalPages.value) return
  pageNum.value = page
  fetchArticles()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

async function fetchArticles() {
  loading.value = true
  error.value = null
  try {
    const data = await getArticles({
      pageNum: pageNum.value,
      limit: 10,
      keyword: keyword.value || undefined,
      categoryId: categoryId.value,
      tagId: tagId.value
    })
    articles.value = data.records
    totalPages.value = data.pages
  } catch (err) {
    console.error(err)
    error.value = err.message || '加载失败'
  } finally {
    loading.value = false
  }
}

async function loadFilters() {
  try {
    const [cats, tgs] = await Promise.all([
      getPublicCategories(),
      getPublicTags()
    ])
    categories.value = cats
    tags.value = tgs
  } catch (err) {
    console.error('加载筛选条件失败:', err)
  }
}

onMounted(() => {
  loadFilters()
  fetchArticles()
})
</script>

<style scoped>
.page-title {
  font-size: 32px;
  font-weight: 700;
  margin-bottom: 28px;
  text-align: center;
}

.articles-toolbar {
  margin-bottom: 28px;
}

.articles-toolbar__row {
  display: flex;
  gap: 12px;
  margin-bottom: 14px;
}

.articles-toolbar__search {
  flex: 1;
  padding: 10px 16px;
  border-radius: 8px;
  border: 1px solid var(--border);
  background: var(--bg-card);
  color: var(--text-primary);
  font-size: 14px;
  outline: none;
  transition: border-color 0.2s;
}

.articles-toolbar__search:focus {
  border-color: var(--accent);
  box-shadow: 0 0 8px var(--accent-glow);
}

.articles-toolbar__search::placeholder {
  color: var(--text-secondary);
}

.articles-toolbar__select {
  padding: 10px 14px;
  border-radius: 8px;
  border: 1px solid var(--border);
  background: var(--bg-card);
  color: var(--text-primary);
  font-size: 14px;
  outline: none;
  cursor: pointer;
  min-width: 120px;
}

.articles-toolbar__tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.articles-toolbar__tag {
  padding: 4px 14px;
  border-radius: 14px;
  border: 1px solid var(--border);
  background: transparent;
  color: var(--text-secondary);
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s;
}

.articles-toolbar__tag:hover {
  border-color: var(--accent);
  color: var(--accent);
}

.articles-toolbar__tag--active {
  border-color: var(--accent);
  background: var(--accent-glow);
  color: var(--accent);
}

.articles-state {
  text-align: center;
  padding: 60px 0;
  color: var(--text-secondary);
  font-size: 15px;
}

.articles-state--error {
  color: #e57373;
}

.articles-pager {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  margin-top: 36px;
}

.articles-pager__info {
  font-size: 14px;
  color: var(--text-secondary);
  min-width: 60px;
  text-align: center;
}

@media (max-width: 768px) {
  .page-title {
    font-size: 26px;
    margin-bottom: 20px;
  }

  .articles-toolbar__row {
    flex-direction: column;
    gap: 10px;
  }
}
</style>
