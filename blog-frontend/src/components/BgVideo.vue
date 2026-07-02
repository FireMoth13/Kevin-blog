<template>
  <div class="bg-wrapper">
    <img
      class="bg-img"
      :class="{ 'bg-img--active': theme === 'night' }"
      src="/assets/bg-night.png"
      alt=""
    />
    <img
      class="bg-img"
      :class="{ 'bg-img--active': theme !== 'night' }"
      src="/assets/bg-day.png"
      alt=""
    />
    <div class="video-overlay" />
  </div>
</template>

<script setup>
import { useTheme } from '@/composables/useTheme'

const { theme } = useTheme()
</script>

<style scoped>
.bg-wrapper {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
  overflow: hidden;
}

.bg-img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  opacity: 0;
  transition: opacity 0.8s ease;
  animation: bg-zoom 30s ease-in-out infinite alternate;
}

.bg-img--active {
  opacity: 1;
}

@keyframes bg-zoom {
  from { transform: scale(1); }
  to { transform: scale(1.05); }
}
</style>
