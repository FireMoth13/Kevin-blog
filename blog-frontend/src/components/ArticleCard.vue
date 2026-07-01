<template>
  <router-link :to="`/article/${article.slug}`" class="article-card">
    <FrostedCard>
      <div class="article-card__inner">
        <div v-if="article.cover" class="article-card__cover">
          <img :src="article.cover" :alt="article.title" />
        </div>
        <div class="article-card__body">
          <h3 class="article-card__title">{{ article.title }}</h3>
          <span class="article-card__date">{{ dateText }}</span>
          <p v-if="article.summary" class="article-card__summary">{{ article.summary }}</p>
          <div class="article-card__meta">
            <div class="article-card__tags">
              <span
                v-for="tag in article.tags"
                :key="tag.id"
                class="article-card__tag"
              >{{ tag.name }}</span>
            </div>
            <span v-if="article.categoryName" class="article-card__category">{{ article.categoryName }}</span>
          </div>
        </div>
      </div>
    </FrostedCard>
  </router-link>
</template>

<script setup>
import { computed } from 'vue'
import FrostedCard from '@/components/FrostedCard.vue'

const props = defineProps({
  article: {
    type: Object,
    required: true
  }
})

const dateText = computed(() => {
  if (!props.article.createTime) return ''
  const d = new Date(props.article.createTime)
  const y = d.getFullYear()
  const m = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  return `${y}/${m}/${day}`
})
</script>

<style scoped>
.article-card {
  display: block;
  text-decoration: none;
  color: var(--text-primary);
  transition: transform 0.2s ease;
}

.article-card:hover {
  transform: translateY(-2px);
}

.article-card__inner {
  display: flex;
  gap: 20px;
}

.article-card__cover {
  flex-shrink: 0;
  width: 200px;
  height: 130px;
  border-radius: 8px;
  overflow: hidden;
}

.article-card__cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.article-card__body {
  flex: 1;
  min-width: 0;
}

.article-card__title {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 6px;
  color: var(--text-primary);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.article-card__date {
  font-size: 13px;
  color: var(--text-secondary);
  display: block;
  margin-bottom: 8px;
}

.article-card__summary {
  font-size: 14px;
  color: var(--text-secondary);
  line-height: 1.6;
  margin-bottom: 12px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.article-card__meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.article-card__tags {
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
}

.article-card__tag {
  font-size: 12px;
  padding: 2px 10px;
  border-radius: 10px;
  background: var(--accent-glow);
  color: var(--accent);
}

.article-card__category {
  font-size: 13px;
  color: var(--text-secondary);
}

@media (max-width: 768px) {
  .article-card__inner {
    flex-direction: column;
  }

  .article-card__cover {
    width: 100%;
    height: 180px;
  }
}
</style>