<script setup>
import { ref, onBeforeMount, onBeforeUpdate, onServerPrefetch} from "vue";
import ListEvent from '../components/listEvent.vue'
import ListCategory from '../components/listCategory.vue'
import AlertBox from '../components/alertBox.vue'

const now = new Date();
console.log(`now : ${now}`); 
const yourDateTime = ref(now)
const yourEmail = ref('')

let status = ref(0)
const token = `Bearer ${localStorage.getItem('accessToken')}`
const filterEvent = ref([])
const eventList = ref([])
const postRefreshToken = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/refresh`,{
        method: 'GET',
        headers: {
          'content-type': 'application/json',
          'Authorization': token,
          'isRefreshToken': true
        }
      })
      if(res.status === 200){
        status.value = res.status
        const response = res.json()
        response.then(jsonRes => {
         const reToken = jsonRes.jwt
         localStorage.setItem('accessToken', reToken);
         console.log(reToken)
         console.log(localStorage.getItem('accessToken'))
         window.location.reload();
        })
      }else alert("Something went wrong! Please log in again.")
}
const getEventList = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/events`
  ,{
        method: 'GET',
        headers: {
          'content-type': 'application/json',
          'authorization': token
        }
  })
  if (res.status === 200) {
    const event = await res.json();
    eventList.value = event
    filterEvent.value = event
  } else if(res.status === 401){
    console.log("Access token expired!!!!")
    postRefreshToken();
  } else {
    console.log("No Scheduled Events");
  }
};
const categoryList = ref([])
const getCategory = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/eventCategories`
  ,{
        method: 'GET',
        headers: {
          'content-type': 'application/json',
          'authorization': token 
        }
  })
  if (res.status === 200) {
    categoryList.value = await res.json(); 
  } else if(res.status === 401){
    console.log("Access token expired!!!!")
    postRefreshToken();
  } else {
    console.log("No Category");
  }
};
onBeforeMount(async () => {
  if(localStorage.getItem('accessToken')){
  console.log("you've localstorage")
  console.log(token)
  await getEventList();
  await getCategory();
  }
});
if(status.value === 401){
    console.log("Access token expired!!!!")
    postRefreshToken();
}
// onServerPrefetch(async () => {
//   if(localStorage.getItem('accessToken')){
//   console.log("you've localstorage")
//   console.log(token)
//   await getEventList();
//   await getCategory();
//   }
// });

const text = ref('')

const checkFuture = () => {
  return filterEvent.value = eventList.value.filter((e) => {
    const date = ref(new Date(e.startTime[0],e.startTime[1]-1,e.startTime[2],e.startTime[3],e.startTime[4]))
    text.value = "No On-Going or Upcoming Events."
    return date.value > now })
}

const checkPast = () => {
  return filterEvent.value = eventList.value.filter((e) => {
    const date = ref(new Date(e.startTime[0],e.startTime[1]-1,e.startTime[2],e.startTime[3],e.startTime[4]))
    text.value = "No Past Events."
    return date.value < now })
}

const checkEmail = () => filterEvent.value = eventList.value.filter((e) => e.bookingEmail == yourEmail.value , text.value = "No Scheduled Events.")
const listAll = () => filterEvent.value = eventList.value

const checkDate = () => {
  return filterEvent.value = eventList.value.filter((e) => {
    const date = ref(new Date(e.startTime[0],e.startTime[1]-1,e.startTime[2],e.startTime[3],e.startTime[4]))
    date.value = date.value.toLocaleDateString();
    const select = ref(yourDateTime.value)
    select.value = new Date(select.value).toLocaleDateString();
    text.value = "No Scheduled Events."
    console.log(`subSelect : ${select.value}`); 
    console.log(`subDate : ${date.value}`);
    return date.value == select.value
  })
}
const sortedEvent = eventList.value.sort((a, b) => (a.startTime < b.startTime) ? 1 : -1);

</script>
 
<template>
  <div class="app">
      <!-- alert No event -->
    <div>
        <AlertBox v-if="eventList.length === 0" text="Scheduled Events"/>
    </div>
      <!-- List All Event-->
    <div id="event" v-if="eventList.length != 0">
        <h1 id="EventTitle">All Event</h1>
        <!-- Filter Mode -->
        <div class = 'search-box'>
            <input class = "search-text" type="email" v-model="yourEmail" placeholder="your email.">
          <a href="#" class = "search-btn" @click="checkEmail">
            <i class="fas fa-search"></i>
          </a>
        </div>
        <div class = 'search-date-box'>
            <input class = "search-text" type="date" v-model="yourDateTime">
          <a href="#" class = "search-btn" @click="checkDate">
            <i class="fas fa-search"></i>
          </a>
        </div>
        <button @click="checkFuture" id="Upcoming">Upcoming</button>
        <button @click="checkPast" id="Past">Past</button>
        <button @click="listAll" id="Listall">List all</button>

        <ListEvent :events="filterEvent" class="scroll" @vnode-before-mount="sortedEvent" v-if="filterEvent.length != 0" />
        <p v-if="filterEvent.length === 0" id="no-scheduled">{{text}}</p>
    </div>
      <!-- List All Category-->
    <div id="category" v-if="eventList.length != 0">
        <h1 id="categoryTitle">Category</h1>
        <ListCategory :categories="categoryList"/>
    </div> 
  </div>  
 
</template>
 
<style lang="scss" scoped>
#no-scheduled{
  font-size:50px;
  color: white;
  text-align: center;
  margin-top:200px;
}
#Upcoming:hover,#Past:hover,#Listall:hover{
  transform: scale(1.08); 
  transition: all .2s ease-in-out;
}

#Upcoming{
  margin-left:750px;
}

#Upcoming:active,#Past:active,#Listall:active{
  background-color: rgba(146, 44, 64, 1);
}

#Upcoming:focus,#Past:focus,#Listall:focus {
  background-color: rgba(146, 44, 64, 1);
}

#Upcoming,#Past,#Listall{
  background-color: white;
  // rgba(146, 44, 64, 1)
  border-radius: 20px;
  padding: 10px 15px;
  margin-right:15px;
  margin-bottom:5px;
  margin-top: 45px;
}

#event {
  position:relative;
  top:5px;
  left:400px;
}

#category {
  position:absolute;
  top: 600px;
  left: 400px
}
#EventTitle{
  color: white;
  margin: 15px;
  font-weight:bold;
  font-size:70px;
  position: absolute;
}
 #categoryTitle {
  color: white;
  margin: 15px;
  font-weight:bold;
  font-size:70px;
  position: relative;
}

#categoryTitle {
  text-align:right;
  margin-right: 7.5%;
}

.search-box {
  position: absolute;
  top : 68px;
  left: 520px;
  transform: translate(-100%,-50%);
  background: #ffffff;
  height: 45px;
  border-radius: 40px;
  padding-top: 5px;
  padding-right: 8px;
}

.search-date-box {
  position: absolute;
  top : 68px;
  left: 730px;
  transform: translate(-100%,-50%);
  background: #ffffff;
  height: 45px;
  border-radius: 40px;
  padding-top: 5px;
  padding-right: 8px;
}
.search-box:hover  > .search-text{
  width: 9em;
  padding: 0 15px;
  padding-right: 10px;
}

.search-box:hover > .search-btn{
  background:  rgba(146, 44, 64, 1);
  color: rgb(253, 253, 253);
}

.search-date-box:hover  > .search-text{
  width: 9em;
  padding: 0 15px;
  padding-right: 10px;
}

.search-date-box:hover > .search-btn{
  background:  rgba(146, 44, 64, 1);
  color: rgb(253, 253, 253);
}

.search-btn {
  color: rgba(146, 44, 64, 1);
  width: 40px;
  height: 40px;
  margin-top: -2px;
  border-radius: 50%;
  background: #ffffff;
  display: flex;
  align-items: center;
  padding-left: 10px;
  position: relative;
  transition: 0.4s;
  cursor: pointer;
  text-decoration: none;
  
}
.search-btn > i {
  font-size: 20px;
}

.search-text {
  border: none;
  background: none;
  outline: none;
  float: left;
  padding: 5px;
  color: rgb(0, 0, 0);
  font-size: 16px;
  font-weight: normal;
  transition: 0.4s;
  line-height: 40px;
  width: 0px;
  
/*   width: 250px; */
}
</style>