<template>
  <div class="login-page">
    <div class="login-card frosted-card">
      <h1 class="login-card__title">管理后台</h1>
      <p class="login-card__subtitle">Kevin's Blog</p>

      <form class="login-form" @submit.prevent="handleSubmit">
        <div class="login-form__group">
          <label class="login-form__label">用户名</label>
          <input
            v-model="username"
            type="text"
            class="login-form__input"
            placeholder="请输入用户名"
            autocomplete="username"
          />
        </div>

        <div class="login-form__group">
          <label class="login-form__label">密码</label>
          <input
            v-model="password"
            type="password"
            class="login-form__input"
            placeholder="请输入密码"
            autocomplete="current-password"
          />
        </div>

        <p v-if="errorMsg" class="login-form__error">{{ errorMsg }}</p>

        <button type="submit" class="btn login-form__btn" :disabled="loading">
          {{ loading ? '登录中...' : '登录' }}
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '@/api/auth'
import { useAuth } from '@/composables/useAuth'

const router = useRouter()
const { setToken } = useAuth()

const username = ref('')
const password = ref('')
const loading = ref(false)
const errorMsg = ref('')

async function handleSubmit() {
  if (!username.value || !password.value) {
    errorMsg.value = '请填写用户名和密码'
    return
  }

  errorMsg.value = ''
  loading.value = true
  try {
    const token = await login(username.value, password.value)
    setToken(token)
    router.push('/admin')
  } catch (err) {
    errorMsg.value = err.message || '登录失败'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  z-index: 2;
}

.login-card {
  width: 100%;
  max-width: 400px;
  padding: 40px 36px;
}

.login-card__title {
  text-align: center;
  font-size: 26px;
  font-weight: 700;
  color: var(--accent);
  text-shadow: 0 0 10px var(--accent-glow);
  margin-bottom: 4px;
}

.login-card__subtitle {
  text-align: center;
  font-size: 14px;
  color: var(--text-secondary);
  margin-bottom: 32px;
}

.login-form__group {
  margin-bottom: 20px;
}

.login-form__label {
  display: block;
  font-size: 14px;
  color: var(--text-secondary);
  margin-bottom: 6px;
}

.login-form__input {
  width: 100%;
  padding: 10px 14px;
  border-radius: 8px;
  border: 1px solid var(--border);
  background: rgba(128, 128, 128, 0.08);
  color: var(--text-primary);
  font-size: 15px;
  outline: none;
  transition: border-color 0.2s;
}

.login-form__input:focus {
  border-color: var(--accent);
  box-shadow: 0 0 8px var(--accent-glow);
}

.login-form__input::placeholder {
  color: var(--text-secondary);
  opacity: 0.6;
}

.login-form__error {
  font-size: 13px;
  color: #e57373;
  margin-bottom: 12px;
  text-align: center;
}

.login-form__btn {
  width: 100%;
  justify-content: center;
  padding: 12px 0;
  font-size: 15px;
  margin-top: 8px;
}
</style>
