<script setup>
import { ref, onBeforeMount } from "vue";
import { useRouter , useRoute } from 'vue-router'
import ConfirmationBox from '../components/confirmationBox.vue'

const appRouter = useRouter()
const {params} = useRoute()
const goBack = () => appRouter.go(-1)
const goToEdit = (e) => appRouter.push({ name: 'EditUser' , params: { userId: e }})

const token = `Bearer ${localStorage.getItem('user')}`
const userListDetails = ref([]) 
const getUserList = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/users/${params.userId}`,{
        method: 'GET',
        headers: {
          'content-type': 'application/json',
          'authorization': token
        }
  })
  if (res.status === 200) {
    userListDetails.value = await res.json();
  } else {
    console.log("No have any users.");
  }
};

onBeforeMount(async () => {
  await getUserList();
});

  // Handle Date and Time
const getDate = ref((e) =>{
  const date = ref(new Date(e[0],e[1] - 1,e[2],e[3],e[4]))
  console.log(`real BE => ${e[0]}/${e[1]}/${e[2]}  ${e[3]}:${e[4]}`)
  console.log(`new Date => ${date.value}`);
  console.clear();
  return date.value.toLocaleDateString();
})
const getTime = ref((e) =>{
  const time = ref(new Date(e[0],e[1] - 1,e[2],e[3],e[4]))
  console.log(`real BE => ${e[3]}:${e[4]}`)
  const conTime  = ref(time.value.toLocaleTimeString('en-US', {hour: '2-digit', minute: '2-digit'}))
  console.log(`new Time => ${conTime.value}`);
  return conTime.value
})
</script>
 
<template>
    <div class="app"> 
      <h1 id="bookingname">{{ userListDetails.username }}</h1>
        <div class="box">
            <div @click="goToEdit(userListDetails.id)">
            <!-- @click="goToEdit($route.params.id)" -->
              <img id='editimg' src='../assets/icons8-edit-64.png'>
            </div>
          <div id="detail" >
                <p>Email : </p> <p id="email">{{ userListDetails.email }}</p>
                <p>Role : </p> <p id="duration">{{ userListDetails.roles }} </p>
                <p>Created On : </p> <p id="startDate" >{{ getDate(userListDetails.createdOn) }} {{ getTime(userListDetails.createdOn) }}</p>
                <p>Updated On  : </p> <p id="startTime" >{{ getDate(userListDetails.updatedOn) }} {{getTime(userListDetails.updatedOn) }}</p>
          </div>
              <button type="button" id="delete" class="material-symbols-outlined trigger-btn" href="#myModal" data-toggle="modal">delete</button>
              <button type="button" class="material-symbols-outlined" @click="goBack" id="backhome">arrow_back</button>
        </div>
        
        <ConfirmationBox :userId="$route.params.userId" :userOrEvent="true"/>
    </div>
    
</template>
 
<style lang="scss" scoped>
#email{
  overflow: hidden;
  overflow-x:auto;
  border-radius: 20px;
}
#description{
  height: 450px;
  overflow: hidden;
  overflow-y:auto;
  border-radius: 20px;
}
::-webkit-scrollbar{
  padding:0px 20px;
  width: 5px;
  height:7px;
}
::-webkit-scrollbar-thumb{
  border-radius: 20px;
  background: rgb(35, 35, 35);
}
#backhome:hover {
  background-color:var(--dark);
}
#backhome{
  background-color:white;
  margin: 50px;
  margin-right: 600px; 
  width:80px;
  height:50px;
  border-radius:30px;
  font-size: 25px;
  margin-top: 90px;
  top:720px;
  left: 390px;
  position: absolute;
}
#editimg{
  width: 50px;
  height: 50px;
  margin: -30px;
  margin-left: 880px;
  position: absolute;
  z-index: 1;
  cursor:pointer;
}
#delete {
  background-color: rgba(146, 44, 64, 1);
  margin: 20px;
  margin-right: 600px;
  width:160px;
  height:50px;
  border-radius:30px;
  font-size: 25px;
  margin-top: 300px;
  top:-80px;
  left: 350px;
  position: relative;
  opacity: 1;
}

#delete:hover {
  background-color:rgb(71, 10, 22);
}

#bookingname {
  position: absolute;
  left:400px;
  top:50px;
  font-size:70px;
  font-weight: bold;
  color: white;
  width:1000px;
  height: 100px;
  overflow: hidden;
  overflow-y:auto;
}

#bookingname::-webkit-scrollbar-thumb{
  border-radius: 20px;
  background: rgb(255, 255, 255);
}
#categoryname {
  background-color: white;
  border-radius: 20px;
  width: 300px;
  padding: 5px;
  position: relative;
  font-size: 20px;
  top:-37px;
  left:250px;
  text-align: center;
  margin: -20px;
}

#email {
  background-color: white;
  border-radius: 20px;
  width: 320px;
  padding: 5px;
  position: relative;
  font-size: 20px;
  top:-37px;
  left:120px;
  text-align: center;
  margin: -20px;
}

#duration {
  background-color: white;
  border-radius: 20px;
  width: 160px;
  padding: 5px;
  position: relative;
  font-size: 20px;
  top:-37px;
  left:110px;
  text-align: center;
  margin: -20px;
}

#startDate {
  background-color: white;
  border-radius: 20px;
  width: 150px;
  padding: 5px;
  position: relative;
  font-size: 20px;
  top:-35px;
  left:210px;
  text-align: center;
  margin: -20px;
}

#endDate{
  background-color: white;
  border-radius: 20px;
  width: 150px;
  padding: 5px;
  position: relative;
  font-size: 20px;
  top:-35px;
  left:180px;
  text-align: center;
  margin: -20px;
}

#startTime{
  background-color: white;
  border-radius: 20px;
  width: 150px;
  padding: 5px;
  position: relative;
  font-size: 20px;
  top:-35px;
  left:210px;
  text-align: center;
  margin: -20px;
}

#endTime {
  background-color: white;
  border-radius: 20px;
  width: 150px;
  padding: 5px;
  position: relative;
  font-size: 20px;
  top:-35px;
  left:167px;
  text-align: center;
  margin: -20px;
}

#description {
  background-color: white;
  border-radius: 20px;
  width: 370px;
  height: 120px;
  padding: 5px;
  position: relative;
  font-size: 20px;
  top:-35px;
  left:200px;
  text-align: center;
  margin: -20px;
}

.box {
background-color: rgba(229, 229, 229, 1);
opacity: 80%;
border-radius: 20px;
width: 1000px;
height: 700px;
margin-top: 180px;
margin-left: 400px;
padding: 50px;
font-size: 30px;
}

</style>