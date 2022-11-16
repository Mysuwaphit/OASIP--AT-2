<script setup>
import { ref, onBeforeMount, computed} from "vue";
import { useRouter , useRoute } from 'vue-router';
import SuccessBox from '../components/successBox.vue'

const now = new Date().toISOString().substring(0,16)

const noteValue = ref('')
const dateValue = ref()
const yourDateTime = ref(dateValue)
const description = ref(noteValue)
let  success = ref(false)

const {params} = useRoute()
const appRouter = useRouter()
const goBack = () => appRouter.go(-1)

let status = ref(0)
const token = `Bearer ${localStorage.getItem('accessToken')}`
const eventList = ref([])
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
    const event = await res.json();
    eventList.value = event
    eventListDetails.value = eventList.value.filter((event) =>  event.id.toString() === params.eventId)
  } else if(res.status === 401){
    console.log("Access token expired!!!!")
    postRefreshToken();
  }  
  else {
    console.log("No Scheduled Events");
  }
};

if(status.value === 401){
    console.log("Access token expired!!!!")
    postRefreshToken();
  }


//Upload File
const isUserUploadFile = ref(false)
let file = new Array() 
let oldFile = new Array()
const getFileStatus = ref(0)
const formData = new FormData();

const getFile = async () => {
  await fetch(`${import.meta.env.VITE_BASE_URL}/files/${params.eventId}`,{
        method: 'GET',headers: {'authorization': token}
  }).then(res => res.json())
    .then(data => {
      if(data.status != 500){
        getFileStatus.value = data.status
        oldFile.value = data[0]
        isUserUploadFile.value = true
        formData.append('file', oldFile[0])
        console.log(oldFile)
      }
      getFileStatus.value = data.status
    })}

const uploadFile = (event) => {
  
  file[0] = event.target.files[0] 
  console.log(file[0].name)
  console.log(`file size : ${file[0].size}(your) vs 10485760`) 
  if(file[0].size <= 10485760) {
    isUserUploadFile.value = true
    formData.append('file', file[0]);
    getFileStatus.value = 0
    console.log(`formData : ${formData.get('file').name}`)
    console.log(`Your file size is allow`) 
  }else{
    if(getFileStatus.value != 500){
      isUserUploadFile.value = true
      formData.append('file', oldFile[0])
      getFileStatus.value = 1
      console.log(`formData : ${formData.get('file').name}`)
      alert('Your file is too large!!! It could be less than 10 MB.')
      file.splice(0,1)
    }
  }
  console.log(`isUserUploadFile: ${isUserUploadFile.value}`)
  return formData,file
}
//remove file 
const removeFile = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/files/${params.eventId}`, {
    method: "DELETE",
    headers: {
          // 'content-type': 'application/json',
          'authorization': token
    }
  });
  if (res.status === 200) {
    file.splice(0,1)
    isUserUploadFile.value = false
    console.log("delete file successfully");
  } else {
    console.log("error,cannot delete file");
  }
}

// Update Event
const updateEvent = async (editEventId,name,email,duration,selectCategory,yourISODateTime,description,formData) => {
console.log(formData)
  // Update data
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/events/${editEventId}`,{
    method: 'PUT',
    headers: {
      'content-type': 'application/json',
      'Authorization': token
    },
    body: JSON.stringify(
      {
      bookingEmail: email,
      bookingName: name,
      category: selectCategory.eventCategoryName,
      startTime: yourISODateTime,
      eventNotes: description,
      eventCategory: selectCategory,
      duration : duration
      }
    )
  }).then(res => res.json())
  .then(data => {
    if(data.status === 200){
    eventList.value.push(data)
    clearForm();
    console.log('added successfully')
    success.value = true
    }else if(data.status === 401){
    console.log("Access token expired!!!!")
    postRefreshToken();}
  }).catch(err => console.log(err))

 // Update File 
  if(getFileStatus.value != 500 && isUserUploadFile.value === true){
    console.log('Upload File process')
    // console.log(`In Fetch : ${formData.get('file').name}`)
    const resFile = await fetch(`${import.meta.env.VITE_BASE_URL}/files/${editEventId}`,{
      method: 'POST',
      headers: {
        // 'Content-Type': 'multipart/form-data',
        'Authorization': token
      },
      body : formData
    })
    .then(res => res.json())
    .then(data => {if(data.status === 200)console.log(`successfully added file ${data}`)})
    .catch(err => console.log(err))
  }

}

onBeforeMount(async () => {
  await getEventList();
  await getFile();
});
  // Handle Date and Time 
const yourISODateTime = computed(() => {
  return new Date(yourDateTime.value).toISOString()
})

const getDate = (e) =>{
  const date = ref(new Date(e[0],e[1] - 1,e[2],e[3],e[4]))
  return date.value.toLocaleString();
}

const validTime = () => {
  const currentTime = new Date().toISOString()
  const selectTime  = new Date(yourDateTime.value).toISOString()
  console.log(`currentTime : ${currentTime}`);
  console.log(`selectTime : ${selectTime}`);
  return (selectTime > currentTime) ? yourDateTime.value = selectTime : alert('Your input Date & Time must be present or future')
}

const clearForm = () => {
  yourDateTime.value = dateValue.value
  description.value = noteValue.value
  file.splice(0,1)
  goBack()
  return console.log('clear');
}

</script>
 
<template>
  
    <div class="app"> 
        <div class="box" v-for="(eventListDetail,index) in eventListDetails" :key="index">
            <h1 id="bookingname">{{ eventListDetail.bookingName }}</h1>
              <div id="detail">
                <p>Category name :</p> <p id="categoryname">{{  eventListDetail.category }}</p>
                <p>Email : </p> <p id="email">{{  eventListDetail.bookingEmail }}</p>
                <p>Duration : </p> <p id="duration">{{  eventListDetail.duration + " Minutes" }} </p>
               
                <!-- Handle datetime -->
                <form>
                <div class="form-group">
                    <label class="col-form-label">Date & Time :</label>
                    <div @vnode-before-mount="dateValue=getDate(eventListDetail.startTime)">
                        <span>      
                        <input type="datetime-local" v-model="yourDateTime" :min="now"/>
                        </span> 
                    </div> 
                </div>

                <!-- Description -->
                <div class="form-group">
                    <label for="message-text" class="col-form-label">Description :</label>
                    
                    <textarea class="form-control" id="message-text"  maxlength="500" type="text" v-model="description" @vnode-before-mount="noteValue = eventListDetail.eventNotes" >{{ eventListDetail.eventNotes }}</textarea> 
                </div>

                <!-- File -->
                <div class="form-group">
                  <label for="message-text" class="col-form-label">Your file :</label>
                  <input type="file" ref="file" class="form-control" id="recipient-email" @change="uploadFile" :data-default-value="oldFile"/> 
                  <button @click="removeFile" class="close-icon" aria-label="Remove"><span class="material-symbols-outlined" @click="isUserUploadFile = false">delete_forever</span></button>
                </div>
                </form>
              </div>
              <!-- Footer -->
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" id="cancel" data-dismiss="modal" @click="clearForm()">cancel</button>
                <button type="button" class="btn btn-primary trigger-btn" id="save" href="#myModal" data-toggle="modal" @click="validTime && 
                updateEvent(eventListDetail.id,eventListDetail.bookingName,eventListDetail.bookingEmail,eventListDetail.duration,eventListDetail.eventCategory,yourISODateTime,description,(getFileStatus != 500 && isUserUploadFile === true)? formData: `No file`)">
                save</button>
                <button type="button" class="material-symbols-outlined" @click="goBack" id="backhome">arrow_back</button>
              </div>

        </div>
    </div>
    <SuccessBox v-if="success === true"/>
</template>
 
<style lang="scss" scoped>
.close-icon{
  position: absolute;
  padding: -5%;
  padding-left: 0.2%;
  padding-right: 0.2%;
  margin-left: 400px; 
  margin-top: -50px;
  background: #ff5858;
  border-radius:10px;
}
.close-icon:hover{
  background-color:rgb(71, 10, 22);
  transition: all .1s ease-in-out;
}
#bookingname::-webkit-scrollbar-thumb{
  border-radius: 20px;
  background: rgb(255, 255, 255);
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
  margin-top: 200px;
  top:700px;
  left: 390px;
  position: absolute;
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
  // opacity: 1;
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
height: 800px;
margin-top: 180px;
margin-left: 400px;
padding: 50px;
font-size: 30px;
}

#cancel:hover {
  background-color:rgb(71, 10, 22);
}

#save:hover {
   background-color:rgb(12, 65, 11);
}

#save {
   background-color:rgba(3, 133, 0, 1);
   margin: 250px;
   margin-right: 200px;
   position: relative;
}

#cancel {
   background-color: rgba(146, 44, 64, 1);
   margin: 250px;
   margin-right: 550px; 
   position: relative;
}

#cancel,#save {
  width:160px;
  height:50px;
  border-radius:30px;
  font-size: 25px;
  font-weight: bold;
  margin-top: 330px;
  position: absolute;
}

#select {
margin-left:30px;
margin: 5px;
}

label {
  font-weight: bold;
  font-size:20px;
}

#message-text {
  width: 500px;
  height: 100px;
  margin: 10px;
  border-radius:15px;
}

#recipient-name , #recipient-email {
  width:370px;
  margin: 10px;
  border-radius:15px;
}

input[type="datetime-local"]::-webkit-clear-button {
    display: none;
}

/* Removes the spin button */
input[type="datetime-local"]::-webkit-inner-spin-button { 
    display: none;
}

/* Always display the drop down caret */
input[type="datetime-local"]::-webkit-calendar-picker-indicator {
    color: #2c3e50;
}

/* A few custom styles for date inputs */
input[type="datetime-local"] {
    appearance: none;
    -webkit-appearance: none;
    color: rgba(196, 196, 196, 1);
    font-family: "Helvetica", arial, sans-serif;
    font-size: 18px;
    border:1px solid #ecf0f1;
    background:#ecf0f1;
    padding:5px;
    display: inline-block !important;
    visibility: visible !important;
}

input[type="datetime-local"], focus {
    color: rgba(196, 196, 196, 1);
    box-shadow: none;
    -webkit-box-shadow: none;
    -moz-box-shadow: none;
    border-radius: 10%;
}
</style>
