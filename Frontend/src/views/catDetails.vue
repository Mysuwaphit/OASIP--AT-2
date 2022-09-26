<script setup>
import { ref, onBeforeMount, computed } from "vue";
import { useRouter , useRoute } from 'vue-router';
import ListEvent from '../components/listEvent.vue';
import ConfirmationBox from '../components/confirmationBox.vue'

const appRouter = useRouter()
const {params} = useRoute()
const goBack = () => appRouter.go(-1)
const goToEdit = () => appRouter.push({ name: 'EditCategory' , params: { catId: params.catId }})

let status = ref(0)
const token = `Bearer ${localStorage.getItem('accessToken')}`
const eventListDetails = ref([]) 

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
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/events`,{
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
const categoryListDetails = ref([])
const getCategory = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/eventCategories`,{
        method: 'GET',
        headers: {
          'content-type': 'application/json',
          'authorization': token
        }
  })
  if (res.status === 200) {
    const List = await res.json(); 
    categoryListDetails.value= List
  }else if(res.status === 401){
    console.log("Access token expired!!!!")
    postRefreshToken();
  }  
  else {
    console.log("No Category");
  }
};
if(status.value === 401){
    console.log("Access token expired!!!!")
    postRefreshToken();
  }
onBeforeMount(async () => {
  await getEventList();
  await getCategory();
  matchCat();
  matchEvent();
});

  // Match Event with param on URL
const matchCat = () => categoryListDetails.value = categoryListDetails.value.filter((cat) => cat.id === +(params.catId))
const matchEvent = () => eventListDetails.value = eventListDetails.value.filter((event) => event.eventCategory.id === +(params.catId))

</script>
 
<template>

    <div class="app" > 
        <div class="box" v-for="(categoryListDetail,index) in categoryListDetails" :key="index">
          <div @click="goToEdit">
                <img id='editimg' src='../assets/icons8-edit-64.png'>
          </div>
          <h1 id="bookingname">{{  categoryListDetail.eventCategoryName }}</h1>
                <p>Duration : </p> <p id="duration">{{ categoryListDetail.duration + " Minutes" }} </p>
                <p>Description :</p> 
                <p id="description" class="scroll" v-if="categoryListDetail.eventCategoryDescription != null">{{ categoryListDetail.eventCategoryDescription }}</p>
                <!-- event that in this category -->
                <div id="event" >
                  <p id="EventTitle" >All Event in {{ categoryListDetail.eventCategoryName }} : </p>
                    <ListEvent :events="eventListDetails" class="scroll" id="listevent" v-if="eventListDetails.length != 0"/>
                    <p v-if="eventListDetails.length === 0" id="nosceduled">" No Scheduled Events. "</p>
                  <button type="button" class="material-symbols-outlined" @click="goBack" id="backhome">arrow_back</button>
                  <button type="button" id="delete" class="material-symbols-outlined trigger-btn" href="#myModal" data-toggle="modal">delete</button>
                </div>
        </div>
        <ConfirmationBox/>
    </div>
    
</template>
 
<style lang="scss" scoped>
.scroll{
  height: 460px;
  overflow: hidden;
  overflow-y:auto;
  border-radius: 20px;
}
::-webkit-scrollbar{
  padding:0px 20px;
  width: 7px;
}
::-webkit-scrollbar-thumb{
  border-radius: 20px;
  background: rgb(241, 241, 241, 1);
}
#editimg{
  width: 50px;
  height: 50px;
  margin: -30px;
  margin-left: 980px;
  position: absolute;
  z-index: 1;
  cursor:pointer;
}
#nosceduled{
  padding-top: 120px;
  text-align: center;
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
#listevent{
  font-size:16px;
  position:absolute;
  width:1100px;
  left: 430px;
}

#delete {
  background-color: rgba(146, 44, 64, 1);
  margin: 20px;
  margin-right: 600px; 
  width:160px;
  height:50px;
  border-radius:30px;
  font-size: 25px;
  margin-top: 400px;
  top:-80px;
  left: 350px;
  position: relative;
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
}

#categoryname {
  border-radius: 20px;
  width: 300px;
  padding: 5px;
  position: absolute;
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
background-color: rgba(196, 196, 196, 1);
opacity: 95%;
border-radius: 20px;
width: 1100px;
height: 700px;
margin-top: 180px;
margin-left: 400px;
padding: 50px;
font-size: 30px;
}

</style>