import { ref, computed } from 'vue'
import router from '@/router'

const token = ref(localStorage.getItem('token') || '')

export function useAuth() {
  const isLoggedIn = computed(() => !!token.value)

  function setToken(newToken) {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  function logout() {
    token.value = ''
    localStorage.removeItem('token')
    router.push('/admin/login')
  }

  return { token, isLoggedIn, setToken, logout }
}
