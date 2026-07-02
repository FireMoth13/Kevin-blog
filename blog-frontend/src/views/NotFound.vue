<template>
  <div class="not-found">
    <div class="not-found__card frosted-card">
      <h1 class="not-found__code neon-text">404</h1>
      <p class="not-found__text">这个页面不存在哦~</p>
      <button class="btn" @click="$router.push('/')">返回首页</button>
    </div>
  </div>
</template>

<style scoped>
.not-found {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  z-index: 2;
}

.not-found__card {
  text-align: center;
  padding: 48px 40px;
}

.not-found__code {
  font-size: 80px;
  font-weight: 800;
  line-height: 1;
  margin-bottom: 12px;
}

.not-found__text {
  font-size: 16px;
  color: var(--text-secondary);
  margin-bottom: 28px;
}
</style>
