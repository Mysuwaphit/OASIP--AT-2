<script setup>
import { ref, onBeforeMount } from "vue";
import AlertBox from '../components/alertBox.vue'
import ListUser from "../components/listUser.vue";

const yourName = ref('')
const yourEmail = ref('')

const filterUser = ref([])
const userList = ref([])
const getUserList = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/users`)
  if (res.status === 200) {
    const event = await res.json();
    userList.value = event
    filterUser.value = event
  } else {
    console.log("No Scheduled Events");
  }
};

const filterEvent = ref([])
const eventList = ref([])
const getEventList = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/events`)
  if (res.status === 200) {
    const event = await res.json();
    eventList.value = event
    filterEvent.value = event
  } else {
    console.log("No have any users.");
  }
};

onBeforeMount(async () => {
  await getUserList();
  await getEventList();

});

const text = ref('')

const checkEmail = () => filterEvent.value = eventList.value.filter((e) => e.bookingEmail == yourEmail.value , text.value = "No have any users.")
const listAllUser = () => filterUser.value = userList.value

// const checkDate = () => {
//   return filterEvent.value = eventList.value.filter((e) => {
//     const date = ref(new Date(e.startTime[0],e.startTime[1]-1,e.startTime[2],e.startTime[3],e.startTime[4]))
//     date.value = date.value.toLocaleDateString();
//     const select = ref(yourDateTime.value)
//     select.value = new Date(select.value).toLocaleDateString();
//     text.value = "No have any users."
//     console.log(`subSelect : ${select.value}`); 
//     console.log(`subDate : ${date.value}`);
//     return date.value == select.value
//   })
// }
const sortedUser = userList.value.sort((a, b) => (a.username > b.username) ? 1 : -1);
</script>
 
<template>
<div class="app">
      <!-- alert No users -->
    <div>
        <router-link class="button" to="AddUser">
          <span class="material-symbols-outlined">person_add</span>
          <span class="text">Add User</span>
        </router-link>
        <AlertBox v-if="userList.length === 0" text="any Users"/>
    </div>
    
      <!-- List All user-->
    <div id="event" v-if="userList.length != 0">
        <h1 id="EventTitle">All Users</h1>
        <router-link class="button" to="AddUser">
          <span class="material-symbols-outlined">person_add</span>
          <span class="text">Add User</span>
        </router-link>
      
        <!-- Filter Mode
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
        <button @click="listAll" id="Listall">List all</button> -->
        <ListUser :users="filterUser" id="listUser" class="scroll" @vnode-before-mount="sortedUser" v-if="filterUser.length != 0" />
        <!-- for filter some user -->
        <!-- <p v-if="filterEvent.length === 0" id="no-scheduled">{{text}}</p> -->

     

    </div>
  </div>  
 
</template>
 
<style lang="scss" scoped>
 .button{
          display: flex;
          align-items: center;
          text-decoration: none;
          width: 140px;
          height: 10px;
          background-color: white;
          padding: 1.5rem 1rem;
          border-radius: 10px;
          transition: 0.3s ease-out;
          position:absolute;
          margin-left: 890px;
          margin-top:-60px;

          .material-symbols-outlined{
              font-size: 2.3rem;
              color: black;
              transition: 0.2s ease-out;
          } 

          .text{
              color: black;
          }
          &:hover{
              background-color: var(--dark-alt);
              .material-symbols-outlined , .text{
                color:var(--primary)
              }
          }
      }
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
  position: relative;
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
  margin-right: 28.5%;
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