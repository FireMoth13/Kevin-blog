<template>
  <div class="neon-clock">
    <span class="neon-clock__time">{{ formatted }}</span>
  </div>
</template>

<script setup>
import{ref,computed,onUnmounted} from 'vue'

const now=ref(new Date())

const formatted=computed(()=>{
  const y = now.value.getFullYear()
  const m = String(now.value.getMonth() + 1).padStart(2, '0')
  const d = String(now.value.getDate()).padStart(2, '0')
  const hh = String(now.value.getHours()).padStart(2, '0')
  const mm = String(now.value.getMinutes()).padStart(2, '0')
  const ss = String(now.value.getSeconds()).padStart(2, '0')
  return `${y}/${m}/${d} ${hh}:${mm}:${ss}`
})

const timer = setInterval(() => {
  now.value = new Date()
}, 1000)

onUnmounted(() => {
  clearInterval(timer)
})
</script>

<style scoped>
.neon-clock {
  text-align:center;
  padding: 20px 0;
}

.neon-clock__time {
  font-family: 'Courier New', monospace;
  font-size: 36px;
  font-weight: bold;
  color: var(--accent);
  letter-spacing: 4px;
  text-shadow:
    0 0 10px var(--accent-glow),
    0 0 20px var(--accent-glow),
    0 0 40px var(--accent-glow),
    0 0 80px var(--accent-glow);
  user-select: none;
}
</style>