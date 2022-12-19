import { createRouter, createWebHistory } from 'vue-router'
import AddEvent from '../views/AddEvent.vue';
import EventListing from '../views/eventListing.vue';
import EventDetail from '../views/eventDetails.vue'
import CatDetail from '../views/catDetails.vue'
import EditCategory from '../views/editCategory.vue'
import CatListing from '../views/catListing.vue'
import AddCategory from '../views/AddCategory.vue'
import EditEvent from '../views/editEvent.vue'
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
    }

]
  
  const router = createRouter({ history, routes })
  export default router