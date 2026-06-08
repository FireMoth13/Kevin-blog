<template>
  <div class="bg-video-wrapper">
    <video
      v-show="nightLoaded !== false"
      class="bg-video"
      :src="'/assets/bg-night.mp4'"
      autoplay
      loop
      muted
      playsinline
      @error="nightLoaded = false"
    />
    <video
      v-show="dayLoaded !== false"
      class="bg-video"
      :src="'/assets/bg-day.mp4'"
      autoplay
      loop
      muted
      playsinline
      @error="dayLoaded = false"
    />
    <div class="video-overlay" />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useTheme } from '@/composables/useTheme'

const { theme } = useTheme()
const nightLoaded = ref(undefined)
const dayLoaded = ref(undefined)
</script>

<style scoped>
.bg-video-wrapper {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  z-index: 0;
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