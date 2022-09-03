import { createRouter, createWebHistory } from 'vue-router'
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
// import Login from '../views/login.vue'

// import.meta.env.VITE_BASE_URL
const history = createWebHistory('/at2/')
const routes = [
    
    {
      path: '/addEvent',
      name: 'addEvent',
      component: AddEvent
    },
    {
      path: '/',
      name: 'EventListing',
      component: EventListing
    },
    {
      path: '/eventDetail/:eventId',
      name: 'EventDetail',
      component: EventDetail
    },
    {
      path:'/editCategory/:catId',
      name: 'EditCategory',
      component: EditCategory
    },
    {
      path:'/allCategory',
      name: 'CatListing',
      component: CatListing
    },
    {
      path:'/addCategory',
      name: 'AddCategory',
      component: AddCategory
    },
    {
      path: '/catDetail/:catId',
      name: 'CatDetail',
      component: CatDetail
    },
    {
      path: '/editEvent/:eventId',
      name: 'EditEvent',
      component: EditEvent
    },
    {
      path: '/allUser',
      name: 'allUser',
      component: UserListing
    },
    {
      path: '/userDetail/:userId',
      name: 'UserDetail',
      component: UserDetail
    },
    {
      path: '/editUser/:userId',
      name: 'EditUser',
      component: EditUser
    },
    {
      path: '/addUser',
      name: 'AddUser',
      component: AddUser
    }
    // {
    //   path: '/login',
    //   name: 'Login',
    //   component: Lo
    // }

]
  
  const router = createRouter({ history, routes })
  export default router