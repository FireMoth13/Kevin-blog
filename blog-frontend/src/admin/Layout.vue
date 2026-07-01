<template>
  <div class="admin-layout">
    <aside class="admin-sidebar">
      <router-link to="/admin" class="admin-sidebar__brand">Kevin Admin</router-link>

      <nav class="admin-sidebar__nav">
        <router-link to="/admin" class="admin-sidebar__link" exact>📊 概览</router-link>
        <router-link to="/admin/categories" class="admin-sidebar__link">📁 分类管理</router-link>
        <router-link to="/admin/tags" class="admin-sidebar__link">🏷 标签管理</router-link>
      </nav>

      <div class="admin-sidebar__footer">
        <router-link to="/" class="admin-sidebar__link">🏠 返回博客</router-link>
        <button class="admin-sidebar__logout" @click="logout">退出登录</button>
      </div>
    </aside>

    <main class="admin-main">
      <div class="admin-main__topbar">
        <span class="admin-main__greeting">你好，{{ nickname }}</span>
      </div>
      <div class="admin-main__content">
        <router-view />
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useAuth } from '@/composables/useAuth'
import { getAdminInfo } from '@/api/auth'

const { logout } = useAuth()
const nickname = ref('')

onMounted(async () => {
  try {
    const info = await getAdminInfo()
    nickname.value = info.nickname || info.username
  } catch (err) {
    console.error('获取管理员信息失败:', err)
  }
})
</script>

<style scoped>
.admin-layout {
  display: flex;
  min-height: 100vh;
  position: relative;
  z-index: 2;
}

/* ========== 侧栏 ========== */
.admin-sidebar {
  width: 220px;
  background: var(--bg-card);
  backdrop-filter: blur(var(--blur));
  -webkit-backdrop-filter: blur(var(--blur));
  border-right: 1px solid var(--border);
  display: flex;
  flex-direction: column;
  padding: 24px 16px;
  flex-shrink: 0;
}

.admin-sidebar__brand {
  font-size: 18px;
  font-weight: 700;
  color: var(--accent);
  text-decoration: none;
  margin-bottom: 32px;
  padding: 0 8px;
  text-shadow: 0 0 8px var(--accent-glow);
}

.admin-sidebar__nav {
  display: flex;
  flex-direction: column;
  gap: 4px;
  flex: 1;
}

.admin-sidebar__link {
  padding: 10px 12px;
  border-radius: 8px;
  font-size: 14px;
  color: var(--text-secondary);
  text-decoration: none;
  transition: all 0.2s;
}

.admin-sidebar__link:hover {
  background: rgba(128, 128, 128, 0.1);
  color: var(--text-primary);
}

.admin-sidebar__link.router-link-exact-active {
  background: var(--accent-glow);
  color: var(--accent);
}

.admin-sidebar__footer {
  display: flex;
  flex-direction: column;
  gap: 4px;
  padding-top: 16px;
  border-top: 1px solid var(--border);
}

.admin-sidebar__logout {
  padding: 10px 12px;
  border-radius: 8px;
  border: none;
  background: transparent;
  font-size: 14px;
  color: var(--text-secondary);
  cursor: pointer;
  text-align: left;
  transition: all 0.2s;
}

.admin-sidebar__logout:hover {
  background: rgba(231, 76, 60, 0.15);
  color: #e57373;
}

/* ========== 主区域 ========== */
.admin-main {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
}

.admin-main__topbar {
  padding: 16px 28px;
  border-bottom: 1px solid var(--border);
  background: var(--bg-card);
  backdrop-filter: blur(var(--blur));
  -webkit-backdrop-filter: blur(var(--blur));
}

.admin-main__greeting {
  font-size: 14px;
  color: var(--text-secondary);
}

.admin-main__content {
  flex: 1;
  padding: 28px;
  overflow-y: auto;
}

@media (max-width: 768px) {
  .admin-sidebar {
    width: 60px;
    padding: 16px 8px;
  }

  .admin-sidebar__brand {
    font-size: 0;
  }

  .admin-sidebar__link {
    padding: 10px 6px;
    text-align: center;
    font-size: 18px;
  }

  .admin-sidebar__link span {
    display: none;
  }

  .admin-main__content {
    padding: 16px;
  }
}
</style>
