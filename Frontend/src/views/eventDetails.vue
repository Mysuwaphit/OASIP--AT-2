<script setup>
import { ref, onBeforeMount } from "vue";
import { useRouter , useRoute } from 'vue-router'
import ConfirmationBox from '../components/confirmationBox.vue'

const appRouter = useRouter()
const {params} = useRoute()
const goBack = () => appRouter.go(-1)
const goToEdit = (e) => appRouter.push({ name: 'EditEvent' , params: { eventId: e }})

let status = ref(0)
const eventListDetails = ref([]) 
const token = `Bearer ${localStorage.getItem('accessToken')}`
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
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/events/${params.eventId}`,{
        method: 'GET',
        headers: {
          'content-type': 'application/json',
          'authorization': token
        }
  })
  if (res.status === 200) {
    eventListDetails.value = await res.json();
  }else if(res.status === 401){
    console.log("Access token expired!!!!")
    postRefreshToken();
  } 
  else {
    console.log("No Scheduled Events");
  }
};



let file = ref('')
const haveFile = ref(false)
const getEventFile = async () => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/files/${params.eventId}`,{
          method: 'GET',
          headers: {
            // 'content-type': 'multipart/form-data ',
            'authorization': token
          }
    }).then(res => res.json())
    .then(data => { 
      if(data.status != 500){
        file.value = data[0]  
        haveFile.value = true
        console.log(file.value)
        }
    }).catch(err => err)
  };

// Download File
// let fileDownload = ref('')
// let downloaded = async () => { await fetch(`${import.meta.env.VITE_BASE_URL}/files/${params.eventId}/${file.value}`,{
//           method: 'GET',
//           headers: {
//             // 'content-type': 'application/json',
//             'authorization': token,
//             'responseType': 'blob'
//           }}).then(res => res.blob())
//           .then(data => {
//             const newBlob = new Blob([data])
//             fileDownload.value = window.URL.createObjectURL(newBlob)
//             // console.log(`download data : ${data}`)
//             console.log(`download data : ${fileDownload.value}`)
//           })}

if(status.value === 401){
    console.log("Access token expired!!!!")
    postRefreshToken();
  }
onBeforeMount(async () => {
  await getEventList();
  await getEventFile();
});

  // Handle Date and Time
const getDate = ref((e) =>{
  const date = ref(new Date(e.startTime[0],e.startTime[1] - 1,e.startTime[2],e.startTime[3],e.startTime[4]))
  console.log(`real BE => ${e.startTime[0]}/${e.startTime[1]}/${e.startTime[2]}  ${e.startTime[3]}:${e.startTime[4]}`)
  console.log(`new Date => ${date.value}`);
  console.clear();
  return date.value.toLocaleDateString();
})
const getTime = ref((e) =>{
  const time = ref(new Date(e.startTime[0],e.startTime[1] - 1,e.startTime[2],e.startTime[3],e.startTime[4]))
  console.log(`real BE => ${e.startTime[3]}:${e.startTime[4]}`)
  const conTime  = ref(time.value.toLocaleTimeString('en-US', {hour: '2-digit', minute: '2-digit'}))
  console.log(`new Time => ${conTime.value}`);
  return conTime.value
})

const getEndTime = ref((e) =>{
   const endTime = ref(new Date(e.startTime[0],e.startTime[1] - 1,e.startTime[2],e.startTime[3],e.startTime[4]))
   console.log(`real BE => ${e.startTime[3]}:${e.startTime[4] + e.duration * 60000}`)
   console.log(`new Time => ${endTime.value}`);
   return new Date(endTime.value.getTime() + e.duration * 60000).toLocaleTimeString('en-US', {hour: '2-digit', minute: '2-digit'})
})

const role = localStorage.getItem('role')
const path = `${import.meta.env.VITE_BASE_URL}/files/${params.eventId}/`

</script>
 
<template>
    <div class="app"> 
      <h1 id="bookingname">{{ eventListDetails.bookingName }}</h1>
        <div class="box">
            <div @click="goToEdit($route.params.eventId)" v-show="role === 'admin' || role === 'student'">
              <img id='editimg' src='../assets/icons8-edit-64.png'>
            </div>
          <div id="detail" >
                <p>Category name : </p> <p id="categoryname">{{  eventListDetails.category }}</p>
                <p>Email : </p> <p id="email">{{  eventListDetails.bookingEmail }}</p>
                <p>Duration : </p> <p id="duration">{{ eventListDetails.duration + " Minutes" }} </p>
                <p>Start Date : </p> <p id="startDate" >{{ getDate(eventListDetails) }}</p>
                <span id="Titlestarttime"> <p>Start Time : </p> <p id="startTime" >{{ getTime(eventListDetails) }}</p> </span> 
                <p>End Date :</p> <p id="endDate">{{ getDate(eventListDetails) }}</p>
                <span id="Titleendtime"> <p>End Time : </p><p id="endTime">{{ getEndTime(eventListDetails) }}</p> </span>
                <p >Description :</p> 
                <p id="description" >{{  eventListDetails.eventNotes }}</p>
                <p v-if="haveFile === true" > File : &nbsp;&nbsp;&nbsp;<a :href="path + file" id="download" class="btn btn-primary py-3 px-3" :download="file" >{{ file }}</a></p>
          </div>
              <button type="button" id="delete" class="material-symbols-outlined trigger-btn" href="#myModal" data-toggle="modal" v-show="role === 'admin' || role === 'student'" >delete</button>
              <button type="button" class="material-symbols-outlined" @click="goBack" id="backhome">arrow_back</button>
        </div>
        
        <ConfirmationBox :eventId="$route.params.eventId" :userOrEvent="false"/>
    </div>
    
</template>
 
<style lang="scss" scoped>
#download{
  background-color: rgb(28, 42, 76);
  border-color: rgb(28, 42, 76);
}
#download:hover{
  background-color: #8fafff;
  border-color:  #8fafff;
}
#Titleendtime{
  position: absolute;
  margin-left: 18%;
  margin-top: -60px;
}
#Titlestarttime{
  position: absolute;
  margin-left: 18%;
  margin-top: -60px;
}
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
  margin-top: 90px;
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
  left:160px;
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
  left:180px;
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
  left:180px;
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