import request from './request'

export function getTags() {
  return request.get('/admin/tags')
}

export function addTag(data) {
  return request.post('/admin/tags', data)
}

export function updateTag(data) {
  return request.put('/admin/tags', data)
}

export function deleteTag(id) {
  return request.delete(`/admin/tags/${id}`)
}

export function getPublicTags() {
  return request.get('/public/tags')
}