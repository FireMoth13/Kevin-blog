<template>
  <header class="nav-header">
    <div class="nav-header__inner">
      <router-link to="/" class="nav-header__logo">Kevin's Blog</router-link>

      <nav class="nav-header__links">
        <router-link to="/" class="nav-header__link">首页</router-link>
        <router-link to="/articles" class="nav-header__link">文章</router-link>
        <router-link to="/archives" class="nav-header__link">归档</router-link>
        <router-link to="/game" class="nav-header__link">游戏</router-link>
        <router-link
          v-if="isLoggedIn"
          to="/admin"
          class="nav-header__link nav-header__link--admin"
        >管理</router-link>
      </nav>

      <button
        class="nav-header__theme-btn"
        @click="toggleTheme"
        :title="theme === 'night' ? '切换白天模式' : '切换夜晚模式'"
      >
        {{ theme === 'night' ? '☀️' : '🌙' }}
      </button>
    </div>
  </header>
</template>

<script setup>
import { useTheme } from '@/composables/useTheme'
import { useAuth } from '@/composables/useAuth'

const { theme, toggleTheme } = useTheme()
const { isLoggedIn } = useAuth()
</script>

<style scoped>
.nav-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 5;
  background: var(--bg-card);
  backdrop-filter: blur(var(--blur));
  -webkit-backdrop-filter: blur(var(--blur));
  border-bottom: 1px solid var(--border);
}

.nav-header__inner {
  max-width: 960px;
  margin: 0 auto;
  padding: 0 24px;
  height: 56px;
  display: flex;
  align-items: center;
  gap: 24px;
}

.nav-header__logo {
  font-size: 18px;
  font-weight: 700;
  color: var(--accent);
  text-decoration: none;
  text-shadow: 0 0 8px var(--accent-glow);
  white-space: nowrap;
  flex-shrink: 0;
}

.nav-header__links {
  display: flex;
  gap: 4px;
  flex: 1;
}

.nav-header__link {
  padding: 6px 14px;
  border-radius: 6px;
  font-size: 14px;
  color: var(--text-secondary);
  text-decoration: none;
  transition: all 0.2s ease;
  white-space: nowrap;
}

.nav-header__link:hover {
  color: var(--text-primary);
  background: rgba(128, 128, 128, 0.1);
}

.nav-header__link.router-link-exact-active {
  color: var(--accent);
  background: var(--accent-glow);
}

.nav-header__link--admin.router-link-exact-active {
  color: #ffd54f;
  background: rgba(255, 213, 79, 0.15);
}

.nav-header__theme-btn {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  border: 1px solid var(--border);
  background: transparent;
  cursor: pointer;
  font-size: 18px;
  line-height: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  transition: all 0.2s ease;
}

.nav-header__theme-btn:hover {
  border-color: var(--accent);
  box-shadow: 0 0 12px var(--accent-glow);
}

@media (max-width: 768px) {
  .nav-header__inner {
    padding: 0 16px;
    gap: 12px;
  }

  .nav-header__links {
    gap: 0;
  }

  .nav-header__link {
    padding: 6px 8px;
    font-size: 13px;
  }

  .nav-header__logo {
    font-size: 16px;
  }
}
</style>
