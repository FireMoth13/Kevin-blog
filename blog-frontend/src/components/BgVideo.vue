<template>
  <div class="bg-video-wrapper">
    <video class="bg-video" src="/assets/bg-night.mp4"
      v-show="theme === 'night'"
      autoplay loop muted playsinline />
    <video class="bg-video" src="/assets/bg-day.mp4"
      v-show="theme !== 'night'"
      autoplay loop muted playsinline />
    <div class="video-overlay" />
  </div>
</template>

<script setup>
import { useTheme } from '@/composables/useTheme'

const { theme } = useTheme()
</script>

<style scoped>
.bg-video-wrapper {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
  overflow: hidden;
}

.bg-video {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: opacity 0.8s ease;
}

/* 夜晚模式：夜晚视频可见，白天视频透明 */
.bg-video:first-child {
  opacity: 1;
}
.bg-video:last-child {
  opacity: 0;
}
</style>