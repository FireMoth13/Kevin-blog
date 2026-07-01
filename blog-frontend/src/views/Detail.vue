<template>
  <div class="page-container">
    <div v-if="loading" class="detail-state">
      <p>加载中...</p>
    </div>

    <div v-else-if="error" class="detail-state detail-state--error">
      <p>{{ error }}</p>
      <router-link to="/" class="btn btn--ghost" style="margin-top: 16px; display: inline-block">返回首页</router-link>
    </div>

    <template v-else-if="article">
      <!-- 文章头部 -->
      <article class="detail-card frosted-card">
        <header class="detail-header">
          <h1 class="detail-header__title">{{ article.title }}</h1>

          <div class="detail-header__meta">
            <span class="detail-header__date">{{ formatDate(article.createTime) }}</span>

            <span v-if="article.categoryName" class="detail-header__sep">·</span>
            <span v-if="article.categoryName" class="detail-header__category">{{ article.categoryName }}</span>

            <span class="detail-header__sep">·</span>
            <span class="detail-header__views">👁 {{ article.viewCount }}</span>
          </div>

          <div v-if="article.tags && article.tags.length" class="detail-header__tags">
            <span v-for="tag in article.tags" :key="tag.id" class="detail-header__tag">{{ tag.name }}</span>
          </div>
        </header>

        <!-- 文章正文 -->
        <div class="detail-body" v-html="article.contentHtml" />
      </article>

      <!-- 上下篇导航 -->
      <nav class="detail-nav">
        <div class="detail-nav__item">
          <span class="detail-nav__label">← 上一篇</span>
          <router-link
            v-if="article.prev"
            :to="`/article/${article.prev.slug}`"
            class="detail-nav__link"
          >{{ article.prev.title }}</router-link>
          <span v-else class="detail-nav__none">没有了</span>
        </div>

        <div class="detail-nav__item detail-nav__item--next">
          <span class="detail-nav__label">下一篇 →</span>
          <router-link
            v-if="article.next"
            :to="`/article/${article.next.slug}`"
            class="detail-nav__link"
          >{{ article.next.title }}</router-link>
          <span v-else class="detail-nav__none">没有了</span>
        </div>
      </nav>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getArticleDetail, incrViewCount } from '@/api/article'

const route = useRoute()

const loading = ref(true)
const error = ref(null)
const article = ref(null)

function formatDate(str) {
  if (!str) return ''
  const d = new Date(str)
  const y = d.getFullYear()
  const m = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${y}/${m}/${day}`
}

async function fetchDetail() {
  loading.value = true
  error.value = null
  try {
    article.value = await getArticleDetail(route.params.slug)
    if (article.value && article.value.id) {
      incrViewCount(article.value.id).catch(() => {})
    }
  } catch (err) {
    console.error(err)
    error.value = err.message || '加载失败'
  } finally {
    loading.value = false
  }
}

onMounted(fetchDetail)
</script>

<style scoped>
.detail-state {
  text-align: center;
  padding: 80px 0;
  color: var(--text-secondary);
  font-size: 15px;
}

.detail-state--error {
  color: #e57373;
}

/* ========== 文章卡片 ========== */
.detail-card {
  padding: 36px 40px;
}

.detail-header {
  margin-bottom: 32px;
  padding-bottom: 24px;
  border-bottom: 1px solid var(--border);
}

.detail-header__title {
  font-size: 30px;
  font-weight: 700;
  line-height: 1.4;
  margin-bottom: 12px;
  color: var(--text-primary);
}

.detail-header__meta {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: var(--text-secondary);
  margin-bottom: 14px;
  flex-wrap: wrap;
}

.detail-header__sep {
  opacity: 0.4;
}

.detail-header__category {
  color: var(--accent);
}

.detail-header__tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.detail-header__tag {
  padding: 3px 12px;
  border-radius: 12px;
  font-size: 12px;
  background: var(--accent-glow);
  color: var(--accent);
}

/* ========== 文章正文样式 ========== */
.detail-body {
  color: var(--text-primary);
  line-height: 1.85;
  font-size: 16px;
  word-break: break-word;
}

.detail-body :deep(h2) {
  font-size: 24px;
  font-weight: 700;
  margin: 36px 0 16px;
  padding-bottom: 8px;
  border-bottom: 1px solid var(--border);
}

.detail-body :deep(h3) {
  font-size: 20px;
  font-weight: 600;
  margin: 28px 0 12px;
}

.detail-body :deep(h4) {
  font-size: 17px;
  font-weight: 600;
  margin: 24px 0 10px;
}

.detail-body :deep(p) {
  margin-bottom: 16px;
}

.detail-body :deep(a) {
  color: var(--accent);
  text-decoration: underline;
  text-underline-offset: 2px;
}

.detail-body :deep(a:hover) {
  opacity: 0.8;
}

.detail-body :deep(ul),
.detail-body :deep(ol) {
  padding-left: 24px;
  margin-bottom: 16px;
}

.detail-body :deep(li) {
  margin-bottom: 6px;
}

.detail-body :deep(blockquote) {
  margin: 16px 0;
  padding: 12px 20px;
  border-left: 4px solid var(--accent);
  background: var(--bg-card);
  border-radius: 0 8px 8px 0;
  color: var(--text-secondary);
}

.detail-body :deep(code) {
  padding: 2px 6px;
  border-radius: 4px;
  font-family: 'Courier New', 'Consolas', monospace;
  font-size: 0.9em;
  background: rgba(128, 128, 128, 0.15);
  color: #e0a060;
}

.detail-body :deep(pre) {
  margin: 16px 0;
  padding: 20px;
  border-radius: 10px;
  overflow-x: auto;
  background: rgba(0, 0, 0, 0.3);
  border: 1px solid var(--border);
}

.detail-body :deep(pre code) {
  padding: 0;
  background: none;
  color: var(--text-primary);
  font-size: 14px;
  line-height: 1.7;
}

.detail-body :deep(img) {
  max-width: 100%;
  border-radius: 8px;
  margin: 12px 0;
}

.detail-body :deep(hr) {
  border: none;
  border-top: 1px solid var(--border);
  margin: 28px 0;
}

.detail-body :deep(table) {
  width: 100%;
  border-collapse: collapse;
  margin: 16px 0;
}

.detail-body :deep(th),
.detail-body :deep(td) {
  padding: 10px 14px;
  border: 1px solid var(--border);
  text-align: left;
}

.detail-body :deep(th) {
  background: rgba(128, 128, 128, 0.1);
  font-weight: 600;
}

/* ========== 上下篇导航 ========== */
.detail-nav {
  display: flex;
  justify-content: space-between;
  margin-top: 32px;
  gap: 16px;
}

.detail-nav__item {
  flex: 1;
  min-width: 0;
}

.detail-nav__item--next {
  text-align: right;
}

.detail-nav__label {
  display: block;
  font-size: 13px;
  color: var(--text-secondary);
  margin-bottom: 6px;
}

.detail-nav__link {
  font-size: 15px;
  color: var(--accent);
  text-decoration: none;
  transition: opacity 0.2s;
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.detail-nav__link:hover {
  opacity: 0.7;
}

.detail-nav__none {
  font-size: 14px;
  color: var(--text-secondary);
  opacity: 0.5;
}

@media (max-width: 768px) {
  .detail-card {
    padding: 24px 20px;
  }

  .detail-header__title {
    font-size: 24px;
  }

  .detail-body {
    font-size: 15px;
  }

  .detail-body :deep(h2) {
    font-size: 21px;
  }

  .detail-body :deep(pre) {
    padding: 14px;
  }

  .detail-nav {
    flex-direction: column;
    gap: 20px;
  }

  .detail-nav__item--next {
    text-align: left;
  }
}
</style>
