import request from './request'

// ==================== 公开端 ====================
export function getArticles(params) {
  return request.get('/public/articles', { params })
}

export function getArticleDetail(slug) {
  return request.get(`/public/articles/${slug}`)
}

export function getArchives() {
  return request.get('/public/archives')
}

export function incrViewCount(id) {
  return request.put(`/public/articles/${id}/view`)
}

// ==================== 管理端 ====================
export function getAdminArticles() {
  return request.get('/admin/articles')
}

export function getArticleForEdit(id) {
  return request.get(`/admin/articles/${id}`)
}

export function saveArticle(data) {
  return request.post('/admin/articles', data)
}

export function updateArticle(data) {
  return request.put('/admin/articles', data)
}

export function deleteArticle(id) {
  return request.delete(`/admin/articles/${id}`)
}