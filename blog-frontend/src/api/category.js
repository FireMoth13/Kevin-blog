import request from './request'

export function getCategories() {
  return request.get('/admin/categories')
}

export function addCategory(data) {
  return request.post('/admin/categories', data)
}

export function updateCategory(data) {
  return request.put('/admin/categories', data)
}

export function deleteCategory(id) {
  return request.delete(`/admin/categories/${id}`)
}

export function getPublicCategories() {
  return request.get('/public/categories')
}