<template>
  <BgVideo />
  <ThemeToggle />
  <router-view />
</template>

<script setup>
import BgVideo from '@/components/BgVideo.vue'
import ThemeToggle from '@/components/ThemeToggle.vue'
</script>