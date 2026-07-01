import request from './request'

export async function login(username, password) {
  const data = await request.post('/admin/login', { username, password })
  return data.token    // 返回纯 Token 字符串，不是 { token: "..." } 对象
}

export function getAdminInfo() {
  return request.get('/admin/info')
}