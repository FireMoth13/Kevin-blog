import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Home.vue')  // 路由懒加载，只有访问时才加载组件
  },
  {
    path: '/articles',
    name: 'Articles',
    component: () => import('@/views/Articles.vue')
  },
  {
    path: '/article/:slug',
    name: 'Detail',
    component: () => import('@/views/Detail.vue')
  },
  {
    path: '/archives',
    name: 'Archives',
    component: () => import('@/views/Archives.vue')
  },
  {
    path: '/game',
    name: 'Game',
    component: () => import('@/views/Game.vue')
  },
  {
    path: '/admin/login',
    name: 'AdminLogin',
    component: () => import('@/admin/Login.vue')
  },
  {
    path: '/admin',
    component: () => import('@/admin/Layout.vue'),
    children: [
      {
        path: '',
        name: 'Dashboard',
        component: () => import('@/admin/Dashboard.vue')
      },
      {
        path: 'article/new',
        name: 'ArticleNew',
        component: () => import('@/admin/ArticleEdit.vue')
      },
      {
        path: 'article/:id',
        name: 'ArticleEdit',
        component: () => import('@/admin/ArticleEdit.vue')
      },
      {
        path: 'categories',
        name: 'Categories',
        component: () => import('@/admin/Categories.vue')
      },
      {
        path: 'tags',
        name: 'Tags',
        component: () => import('@/admin/Tags.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')

  if (to.path.startsWith('/admin') && to.path !== '/admin/login' && !token) {
    next('/admin/login')
    return
  }

  if (to.path === '/admin/login' && token) {
    next('/admin')
    return
  }

  next()
})

export default router