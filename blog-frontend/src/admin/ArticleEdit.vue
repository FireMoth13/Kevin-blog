<template>
  <div class="editor">
    <div class="editor__header">
      <h2 class="editor__title">{{ isEdit ? '编辑文章' : '新建文章' }}</h2>
      <div class="editor__header-actions">
        <button class="btn btn--ghost" @click="saveDraft">保存草稿</button>
        <button class="btn" @click="publish">{{ isEdit ? '更新发布' : '立即发布' }}</button>
      </div>
    </div>

    <div class="editor__body">
      <div class="editor__panel editor__panel--form">
        <div class="editor__group">
          <label class="editor__label">标题</label>
          <input v-model="form.title" type="text" class="editor__input" placeholder="文章标题" />
        </div>

        <div class="editor__group">
          <label class="editor__label">摘要</label>
          <textarea v-model="form.summary" class="editor__textarea editor__textarea--sm" rows="2" placeholder="文章摘要（可选）" />
        </div>

        <div class="editor__group">
          <label class="editor__label">正文 (Markdown)</label>
          <textarea v-model="form.contentMd" class="editor__textarea editor__textarea--md" placeholder="使用 Markdown 语法编写文章..." />
        </div>

        <div class="editor__row">
          <div class="editor__group editor__group--half">
            <label class="editor__label">封面图 URL</label>
            <input v-model="form.cover" type="text" class="editor__input" placeholder="https://..." />
          </div>

          <div class="editor__group editor__group--half">
            <label class="editor__label">分类</label>
            <select v-model="form.categoryId" class="editor__input">
              <option :value="null">未分类</option>
              <option v-for="cat in categories" :key="cat.id" :value="cat.id">{{ cat.name }}</option>
            </select>
          </div>
        </div>

        <div class="editor__group">
          <label class="editor__label">标签</label>
          <div class="editor__tags">
            <button
              v-for="tag in tags"
              :key="tag.id"
              class="editor__tag"
              :class="{ 'editor__tag--active': form.tagIds.includes(tag.id) }"
              @click="toggleTag(tag.id)"
            >{{ tag.name }}</button>
          </div>
        </div>
      </div>

      <div class="editor__panel editor__panel--preview">
        <h3 class="editor__label">预览</h3>
        <div v-if="form.contentMd" class="editor__preview frosted-card" v-html="previewHtml" />
        <div v-else class="editor__preview-hint frosted-card">
          <p>在左侧输入 Markdown 内容，这里会实时预览</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { marked } from 'marked'
import { saveArticle, updateArticle, getArticleForEdit } from '@/api/article'
import { getPublicCategories } from '@/api/category'
import { getPublicTags } from '@/api/tag'

const route = useRoute()
const router = useRouter()

const isEdit = computed(() => !!route.params.id)

const categories = ref([])
const tags = ref([])

const form = ref({
  id: null,
  title: '',
  contentMd: '',
  summary: '',
  cover: '',
  categoryId: null,
  tagIds: [],
  isTop: 0
})

const previewHtml = computed(() => {
  if (!form.value.contentMd) return ''
  return marked(form.value.contentMd)
})

function toggleTag(id) {
  const idx = form.value.tagIds.indexOf(id)
  if (idx === -1) {
    form.value.tagIds.push(id)
  } else {
    form.value.tagIds.splice(idx, 1)
  }
}

async function saveDraft() {
  await submit(0)
}

async function publish() {
  await submit(1)
}

async function submit(status) {
  if (!form.value.title.trim()) {
    alert('请输入文章标题')
    return
  }
  if (!form.value.contentMd.trim()) {
    alert('请输入文章内容')
    return
  }

  try {
    if (isEdit.value) {
      await updateArticle({ ...form.value, status })
    } else {
      await saveArticle({ ...form.value, status })
    }
    router.push('/admin')
  } catch (err) {
    alert('保存失败: ' + (err.message || '未知错误'))
  }
}

onMounted(async () => {
  try {
    const [cats, tgs] = await Promise.all([
      getPublicCategories(),
      getPublicTags()
    ])
    categories.value = cats
    tags.value = tgs
  } catch (err) {
    console.error('加载表单选项失败:', err)
  }

  if (isEdit.value) {
    try {
      const data = await getArticleForEdit(route.params.id)
      form.value = {
        id: data.id,
        title: data.title || '',
        contentMd: data.contentMd || '',
        summary: data.summary || '',
        cover: data.cover || '',
        categoryId: data.categoryId || null,
        tagIds: data.tagIds || [],
        isTop: data.isTop || 0
      }
    } catch (err) {
      alert('加载文章失败: ' + (err.message || '未知错误'))
      router.push('/admin')
    }
  }
})
</script>

<style scoped>
.editor__header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 24px;
}

.editor__title {
  font-size: 22px;
  font-weight: 700;
  color: var(--text-primary);
}

.editor__header-actions {
  display: flex;
  gap: 10px;
}

.editor__body {
  display: flex;
  gap: 24px;
}

.editor__panel {
  flex: 1;
  min-width: 0;
}

.editor__panel--form {
  flex: 1.2;
}

.editor__group {
  margin-bottom: 18px;
}

.editor__group--half {
  flex: 1;
  min-width: 0;
}

.editor__label {
  display: block;
  font-size: 14px;
  color: var(--text-secondary);
  margin-bottom: 6px;
}

.editor__input {
  width: 100%;
  padding: 10px 14px;
  border-radius: 8px;
  border: 1px solid var(--border);
  background: rgba(128, 128, 128, 0.08);
  color: var(--text-primary);
  font-size: 14px;
  outline: none;
  transition: border-color 0.2s;
}

.editor__input:focus {
  border-color: var(--accent);
  box-shadow: 0 0 6px var(--accent-glow);
}

.editor__textarea {
  width: 100%;
  padding: 10px 14px;
  border-radius: 8px;
  border: 1px solid var(--border);
  background: rgba(128, 128, 128, 0.08);
  color: var(--text-primary);
  font-size: 14px;
  outline: none;
  resize: vertical;
  font-family: inherit;
  line-height: 1.6;
  transition: border-color 0.2s;
}

.editor__textarea:focus {
  border-color: var(--accent);
  box-shadow: 0 0 6px var(--accent-glow);
}

.editor__textarea--md {
  height: 400px;
  font-family: 'Consolas', 'Courier New', monospace;
  font-size: 13px;
  line-height: 1.7;
}

.editor__textarea--sm {
  resize: none;
}

.editor__row {
  display: flex;
  gap: 16px;
}

.editor__tags {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.editor__tag {
  padding: 4px 14px;
  border-radius: 14px;
  border: 1px solid var(--border);
  background: transparent;
  color: var(--text-secondary);
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s;
}

.editor__tag:hover {
  border-color: var(--accent);
}

.editor__tag--active {
  border-color: var(--accent);
  background: var(--accent-glow);
  color: var(--accent);
}

.editor__preview {
  padding: 20px;
  min-height: 200px;
  line-height: 1.8;
  font-size: 15px;
  max-height: 80vh;
  overflow-y: auto;
}

.editor__preview :deep(h2) { font-size: 22px; margin: 24px 0 12px; }
.editor__preview :deep(h3) { font-size: 18px; margin: 20px 0 10px; }
.editor__preview :deep(p) { margin-bottom: 12px; }
.editor__preview :deep(code) {
  padding: 2px 6px;
  border-radius: 4px;
  background: rgba(128, 128, 128, 0.15);
  font-size: 0.9em;
}
.editor__preview :deep(pre) {
  padding: 14px;
  border-radius: 8px;
  background: rgba(0, 0, 0, 0.2);
  overflow-x: auto;
}

.editor__preview-hint {
  padding: 40px;
  text-align: center;
  color: var(--text-secondary);
  font-size: 14px;
}

/* 白天模式：输入框背景加白 */
[data-theme="day"] .editor__input,
[data-theme="day"] .editor__textarea {
  background: rgba(255, 255, 255, 0.70);
}

[data-theme="day"] .editor__label {
  color: rgba(0, 0, 0, 0.7);
}

@media (max-width: 900px) {
  .editor__body {
    flex-direction: column;
  }

  .editor__textarea--md {
    height: 300px;
  }
}
</style>
