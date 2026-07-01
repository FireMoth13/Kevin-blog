<template>
  <div class="page-container">
    <div class="hero">
      <h1 class="hero__title neon-text">Kevin's Blog</h1>
      <p class="hero__subtitle">一个热爱技术的博客</p>
      <NeonClock />
    </div>

    <section v-if="featured.length" class="home-section">
      <h2 class="section-title"><span class="section-title__icon">📌</span> 置顶</h2>
      <ArticleCard
        v-for="item in featured"
        :key="item.id"
        :article="item"
      />
    </section>

    <section class="home-section">
      <h2 class="section-title">最新文章</h2>

      <div v-if="loading" class="home-state">
        <p>加载中...</p>
      </div>

      <div v-else-if="error" class="home-state home-state--error">
        <p>{{ error }}</p>
        <button class="btn btn--ghost" style="margin-top: 12px" @click="fetchArticles">重试</button>
      </div>

      <div v-else-if="!articles.length" class="home-state">
        <p>还没有文章，快去管理后台发布吧~</p>
      </div>

      <template v-else>
        <ArticleCard
          v-for="item in articles"
          :key="item.id"
          :article="item"
        />

        <div class="home-more">
          <router-link to="/articles" class="btn btn--ghost">查看更多文章 →</router-link>
        </div>
      </template>
    </section>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import NeonClock from '@/components/NeonClock.vue'
import ArticleCard from '@/components/ArticleCard.vue'
import { getArticles } from '@/api/article'

const loading = ref(true)
const error = ref(null)
const articles = ref([])

const featured = computed(() => articles.value.filter(a => a.isTop === 1))

async function fetchArticles() {
  loading.value = true
  error.value = null
  try {
    const data = await getArticles({ pageNum: 1, limit: 10 })
    articles.value = data.records
  } catch (err) {
    console.error(err)
    error.value = err.message || '加载失败，请稍后重试'
  } finally {
    loading.value = false
  }
}

onMounted(fetchArticles)
</script>

<style scoped>
.hero {
  text-align: center;
  padding: 30px 0 10px;
}

.hero__title {
  font-size: 52px;
  font-weight: 800;
  margin-bottom: 6px;
  letter-spacing: 6px;
}

.hero__subtitle {
  font-size: 16px;
  color: var(--text-secondary);
  margin-bottom: 16px;
}

.home-section {
  margin-top: 44px;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 20px;
  padding-left: 14px;
  border-left: 3px solid var(--accent);
}

.section-title__icon {
  margin-right: 4px;
}

.home-state {
  text-align: center;
  padding: 40px 0;
  color: var(--text-secondary);
  font-size: 15px;
}

.home-state--error {
  color: #e57373;
}

.home-more {
  text-align: center;
  margin-top: 32px;
}

@media (max-width: 768px) {
  .hero__title {
    font-size: 36px;
    letter-spacing: 3px;
  }

  .hero {
    padding: 20px 0 10px;
  }

  .home-section {
    margin-top: 32px;
  }
}
</style>
