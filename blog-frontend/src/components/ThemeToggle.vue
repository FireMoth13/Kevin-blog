<template>
    <button class="theme-toggle" @click="toggleTheme" :title="theme === 'night' ? '切换白天模式' : '切换夜晚模式'">
      <span class="theme-toggle__icon">{{ theme === 'night' ? '☀️' : '🌙' }}</span>
    </button>
</template>

<script setup>
import { useTheme } from '@/composables/useTheme'
const { theme, toggleTheme } = useTheme()
</script>

<style scoped>
.theme-toggle {
  position: fixed;
  top: 20px;
  right: 20px;
  z-index: 10;
  width: 44px;
  height: 44px;
  border-radius: 50%;
  border: 1px solid var(--border);
  background: var(--bg-card);
  backdrop-filter: blur(var(--blur));
  -webkit-backdrop-filter: blur(var(--blur));
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.theme-toggle:hover {
  transform: scale(1.1);
  border-color: var(--accent);
  box-shadow: 0 0 16px var(--accent-glow);
}

.theme-toggle__icon {
  font-size: 20px;
  line-height: 1;
  transition: transform 0.4s ease;
}

.theme-toggle:hover .theme-toggle__icon {
  transform: rotate(30deg);
}
</style>
    