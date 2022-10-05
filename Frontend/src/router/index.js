import { createRouter, createWebHistory } from 'vue-router'
import Meta from 'vue-meta'
import AddEvent from '../views/AddEvent.vue';
import EventListing from '../views/eventListing.vue';
import EventDetail from '../views/eventDetails.vue'
import CatDetail from '../views/catDetails.vue'
import EditCategory from '../views/editCategory.vue'
import CatListing from '../views/catListing.vue'
import AddCategory from '../views/AddCategory.vue'
import EditEvent from '../views/editEvent.vue'
import UserListing from '../views/userListing.vue'
import UserDetail from '../views/userDetails.vue'
import EditUser from '../views/editUser.vue'
import AddUser from '../views/AddUser.vue'
import Login from '../views/login.vue'

// import.meta.env.VITE_BASE_URL
const history = createWebHistory('/at2/')
const routes = [
    
    {
      path: '/addEvent',
      name: 'addEvent',
      component: AddEvent,
      meta: {
        requireAuth : false,
        role: ['admin','student']
      }
    },
    {
      path: '/',
      name: 'EventListing',
      component: EventListing,
      meta: {
        requireAuth : false,
        role: ['admin','lecturer','student']
      }
    },
    {
      path: '/eventDetail/:eventId',
      name: 'EventDetail',
      component: EventDetail,
      meta: {
        requireAuth : true,
        role: ['admin','lecturer','student']
      }
    },
    {
      path:'/editCategory/:catId',
      name: 'EditCategory',
      component: EditCategory,
      meta: {
        requireAuth : true,
        role: ['admin','lecturer']
      }
    },
    {
      path:'/allCategory',
      name: 'CatListing',
      component: CatListing,
      meta: {
        requireAuth : true,
        role: ['admin','lecturer','student']
      }
    },
    {
      path:'/addCategory',
      name: 'AddCategory',
      component: AddCategory,
      meta: {
        requireAuth : true,
        role: ['admin','lecturer']
      }
    },
    {
      path: '/catDetail/:catId',
      name: 'CatDetail',
      component: CatDetail,
      meta: {
        requireAuth : true,
        role: ['admin','lecturer','student']
      }
    },
    {
      path: '/editEvent/:eventId',
      name: 'EditEvent',
      component: EditEvent,
      meta: {
        requireAuth : true,
        role: ['admin','student']
      }
    },
    {
      path: '/allUser',
      name: 'allUser',
      component: UserListing,
      meta: {
        requireAuth : true,
        role: ['admin']
      }
    },
    {
      path: '/userDetail/:userId',
      name: 'UserDetail',
      component: UserDetail,
      meta: {
        requireAuth : true,
        role: ['admin']
      }
    },
    {
      path: '/editUser/:userId',
      name: 'EditUser',
      component: EditUser,
      meta: {
        requireAuth : true,
        role: ['admin']
      }
    },
    {
      path: '/addUser',
      name: 'AddUser',
      component: AddUser,
      meta: {
        requireAuth : false
      }
    },
    {
      path: '/login',
      name: 'Login',
      component: Login,
      meta: {
        requireAuth : false
      }
    }

]

const router = createRouter({ history, routes }) 
router.beforeEach((to,from,next) => {
  const requireAuth = to.matched.some((record) => record.meta.requireAuth)
  const role = to.matched.filter((record) => record.meta.role)
  console.log(`Auth-Role: ${role}`)
  if(requireAuth){
    if(localStorage.getItem('accessToken')){
      if(role.filter((e) => e === localStorage.getItem('role'))){
        // console.log(role.matched.some((e) => e == localStorage.getItem('role')))
        next()
      }
    }
    else
    next({name : 'Login'})
  }
  else 
    next()
})


  export default router